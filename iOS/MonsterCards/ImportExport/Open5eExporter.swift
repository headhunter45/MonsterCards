//
//  Open5eExporter.swift
//  MonsterCards
//
//  Created by Antigravity on 10/1/26.
//

import Foundation

struct Open5eExporter {
    @MainActor
    static func exportMonster(_ monster: MonsterViewModel) throws -> String {
        var root: [String: Any] = [
            "$schema": "../../../schema/entity.json",
            "uuid": UUID().uuidString,
            "ruleset_id": "open5e",
            "entity_type": "character",
            "display_name": monster.name.isEmpty ? "Unnamed Entity" : monster.name,
            "description": monster.meta,
            "version": "1.0.0",
            "template_id": "stat_block"
        ]

        var props: [String: Any] = [
            "name": monster.name,
            "size": monster.size,
            "type": monster.type,
            "subtype": monster.subType,
            "alignment": monster.alignment,
            "armor_description": monster.armorClassDescription,
            "speed": monster.walkSpeed,
            "speed_desc": monster.speed,
            "challenge_rating": monster.challengeRatingDescription,
            "cr": monster.challengeRating.rawValue,
            "senses": monster.sensesDescription
        ]

        let abilities: [String: Any] = [
            "strength": monster.strengthScore,
            "dexterity": monster.dexterityScore,
            "constitution": monster.constitutionScore,
            "intelligence": monster.intelligenceScore,
            "wisdom": monster.wisdomScore,
            "charisma": monster.charismaScore
        ]
        props["abilities"] = abilities

        let hpObj: [String: Any] = [
            "formula": monster.hitPoints,
            "hit_dice": "\(monster.hitDice)d8"
        ]
        props["hit_points"] = hpObj

        if monster.armorType != .none {
            var armorObj: [String: Any] = [
                "id": monster.armorType.rawValue,
                "name": monster.armorType.rawValue.capitalized
            ]
            props["equipped_armor"] = armorObj
        }

        if monster.hasShield {
            props["equipped_shield"] = [
                "id": "shield",
                "name": "Shield",
                "ac_bonus": monster.shieldBonus > 0 ? monster.shieldBonus : 2
            ]
        }

        // Saving throws
        var saves: [String] = []
        if monster.strengthSavingThrowProficiency != .none { saves.append("Strength \(monster.strengthModifier + monster.proficiencyBonusForType(monster.strengthSavingThrowProficiency))") }
        if monster.dexteritySavingThrowProficiency != .none { saves.append("Dexterity \(monster.dexterityModifier + monster.proficiencyBonusForType(monster.dexteritySavingThrowProficiency))") }
        if monster.constitutionSavingThrowProficiency != .none { saves.append("Constitution \(monster.constitutionModifier + monster.proficiencyBonusForType(monster.constitutionSavingThrowProficiency))") }
        if monster.intelligenceSavingThrowProficiency != .none { saves.append("Intelligence \(monster.intelligenceModifier + monster.proficiencyBonusForType(monster.intelligenceSavingThrowProficiency))") }
        if monster.wisdomSavingThrowProficiency != .none { saves.append("Wisdom \(monster.wisdomModifier + monster.proficiencyBonusForType(monster.wisdomSavingThrowProficiency))") }
        if monster.charismaSavingThrowProficiency != .none { saves.append("Charisma \(monster.charismaModifier + monster.proficiencyBonusForType(monster.charismaSavingThrowProficiency))") }
        if !saves.isEmpty {
            props["saving_throws"] = saves
        }

        // Skills
        if !monster.skills.isEmpty {
            props["skills"] = monster.skills.map { "\($0.name.lowercased()) \($0.modifier(forMonster: monster))" }
        }

        // Damage & Conditions
        if !monster.damageImmunities.isEmpty { props["damage_immunities"] = monster.damageImmunities.map { $0.name } }
        if !monster.damageResistances.isEmpty { props["damage_resistances"] = monster.damageResistances.map { $0.name } }
        if !monster.damageVulnerabilities.isEmpty { props["damage_vulnerabilities"] = monster.damageVulnerabilities.map { $0.name } }
        if !monster.conditionImmunities.isEmpty { props["condition_immunities"] = monster.conditionImmunities.map { $0.name } }

        // Languages
        if !monster.languages.isEmpty {
            props["languages"] = monster.languages.map { $0.name }
        }

        // Traits & Actions
        if !monster.abilities.isEmpty {
            props["traits"] = monster.abilities.map { ["name": $0.name, "desc": $0.description] }
        }
        if !monster.actions.isEmpty {
            props["actions"] = monster.actions.map { ["name": $0.name, "desc": $0.description] }
        }
        if !monster.reactions.isEmpty {
            props["reactions"] = monster.reactions.map { ["name": $0.name, "desc": $0.description] }
        }
        if !monster.legendaryActions.isEmpty {
            props["legendary_actions"] = monster.legendaryActions.map { ["name": $0.name, "desc": $0.description] }
        }
        if !monster.lairActions.isEmpty {
            props["lair_actions"] = monster.lairActions.map { ["name": $0.name, "desc": $0.description] }
        }
        if !monster.regionalActions.isEmpty {
            props["regional_effects"] = monster.regionalActions.map { ["name": $0.name, "desc": $0.description] }
        }

        root["properties"] = props

        let data = try JSONSerialization.data(withJSONObject: root, options: [.prettyPrinted, .sortedKeys])
        return String(data: data, encoding: .utf8) ?? "{}"
    }
}
