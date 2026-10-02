//
//  MonsterCardExporter.swift
//  MonsterCards
//
//  Created by Antigravity on 10/1/26.
//

import Foundation

struct MonsterCardExporter {
    @MainActor
    static func exportCardJSON(_ monster: MonsterViewModel) throws -> String {
        var dict: [String: Any] = [
            "schema": "https://majinnaibu.com/schemas/monster-card.schema.json",
            "schemaVersion": 1,
            "name": monster.name,
            "size": monster.size,
            "type": monster.type,
            "subType": monster.subType,
            "alignment": monster.alignment,
            "hitDice": monster.hitDice,
            "hasCustomHP": monster.hasCustomHP,
            "customHP": monster.customHP,
            "armorType": monster.armorType.rawValue,
            "hasShield": monster.hasShield,
            "shieldBonus": monster.shieldBonus,
            "naturalArmorBonus": monster.naturalArmorBonus,
            "customArmor": monster.customArmor,
            "otherArmorDescription": monster.otherArmorDescription,
            "walkSpeed": monster.walkSpeed,
            "burrowSpeed": monster.burrowSpeed,
            "climbSpeed": monster.climbSpeed,
            "flySpeed": monster.flySpeed,
            "canHover": monster.canHover,
            "swimSpeed": monster.swimSpeed,
            "hasCustomSpeed": monster.hasCustomSpeed,
            "customSpeed": monster.customSpeed,
            "strengthScore": monster.strengthScore,
            "dexterityScore": monster.dexterityScore,
            "constitutionScore": monster.constitutionScore,
            "intelligenceScore": monster.intelligenceScore,
            "wisdomScore": monster.wisdomScore,
            "charismaScore": monster.charismaScore,
            "strengthSavingThrowProficiency": monster.strengthSavingThrowProficiency.rawValue,
            "dexteritySavingThrowProficiency": monster.dexteritySavingThrowProficiency.rawValue,
            "constitutionSavingThrowProficiency": monster.constitutionSavingThrowProficiency.rawValue,
            "intelligenceSavingThrowProficiency": monster.intelligenceSavingThrowProficiency.rawValue,
            "wisdomSavingThrowProficiency": monster.wisdomSavingThrowProficiency.rawValue,
            "charismaSavingThrowProficiency": monster.charismaSavingThrowProficiency.rawValue,
            "challengeRating": monster.challengeRating.rawValue,
            "customChallengeRating": monster.customChallengeRating,
            "customProficiencyBonus": monster.customProficiencyBonus,
            "isBlind": monster.isBlind,
            "telepathy": monster.telepathy,
            "understandsBut": monster.understandsBut
        ]

        dict["skills"] = monster.skills.map { [
            "name": $0.name,
            "abilityScore": $0.abilityScore.rawValue,
            "advantage": $0.advantage.rawValue,
            "proficiency": $0.proficiency.rawValue
        ] }

        dict["damageImmunities"] = monster.damageImmunities.map { $0.name }
        dict["damageResistances"] = monster.damageResistances.map { $0.name }
        dict["damageVulnerabilities"] = monster.damageVulnerabilities.map { $0.name }
        dict["conditionImmunities"] = monster.conditionImmunities.map { $0.name }
        dict["senses"] = monster.senses.map { $0.name }
        dict["languages"] = monster.languages.map { ["name": $0.name, "speaks": $0.speaks] }

        dict["abilities"] = monster.abilities.map { ["name": $0.name, "description": $0.description] }
        dict["actions"] = monster.actions.map { ["name": $0.name, "description": $0.description] }
        dict["reactions"] = monster.reactions.map { ["name": $0.name, "description": $0.description] }
        dict["legendaryActions"] = monster.legendaryActions.map { ["name": $0.name, "description": $0.description] }
        dict["lairActions"] = monster.lairActions.map { ["name": $0.name, "description": $0.description] }
        dict["regionalActions"] = monster.regionalActions.map { ["name": $0.name, "description": $0.description] }

        let data = try JSONSerialization.data(withJSONObject: dict, options: [.prettyPrinted, .sortedKeys])
        return String(data: data, encoding: .utf8) ?? "{}"
    }

