//
//  Open5eImporter.swift
//  MonsterCards
//
//  Created by Antigravity on 10/1/26.
//

import Foundation

struct Open5eImporter: EntityImporter {

    private static let hitDiceRegex = try? NSRegularExpression(pattern: "(\\d+)d(\\d+)", options: [])

    static func canImport(_ input: String) -> Bool {
        let trimmed = input.trimmingCharacters(in: .whitespacesAndNewlines)
        guard trimmed.hasPrefix("{") && trimmed.hasSuffix("}") else { return false }
        guard let data = trimmed.data(using: .utf8),
              let root = try? JSONSerialization.jsonObject(with: data) as? [String: Any] else {
            return false
        }
        if let rulesetId = root["ruleset_id"] as? String, rulesetId == "open5e" {
            return true
        }
        if let props = root["properties"] as? [String: Any], props["abilities"] != nil {
            return true
        }
        if root["slug"] != nil || root["challenge_rating"] != nil || root["hit_points"] != nil {
            return true
        }
        return false
    }

    @MainActor
    static func parse(_ input: String) throws -> MonsterViewModel {
        let trimmed = input.trimmingCharacters(in: .whitespacesAndNewlines)
        guard let data = trimmed.data(using: .utf8),
              let root = try? JSONSerialization.jsonObject(with: data) as? [String: Any] else {
            throw ImporterError.invalidFormat("Invalid JSON for Open5e")
        }

        let monster = MonsterViewModel()
        let props = (root["properties"] as? [String: Any]) ?? root

        // Name
        if let displayName = root["display_name"] as? String, !displayName.isEmpty {
            monster.name = displayName
        } else if let name = props["name"] as? String, !name.isEmpty {
            monster.name = name
        } else {
            monster.name = "Unnamed Open5e Entity"
        }

        // Basic Info
        monster.size = (props["size"] as? String) ?? ""
        monster.type = (props["type"] as? String) ?? ""
        monster.subType = (props["subtype"] as? String) ?? ""
        monster.alignment = (props["alignment"] as? String) ?? ""

        parseArmorClass(props: props, monster: monster)
        parseHitPoints(props: props, monster: monster)
        parseSpeed(props: props, monster: monster)
        parseChallengeRating(props: props, monster: monster)
        parseAbilities(props: props, monster: monster)
        parseSavingThrows(props: props, monster: monster)
        parseSkills(props: props, monster: monster)
        parseSenses(props: props, monster: monster)
        parseLanguages(props: props, monster: monster)
        parseDamageAndConditions(props: props, monster: monster)
        parseTraitsAndActions(props: props, monster: monster)
        monster.gameSystem = .dnd5e
        monster.sourceLabel = "open5e.com"

        return monster
    }

    @MainActor
    private static func parseArmorClass(props: [String: Any], monster: MonsterViewModel) {
        if let armorObj = props["equipped_armor"] as? [String: Any],
           let armorId = armorObj["id"] as? String {
            if let armorType = ArmorType(rawValue: armorId) {
                monster.armorType = armorType
            }
        }

        if let shieldObj = props["equipped_shield"] as? [String: Any],
           let bonus = shieldObj["ac_bonus"] as? Int {
            monster.hasShield = bonus > 0
            monster.shieldBonus = bonus
        }

        if monster.armorType == .none, let desc = props["armor_description"] as? String, !desc.isEmpty {
            monster.armorType = .other
            monster.otherArmorDescription = desc
        }
    }

    @MainActor
    private static func parseHitPoints(props: [String: Any], monster: MonsterViewModel) {
        if let hpObj = props["hit_points"] as? [String: Any] {
            let maxHp = (hpObj["max"] as? Int) ?? (hpObj["current"] as? Int) ?? 10
            if let hitDiceStr = hpObj["hit_dice"] as? String,
               let regex = hitDiceRegex,
               let match = regex.firstMatch(in: hitDiceStr, range: NSRange(hitDiceStr.startIndex..., in: hitDiceStr)),
               let r1 = Range(match.range(at: 1), in: hitDiceStr),
               let count = Int64(hitDiceStr[r1]) {
                monster.hitDice = count
            }
            if let formula = hpObj["formula"] as? String, !formula.isEmpty {
                monster.hasCustomHP = true
                monster.customHP = "\(maxHp) (\(formula))"
            }
        } else if let hp = props["hit_points"] as? Int {
            monster.hasCustomHP = true
            monster.customHP = "\(hp)"
        }
    }

