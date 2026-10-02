//
//  Pf2eImporter.swift
//  MonsterCards
//
//  Created by Antigravity on 10/1/26.
//

import Foundation

struct Pf2eImporter: EntityImporter {
    static func canImport(_ input: String) -> Bool {
        let trimmed = input.trimmingCharacters(in: .whitespacesAndNewlines)
        if trimmed.hasPrefix("[") && trimmed.hasSuffix("]") {
            guard let data = trimmed.data(using: .utf8),
                  let arr = try? JSONSerialization.jsonObject(with: data) as? [[String: Any]],
                  let first = arr.first else {
                return false
            }
            return (first["type"] as? String) == "npc" || (first["system"] as? [String: Any])?["attributes"] != nil
        }
        guard trimmed.hasPrefix("{") && trimmed.hasSuffix("}") else { return false }
        guard let data = trimmed.data(using: .utf8),
              let root = try? JSONSerialization.jsonObject(with: data) as? [String: Any] else {
            return false
        }
        if let type = root["type"] as? String, type == "npc" {
            if let system = root["system"] as? [String: Any], system["attributes"] != nil {
                return true
            }
        }
        return false
    }

    @MainActor
    static func parse(_ input: String) throws -> MonsterViewModel {
        let trimmed = input.trimmingCharacters(in: .whitespacesAndNewlines)
        guard let data = trimmed.data(using: .utf8),
              let root = try? JSONSerialization.jsonObject(with: data) as? [String: Any] else {
            throw ImporterError.invalidFormat("Failed to parse JSON for Pathfinder 2e")
        }

        return parse(jsonObject: root)
    }

    @MainActor
    static func parseArray(_ input: String) throws -> [MonsterViewModel] {
        let trimmed = input.trimmingCharacters(in: .whitespacesAndNewlines)
        guard let data = trimmed.data(using: .utf8),
              let arr = try? JSONSerialization.jsonObject(with: data) as? [[String: Any]] else {
            throw ImporterError.invalidFormat("Failed to parse JSON array for Pathfinder 2e")
        }

        return arr.compactMap { obj in
            if (obj["type"] as? String) == "npc" || (obj["system"] as? [String: Any])?["attributes"] != nil {
                return parse(jsonObject: obj)
            }
            return nil
        }
    }

    @MainActor
    static func parse(jsonObject root: [String: Any]) -> MonsterViewModel {
        let system = (root["system"] as? [String: Any]) ?? root
        let monster = MonsterViewModel()

        // Name
        monster.name = (root["name"] as? String) ?? "Unknown PF2e Monster"

        // Traits: size, type
        if let sizeStr = (root["size"] as? String) ?? (system["size"] as? String) {
            monster.size = mapPf2eSize(sizeStr)
        } else if let traits = system["traits"] as? [String: Any] {
            if let sizeObj = traits["size"] as? [String: Any],
               let sizeVal = sizeObj["value"] as? String {
                monster.size = mapPf2eSize(sizeVal)
            } else if let sizeVal = traits["size"] as? String {
                monster.size = mapPf2eSize(sizeVal)
            }
            if let tags = traits["value"] as? [String] {
                monster.type = tags.map { $0.capitalized }.joined(separator: ", ")
            }
        }

        if monster.type.isEmpty, let traitsArr = root["traits"] as? [String] {
            monster.type = traitsArr.first?.capitalized ?? ""
        }

        // Ability Scores (PF2e stores modifiers: mod -> score = 10 + mod * 2)
        let abilities = (system["abilities"] as? [String: Any]) ?? (root["abilities"] as? [String: Any]) ?? [:]
        monster.strengthScore = Int64(10 + getMod(abilities, "str") * 2)
        monster.dexterityScore = Int64(10 + getMod(abilities, "dex") * 2)
        monster.constitutionScore = Int64(10 + getMod(abilities, "con") * 2)
        monster.intelligenceScore = Int64(10 + getMod(abilities, "int") * 2)
        monster.wisdomScore = Int64(10 + getMod(abilities, "wis") * 2)
        monster.charismaScore = Int64(10 + getMod(abilities, "cha") * 2)

        // Attributes (AC, HP, Speed)
        let attributes = (system["attributes"] as? [String: Any]) ?? root
        if let acVal = (attributes["ac"] as? Int) {
            monster.armorType = .other
            monster.otherArmorDescription = "\(acVal)"
        } else if let acObj = attributes["ac"] as? [String: Any], let acVal = acObj["value"] as? Int {
            monster.armorType = .other
            monster.otherArmorDescription = "\(acVal)"
        }

        if let maxHp = (attributes["hp"] as? Int) {
            monster.hasCustomHP = true
            monster.customHP = "\(maxHp)"
        } else if let hpObj = attributes["hp"] as? [String: Any], let maxHp = hpObj["max"] as? Int {
            monster.hasCustomHP = true
            monster.customHP = "\(maxHp)"
        }

        if let speedVal = (attributes["speed"] as? Int) {
            monster.walkSpeed = Int64(speedVal)
        } else if let speedObj = attributes["speed"] as? [String: Any], let speedVal = speedObj["value"] as? Int {
            monster.walkSpeed = Int64(speedVal)
        }

        // Details (Level -> CR, Languages)
        let details = (system["details"] as? [String: Any]) ?? root
        if let level = (details["level"] as? Int) {
            monster.challengeRating = mapLevelToCr(level)
        } else if let levelObj = details["level"] as? [String: Any], let level = levelObj["value"] as? Int {
            monster.challengeRating = mapLevelToCr(level)
        }

        if let langArr = (details["languages"] as? [String]) {
            for l in langArr {
                monster.languages.append(LanguageViewModel(l.capitalized, true))
            }
        } else if let langObj = details["languages"] as? [String: Any], let langArr = langObj["value"] as? [String] {
            for l in langArr {
                monster.languages.append(LanguageViewModel(l.capitalized, true))
            }
        }

        return monster
    }

    private static func getMod(_ dict: [String: Any], _ key: String) -> Int {
        if let val = dict[key] as? Int {
            return val
        }
        if let obj = dict[key] as? [String: Any], let mod = obj["mod"] as? Int {
            return mod
        }
        return 0
    }

    private static func mapPf2eSize(_ size: String) -> String {
        switch size.lowercased() {
        case "tiny": return "Tiny"
        case "sm", "small": return "Small"
        case "med", "medium": return "Medium"
        case "lg", "large": return "Large"
        case "huge": return "Huge"
        case "grg", "gargantuan": return "Gargantuan"
        default: return size.capitalized
        }
    }

    private static func mapLevelToCr(_ level: Int) -> ChallengeRating {
        if level <= -1 { return .oneEighth }
        if level == 0 { return .zero }
        if level >= 30 { return .thirty }
        return ChallengeRating(rawValue: "\(level)") ?? .one
    }
}

