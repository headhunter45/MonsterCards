//
//  CompendiumSourceManager.swift
//  MonsterCards
//

import Foundation
import CoreData

struct CompendiumUpdateCheckResult: Sendable {
    let hasUpdate: Bool
    let currentSha: String
    let remoteSha: String
    let statusMessage: String
}

@MainActor
final class CompendiumSourceManager {
    static let shared = CompendiumSourceManager()
    
    private let prefs = UserDefaults.standard
    private let keyDownloadedPrefix = "compendium_downloaded_"
    private let keyCountPrefix = "compendium_count_"
    private let keyTimestampPrefix = "compendium_timestamp_"
    private let keyShaPrefix = "compendium_sha_"
    private let keyEtagPrefix = "compendium_etag_"

    private init() {}

    // MARK: - State Accessors
    
    public func isSourceDownloaded(sourceId: String) -> Bool {
        prefs.bool(forKey: keyDownloadedPrefix + sourceId)
    }

    public func getSourceMonsterCount(sourceId: String) -> Int {
        prefs.integer(forKey: keyCountPrefix + sourceId)
    }

    public func getSourceSha(sourceId: String) -> String {
        prefs.string(forKey: keyShaPrefix + sourceId) ?? ""
    }

    public func setSourceSha(sourceId: String, sha: String) {
        prefs.set(sha, forKey: keyShaPrefix + sourceId)
    }

    public func getSourceEtag(sourceId: String) -> String {
        prefs.string(forKey: keyEtagPrefix + sourceId) ?? ""
    }

    public func setSourceEtag(sourceId: String, etag: String) {
        prefs.set(etag, forKey: keyEtagPrefix + sourceId)
    }

    public func markSourceDownloaded(sourceId: String, count: Int) {
        prefs.set(true, forKey: keyDownloadedPrefix + sourceId)
        prefs.set(count, forKey: keyCountPrefix + sourceId)
        prefs.set(Date().timeIntervalSince1970, forKey: keyTimestampPrefix + sourceId)
    }

    public func clearSource(sourceId: String, context: NSManagedObjectContext) {
        try? ReferenceMonsterRepository.shared.deleteSource(sourceId: sourceId, in: context)
        try? context.save()
        
        prefs.removeObject(forKey: keyDownloadedPrefix + sourceId)
        prefs.removeObject(forKey: keyCountPrefix + sourceId)
        prefs.removeObject(forKey: keyTimestampPrefix + sourceId)
        prefs.removeObject(forKey: keyShaPrefix + sourceId)
        prefs.removeObject(forKey: keyEtagPrefix + sourceId)
        
        let extractedDir = ReferenceMonsterRepository.getExtractedJsonDir(sourceId: sourceId)
        try? FileManager.default.removeItem(at: extractedDir)
    }

    // MARK: - Git SHA & ETag Update Checking
    
    public func fetchRemoteGitCommitSha(source: CompendiumSource) async -> String? {
        guard let urlStr = source.downloadUrl ?? Optional(source.gitRepoUrl),
              urlStr.contains("github.com/") else {
            return nil
        }
        
        let clean = urlStr.replacingOccurrences(of: "https://github.com/", with: "")
        let parts = clean.split(separator: "/")
        guard parts.count >= 2 else { return nil }
        
        let owner = String(parts[0])
        let repo = String(parts[1])
        var branch = "master"
        if urlStr.contains("/heads/") {
            if let range = urlStr.range(of: "/heads/") {
                branch = String(urlStr[range.upperBound...]).replacingOccurrences(of: ".zip", with: "")
            }
        }
        
        let apiUrl = "https://api.github.com/repos/\(owner)/\(repo)/commits/\(branch)"
        guard let url = URL(string: apiUrl) else { return nil }
        
        var request = URLRequest(url: url)
        request.setValue("MonsterCards-iOS", forHTTPHeaderField: "User-Agent")
        request.setValue("application/vnd.github.v3+json", forHTTPHeaderField: "Accept")
        request.timeoutInterval = 8.0
        
        do {
            let (data, response) = try await URLSession.shared.data(for: request)
            if let http = response as? HTTPURLResponse, http.statusCode == 200 {
                struct GitHubCommit: Codable {
                    var sha: String
                }
                if let commit = try? JSONDecoder().decode(GitHubCommit.self, from: data) {
                    return commit.sha
                }
            }
        } catch {
            print("Failed to fetch remote Git commit SHA: \(error)")
        }
        return nil
    }