    @MainActor
    private static func parseSpeed(props: [String: Any], monster: MonsterViewModel) {
        if let speedObj = props["speed"] as? [String: Any] {
            monster.walkSpeed = Int64((speedObj["walk"] as? Int) ?? 30)
            monster.flySpeed = Int64((speedObj["fly"] as? Int) ?? 0)
            monster.swimSpeed = Int64((speedObj["swim"] as? Int) ?? 0)
            monster.burrowSpeed = Int64((speedObj["burrow"] as? Int) ?? 0)
            monster.climbSpeed = Int64((speedObj["climb"] as? Int) ?? 0)
            monster.canHover = (speedObj["hover"] as? Bool) ?? false
        } else if let speed = props["speed"] as? Int {
            monster.walkSpeed = Int64(speed)
        }

        if let desc = props["speed_desc"] as? String, !desc.isEmpty {
            monster.hasCustomSpeed = true
            monster.customSpeed = desc
        }
    }

    @MainActor
    private static func parseChallengeRating(props: [String: Any], monster: MonsterViewModel) {
        var crStr = ""
        if let cr = props["cr"] as? String {
            crStr = cr
        } else if let cr = props["challenge_rating"] as? String {
            crStr = cr
        } else if let crNum = props["cr"] as? Double {
            if crNum == 0.125 { crStr = "1/8" }
            else if crNum == 0.25 { crStr = "1/4" }
            else if crNum == 0.5 { crStr = "1/2" }
            else { crStr = String(Int(crNum)) }
        }

        if !crStr.isEmpty {
            crStr = crStr.replacingOccurrences(of: ".0", with: "")
            if crStr == "0.125" { crStr = "1/8" }
            if crStr == "0.25" { crStr = "1/4" }
            if crStr == "0.5" { crStr = "1/2" }
            if let crEnum = ChallengeRating(rawValue: crStr) {
                monster.challengeRating = crEnum
            }
        }
    }

    @MainActor
    private static func parseAbilities(props: [String: Any], monster: MonsterViewModel) {
        let abs = (props["abilities"] as? [String: Any]) ?? props
        monster.strengthScore = Int64(extractScore(abs, "strength"))
        monster.dexterityScore = Int64(extractScore(abs, "dexterity"))
        monster.constitutionScore = Int64(extractScore(abs, "constitution"))
        monster.intelligenceScore = Int64(extractScore(abs, "intelligence"))
        monster.wisdomScore = Int64(extractScore(abs, "wisdom"))
        monster.charismaScore = Int64(extractScore(abs, "charisma"))
    }

    private static func extractScore(_ dict: [String: Any], _ key: String) -> Int {
        if let val = dict[key] as? Int { return val }
        let shortKey = String(key.prefix(3))
        if let val = dict[shortKey] as? Int { return val }
        if let obj = dict[key] as? [String: Any], let val = obj["value"] as? Int { return val }
        return 10
    }

    @MainActor
    private static func parseSavingThrows(props: [String: Any], monster: MonsterViewModel) {
        if let savesObj = props["saving_throws"] as? [String: Any] {
            for key in savesObj.keys {
                applySaveProficiency(monster: monster, str: key.lowercased())
            }
        } else if let arr = props["saving_throws"] as? [String] {
            for s in arr {
                applySaveProficiency(monster: monster, str: s.lowercased())
            }
        }

        if props["strength_save"] != nil { monster.strengthSavingThrowProficiency = .proficient }
        if props["dexterity_save"] != nil { monster.dexteritySavingThrowProficiency = .proficient }
        if props["constitution_save"] != nil { monster.constitutionSavingThrowProficiency = .proficient }
        if props["intelligence_save"] != nil { monster.intelligenceSavingThrowProficiency = .proficient }
        if props["wisdom_save"] != nil { monster.wisdomSavingThrowProficiency = .proficient }
        if props["charisma_save"] != nil { monster.charismaSavingThrowProficiency = .proficient }
    }

    @MainActor
    private static func applySaveProficiency(monster: MonsterViewModel, str: String) {
        if str.contains("str") { monster.strengthSavingThrowProficiency = .proficient }
        else if str.contains("dex") { monster.dexteritySavingThrowProficiency = .proficient }
        else if str.contains("con") { monster.constitutionSavingThrowProficiency = .proficient }
        else if str.contains("int") { monster.intelligenceSavingThrowProficiency = .proficient }
        else if str.contains("wis") { monster.wisdomSavingThrowProficiency = .proficient }
        else if str.contains("cha") { monster.charismaSavingThrowProficiency = .proficient }
    }

