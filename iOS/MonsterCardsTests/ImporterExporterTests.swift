//
//  ImporterExporterTests.swift
//  MonsterCardsTests
//
//  Created by Antigravity on 10/1/26.
//

import XCTest
@testable import MonsterCards

@MainActor
final class ImporterExporterTests: XCTestCase {

    func testOpen5eImporterParsing() throws {
        let open5eJson = """
        {
            "slug": "adult-red-dragon",
            "name": "Adult Red Dragon",
            "size": "Huge",
            "type": "dragon",
            "subtype": "",
            "alignment": "chaotic evil",
            "armor_class": 19,
            "armor_desc": "natural armor",
            "hit_points": 256,
            "hit_dice": "19d12 + 133",
            "speed": {
                "walk": 40,
                "climb": 40,
                "fly": 80
            },
            "strength": 27,
            "dexterity": 10,
            "constitution": 25,
            "intelligence": 16,
            "wisdom": 13,
            "charisma": 21,
            "strength_save": null,
            "dexterity_save": 6,
            "constitution_save": 13,
            "intelligence_save": null,
            "wisdom_save": 7,
            "charisma_save": 11,
            "perception": 13,
            "skills": {
                "perception": 13,
                "stealth": 6
            },
            "damage_vulnerabilities": "",
            "damage_resistances": "",
            "damage_immunities": "fire",
            "condition_immunities": "",
            "senses": "blindsight 60 ft., darkvision 120 ft., passive Perception 23",
            "languages": "Common, Draconic",
            "challenge_rating": "17",
            "actions": [
                {
                    "name": "Multiattack",
                    "desc": "The dragon can use its Frightful Presence. It then makes three attacks: one with its bite and two with its claws."
                },
                {
                    "name": "Bite",
                    "desc": "Melee Weapon Attack: +14 to hit, reach 10 ft., one target. Hit: 19 (2d10 + 8) piercing damage plus 7 (2d6) fire damage."
                }
            ],
            "legendary_actions": [
                {
                    "name": "Detect",
                    "desc": "The dragon makes a Wisdom (Perception) check."
                },
                {
                    "name": "Tail Attack",
                    "desc": "The dragon makes a tail attack."
                }
            ]
        }
        """

        let vm = try Open5eImporter.parse(open5eJson)
        XCTAssertEqual(vm.name, "Adult Red Dragon")
        XCTAssertEqual(vm.size, "Huge")
        XCTAssertEqual(vm.type, "dragon")
        XCTAssertEqual(vm.alignment, "chaotic evil")
        XCTAssertEqual(vm.gameSystem, .dnd5e)
        XCTAssertEqual(vm.sourceLabel, "open5e.com")
        XCTAssertEqual(vm.strengthScore, 27)
        XCTAssertEqual(vm.dexterityScore, 10)
        XCTAssertEqual(vm.constitutionScore, 25)
        XCTAssertEqual(vm.challengeRating, .seventeen)
        XCTAssertEqual(vm.damageImmunities.map { $0.name.lowercased() }, ["fire"])
        XCTAssertEqual(vm.actions.count, 2)
        XCTAssertEqual(vm.legendaryActions.count, 2)
        XCTAssertEqual(vm.actions.first?.name, "Multiattack")
    }

    func testDnDBeyondImporterParsing() throws {
        let ddbJson = """
        {
            "name": "Mind Flayer",
            "size": "Medium",
            "type": "aberration",
            "alignment": "lawful evil",
            "armorClass": 15,
            "armorType": "breastplate",
            "hitPoints": 71,
            "hitDice": "13d8+13",
            "speed": { "walk": 30 },
            "stats": {
                "STR": 11,
                "DEX": 12,
                "CON": 12,
                "INT": 19,
                "WIS": 17,
                "CHA": 17
            },
            "challengeRating": "7",
            "languages": ["Deep Speech", "Undercommon", "telepathy 120 ft."],
            "actions": [
                {
                    "name": "Tentacles",
                    "description": "Melee Weapon Attack: +7 to hit, reach 5 ft., one creature."
                }
            ]
        }
        """

        let vm = try DnDBeyondImporter.parse(ddbJson)
        XCTAssertEqual(vm.name, "Mind Flayer")
        XCTAssertEqual(vm.size, "Medium")
        XCTAssertEqual(vm.type, "aberration")
        XCTAssertEqual(vm.intelligenceScore, 19)
        XCTAssertEqual(vm.challengeRating, .seven)
        XCTAssertEqual(vm.actions.count, 1)
        XCTAssertEqual(vm.actions.first?.name, "Tentacles")
    }

    func testPf2eImporterParsing() throws {
        let pf2eJson = """
        {
            "name": "Goblin Warrior",
            "size": "small",
            "traits": ["humanoid", "goblin"],
            "alignment": "NE",
            "level": -1,
            "hp": 6,
            "ac": 16,
            "speed": 25,
            "abilities": {
                "str": 0,
                "dex": 3,
                "con": 1,
                "int": 0,
                "wis": -1,
                "cha": -1
            },
            "items": ["Dogslicer", "Shortbow"]
        }
        """

        let vm = try Pf2eImporter.parse(pf2eJson)
        XCTAssertEqual(vm.name, "Goblin Warrior")
        XCTAssertEqual(vm.size, "Small")
        XCTAssertEqual(vm.dexterityScore, 16) // 10 + 3*2 = 16
        XCTAssertEqual(vm.challengeRating, .oneEighth)
    }

    func testMonsterCardExporterRoundtrip() throws {
        let vm = MonsterViewModel()
        vm.name = "Beholder"
        vm.size = "Large"
        vm.type = "aberration"
        vm.alignment = "lawful evil"
        vm.strengthScore = 10
        vm.dexterityScore = 14
        vm.constitutionScore = 18
        vm.intelligenceScore = 17
        vm.wisdomScore = 15
        vm.charismaScore = 17
        vm.challengeRating = .thirteen

        let exportedJson = try MonsterCardExporter.exportCardJSON(vm)
        XCTAssertTrue(exportedJson.contains("Beholder"))
        XCTAssertTrue(exportedJson.contains("aberration"))
        XCTAssertTrue(exportedJson.contains("monster-card.schema.json"))

        let markdown = MonsterCardExporter.exportMarkdown(vm)
        XCTAssertTrue(markdown.contains("# Beholder"))
        XCTAssertTrue(markdown.contains("STR | DEX | CON | INT | WIS | CHA"))
    }

    func testImporterRegistryDetection() throws {
        let open5eJson = "{\"slug\": \"zombie\", \"name\": \"Zombie\", \"challenge_rating\": \"1/4\", \"actions\": []}"
        let parsed = ImporterRegistry.importMonster(from: open5eJson)
        XCTAssertNotNil(parsed)
        XCTAssertEqual(parsed?.name, "Zombie")
    }
}
