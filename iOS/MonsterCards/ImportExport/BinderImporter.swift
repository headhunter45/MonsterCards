//
//  BinderImporter.swift
//  MonsterCards
//
//  Detects and parses Binder (TRPG platform) JSON format into a MonsterViewModel.
//  Companion to Android BinderImporter.java — shares the same detection logic (schema or collections key).
//

import Foundation

/// Imports a single monster from the Binder export format onto a new `MonsterViewModel`.
struct BinderImporter: EntityImporter {
    
    // MARK: - canImport
    
    /// Detects whether `raw` is a Binder import payload (single collection file or multi-collection file).
    static func canImport(_ raw: String) -> Bool {
        guard let data = raw.data(using: .utf8),
              let root = try? JSONSerialization.jsonObject(with: data, options: []) as? [String: Any] else {
            return false
        }
        
        // Check for $schema reference to binder.schema.json  (Android BinderImporter.java line ~32)
        if let schema = root["$schema"] as? String, schema.contains("binder.schema.json") {
            return true
        }
        
        // Fall back: check known Binder fields from collections array (matches Android logic)
        if root["collections"] is [Any]  {
            return !CollectionsCheck.isEmpty(try? JSONSerialization.jsonObject(with: data, options: []) as? [[String: Any]])
        }
        
        return false
    }
    
    // MARK: - parse
    
    static func parse(_ raw: String) throws -> MonsterViewModel {
        guard let data = raw.data(using: .utf8) else {
            throw BinderParseError.unsupportedEncoding
        }
        
        // Try full export with collections
        if let binderRoot = try? JSONDecoder().decode(BinderRoot.self, from: data),
           !binderRoot.collections.isEmpty,
           !binderRoot.collections[0].cards.isEmpty {
            return mapMonster(binderRoot.collections[0].cards[0])
        }
        
        // Fall back to single-card format
        guard let card = try? JSONDecoder().decode(MonsterCard.self, from: data) else {
            throw BinderParseError.invalidFormat("Failed to parse Binder JSON")
        }
        
        return mapMonster(card)
    }
    
    // MARK: - Mapping
    
