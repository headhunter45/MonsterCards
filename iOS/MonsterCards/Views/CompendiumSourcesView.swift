//
//  CompendiumSourcesView.swift
//  MonsterCards
//

import SwiftUI
import CoreData

struct CompendiumSourcesView: View {
    @Environment(\.managedObjectContext) private var viewContext
    @State private var sources = CompendiumRegistry.defaultSources
    @State private var selectedSourceForDisclaimer: CompendiumSource? = nil
    @State private var activeDownloadSourceId: String? = nil
    @State private var downloadProgress: Double = 0.0
    @State private var downloadStatusMessage: String = ""
    @State private var downloadTask: Task<Void, Never>? = nil
    @State private var updateCheckResults: [String: CompendiumUpdateCheckResult] = [:]
    @State private var isCheckingUpdates = false
    @State private var errorMessage: String? = nil

    var body: some View {
        NavigationStack {
            List {
                Section {
                    VStack(alignment: .leading, spacing: 8) {
                        HStack {
                            Image(systemName: "books.vertical.fill")
                                .font(.title2)
                                .foregroundColor(.accentColor)
                            Text("Community Compendiums")
                                .font(.title3)
                                .fontWeight(.bold)
                        }
                        Text("Download unofficial game compendiums directly from 3rd-party repositories under their own 3rd-party licenses. Reference monsters can be searched and cloned into your local library.")
                            .font(.subheadline)
                            .foregroundColor(.secondary)
                    }
                    .padding(.vertical, 4)
                }

                Section("Available Repositories") {
                    ForEach(sources) { source in
                        CompendiumSourceRow(
                            source: source,
                            isDownloaded: CompendiumSourceManager.shared.isSourceDownloaded(sourceId: source.id),
                            monsterCount: CompendiumSourceManager.shared.getSourceMonsterCount(sourceId: source.id),
                            updateResult: updateCheckResults[source.id],
                            isDownloading: activeDownloadSourceId == source.id,
                            onDownloadTapped: {
                                selectedSourceForDisclaimer = source
                            },
                            onDeleteTapped: {
                                removeSource(source)
                            }
                        )
                    }
                }

                Section {
                    Button {
                        checkForUpdatesAll()
                    } label: {
                        HStack {
                            Spacer()
                            if isCheckingUpdates {
                                ProgressView()
                                    .padding(.trailing, 6)
                                Text("Checking for Updates…")
                            } else {
                                Label("Check for Compendium Updates", systemImage: "arrow.clockwise")
                            }
                            Spacer()
                        }
                    }
                    .disabled(isCheckingUpdates || activeDownloadSourceId != nil)
                }
            }
            .navigationTitle("Compendium Sources")
            .sheet(item: $selectedSourceForDisclaimer) { source in
                CompendiumDisclaimerSheet(
                    source: source,
                    onConfirm: {
                        selectedSourceForDisclaimer = nil
                        startDownload(source: source)
                    },
                    onCancel: {
                        selectedSourceForDisclaimer = nil
                    }
                )
            }
            .overlay {
                if activeDownloadSourceId != nil {
                    downloadProgressOverlay
                }
            }
            .alert("Download Error", isPresented: Binding(
                get: { errorMessage != nil },
                set: { if !$0 { errorMessage = nil } }
            )) {
                Button("OK") { errorMessage = nil }
            } message: {
                Text(errorMessage ?? "")
            }
        }
    }

    // MARK: - Download Overlay

    private var downloadProgressOverlay: some View {
        ZStack {
            Color.black.opacity(0.4)
                .ignoresSafeArea()

            VStack(spacing: 16) {
                ProgressView(value: downloadProgress, total: 1.0)
                    .progressViewStyle(.linear)
                    .tint(.accentColor)

                VStack(spacing: 4) {
                    Text("\(Int(downloadProgress * 100))%")
                        .font(.title2)
                        .fontWeight(.bold)
                    Text(downloadStatusMessage)
                        .font(.subheadline)
                        .foregroundColor(.secondary)
                        .multilineTextAlignment(.center)
                }

                Button(role: .destructive) {
                    cancelDownload()
                } label: {
                    Text("Cancel Download")
                        .font(.subheadline)
                        .fontWeight(.medium)
                }
                .buttonStyle(.bordered)
            }
            .padding(24)
            .background(RoundedRectangle(cornerRadius: 16).fill(Color(uiColor: .systemBackground)))
            .shadow(radius: 10)
            .padding(.horizontal, 32)
        }
    }