    @MainActor
    static func exportMarkdown(_ monster: MonsterViewModel) -> String {
        var sb = ""
        sb += "# \(monster.name.isEmpty ? "Monster" : monster.name)\n\n"
        sb += "*\(monster.meta)*\n\n"
        sb += "---\n\n"
        sb += "- **Armor Class** \(monster.armorClassDescription)\n"
        sb += "- **Hit Points** \(monster.hitPoints)\n"
        sb += "- **Speed** \(monster.speed)\n\n"
        sb += "---\n\n"
        sb += "| STR | DEX | CON | INT | WIS | CHA |\n"
        sb += "|:---:|:---:|:---:|:---:|:---:|:---:|\n"
        sb += "| \(monster.strengthScore) (\(monster.strengthModifier >= 0 ? "+" : "")\(monster.strengthModifier)) | \(monster.dexterityScore) (\(monster.dexterityModifier >= 0 ? "+" : "")\(monster.dexterityModifier)) | \(monster.constitutionScore) (\(monster.constitutionModifier >= 0 ? "+" : "")\(monster.constitutionModifier)) | \(monster.intelligenceScore) (\(monster.intelligenceModifier >= 0 ? "+" : "")\(monster.intelligenceModifier)) | \(monster.wisdomScore) (\(monster.wisdomModifier >= 0 ? "+" : "")\(monster.wisdomModifier)) | \(monster.charismaScore) (\(monster.charismaModifier >= 0 ? "+" : "")\(monster.charismaModifier)) |\n\n"
        sb += "---\n\n"

        if !monster.savingThrowsDescription.isEmpty {
            sb += "- **Saving Throws** \(monster.savingThrowsDescription)\n"
        }
        if !monster.skillsDescription.isEmpty {
            sb += "- **Skills** \(monster.skillsDescription)\n"
        }
        if !monster.damageVulnerabilitiesDescription.isEmpty {
            sb += "- **Damage Vulnerabilities** \(monster.damageVulnerabilitiesDescription)\n"
        }
        if !monster.damageResistancesDescription.isEmpty {
            sb += "- **Damage Resistances** \(monster.damageResistancesDescription)\n"
        }
        if !monster.damageImmunitiesDescription.isEmpty {
            sb += "- **Damage Immunities** \(monster.damageImmunitiesDescription)\n"
        }
        if !monster.conditionImmunitiesDescription.isEmpty {
            sb += "- **Condition Immunities** \(monster.conditionImmunitiesDescription)\n"
        }
        if !monster.sensesDescription.isEmpty {
            sb += "- **Senses** \(monster.sensesDescription)\n"
        }
        if !monster.languagesDescription.isEmpty {
            sb += "- **Languages** \(monster.languagesDescription)\n"
        }
        sb += "- **Challenge** \(monster.challengeRatingDescription) (Proficiency Bonus +\(monster.proficiencyBonus))\n\n"

        if !monster.abilities.isEmpty {
            sb += "---\n\n### Special Traits\n\n"
            for ab in monster.abilities {
                sb += "***\(ab.name).*** \(ab.description)\n\n"
            }
        }

        if !monster.actions.isEmpty {
            sb += "---\n\n### Actions\n\n"
            for act in monster.actions {
                sb += "***\(act.name).*** \(act.description)\n\n"
            }
        }

        if !monster.reactions.isEmpty {
            sb += "---\n\n### Reactions\n\n"
            for r in monster.reactions {
                sb += "***\(r.name).*** \(r.description)\n\n"
            }
        }

        if !monster.legendaryActions.isEmpty {
            sb += "---\n\n### Legendary Actions\n\n"
            for l in monster.legendaryActions {
                sb += "***\(l.name).*** \(l.description)\n\n"
            }
        }

        return sb
    }
}