    /// Maps a `MonsterCard` (Binder inner type or top-level export entry) to MonsterViewModel.
    private static func mapMonster(_ card: MonsterCard) -> MonsterViewModel {
        let monster = MonsterViewModel()
        
        // MARK: Basic info  (Android: name, displayType, sizeAbbrv, alignment)
        monster.name         = card.displayName ?? card.name ?? ""
        monster.type         = card.displayType ?? ""
        // sizeAbbrv maps like Android: "T" → tiny, "S" → small, etc.
        monster.size         = abrvToSize(card.sizeAbbrv)
        monster.subType      = ""  // Binder does not have a separate subtype field
        monster.alignment    = card.alignment ?? ""
        
        // MARK: AC & armor (from Android Monster.java fields: shieldName, baseAcFormula, etc.)
        monster.hasShield     = (card.shieldAcNum ?? 0) != 0 || ((card.baseAcFormula?.contains("shield")) ?? false)
        monster.shieldBonus   = card.shieldAcNum ?? (monster.hasShield ? 2 : 0)
        
        if card.baseAcFormula != nil || (card.shieldName != nil && !card.shieldName!.isEmpty) {
            monster.armorType = .other  // unarmored-defense / custom approach
            monster.otherArmorDescription = card.shieldName ?? "no shield"
        } else {
            monster.armorType = .none
        }
        
        if let armorModStr = card.extraArmorAcMod, let modVal = Int(armorModStr) {
            // For "no armor" style, extraArmorAcMod is added directly to the AC formula text.
            let suffix = (card.otherArmorDescription.isEmpty ? "" : ", ") + "(+\(modVal))"
            monster.otherArmorDescription += suffix
        }
        
        // MARK: HP & hit dice  (Android: hitDice → String like "38d10"; customHpText for override)
        if card.customHpText != nil && !card.customHpText!.isEmpty {
            monster.hasCustomHP = true
            monster.customHP    = card.customHpText!
        } else {
            // Parse hit dice string like "38d10" → hitDice = 38
            if let hdStr = card.hitDice, let parts = splitHitDice(hdStr) {
                monster.hitDice = Int64(parts.diceCount ?? 0)
            }
        }
        
        // MARK: Speed (Android MonsterImportHelper.speedFromString)
        if let stxt = card.speedText where !stxt.isEmpty && !stxt.contains("feet") {
            monster.customSpeed = stxt
            monster.hasCustomSpeed = true
        } else if card.speedText != nil {
            monster.walkSpeed     = Int64(extractSpeedMeters(card.speedText, forSpeed: "walk"))
            monster.burrowSpeed   = Int64(extractSpeedMeters(card.speedText, forSpeed: "burrow"))
            monster.climbSpeed    = Int64(extractSpeedMeters(card.speedText, forSpeed: "climb"))
            monster.flySpeed      = Int64(extractSpeedMeters(card.speedText, forSpeed: "fly"))
            monster.swimSpeed     = Int64(extractSpeedMeters(card.speedText, forSpeed: "swim"))
        }
        
        // MARK: Ability scores (Android Monster.primaryAttributes[0…5] in [STR, DEX, CON, INT, WIS, CHA])
        if let attrs = card.primaryAttributes, attrs.count >= 6 {
            monster.strengthScore   = Int64(attrs[0].value ?? 10)
            monster.dexterityScore  = Int64(attrs[1].value ?? 10)
            monster.constitutionScore = Int64(attrs[2].value ?? 10)
            monster.intelligenceScore = Int64(attrs[3].value ?? 10)
            monster.wisdomScore     = Int64(attrs[4].value ?? 10)
            monster.charismaScore   = Int64(attrs[5].value ?? 10)
        }
        
        // MARK: Saving throws proficiency (Android Monster.proSavingThrows)
        for throwName in (card.proSavingThrows ?? []) {
            switch throwName.lowercased() {
            case "str"    : monster.strengthSavingThrowProficiency      = .proficient
            case "dex"    : monster.dexteritySavingThrowProficiency     = .proficient
            case "con"    : monster.constitutionSavingThrowProficiency  = .proficient
            case "int"    : monster.intelligenceSavingThrowProficiency  = .proficient
            case "wis"    : monster.wisdomSavingThrowProficiency        = .proficient
            case "cha"    : monster.charismaSavingThrowProficiency      = .proficient
            default: break
            }
        }
        
        // MARK: Skills (Android Monster.proficiencySkills)
        for skillEntry in (card.proficiencySkills ?? []) {
            guard let sname = skillEntry.name else { continue }
            let prof: ProficiencyType = skillEntry.isExpertise == 1 ? .expertise : .proficient
            if let ability = SkillViewModel.knownSkillForName(sname) {
                monster.skills.append(SkillViewModel(sname, ability, prof))
            } else {
                // Fallback
                monster.skills.append(SkillViewModel(sname, .dexterity, prof))
            }
        }
        
        // MARK: Damage immunities / resistances / vulnerabilities
        if let di = card.damageImmunities {
            monster.damageImmunities = parseDelimitedString(di)
        }
        if let dv = card.damageVulnerabilities {
            monster.damageVulnerabilities = parseDelimitedString(dv)
        }
        
        // Condition immunities
        if let ci = card.conditionImmunities {
            monster.conditionImmunities = parseDelimitedString(ci)
        }
        
        // MARK: Languages (Android MonsterImportHelper.parseLanguageLine)
        if let langLine = card.languageLine {
            monster.languages = parseLanguages(langLine)
        }
        
        // MARK: Senses  (Android sensesDescription → split into sense entries)
        if let sensesStr = card.sensesDescription, !sensesStr.isEmpty {
            // Each item is typically "<senseName> <distance> ft." or just a name.
            monster.senses = parseDelimitedString(sensesStr)
        }
        
        // MARK: Challenge rating  (Android Monster.crDisplay)
        let crText = card.crDisplay ?? "0"
        if let cr = challengeRating(for: crText) {
            monster.challengeRating = cr
        } else {
            monster.challengeRating = .zero
        }
        
        // Proficiency bonus
        if let pbVal = card.proficiencyBonusValue {
            monster.customProficiencyBonus = Int64(pbVal)
        }
        
        // MARK: Traits / actions (from Android Monster.cardTraits grouped by actionSection)
        processCardTraits(card, into: monster)
    
        return monster
    }
}

// MARK: - Private Helpers

private extension BinderImporter {
    
    /// Convert a size abbreviation to the full size enum name.  (Android uses the same mapping.)
    static func abrvToSize(_ abbrv: String?) -> String {
        guard let a = abbrv else { return "" }
        switch a.uppercased() {
        case "T": return "Tiny"
        case "S": return "Small"
        case "M": return "Medium"
        case "L": return "Large"
        case "H": return "Huge"
        case "G": return "Gargantuan"
        default:  return a
        }
    }
    
    /// Parse "38d10" → (diceCount: 38, dieSize: 10). Android uses this format.
    static func splitHitDice(_ hdStr: String) -> (diceCount: Int?, dieSize: Int)? {
        let parts = hdStr.split(separator: "d")
        guard parts.count == 2 else { return nil }
        let count = Int(parts[0]) ?? 0
        let size  = Int(parts[1]) ?? 6
        return (count, size)
    }
    
