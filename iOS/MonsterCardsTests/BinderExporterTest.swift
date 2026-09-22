//
//  BinderExporterTest.swift
//  MonsterCardsTests
//
//  Unit tests for BinderExporter — mirrors Android's BinderImportExportTest.
//

import XCTest
@testable import MonsterCards

final class BinderExporterTest: XCTestCase {

    // MARK: - Export with collection name

    func testExportBinder_singleCollection_createsValidJson() throws {
        let monster = sampleMonster()

        let json = BinderExporter.exportBinder(collectionName: "Goblins", monsters: [monster])

        // Verify root structure.
        guard let data = json.data(using: .utf8),
              let root = try JSONSerialization.jsonObject(with: data) as? [String: Any],
              let collections = root["collections"] as? [[String: Any]],
              let col = collections.first,
              let cards = col["cards"] as? [[String: Any]] else {
            XCTFail("JSON structure invalid")
            return
        }

        XCTAssertEqual(root["$schema"], "https://majinnaibu.com/schemas/binder.schema.json")
        XCTAssertEqual(root["schemaVersion"] as? Int, 1)
        XCTAssertEqual(col["name"] as? String, "Goblins")
        XCTAssertEqual(cards.count, 1)

        // Verify monster card fields.
        let card = cards[0]
        XCTAssertEqual(card["$schema"] as? String, "https://majinnaibu.com/schemas/monster-card.schema.json")
        XCTAssertEqual(card["schemaVersion"] as? Int, 1)
        XCTAssertEqual(card["id"] as? String, monster.id?.uuidString ?? "")
        XCTAssertEqual(card["name"] as? String, "Goblin Warrior")
        XCTAssertEqual(card["size"] as? String, "Small")
        XCTAssertEqual(card["type"] as? String, "humanoid")
    }

    func testExportBinder_customId_preservesIt() throws {
        let monster = sampleMonster()
        let customId = UUID()

        let json = BinderExporter.exportBinder(collectionName: "", monsters: [monster], idOverride: customId)

        guard let data = json.data(using: .utf8),
              let root = try JSONSerialization.jsonObject(with: data) as? [String: Any],
              let collections = root["collections"] as? [[String: Any]],
              let cards = collections.first["cards"] as? [[String: Any]] else {
            XCTFail("JSON structure invalid")
            return
        }

        XCTAssertEqual(cards[0]["id"] as? String, customId.uuidString)
    }

    // MARK: - Export with full backup (dashboard)

    func testExportFullBackup_withDashboard_includesDashboardField() throws {
        let col = BinderExport.CollectionExport(
            id: "test-col-id",
            name: "Test Collection",
            description: "A test collection",
            cards: [MonsterExport.makeDictionary(monster: sampleMonster())]
        )

        let json = BinderExporter.exportFullBackup(collections: [col], dashboardMonsters: [sampleMonster()])

        guard let data = json.data(using: .utf8),
              let root = try JSONSerialization.jsonObject(with: data) as? [String: Any] else {
            XCTFail("JSON structure invalid")
            return
        }

        XCTAssertEqual(root["$schema"] as? String, "https://majinnaibu.com/schemas/binder.schema.json")

        // Should have a dashboard field with at least 1 entry.
        XCTAssertTrue(root["dashboard"] is NSArray || root["dashboard"] is [[String: Any]])
    }

    func testExportFullBackup_withoutDashboard_excludesDashboardField() throws {
        let col = BinderExport.CollectionExport(
            id: "uuid-test",
            name: "No Dash",
            description: "",
            cards: []
        )

        let json = BinderExporter.exportFullBackup(collections: [col], dashboardMonsters: nil)

        XCTAssertFalse(json.contains("\"dashboard\""), "Should not contain 'dashboard' when none provided")
    }

    // MARK: - Ability score mapping

    func testExportBinder_abilityScores_matchJsonKeys() throws {
        let monster = sampleMonster()

        let json = BinderExporter.exportBinder(collectionName: "", monsters: [monster])

        guard let data = json.data(using: .utf8),
              let root = try JSONSerialization.jsonObject(with: data) as? [String: Any],
              let collections = root["collections"] as? [[String: Any]],
              let cards = collections.first["cards"] as? [[String: Any]] else {
            XCTFail("JSON structure invalid")
            return
        }

        let monsterJson = cards[0]

        // Verify ability score keys and values.
        XCTAssertEqual(monsterJson["strengthScore"] as? Int64, monster.strengthScore)
        XCTAssertEqual(monsterJson["dexterityScore"] as? Int64, monster.dexterityScore)
        XCTAssertEqual(monsterJson["constitutionScore"] as? Int64, monster.constitutionScore)
        XCTAssertEqual(monsterJson["intelligenceScore"] as? Int64, monster.intelligenceScore)
        XCTAssertEqual(monsterJson["wisdomScore"] as? Int64, monster.wisdomScore)
        XCTAssertEqual(monsterJson["charismaScore"] as? Int64, monster.charismaScore)

        // Verify saving throw keys exist.
        XCTAssertNotNil(monsterJson["strengthSavingThrowProficiency"])
        XCTAssertNotNil(monsterJson["strengthSavingThrowAdvantage"])
        XCTAssertNotNil(monsterJson["dexteritySavingThrowProficiency"])
    }

