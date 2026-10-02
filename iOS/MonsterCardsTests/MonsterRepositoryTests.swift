//
//  MonsterRepositoryTests.swift
//  MonsterCardsTests
//
//  Created by Antigravity on 10/1/26.
//

import XCTest
import CoreData
@testable import MonsterCards

@MainActor
final class MonsterRepositoryTests: XCTestCase {
    var repository: MonsterRepository!
    var context: NSManagedObjectContext!

    override func setUp() async throws {
        try await super.setUp()
        let controller = PersistenceController(inMemory: true)
        context = controller.container.viewContext
        repository = MonsterRepository(persistenceController: controller)
    }

    override func tearDown() async throws {
        repository = nil
        context = nil
        try await super.tearDown()
    }

    func testCreateAndFetchMonster() throws {
        let monster = Monster(context: context)
        let uuid = UUID()
        monster.uuid = uuid
        monster.name = "Gelatinous Cube"
        monster.size = "Large"
        monster.type = "ooze"
        monster.alignment = "unaligned"
        monster.challengeRating = "2"
        try context.save()

        let fetched = try repository.fetchMonster(by: uuid)
        XCTAssertNotNil(fetched)
        XCTAssertEqual(fetched?.name, "Gelatinous Cube")
        XCTAssertEqual(fetched?.size, "Large")

        let all = try repository.fetchMonsters()
        XCTAssertEqual(all.count, 1)
        XCTAssertEqual(all.first?.name, "Gelatinous Cube")
    }

    func testSearchAndFilterMonsters() throws {
        let m1 = Monster(context: context)
        m1.uuid = UUID()
        m1.name = "Fire Elemental"
        m1.type = "elemental"
        m1.size = "Large"
        m1.challengeRating = "5"

        let m2 = Monster(context: context)
        m2.uuid = UUID()
        m2.name = "Water Elemental"
        m2.type = "elemental"
        m2.size = "Large"
        m2.challengeRating = "5"

        let m3 = Monster(context: context)
        m3.uuid = UUID()
        m3.name = "Goblin"
        m3.type = "humanoid"
        m3.size = "Small"
        m3.challengeRating = "1/4"

        try context.save()

        let fireResults = try repository.fetchMonsters(query: "Fire", type: nil, cr: nil)
        XCTAssertEqual(fireResults.count, 1)
        XCTAssertEqual(fireResults.first?.name, "Fire Elemental")

        let elementalResults = try repository.fetchMonsters(query: nil, type: "elemental", cr: nil)
        XCTAssertEqual(elementalResults.count, 2)

        let crResults = try repository.fetchMonsters(query: nil, type: nil, cr: "1/4")
        XCTAssertEqual(crResults.count, 1)
        XCTAssertEqual(crResults.first?.name, "Goblin")
    }

    func testDeleteMonster() throws {
        let monster = Monster(context: context)
        let uuid = UUID()
        monster.uuid = uuid
        monster.name = "Temporary Monster"
        try context.save()

        XCTAssertEqual(try repository.fetchMonsters().count, 1)

        try repository.deleteMonster(id: uuid)
        XCTAssertEqual(try repository.fetchMonsters().count, 0)
    }

    func testCollectionLinking() throws {
        let monster = Monster(context: context)
        let monsterUUID = UUID()
        monster.uuid = monsterUUID
        monster.name = "Orc"

        let collection = Collection(context: context)
        collection.name = "Dungeon Ambush"
        collection.details = "Wave 1"

        try context.save()

        let link = CollectionMonster(context: context)
        link.collectionId = collection.name ?? ""
        link.monsterId = monsterUUID.uuidString

        try context.save()

        let collections = try repository.fetchCollections()
        XCTAssertEqual(collections.count, 1)

        let monstersInCollection = try repository.fetchMonsters(for: collection)
        XCTAssertEqual(monstersInCollection.count, 1)
        XCTAssertEqual(monstersInCollection.first?.name, "Orc")
    }
}

