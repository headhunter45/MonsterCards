//
//  CompendiumSourceManagerTests.swift
//  MonsterCardsTests
//

import XCTest
import CoreData
@testable import MonsterCards

class CompendiumSourceManagerTests: XCTestCase {
    
    var persistenceController: PersistenceController!
    var context: NSManagedObjectContext!
    
    override func setUp() {
        super.setUp()
        persistenceController = PersistenceController(inMemory: true)
        context = persistenceController.container.viewContext
    }
    
    override func tearDown() {
        context = nil
        persistenceController = nil
        super.tearDown()
    }
    
    func testCompendiumRegistrySources() {
        let sources = CompendiumRegistry.defaultSources
        XCTAssertGreaterThanOrEqual(sources.count, 3)
        
        guard let open5e = sources.first(where: { $0.id == "open5e_srd" }),
              let pf2e = sources.first(where: { $0.id == "pf2e_bestiary" }),
              let sf2e = sources.first(where: { $0.id == "sf2e_alien_archive" }) else {
            XCTFail("Expected sources missing in CompendiumRegistry")
            return
        }
        
        XCTAssertEqual(open5e.gameSystem, .dnd5e)
        XCTAssertEqual(pf2e.gameSystem, .pf2e)
        XCTAssertEqual(sf2e.gameSystem, .sf2e)
    }
    
    @MainActor
    func testSourceManagerDownloadAndClearState() async throws {
        guard let pf2eSource = CompendiumRegistry.source(for: "pf2e_bestiary") else {
            XCTFail("pf2e_bestiary source not found")
            return
        }
        
        let manager = CompendiumSourceManager.shared
        
        // Clear first
        manager.clearSource(sourceId: pf2eSource.id, context: context)
        let isDownloadedBefore = manager.isSourceDownloaded(sourceId: pf2eSource.id)
        XCTAssertFalse(isDownloadedBefore)
        
        let count = try await manager.downloadAndIngest(source: pf2eSource, context: context) { _, _ in }
        
        XCTAssertGreaterThan(count, 0)
        let isDownloadedAfter = manager.isSourceDownloaded(sourceId: pf2eSource.id)
        XCTAssertTrue(isDownloadedAfter)
        let monsterCount = manager.getSourceMonsterCount(sourceId: pf2eSource.id)
        XCTAssertEqual(monsterCount, count)
        
        // Query repo to ensure monsters exist
        let fetchedCount = ReferenceMonsterRepository.shared.countForSource(sourceId: pf2eSource.id, in: context)
        XCTAssertEqual(fetchedCount, count)
        
        // Clear again
        manager.clearSource(sourceId: pf2eSource.id, context: context)
        let isDownloadedAfterClear = manager.isSourceDownloaded(sourceId: pf2eSource.id)
        XCTAssertFalse(isDownloadedAfterClear)
        let afterClearCount = ReferenceMonsterRepository.shared.countForSource(sourceId: pf2eSource.id, in: context)
        XCTAssertEqual(afterClearCount, 0)
    }
    
    @MainActor
    func testSourceManagerShaAndEtagUpdateCheck() async {
        guard let pf2eSource = CompendiumRegistry.source(for: "pf2e_bestiary") else {
            XCTFail("pf2e_bestiary source not found")
            return
        }
        
        let manager = CompendiumSourceManager.shared
        manager.setSourceSha(sourceId: pf2eSource.id, sha: "old_sha_123456789")
        XCTAssertEqual(manager.getSourceSha(sourceId: pf2eSource.id), "old_sha_123456789")
        
        manager.setSourceEtag(sourceId: pf2eSource.id, etag: "W/\"etag123\"")
        XCTAssertEqual(manager.getSourceEtag(sourceId: pf2eSource.id), "W/\"etag123\"")
        
        let updateResult = await manager.checkForUpdates(source: pf2eSource)
        XCTAssertNotNil(updateResult)
    }
}