    // MARK: - Round-trip (export then verify it would import)

    func testExportRoundTrip_matchesImportExportTest_expectancy() throws {
        // Mirror the Android BinderImportExportTest.testExportAndImportBinder expectations.
        let monster = sampleMonster()
        let json = BinderExporter.exportBinder(collectionName: "Goblins", monsters: [monster])

        // Deserialize to verify shape (simulates what the Android BinderImporter would read).
        guard let data = json.data(using: .utf8),
              let root = try JSONSerialization.jsonObject(with: data) as? [String: Any],
              let collections = root["collections"] as? [[String: Any]],
              let col = collections.first,
              let cards = col["cards"] as? [[String: Any]] else {
            XCTFail("Failed to parse exported JSON")
            return
        }

        XCTAssertEqual(root["schemaVersion"] as? Int, 1)
        XCTAssertEqual(collections.count, 1)
        XCTAssertEqual(col["name"] as? String, "Goblins")
        XCTAssertEqual(cards.count, 1)
        XCTAssertEqual(cards[0]["name"] as? String, "Goblin Warrior")
        XCTAssertEqual(cards[0]["schemaVersion"] as? Int, 1)
    }

    // MARK: - All MonsterViewModel fields present

    func testExportAllMonsterFields_haveValidKeys() throws {
        let monster = sampleFullMonster()

        let json = BinderExporter.exportBinder(collectionName: "", monsters: [monster])

        guard let data = json.data(using: .utf8),
              let root = try JSONSerialization.jsonObject(with: data) as? [String: Any],
              let cards = (root["collections"] as? [[String: Any]])?.first["cards"] as? [[String: Any]] else {
            XCTFail("JSON structure invalid")
            return
        }

        let m = cards[0]

        // Verify all expected keys are present.
        let expectedKeys: [String] = [
            "$schema", "schemaVersion", "id", "name", "size", "type", "subtype",
            "alignment",
            "strengthScore", "dexterityScore", "constitutionScore", "intelligenceScore",
            "wisdomScore", "charismaScore",
            "strengthSavingThrowProficiency", "strengthSavingThrowAdvantage",
            "dexteritySavingThrowProficiency", "dexteritySavingThrowAdvantage",
            "constitutionSavingThrowProficiency", "constitutionSavingThrowAdvantage",
            "intelligenceSavingThrowProficiency", "intelligenceSavingThrowAdvantage",
            "wisdomSavingThrowProficiency", "wisdomSavingThrowAdvantage",
            "charismaSavingThrowProficiency", "charismaSavingThrowAdvantage",
            "armorType", "shieldBonus", "naturalArmorBonus", "otherArmorDescription",
            "hitDice", "hasCustomHP", "customHitPointsDescription",
            "walkSpeed", "burrowSpeed", "climbSpeed", "flySpeed",
            "canHover", "swimSpeed", "hasCustomSpeed", "customSpeedDescription",
            "challengeRating", "customChallengeRatingDescription", "customProficiencyBonus",
            "telepathyRange", "understandsButDescription",
            "senses", "damageImmunities", "damageResistances", "damageVulnerabilities",
            "conditionImmunities", "skills", "languages",
            "abilities", "actions", "reactions",
            "legendaryActions", "lairActions", "regionalActions",
            "bonusActions"
        ]

        for key in expectedKeys {
            XCTAssertTrue(m.keys.contains(key), "Missing key '\(key)' in export; keys found: \(m.keys.sorted())")
        }
    }

    // MARK: - Helpers

    private func sampleMonster() -> MonsterViewModel {
        let monster = MonsterViewModel()
        monster.name = "Goblin Warrior"
        monster.size = "Small"
        monster.type = "humanoid"
        monster.subType = "goblinoid"
        monster.alignment = "neutral evil"
        monster.strengthScore = 8
        monster.dexterityScore = 14
        // Keep id as default (not set).
        return monster
    }

    private func sampleFullMonster() -> MonsterViewModel {
        let m = sampleMonster()
        m.hitDice = 2
        m.strengthScore = 8
        m.dexterityScore = 14
        m.constitutionScore = 10
        m.intelligenceScore = 8
        m.wisdomScore = 8
        m.charismaScore = 8

        m.walkSpeed = 30
        m.hitDice = 2

        // Add some damage immunities.
        let di = StringViewModel("fire")
        m.damageImmunities.append(di)

        // Add abilities.
        m.abilities.append(AbilityViewModel("Nimble Escape", "Disengage or Hide as a bonus action."))
        m.actions.append(AbilityViewModel("Scimitar", "+4 to hit, 1d6+2 slashing"))

        return m
    }
}