    @MainActor
    private static func parseSkills(props: [String: Any], monster: MonsterViewModel) {
        if let skillsObj = props["skill_bonuses"] as? [String: Any] {
            for key in skillsObj.keys {
                let name = key.replacingOccurrences(of: "_", with: " ").capitalized
                let ability = getAbilityForSkill(name)
                monster.skills.append(SkillViewModel(name, ability, .proficient, .none))
            }
        } else if let arr = props["skills"] as? [String] {
            for item in arr {
                let name = item.replacingOccurrences(of: "[+-]\\d+", with: "", options: .regularExpression).trimmingCharacters(in: .whitespaces).capitalized
                if !name.isEmpty {
                    let ability = getAbilityForSkill(name)
                    monster.skills.append(SkillViewModel(name, ability, .proficient, .none))
                }
            }
        }
    }

    private static func getAbilityForSkill(_ name: String) -> AbilityScore {
        let lower = name.lowercased()
        if lower.contains("acro") || lower.contains("sleight") || lower.contains("stealth") { return .dexterity }
        if lower.contains("arca") || lower.contains("hist") || lower.contains("inve") || lower.contains("natu") || lower.contains("reli") { return .intelligence }
        if lower.contains("anim") || lower.contains("insi") || lower.contains("medi") || lower.contains("perc") || lower.contains("surv") { return .wisdom }
        if lower.contains("dece") || lower.contains("inti") || lower.contains("perf") || lower.contains("pers") { return .charisma }
        return .strength
    }

    @MainActor
    private static func parseSenses(props: [String: Any], monster: MonsterViewModel) {
        if let sensesStr = props["senses"] as? String {
            for s in sensesStr.components(separatedBy: ",") {
                let clean = s.trimmingCharacters(in: .whitespaces)
                if !clean.isEmpty {
                    monster.senses.append(StringViewModel(clean))
                }
            }
        }
    }

    @MainActor
    private static func parseLanguages(props: [String: Any], monster: MonsterViewModel) {
        if let langStr = props["languages"] as? String {
            for l in langStr.components(separatedBy: ",") {
                let clean = l.trimmingCharacters(in: .whitespaces)
                if !clean.isEmpty {
                    monster.languages.append(LanguageViewModel(clean, true))
                }
            }
        } else if let langArr = props["languages"] as? [String] {
            for l in langArr {
                let clean = l.trimmingCharacters(in: .whitespaces)
                if !clean.isEmpty {
                    monster.languages.append(LanguageViewModel(clean, true))
                }
            }
        }
    }

    @MainActor
    private static func parseDamageAndConditions(props: [String: Any], monster: MonsterViewModel) {
        let resSource = (props["resistances_and_immunities"] as? [String: Any]) ?? props
        parseStrings(from: resSource, key: "damage_immunities", into: &monster.damageImmunities)
        parseStrings(from: resSource, key: "damage_resistances", into: &monster.damageResistances)
        parseStrings(from: resSource, key: "damage_vulnerabilities", into: &monster.damageVulnerabilities)
        parseStrings(from: resSource, key: "condition_immunities", into: &monster.conditionImmunities)
    }

    @MainActor
    private static func parseStrings(from dict: [String: Any], key: String, into list: inout [StringViewModel]) {
        if let arr = dict[key] as? [String] {
            for item in arr {
                let clean = item.trimmingCharacters(in: .whitespaces).capitalized
                if !clean.isEmpty { list.append(StringViewModel(clean)) }
            }
        } else if let str = dict[key] as? String {
            for item in str.components(separatedBy: ",") {
                let clean = item.trimmingCharacters(in: .whitespaces).capitalized
                if !clean.isEmpty { list.append(StringViewModel(clean)) }
            }
        }
    }

    @MainActor
    private static func parseTraitsAndActions(props: [String: Any], monster: MonsterViewModel) {
        parseAbilityList(props: props, key: "traits", into: &monster.abilities)
        parseAbilityList(props: props, key: "actions", into: &monster.actions)
        parseAbilityList(props: props, key: "reactions", into: &monster.reactions)
        parseAbilityList(props: props, key: "legendary_actions", into: &monster.legendaryActions)
        parseAbilityList(props: props, key: "lair_actions", into: &monster.lairActions)
        parseAbilityList(props: props, key: "regional_effects", into: &monster.regionalActions)
    }

    @MainActor
    private static func parseAbilityList(props: [String: Any], key: String, into list: inout [AbilityViewModel]) {
        guard let arr = props[key] as? [[String: Any]] else { return }
        for item in arr {
            let name = (item["name"] as? String) ?? ""
            let desc = (item["desc"] as? String) ?? (item["description"] as? String) ?? ""
            if !name.isEmpty {
                list.append(AbilityViewModel(name, desc))
            }
        }
    }
}

public enum ImporterError: LocalizedError {
    case invalidFormat(String)

    public var errorDescription: String? {
        switch self {
        case .invalidFormat(let msg): return msg
        }
    }
}