    // MARK: - Actions

    private func startDownload(source: CompendiumSource) {
        activeDownloadSourceId = source.id
        downloadProgress = 0.05
        downloadStatusMessage = "Starting download for \(source.name)…"

        downloadTask = Task {
            do {
                _ = try await CompendiumSourceManager.shared.downloadAndIngest(
                    source: source,
                    context: viewContext,
                    progressHandler: { progress, message in
                        Task { @MainActor in
                            self.downloadProgress = progress
                            self.downloadStatusMessage = message
                        }
                    }
                )

                await MainActor.run {
                    self.activeDownloadSourceId = nil
                    self.updateCheckResults[source.id] = nil
                }
            } catch is CancellationError {
                await MainActor.run {
                    self.activeDownloadSourceId = nil
                    self.downloadStatusMessage = "Download cancelled."
                }
            } catch {
                await MainActor.run {
                    self.activeDownloadSourceId = nil
                    self.errorMessage = error.localizedDescription
                }
            }
        }
    }

    private func cancelDownload() {
        downloadTask?.cancel()
        downloadTask = nil
        activeDownloadSourceId = nil
    }

    private func removeSource(_ source: CompendiumSource) {
        withAnimation {
            CompendiumSourceManager.shared.clearSource(sourceId: source.id, context: viewContext)
            updateCheckResults[source.id] = nil
        }
    }

    private func checkForUpdatesAll() {
        isCheckingUpdates = true
        Task {
            for source in sources where CompendiumSourceManager.shared.isSourceDownloaded(sourceId: source.id) {
                let res = await CompendiumSourceManager.shared.checkForUpdates(source: source)
                await MainActor.run {
                    self.updateCheckResults[source.id] = res
                }
            }
            await MainActor.run {
                self.isCheckingUpdates = false
            }
        }
    }
}

struct CompendiumSourceRow: View {
    let source: CompendiumSource
    let isDownloaded: Bool
    let monsterCount: Int
    let updateResult: CompendiumUpdateCheckResult?
    let isDownloading: Bool
    let onDownloadTapped: () -> Void
    let onDeleteTapped: () -> Void

    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            HStack(alignment: .top) {
                VStack(alignment: .leading, spacing: 4) {
                    HStack(spacing: 6) {
                        Text(source.name)
                            .font(.headline)
                            .foregroundColor(.primary)
                        SourceTagView(gameSystem: source.gameSystem, sourceLabel: source.sourceLabel)
                    }

                    Text(source.description)
                        .font(.caption)
                        .foregroundColor(.secondary)
                        .lineLimit(2)
                }
                Spacer()
            }

            HStack {
                Label(source.gitRepoUrl.replacingOccurrences(of: "https://github.com/", with: ""), systemImage: "link")
                    .font(.caption2)
                    .foregroundColor(.secondary)
                    .lineLimit(1)

                Spacer()

                if isDownloaded {
                    HStack(spacing: 8) {
                        if let update = updateResult, update.hasUpdate {
                            Button(action: onDownloadTapped) {
                                Label("Update", systemImage: "arrow.triangle.2.circlepath")
                                    .font(.caption.weight(.bold))
                                    .padding(.horizontal, 8)
                                    .padding(.vertical, 4)
                            }
                            .buttonStyle(.borderedProminent)
                            .tint(.orange)
                        } else {
                            Text("✓ \(monsterCount) monsters")
                                .font(.caption2.weight(.semibold))
                                .foregroundColor(.green)
                        }

                        Button(role: .destructive, action: onDeleteTapped) {
                            Image(systemName: "trash")
                                .font(.caption)
                                .foregroundColor(.red)
                        }
                        .buttonStyle(.borderless)
                    }
                } else {
                    Button(action: onDownloadTapped) {
                        Label("Download", systemImage: "arrow.down.circle.fill")
                            .font(.caption.weight(.bold))
                            .padding(.horizontal, 10)
                            .padding(.vertical, 4)
                    }
                    .buttonStyle(.borderedProminent)
                }
            }
            .padding(.top, 2)
        }
        .padding(.vertical, 4)
    }
}

