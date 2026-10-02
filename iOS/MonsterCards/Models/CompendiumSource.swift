//
//  CompendiumSource.swift
//  MonsterCards
//

import Foundation

enum CompendiumImportType: String, Codable, CaseIterable, Sendable {
    case open5eApi = "OPEN5E_API"
    case gitArchive = "GIT_ARCHIVE"
}

struct CompendiumSource: Identifiable, Hashable, Sendable {
    let id: String
    let name: String
    let description: String
    let gameSystem: GameSystem
    let sourceLabel: String
    let bookSource: String
    let gitRepoUrl: String
    let downloadUrl: String?
    let importType: CompendiumImportType

    init(
        id: String,
        name: String,
        description: String,
        gameSystem: GameSystem,
        sourceLabel: String,
        bookSource: String = "",
        gitRepoUrl: String,
        downloadUrl: String? = nil,
        importType: CompendiumImportType
    ) {
        self.id = id
        self.name = name
        self.description = description
        self.gameSystem = gameSystem
        self.sourceLabel = sourceLabel
        self.bookSource = bookSource
        self.gitRepoUrl = gitRepoUrl
        self.downloadUrl = downloadUrl
        self.importType = importType
    }
}

enum CompendiumRegistry {
    static let defaultSources: [CompendiumSource] = [
        CompendiumSource(
            id: "open5e_srd",
            name: "Open5e 5e SRD",
            description: "Full SRD 5.1 creature compendium provided through the Open5e REST API.",
            gameSystem: .dnd5e,
            sourceLabel: "5e SRD",
            bookSource: "SRD 5.1",
            gitRepoUrl: "https://github.com/open5e/open5e-api",
            downloadUrl: "https://api.open5e.com/v2/creatures/?limit=50",
            importType: .open5eApi
        ),
        CompendiumSource(
            id: "pf2e_bestiary",
            name: "Pathfinder 2e Bestiary (Community)",
            description: "Community-maintained Pathfinder 2e monsters and NPCs from the Foundry VTT PF2e system repository.",
            gameSystem: .pf2e,
            sourceLabel: "PF2e Bestiary",
            bookSource: "Bestiary",
            gitRepoUrl: "https://github.com/foundryvtt/pf2e",
            downloadUrl: "https://github.com/foundryvtt/pf2e/releases/download/pf2e-8.5.1/json-assets.zip",
            importType: .gitArchive
        ),
        CompendiumSource(
            id: "sf2e_alien_archive",
            name: "Starfinder 2e Playtest Bestiary (Community)",
            description: "Starfinder 2e playtest alien archive creatures from the community repository.",
            gameSystem: .sf2e,
            sourceLabel: "SF2e Playtest",
            bookSource: "Alien Archive",
            gitRepoUrl: "https://github.com/foundryvtt/sf2e",
            downloadUrl: "https://github.com/foundryvtt/pf2e/releases/download/sf2e-1.5.1/json-assets.zip",
            importType: .gitArchive
        )
    ]

    static func source(for id: String) -> CompendiumSource? {
        defaultSources.first { $0.id == id }
    }
}
