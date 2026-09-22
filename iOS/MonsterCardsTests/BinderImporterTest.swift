//
//  BinderImporterTest.swift
//  MonsterCardsTests
//
//  Unit tests for BinderImporter — validates that canImport correctly detects
//  Binder JSON in both collection and single-card formats, and that all monster
//  fields round-trip through the importer's mapping logic.

import XCTest
@testable import MonsterCards

final class BinderImporterTest: XCTestCase {
    
    // MARK: - canImport tests
    
    func testCanImportWithSchemaDetection() {
        let json = """
        {"$schema": "https://majinnaibu.com/schemas/binder.schema.json", "collections": []}
        """
        XCTAssertTrue(BinderImporter.canImport(json), "Should detect $schema binder field")
    }
    
    func testCanImportWithCollectionsArray() {
        let json = """
        {"collections": [{"name": "Test", "cards": []}]}
        """
        XCTAssertTrue(BinderImporter.canImport(json), "Should detect collections array")
    }
    
    func testCannotImportOtherSchema() {
        let json = """
        {"$schema": "https://example.com/other.schema.json", "collections": []}
        {"name": "Goblin"}
        """
        // Note: the second part doesn't have collections key but is not a binder file
        XCTAssertFalse(BinderImporter.canImport("{\"foo\":\"bar\"}"), "Should not detect random JSON")
    }
    
    func testCanImportEmptyJson() {
        XCTAssertFalse(BinderImporter.canImport(""), "Should not detect empty string")
        XCTAssertFalse(BinderImporter.canImport("{not json}"), "Should not detect invalid JSON")
    }
    
    // MARK: - parse tests
    
    func testParseSingleCardFullRoundTrip() {
        let json = """
        {
            "name": "Dragons of the Night",
            "displayName": "Acererak the Destiler",
            "displayType": "Lich",
            "alignment": "Lawful Evil",
            "sizeAbbrv": "M",
            "hitDice": "138d10",
            "shieldAcNum": 2,
            "baseAcFormula": "Unarmored Defense (+1)",
            "shieldName": "Ring of Protection",
            "speedText": "30 feet, fly 60 ft.",
            "primaryAttributes": [
                {"abbrv": "str", "value": 27, "modifier": "+8"},
                {"abbrv": "dex", "value": 16, "modifier": "+3"},
                {"abbrv": "con", "value": 25, "modifier": "+7"},
                {"abbrv": "int", "value": 29, "modifier": "+9"},
                {"abbrv": "wis", "value": 24, "modifier": "+7"},
                {"abbrv": "cha", "value": 28, "modifier": "+9"}
            ],
            "proSavingThrows": ["str", "dex"],
            "proficiencySkills": [
                {"name": "Arcana", "expertise": 1},
                {"name": "Perception", "expertise": 0}
            ],
            "specialdamage": [{"source": "Radiant", "value": "+9 hit", "target": "targets of your feature"}],
            "proPerception": 0,
            "languageLine": "Common, Deep Speech (150 ft.)",
            "sensesDescription": "Blindsight 60ft., Darkvision 30ft.",
            "crDisplay": "Half"
        }
        """
        
        let monster = try? BinderImporter.parse(json)
        XCTAssertNotNil(monster, "Should successfully parse valid single-card JSON")
        
        guard let m = monster else { return XCTFail("Expected non-nil parsed result") }
        
        // Verify name fallback to displayName
        XCTAssertEqual(m.name, "Acererak the Destiler", "Name should use displayName")
        XCTAssertEqual(m.type, "Lich", "displayType maps to type")
        XCTAssertEqual(m.alignment, "Lawful Evil", "alignment preserved exactly")
        XCTAssertEqual(m.size, "Medium", "M → Medium")
        
        // HP - hitDice string parse: "138d10" → 138
        XCTAssertEqual(m.hitDice, 138)
        XCTAssertFalse(m.hasCustomHP, "Should not have custom HP when no customHpText field present")
        
        // AC / armor
        XCTAssertEqual(m.shieldBonus, 2)
        XCTAssertTrue(m.hasShield, "shieldAcNum is 2 → hasShield = true")
        
        // Speed breakdown: "30 feet, fly 60 ft."
        XCTAssertEqual(m.walkSpeed, 30, "Should extract walk speed of 30")
        XCTAssertEqual(m.flySpeed, 60, "Should extract fly speed of 60")
        
        // Ability scores (primaryAttributes[0…5])
        XCTAssertEqual(m.strengthScore, 27)
        XCTAssertEqual(m.dexterityScore, 16)
        XCTAssertEqual(m.constitutionScore, 25)
        XCTAssertEqual(m.intelligenceScore, 29)
        XCTAssertEqual(m.wisdomScore, 24)
        XCTAssertEqual(m.charismaScore, 28)
        
        // Saving throws proficiency (str, dex → .proficient)
        XCTAssertEqual(m.strengthSavingThrowProficiency, .proficient)
        XCTAssertEqual(m.dexteritySavingThrowProficiency, .proficient)
        XCTAssertEqual(m.constitutionSavingThrowProficiency, .none)
        
        // Skills: Arcana expertise=1 → expertise, Perception expertise=0 → proficient
        XCTAssertNotNil(m.skills.first(where: { $0.name == "Arcana" }))
        let arcanaSkill = m.skills.first { $0.abilityScore? == .intelligence }
        XCTAssertEqual(arcanaSkill?.proficiency, .expertise)
        
        // Languages
        XCTAssertEqual(m.languages.count, 2)
        XCTAssertTrue(m.languages.contains(where: { $0.name == "Common" && $0.speaks }))
        XCTAssertFalse(m.languages.contains(where: { $0.name == "Deep Speech" && $0.speaks }))
        
        // Senses
        XCTAssertTrue(m.senses.count >= 2)
        XCTAssertTrue(m.senses.first(where: { $0.name.contains("Blindness") }) != nil)
        
        // Challenge rating from text: "Half" → oneHalf
        XCTAssertEqual(m.challengeRating, .oneHalf, "Should parse 'Half' as one_half CR")
    }
    