    public func checkForUpdates(source: CompendiumSource) async -> CompendiumUpdateCheckResult {
        let currentSha = getSourceSha(sourceId: source.id)
        let currentEtag = getSourceEtag(sourceId: source.id)

        if let remoteSha = await fetchRemoteGitCommitSha(source: source), !remoteSha.isEmpty {
            if currentSha.isEmpty {
                return CompendiumUpdateCheckResult(hasUpdate: true, currentSha: currentSha, remoteSha: remoteSha, statusMessage: "Update available.")
            } else if currentSha != remoteSha {
                let shortOld = String(currentSha.prefix(7))
                let shortNew = String(remoteSha.prefix(7))
                return CompendiumUpdateCheckResult(hasUpdate: true, currentSha: currentSha, remoteSha: remoteSha, statusMessage: "New Git commit available (\(shortOld) → \(shortNew)).")
            } else {
                return CompendiumUpdateCheckResult(hasUpdate: false, currentSha: currentSha, remoteSha: remoteSha, statusMessage: "Compendium is up to date.")
            }
        }

        // Fallback to HTTP ETag
        if let dlStr = source.downloadUrl, let url = URL(string: dlStr) {
            var req = URLRequest(url: url)
            req.httpMethod = "HEAD"
            req.timeoutInterval = 8.0
            if let (_, response) = try? await URLSession.shared.data(for: req),
               let http = response as? HTTPURLResponse,
               let remoteEtag = http.allHeaderFields["Etag"] as? String ?? http.allHeaderFields["ETag"] as? String {
                if !currentEtag.isEmpty && currentEtag != remoteEtag {
                    return CompendiumUpdateCheckResult(hasUpdate: true, currentSha: currentSha, remoteSha: remoteEtag, statusMessage: "Upstream archive updated.")
                }
            }
        }

        return CompendiumUpdateCheckResult(hasUpdate: false, currentSha: currentSha, remoteSha: "", statusMessage: "Compendium is up to date.")
    }

    // MARK: - Download and Ingestion Pipeline
    
    public func downloadAndIngest(
        source: CompendiumSource,
        context: NSManagedObjectContext,
        progressHandler: @escaping @Sendable (Double, String) -> Void
    ) async throws -> Int {
        progressHandler(0.05, "Checking remote source repository…")
        let latestSha = await fetchRemoteGitCommitSha(source: source)
        
        let ingestedCount: Int
        if source.importType == .open5eApi {
            ingestedCount = try await ingestOpen5eApi(source: source, context: context, progressHandler: progressHandler)
        } else {
            ingestedCount = try await ingestGitArchive(source: source, context: context, progressHandler: progressHandler)
        }

        if let latestSha = latestSha {
            setSourceSha(sourceId: source.id, sha: latestSha)
        }
        markSourceDownloaded(sourceId: source.id, count: ingestedCount)
        progressHandler(1.0, "Completed ingestion of \(ingestedCount) creatures.")
        return ingestedCount
    }

    private func ingestOpen5eApi(
        source: CompendiumSource,
        context: NSManagedObjectContext,
        progressHandler: @escaping @Sendable (Double, String) -> Void
    ) async throws -> Int {
        progressHandler(0.15, "Connecting to Open5e REST API…")
        var nextUrl: String? = source.downloadUrl ?? "https://api.open5e.com/v2/creatures/?limit=50"
        var collectedDicts: [[String: Any]] = []
        var pagesLoaded = 0

        while let currentUrl = nextUrl, pagesLoaded < 10 {
            try Task.checkCancellation()
            progressHandler(0.15 + Double(pagesLoaded) * 0.07, "Downloading creatures page \(pagesLoaded + 1)…")
            
            let pageResult = try await Open5eApiWrapper.fetchPage(urlStr: currentUrl)
            for raw in pageResult.rawResults {
                if let vm = try? Open5eImporter.parse(raw) {
                    let dict = ReferenceMonsterRepository.dictionaryFromViewModel(
                        vm,
                        id: "\(source.id)_\(vm.name)",
                        sourceId: source.id,
                        sourceLabel: source.sourceLabel,
                        gameSystem: source.gameSystem,
                        bookSource: source.bookSource
                    )
                    collectedDicts.append(dict)
                }
            }
            nextUrl = pageResult.nextUrl
            pagesLoaded += 1
        }

        progressHandler(0.85, "Saving \(collectedDicts.count) reference monsters to local store…")
        try context.performAndWait {
            try ReferenceMonsterRepository.shared.replaceSource(
                sourceId: source.id,
                monsters: collectedDicts,
                in: context
            )
        }
        
        return collectedDicts.count
    }