    /// Extract a specific movement speed type from formatted string like "40 feet, climb 20 ft."
    static func extractSpeedMeters(_ text: String?, forSpeed type: String) -> Int {
        guard let t = text else { return 0 }
        let pattern = "\\b\(type)\\b"
        if let regex = try? NSRegularExpression(pattern: "\(pattern) (\\d+)", options: [.caseInsensitive]) {
            let range = NSRange(t.startIndex..., in: t)
            if let match = regex.firstMatch(in: t, options: [], range: range),
               let numRange = Range(match.range(at: 1), in: t) {
                var extractedText = ""
                #if swift(>=5.0)
                extractedText = String(t[numRange])
                #else
                extractedText = (t as NSString).substring(with: numRange)
                #endif
                if let val = Int(extractedText) { return val }
            }
        }
        return 0
    }
    
    /// Parse comma-separated string into [StringViewModel].
    static func parseDelimitedString(_ input: String) -> [StringViewModel] {
        return input.components(separatedBy: ",").compactMap {
            let trimmed = $0.trimmingCharacters(in: .whitespacesAndNewlines)
            return trimmed.isEmpty ? nil : StringViewModel(trimmed)
        }
    }
    
    /// Parse a language line like "Common, Deep Speech (150 ft.)" into [LanguageViewModel].
    static func parseLanguages(_ line: String) -> [LanguageViewModel] {
        var result: [LanguageViewModel] = []
        let components = line.components(separatedBy: ",")
        
        for comp in components {
            let trimmed = comp.trimmingCharacters(in: .whitespacesAndNewlines)
            guard !trimmed.isEmpty else { continue }
            
            var hasSpeaks = true
            
            // Check for understands-only patterns
            if trimmed.lowercased().contains("understand") || trimmed.lowercased().contains("(silently)") {
                hasSpeaks = false
            }
            
            // Remove trailing range annotation like " (150 ft.)" → keep just the name
            let parts = trimmed.split(separator: " (")
            var nameOnly = trimmed
            if parts.count >= 2 {
                nameOnly = String(parts.first ?? "")
            }
            
            result.append(LanguageViewModel(nameOnly, hasSpeaks))
        }
        
        return result
    }
    
    /// Determine ChallengeRating from text (Android Monster.crDisplay → challengeRating conversion).
    static func challengeRating(for text: String?) -> ChallengeRating? {
        guard let crText = text else { return .zero }
        
        // Handle common text aliases
        switch crText.lowercased() {
        case "half":              return .oneHalf
        case "quarter", "one quarter":  return .oneQuarter
        case "eighth":            return .oneEighth
        case "three halves":      return .two
        default: break
        }
        
        // Try numeric parse first (e.g. "0" → zero, "3" → three)
        if let val = Int(crText), let cr = ChallengeRating(rawValue: String(val)) {
            return cr
        }
        
        // Handle fractions like "1/2", "5/2"
        if let rangeIdx = crText.range(of: "/") {
            let numStr = crText[..<rangeIdx.lowerBound]
            let denStr = crText[rangeIdx.upperBound...]
            if let num = Double(numStr), let den = Double(denStr), den != 0 {
                let ratio = num / den
                switch ratio {
                case 0.125: return .oneEighth
                case 0.25:  return .oneQuarter
                case 0.5:   return .oneHalf
                case 1.5:   return .two
                default:    break
                }
            }
        }
        
        return .zero
    }
    
    /// Process cardTraits from Android Monster.exportCards (grouped by actionSection).
    static func processCardTraits(_ card: MonsterCard, into monster: MonsterViewModel) {
        guard let traits = card.cardTraits else { return }
        
        for trait in traits {
            guard let displayName = trait.displayName, !displayName.isEmpty else { continue }
            
            if let isHeader = trait.isActionHeader, isHeader == 1 {
                // Section header like "Spellcasting", "Legendary Actions" etc.
                if displayName.lowercased().contains("legendary") {
                    monster.legendaryActions.append(AbilityViewModel(displayName, trait.desc ?? ""))
                } else if displayName.lowercased().contains("lair") || displayName.lowercased().contains("mythic") {
                    monster.lairActions.append(AbilityViewModel(displayName, trait.desc ?? ""))
                } else {
                    // Action section header gets added to actions as a category label.
                    monster.actions.append(AbilityViewModel(displayName, trait.desc ?? ""))
                }
            } else if let section = trait.actionSection {
                switch section {
                case 0: monster.bonusActions    .append(AbilityViewModel(displayName, trait.desc ?? ""))
                case 1: monster.actions         .append(AbilityViewModel(displayName, trait.desc ?? ""))
                case 2: monster.reactions       .append(AbilityViewModel(displayName, trait.desc ?? ""))
                default: break
                }
            } else {
                // No section key — add to actions by default (standard Android behavior)
                monster.actions.append(AbilityViewModel(displayName, trait.desc ?? ""))
            }
        }
    }
}

// MARK: - Error type

enum BinderParseError: LocalizedError {
    case unsupportedEncoding
    case invalidFormat(String)
    
    var errorDescription: String? {
        switch self {
        case .unsupportedEncoding: return "Binder import requires UTF-8 JSON"
        case .invalidFormat(let msg): return "Invalid Binder format: \(msg)"
        }
    }
}