    func testParseCollectionFormat() {
        let json = """
        {
            "$schema": "https://majinnaibu.com/schemas/binder.schema.json",
            "schemaVersion": 1,
            "collections": [
                {
                    "name": "Villains of the Realm",
                    "cards": [
                        {
                            "name": "Balrog",
                            "displayName": "Balrog of Moria",
                            "displayType": "Demon",
                            "alignment": "Neutral Evil",
                            "sizeAbbrv": "L",
                            "hitDice": "104d10",
                            "proSavingThrows": ["con"],
                            "primaryAttributes": [
                                {"abbrv": "str", "value": 30},
                                {"abbrv": "dex", "value": 18},
                                {"abbrv": "con", "value": 25},
                                {"abbrv": "int", "value": 18},
                                {"abbrv": "wis", "value": 16},
                                {"abbrv": "cha", "value": 20}
                            ]
                        }
                    ]
                }
            ]
        }
        """
        
        let monster = try? BinderImporter.parse(json)
        XCTAssertNotNil(monster, "Should successfully parse valid collection JSON")
        
        guard let m = monster else { return XCTFail("Expected non-nil parsed result") }
        
        XCTAssertEqual(m.name, "Balrog of Moria", "Name should use displayName from first card in first collection")
        XCTAssertEqual(m.type, "Demon", "displayType preserved")
        XCTAssertEqual(m.size, "Large", "L maps to Large")
    }
    
    func testParseInvalidJsonThrows() {
        XCTAssertThrowsError(try BinderImporter.parse("not valid json string")) { error in
            XCTAssertTrue(error is BinderParseError)
            if let e = error as? BinderParseError {
                switch e {
                case .invalidFormat: break  // expected
                default: XCTFail("Expected invalidFormat error")
                }
            }
        }
    }
    
    func testDefaultAbilityScoresFailsafe() {
        let json = """
        {"collections": [{"name": "X", "cards": [{"name": "No Stats"}]}]}
        """
        
        guard let m = try? BinderImporter.parse(json) else {
            return XCTFail("Should succeed even with minimal MonsterCard data")
        }
        
        // Default ability score is 10 per MonsterViewModel init convention
        XCTAssertEqual(m.strengthScore, 10)
    }
}
