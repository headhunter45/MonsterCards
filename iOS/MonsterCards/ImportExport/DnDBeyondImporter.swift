//
//  DnDBeyondImporter.swift
//  MonsterCards
//
//  Created by Antigravity on 10/1/26.
//

import Foundation

struct DnDBeyondImporter: EntityImporter {
    private static let dndBeyondUrlRegex = try? NSRegularExpression(
        pattern: #"https?://(?:www\.)?dndbeyond\.com/characters/(\d+)(?:/([a-zA-Z0-9]+))?"#,
        options: [.caseInsensitive]
    )
    private static let characterIdRegex = try? NSRegularExpression(
        pattern: #"^\d+$"#,
        options: []
    )

    static func canImport(_ input: String) -> Bool {
        let trimmed = input.trimmingCharacters(in: .whitespacesAndNewlines)
        guard !trimmed.isEmpty else { return false }

        let range = NSRange(trimmed.startIndex..., in: trimmed)
        if let reg = dndBeyondUrlRegex, reg.firstMatch(in: trimmed, range: range) != nil {
            return true
        }
        if let reg = characterIdRegex, reg.firstMatch(in: trimmed, range: range) != nil {
            return true
        }

        if trimmed.hasPrefix("{") && trimmed.hasSuffix("}"),
           let data = trimmed.data(using: .utf8),
           let root = try? JSONSerialization.jsonObject(with: data) as? [String: Any] {
            if let d = root["data"] as? [String: Any], d["baseHitPoints"] != nil {
                return true
            }
        }
        return false
    }

    static func extractCharacterId(from input: String) -> String? {
        let trimmed = input.trimmingCharacters(in: .whitespacesAndNewlines)
        let range = NSRange(trimmed.startIndex..., in: trimmed)
        if let reg = dndBeyondUrlRegex,
           let match = reg.firstMatch(in: trimmed, range: range),
           let r1 = Range(match.range(at: 1), in: trimmed) {
            return String(trimmed[r1])
        }
        if let reg = characterIdRegex, reg.firstMatch(in: trimmed, range: range) != nil {
            return trimmed
        }
        return nil
    }

    @MainActor
    static func parse(_ input: String) throws -> MonsterViewModel {
        let trimmed = input.trimmingCharacters(in: .whitespacesAndNewlines)
        var characterData: [String: Any]?

        if trimmed.hasPrefix("{") {
            guard let data = trimmed.data(using: .utf8),
                  let root = try? JSONSerialization.jsonObject(with: data) as? [String: Any] else {
                throw ImporterError.invalidFormat("Failed to parse JSON for D&D Beyond")
            }
            characterData = (root["data"] as? [String: Any]) ?? root
        }

        guard let charData = characterData else {
            throw ImporterError.invalidFormat("Cannot parse D&D Beyond without character JSON data or use fetchCharacter()")
        }

        return buildMonster(from: charData)
    }

    @MainActor
    static func fetchAndParse(characterId: String) async throws -> MonsterViewModel {
        let serviceUrl = "https://character-service.dndbeyond.com/character/v5/character/\(characterId)?includeCustomItems=true"
        guard let url = URL(string: serviceUrl) else {
            throw ImporterError.invalidFormat("Invalid D&D Beyond URL")
        }

        var req = URLRequest(url: url)
        req.setValue("application/json", forHTTPHeaderField: "Accept")
        req.setValue("MonsterCards-iOS", forHTTPHeaderField: "User-Agent")
        req.timeoutInterval = 15

        let (data, response) = try await URLSession.shared.data(for: req)
        guard let httpResponse = response as? HTTPURLResponse, (200...299).contains(httpResponse.statusCode) else {
            let code = (response as? HTTPURLResponse)?.statusCode ?? -1
            throw ImporterError.invalidFormat("D&D Beyond service returned HTTP \(code)")
        }

        guard let root = try? JSONSerialization.jsonObject(with: data) as? [String: Any],
              let charData = root["data"] as? [String: Any] else {
            throw ImporterError.invalidFormat("D&D Beyond response missing character data")
        }

        return buildMonster(from: charData)
    }

    @MainActor
    private static func buildMonster(from data: [String: Any]) -> MonsterViewModel {
        let monster = MonsterViewModel()

        var rawName = (data["name"] as? String) ?? "D&D Beyond Character"
        if rawName.hasPrefix("\"") && rawName.hasSuffix("\"") && rawName.count > 1 {
            rawName = String(rawName.dropFirst().dropLast())
        }
        monster.name = rawName
        monster.size = "Medium"
        monster.type = "Humanoid"

        parseClassesAndLevel(data: data, monster: monster)
        parseStats(data: data, monster: monster)
        parseHitPoints(data: data, monster: monster)
        parseSpeed(data: data, monster: monster)
        parseModifiers(data: data, monster: monster)
        parseTraits(data: data, monster: monster)
        parseActions(data: data, monster: monster)

        return monster
    }