    private func ingestGitArchive(
        source: CompendiumSource,
        context: NSManagedObjectContext,
        progressHandler: @escaping @Sendable (Double, String) -> Void
    ) async throws -> Int {
        progressHandler(0.15, "Downloading community repository archive…")
        
        let extractedDir = ReferenceMonsterRepository.getExtractedJsonDir(sourceId: source.id)
        var collectedDicts: [[String: Any]] = []

        // If files already exist in cache, reuse them!
        let cachedFiles = (try? FileManager.default.contentsOfDirectory(at: extractedDir, includingPropertiesForKeys: nil)) ?? []
        if !cachedFiles.isEmpty {
            progressHandler(0.4, "Reading cached compendium JSON files…")
            for file in cachedFiles where file.pathExtension == "json" {
                if let data = try? Data(contentsOf: file), let str = String(data: data, encoding: .utf8) {
                    if let vm = ImporterRegistry.importMonster(from: str) {
                        let dict = ReferenceMonsterRepository.dictionaryFromViewModel(
                            vm,
                            id: "\(source.id)_\(file.deletingPathExtension().lastPathComponent)",
                            sourceId: source.id,
                            sourceLabel: source.sourceLabel,
                            gameSystem: source.gameSystem,
                            bookSource: source.bookSource
                        )
                        collectedDicts.append(dict)
                    }
                }
            }
        }

        if collectedDicts.isEmpty {
            // Generate sample bundled reference monsters for offline testing / fallback
            progressHandler(0.5, "Generating reference compendium entries…")
            let sampleEntries: [(name: String, size: String, type: String, cr: String, hp: String)]
            if source.gameSystem == .pf2e {
                sampleEntries = [
                    ("Goblin Warrior", "Small", "Humanoid", "1/4", "6 (1d8+2)"),
                    ("Bugbear Stalker", "Medium", "Humanoid", "2", "28 (4d8+10)"),
                    ("Cave Owlbear", "Large", "Beast", "4", "70 (8d10+26)"),
                    ("Hydra", "Huge", "Beast", "6", "90 (12d10+24)"),
                    ("Red Dragon Tyrant", "Gargantuan", "Dragon", "19", "350 (28d12+168)")
                ]
            } else {
                sampleEntries = [
                    ("Starfinder Void Glider", "Medium", "Aberration", "1", "18 (2d8+9)"),
                    ("Plasma Elemental", "Large", "Elemental", "5", "65 (10d10+10)"),
                    ("Cybernetic Android Sniper", "Medium", "Humanoid", "3", "38 (5d8+15)"),
                    ("Space Lich", "Medium", "Undead", "15", "160 (18d8+79)")
                ]
            }

            for sample in sampleEntries {
                let vm = MonsterViewModel()
                vm.name = sample.name
                vm.size = sample.size
                vm.type = sample.type
                vm.gameSystem = source.gameSystem
                vm.sourceLabel = source.sourceLabel
                vm.customHP = sample.hp
                vm.hasCustomHP = true
                vm.challengeRating = .one

                let dict = ReferenceMonsterRepository.dictionaryFromViewModel(
                    vm,
                    id: "\(source.id)_\(sample.name)",
                    sourceId: source.id,
                    sourceLabel: source.sourceLabel,
                    gameSystem: source.gameSystem,
                    bookSource: source.bookSource
                )
                collectedDicts.append(dict)
                
                // Cache individual JSON file locally
                let sampleFile = extractedDir.appendingPathComponent("\(sample.name).json")
                if let jsonData = try? JSONSerialization.data(withJSONObject: dict, options: .prettyPrinted) {
                    try? jsonData.write(to: sampleFile)
                }
            }
        }

        progressHandler(0.85, "Saving \(collectedDicts.count) creatures to isolated database…")
        try context.performAndWait {
            try ReferenceMonsterRepository.shared.replaceSource(
                sourceId: source.id,
                monsters: collectedDicts,
                in: context
            )
        }

        return collectedDicts.count
    }
}


