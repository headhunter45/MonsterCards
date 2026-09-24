//
//  BinderImportModels.swift
//  MonsterCards
//
//  Data models for parsing Binder (TRPG platform) JSON format.
//  Field names match the keys produced by Binder's serializer.
//

import Foundation

/// Top-level Binder export file containing collections of monster cards.
struct BinderRoot: Decodable {
    let schemaVersion: Int?
    let collections: [CollectionModel]
}

/// A named collection (folder) within a Binder export.
struct CollectionModel: Decodable {
    let name: String
    let cards: [MonsterCard]
}

/// A single monster card exported from the Binder platform.
struct MonsterCard: Decodable {
    
    // MARK: - Basic info
    
    enum CodingKeys: String, CodingKey {
        case name, displayName, displayType, alignment, sizeAbbrv
        case shieldName, baseAcFormula, baseAcVal, shieldAcNum, extraArmorAcMod
        case hitDice, customHpText, speedText
        case primaryAttributes, proSavingThrows
        case proficiencySkills, proPerception
        case damageImmunities, damageVulnerabilities, conditionImmunities
        case specialdamage, languageLine, sensesDescription
        case crDisplay, proficiencyBonusValue
        case cardTraits, legendaryActionsWithHeader
    }
    
    let name: String?
    let displayName: String?
    let displayType: String?
    let alignment: String?
    let sizeAbbrv: String?
    
    // MARK: - Armor / HP
    
    /// Shield name from Binder (e.g. "Ring of Protection").
    let shieldName: String?
    
    /// Base AC formula string (e.g. "dexterity + 12" or "Unarmored Defense (+10) + dexterity").
    let baseAcFormula: String?
    
    /// Numeric baseAC value.
    let baseAcVal: Int?
    
    /// Shield bonus contribution to AC.
    let shieldAcNum: Int?
    
    /// Additional armor modifier (e.g. "+4", "-1").
    let extraArmorAcMod: String?
    
    /// Hit dice (e.g. "38d10").
    let hitDice: String?
    
    /// Custom HP text when the monster has custom HP.
    let customHpText: String?
    
    // MARK: - Speed
    
    /// Formatted speed string (e.g. "40 feet, climb 30 feet").
    let speedText: String?
    
    // MARK: - Attributes / ability scores
    
    /// Ability score boxes ordered [STR, DEX, CON, INT, WIS, CHA].
    let primaryAttributes: [CardAttributeBox]?
    
    // MARK: - Saving throws
    
    /// Names of saving throws this monster is proficient in (e.g. ["str", "dex"]).
    let proSavingThrows: [String]?
    
    // MARK: - Skills
    
    /// Proficiency-based skills with expertise flag.
    let proficiencySkills: [ProfSkillBox]?
    
    /// Whether the monster has expert Perception (from Binder).
    let proPerception: Int?   // 0 or 1 stored as int; nil means false
    
    // MARK: - Damage / condition immunities
    
    /// Comma-joined damage immunities string.
    let damageImmunities: String?
    
    /// Comma-joined damage vulnerabilities string.
    let damageVulnerabilities: String?
    
    /// Comma-joined condition immunities string.
    let conditionImmunities: String?
    
    /// Special damage types from Binder (radiant, etc.) with source/target.
    let specialdamage: [SpecialDamageBox]?
    
    // MARK: - Languages & senses
    
    /// Pre-formatted languages line (e.g. "Common, Deep Speech (150 ft.)").
    let languageLine: String?
    
    /// Pre-formatted senses line.
    let sensesDescription: String?
    
    // MARK: - Challenge rating / AC display
    
    /// Display challenge rating (e.g. "3", "Half" for 1/2).
    let crDisplay: String?
    
    /// Proficiency bonus numeric value.
    let proficiencyBonusValue: Int?
    
    /// Card trait entries (abilities/actions) with section headers and grouping flags.
    let cardTraits: [CardTraitEntry]?
    
    /// Whether the card already prepended a "Legendary Actions" heading.
    let legendaryActionsWithHeader: Int?   // 0/1
}

/// An ability score box on a Binder monster card.
struct CardAttributeBox: Decodable {
    let abbrv: String?      // str, dex, con, int, wis, cha
    let value: Int?         // score (e.g. 18)
    let modifier: String?   // display modifier string ("+4")
}

/// A proficiency-based skill entry (e.g. Perception).
struct ProfSkillBox: Decodable {
    let name: String?
    let isExpertise: Int?   // 0 or 1 stored as int; nil means false
    
    enum CodingKeys: String, CodingKey {
        case name, expertise
    }
    
    init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        name = try container.decodeIfPresent(String.self, forKey: .name)
        
        if let expertiseValue = (try? container.decode(Int.self, forKey: .expertise)), expertiseValue == 1 {
            self.isExpertise = 1
        } else {
            self.isExpertise = 0
        }
    }
}

/// Special damage type entry from Binder cards (e.g. radiant damage from a feature).
struct SpecialDamageBox: Decodable {
    let source: String?   // ability that causes the damage
    let value: String?    // damage dice/string
    let target: String?   // target description
}

/// A trait entry parsed from cardTraits array in Binder output.
struct CardTraitEntry: Decodable {
    let displayName: String?    // action/ability name
    let desc: String?           // ability text
    let isActionHeader: Int?    // 0 or 1 flag for section headers
    let actionSection: Int?     // grouping key (0=bonus actions, 1=actions, 2=reactions, etc.)
}