    @MainActor
    private static func parseClassesAndLevel(data: [String: Any], monster: MonsterViewModel) {
        var classSummaryParts: [String] = []
        var totalLevel = 0

        if let classes = data["classes"] as? [[String: Any]] {
            for cls in classes {
                let level = (cls["level"] as? Int) ?? 1
                totalLevel += level
                var className = ""
                if let def = cls["definition"] as? [String: Any] {
                    className = (def["name"] as? String) ?? ""
                }
                var subclassName = ""
                if let subDef = cls["subclassDefinition"] as? [String: Any] {
                    subclassName = (subDef["name"] as? String) ?? ""
                }
                var item = className
                if !subclassName.isEmpty {
                    item += " (\(subclassName))"
                }
                item += " \(level)"
                classSummaryParts.append(item)
            }
        }
        monster.hitDice = Int64(totalLevel > 0 ? totalLevel : 1)

        var raceName = ""
        if let race = data["race"] as? [String: Any] {
            raceName = (race["fullName"] as? String) ?? (race["baseRaceName"] as? String) ?? ""
        }

        var subtypeParts: [String] = []
        if totalLevel > 0 {
            subtypeParts.append("Level \(totalLevel)")
        }
        if !raceName.isEmpty {
            subtypeParts.append(raceName)
        }
        if !classSummaryParts.isEmpty {
            subtypeParts.append(classSummaryParts.joined(separator: " / "))
        }
        monster.subType = subtypeParts.joined(separator: " ")
    }

    @MainActor
    private static func parseStats(data: [String: Any], monster: MonsterViewModel) {
        if let stats = data["stats"] as? [[String: Any]] {
            for stat in stats {
                let id = (stat["id"] as? Int) ?? 0
                let val = Int64((stat["value"] as? Int) ?? 10)
                switch id {
                case 1: monster.strengthScore = val
                case 2: monster.dexterityScore = val
                case 3: monster.constitutionScore = val
                case 4: monster.intelligenceScore = val
                case 5: monster.wisdomScore = val
                case 6: monster.charismaScore = val
                default: break
                }
            }
        }
    }

    @MainActor
    private static func parseHitPoints(data: [String: Any], monster: MonsterViewModel) {
        let baseHp = (data["baseHitPoints"] as? Int) ?? 0
        let overrideHp = (data["overrideHitPoints"] as? Int) ?? 0
        let maxHp = overrideHp > 0 ? overrideHp : baseHp
        if maxHp > 0 {
            monster.hasCustomHP = true
            monster.customHP = "\(maxHp) (\(monster.hitDice)d8)"
        }
    }

    @MainActor
    private static func parseSpeed(data: [String: Any], monster: MonsterViewModel) {
        if let race = data["race"] as? [String: Any],
           let weightSpeeds = race["weightSpeeds"] as? [String: Any],
           let normal = weightSpeeds["normal"] as? [String: Any] {
            monster.walkSpeed = Int64((normal["walk"] as? Int) ?? 30)
            monster.flySpeed = Int64((normal["fly"] as? Int) ?? 0)
            monster.burrowSpeed = Int64((normal["burrow"] as? Int) ?? 0)
            monster.swimSpeed = Int64((normal["swim"] as? Int) ?? 0)
            monster.climbSpeed = Int64((normal["climb"] as? Int) ?? 0)
        }
        if monster.walkSpeed == 0 {
            monster.walkSpeed = 30
        }
    }

    @MainActor
    private static func parseModifiers(data: [String: Any], monster: MonsterViewModel) {
        guard let modifiersObj = data["modifiers"] as? [String: Any] else { return }
        let categories = ["race", "class", "background", "item", "feat"]

        for cat in categories {
            guard let mods = modifiersObj[cat] as? [[String: Any]] else { continue }
            for mod in mods {
                let type = (mod["type"] as? String) ?? ""
                let subType = (mod["subType"] as? String) ?? ""
                let fixedVal = (mod["fixedValue"] as? Int) ?? 0

                if type == "set-base" && subType == "darkvision" {
                    let dist = fixedVal > 0 ? fixedVal : 60
                    monster.senses.append(StringViewModel("darkvision \(dist) ft."))
                } else if type == "language" {
                    let langName = subType.replacingOccurrences(of: "-", with: " ").capitalized
                    monster.languages.append(LanguageViewModel(langName, true))
                } else if type == "proficiency" {
                    if subType.hasSuffix("-saving-throws") {
                        let stat = subType.replacingOccurrences(of: "-saving-throws", with: "")
                        applySavingThrowProficiency(monster: monster, stat: stat)
                    } else {
                        applySkillProficiency(monster: monster, subType: subType, profType: .proficient)
                    }
                } else if type == "expertise" {
                    applySkillProficiency(monster: monster, subType: subType, profType: .expertise)
                } else if type == "immunity" {
                    monster.damageImmunities.append(StringViewModel(subType.replacingOccurrences(of: "-", with: " ").capitalized))
                } else if type == "resistance" {
                    monster.damageResistances.append(StringViewModel(subType.replacingOccurrences(of: "-", with: " ").capitalized))
                } else if type == "vulnerability" {
                    monster.damageVulnerabilities.append(StringViewModel(subType.replacingOccurrences(of: "-", with: " ").capitalized))
                }
            }
        }
    }