struct CompendiumDisclaimerSheet: View {
    let source: CompendiumSource
    let onConfirm: () -> Void
    let onCancel: () -> Void

    @State private var hasAcknowledged = false

    var body: some View {
        NavigationStack {
            ScrollView {
                VStack(alignment: .leading, spacing: 20) {
                    HStack {
                        Image(systemName: "exclamationmark.triangle.fill")
                            .font(.largeTitle)
                            .foregroundColor(.orange)
                        VStack(alignment: .leading, spacing: 2) {
                            Text("3rd-Party Content Notice")
                                .font(.headline)
                            Text(source.name)
                                .font(.subheadline)
                                .foregroundColor(.secondary)
                        }
                    }
                    .padding(.top, 8)

                    VStack(alignment: .leading, spacing: 12) {
                        Text("You are about to download game reference content hosted on a third-party community Git repository:")
                            .font(.body)

                        HStack {
                            Image(systemName: "link.circle.fill")
                                .foregroundColor(.blue)
                            Text(source.gitRepoUrl)
                                .font(.caption.weight(.medium))
                                .foregroundColor(.blue)
                        }
                        .padding(10)
                        .background(Color.blue.opacity(0.1))
                        .cornerRadius(8)

                        Text("Important Terms & Understanding:")
                            .font(.headline)
                            .padding(.top, 4)

                        VStack(alignment: .leading, spacing: 6) {
                            LegalBullet(text: "This content is NOT created, reviewed, or owned by MonsterCards.")
                            LegalBullet(text: "Content is licensed by its respective authors under their own 3rd-party licenses.")
                            LegalBullet(text: "The data will be stored in an isolated local database and will NOT be exported with your personal cards unless explicitly cloned.")
                        }
                    }

                    Divider()

                    Toggle(isOn: $hasAcknowledged) {
                        Text("I understand this is 3rd-party community content and agree to download it from the source repository.")
                            .font(.footnote)
                            .foregroundColor(.primary)
                    }
                    .toggleStyle(CheckboxToggleStyle())

                    HStack(spacing: 12) {
                        Button(role: .cancel, action: onCancel) {
                            Text("Cancel")
                                .frame(maxWidth: .infinity)
                        }
                        .buttonStyle(.bordered)

                        Button(action: onConfirm) {
                            Text("Confirm & Download")
                                .frame(maxWidth: .infinity)
                        }
                        .buttonStyle(.borderedProminent)
                        .disabled(!hasAcknowledged)
                    }
                    .padding(.top, 12)
                }
                .padding()
            }
            .navigationTitle("Legal Confirmation")
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .cancellationAction) {
                    Button("Cancel", action: onCancel)
                }
            }
        }
    }
}

struct LegalBullet: View {
    let text: String

    var body: some View {
        HStack(alignment: .top, spacing: 8) {
            Text("•")
                .fontWeight(.bold)
            Text(text)
                .font(.footnote)
                .foregroundColor(.secondary)
        }
    }
}

struct CheckboxToggleStyle: ToggleStyle {
    func makeBody(configuration: Configuration) -> some View {
        HStack(alignment: .top, spacing: 10) {
            Image(systemName: configuration.isOn ? "checkmark.square.fill" : "square")
                .font(.title3)
                .foregroundColor(configuration.isOn ? .accentColor : .secondary)
                .onTapGesture {
                    configuration.isOn.toggle()
                }

            configuration.label
                .onTapGesture {
                    configuration.isOn.toggle()
                }
        }
    }
}

#Preview {
    CompendiumSourcesView()
        .environment(\.managedObjectContext, PersistenceController.preview.container.viewContext)
}