    @MainActor
    private static func applySavingThrowProficiency(monster: MonsterViewModel, stat: String) {
        if stat == "strength" || stat == "str" { monster.strengthSavingThrowProficiency = .proficient }
        else if stat == "dexterity" || stat == "dex" { monster.dexteritySavingThrowProficiency = .proficient }
        else if stat == "constitution" || stat == "con" { monster.constitutionSavingThrowProficiency = .proficient }
        else if stat == "intelligence" || stat == "int" { monster.intelligenceSavingThrowProficiency = .proficient }
        else if stat == "wisdom" || stat == "wis" { monster.wisdomSavingThrowProficiency = .proficient }
        else if stat == "charisma" || stat == "cha" { monster.charismaSavingThrowProficiency = .proficient }
    }

    @MainActor
    private static func applySkillProficiency(monster: MonsterViewModel, subType: String, profType: ProficiencyType) {
        let skillName = subType.replacingOccurrences(of: "-", with: " ").capitalized
        let abilityScore = getSkillAbility(skillName)
        monster.skills.append(SkillViewModel(skillName, abilityScore, profType, .none))
    }

    private static func getSkillAbility(_ name: String) -> AbilityScore {
        let lower = name.lowercased()
        if lower.contains("acrobatics") || lower.contains("sleight") || lower.contains("stealth") { return .dexterity }
        if lower.contains("arcana") || lower.contains("history") || lower.contains("investigation") || lower.contains("nature") || lower.contains("religion") { return .intelligence }
        if lower.contains("animal") || lower.contains("insight") || lower.contains("medicine") || lower.contains("perception") || lower.contains("survival") { return .wisdom }
        if lower.contains("deception") || lower.contains("intimidation") || lower.contains("performance") || lower.contains("persuasion") { return .charisma }
        return .strength
    }

    @MainActor
    private static func parseTraits(data: [String: Any], monster: MonsterViewModel) {
        if let race = data["race"] as? [String: Any],
           let traits = race["racialTraits"] as? [[String: Any]] {
            for tr in traits {
                if let def = tr["definition"] as? [String: Any] {
                    let name = (def["name"] as? String) ?? ""
                    var desc = stripHtml((def["snippet"] as? String) ?? "")
                    if desc.isEmpty {
                        desc = stripHtml((def["description"] as? String) ?? "")
                    }
                    if !name.isEmpty && !desc.isEmpty {
                        monster.abilities.append(AbilityViewModel(name, desc))
                    }
                }
            }
        }
    }

    @MainActor
    private static func parseActions(data: [String: Any], monster: MonsterViewModel) {
        guard let actionsObj = data["actions"] as? [String: Any] else { return }
        let categories = ["class", "race", "background", "item", "feat"]

        for cat in categories {
            guard let acts = actionsObj[cat] as? [[String: Any]] else { continue }
            for act in acts {
                let name = (act["name"] as? String) ?? ""
                var desc = stripHtml((act["snippet"] as? String) ?? "")
                if desc.isEmpty {
                    desc = stripHtml((act["description"] as? String) ?? "")
                }
                var actType = 1
                if let activation = act["activation"] as? [String: Any] {
                    actType = (activation["activationType"] as? Int) ?? 1
                }
                if !name.isEmpty {
                    let vm = AbilityViewModel(name, desc)
                    if actType == 3 {
                        // Bonus action
                        monster.actions.append(vm)
                    } else if actType == 4 {
                        // Reaction
                        monster.reactions.append(vm)
                    } else {
                        monster.actions.append(vm)
                    }
                }
            }
        }
    }

    private static func stripHtml(_ html: String) -> String {
        html.replacingOccurrences(of: "<[^>]+>", with: "", options: .regularExpression)
            .replacingOccurrences(of: "&nbsp;", with: " ")
            .trimmingCharacters(in: .whitespacesAndNewlines)
    }
}
