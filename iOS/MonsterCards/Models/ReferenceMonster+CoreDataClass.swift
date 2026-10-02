//
//  ReferenceMonster+CoreDataClass.swift
//  MonsterCards
//

import Foundation
import CoreData

@objc(ReferenceMonster)
public class ReferenceMonster: NSManagedObject {
    
    var gameSystemEnum: GameSystem {
        get {
            return GameSystem.fromRawValue(gameSystem)
        }
        set {
            gameSystem = newValue.rawValue
        }
    }
    
    var armorTypeEnum: ArmorType {
        get {
            return ArmorType(rawValue: armorType ?? "none") ?? .none
        }
        set {
            armorType = newValue.rawValue
        }
    }
    
    var challengeRatingEnum: ChallengeRating {
        get {
            return ChallengeRating(rawValue: challengeRating ?? "1") ?? .one
        }
        set {
            challengeRating = newValue.rawValue
        }
    }
    
    var strengthSavingThrowProficiencyEnum: ProficiencyType {
        get {
            return ProficiencyType(rawValue: strengthSavingThrowProficiency ?? "") ?? .none
        }
        set {
            strengthSavingThrowProficiency = newValue.rawValue
        }
    }

    var strengthSavingThrowAdvantageEnum: AdvantageType {
        get {
            return AdvantageType(rawValue: strengthSavingThrowAdvantage ?? "") ?? .none
        }
        set {
            strengthSavingThrowAdvantage = newValue.rawValue
        }
    }
    
    var dexteritySavingThrowProficiencyEnum: ProficiencyType {
        get {
            return ProficiencyType(rawValue: dexteritySavingThrowProficiency ?? "") ?? .none
        }
        set {
            dexteritySavingThrowProficiency = newValue.rawValue
        }
    }

    var dexteritySavingThrowAdvantageEnum: AdvantageType {
        get {
            return AdvantageType(rawValue: dexteritySavingThrowAdvantage ?? "") ?? .none
        }
        set {
            dexteritySavingThrowAdvantage = newValue.rawValue
        }
    }
    
    var constitutionSavingThrowProficiencyEnum: ProficiencyType {
        get {
            return ProficiencyType(rawValue: constitutionSavingThrowProficiency ?? "") ?? .none
        }
        set {
            constitutionSavingThrowProficiency = newValue.rawValue
        }
    }

    var constitutionSavingThrowAdvantageEnum: AdvantageType {
        get {
            return AdvantageType(rawValue: constitutionSavingThrowAdvantage ?? "") ?? .none
        }
        set {
            constitutionSavingThrowAdvantage = newValue.rawValue
        }
    }
    
    var intelligenceSavingThrowProficiencyEnum: ProficiencyType {
        get {
            return ProficiencyType(rawValue: intelligenceSavingThrowProficiency ?? "") ?? .none
        }
        set {
            intelligenceSavingThrowProficiency = newValue.rawValue
        }
    }

    var intelligenceSavingThrowAdvantageEnum: AdvantageType {
        get {
            return AdvantageType(rawValue: intelligenceSavingThrowAdvantage ?? "") ?? .none
        }
        set {
            intelligenceSavingThrowAdvantage = newValue.rawValue
        }
    }
    
    var wisdomSavingThrowProficiencyEnum: ProficiencyType {
        get {
            return ProficiencyType(rawValue: wisdomSavingThrowProficiency ?? "") ?? .none
        }
        set {
            wisdomSavingThrowProficiency = newValue.rawValue
        }
    }

    var wisdomSavingThrowAdvantageEnum: AdvantageType {
        get {
            return AdvantageType(rawValue: wisdomSavingThrowAdvantage ?? "") ?? .none
        }
        set {
            wisdomSavingThrowAdvantage = newValue.rawValue
        }
    }
    
    var charismaSavingThrowProficiencyEnum: ProficiencyType {
        get {
            return ProficiencyType(rawValue: charismaSavingThrowProficiency ?? "") ?? .none
        }
        set {
            charismaSavingThrowProficiency = newValue.rawValue
        }
    }

    var charismaSavingThrowAdvantageEnum: AdvantageType {
        get {
            return AdvantageType(rawValue: charismaSavingThrowAdvantage ?? "") ?? .none
        }
        set {
            charismaSavingThrowAdvantage = newValue.rawValue
        }
    }
    
    func toViewModel() -> MonsterViewModel {
        let vm = MonsterViewModel()
        vm.name = self.name ?? ""
        vm.gameSystem = self.gameSystemEnum
        vm.sourceLabel = self.sourceLabel ?? ""
        vm.size = self.size ?? ""
        vm.type = self.type ?? ""
        vm.subType = self.subtype ?? ""
        vm.alignment = self.alignment ?? ""
        vm.hitDice = self.hitDice
        vm.hasCustomHP = self.hasCustomHP
        vm.customHP = self.customHP ?? ""
        vm.armorType = self.armorTypeEnum
        vm.hasShield = self.hasShield
        vm.naturalArmorBonus = self.naturalArmorBonus
        vm.customArmor = self.customArmor ?? ""
        vm.walkSpeed = self.walkSpeed
        vm.burrowSpeed = self.burrowSpeed
        vm.climbSpeed = self.climbSpeed
        vm.flySpeed = self.flySpeed
        vm.canHover = self.canHover
        vm.swimSpeed = self.swimSpeed
        vm.hasCustomSpeed = self.hasCustomSpeed
        vm.customSpeed = self.customSpeed ?? ""
        vm.strengthScore = self.strengthScore
        vm.strengthSavingThrowAdvantage = self.strengthSavingThrowAdvantageEnum
        vm.strengthSavingThrowProficiency = self.strengthSavingThrowProficiencyEnum
        vm.dexterityScore = self.dexterityScore
        vm.dexteritySavingThrowAdvantage = self.dexteritySavingThrowAdvantageEnum
        vm.dexteritySavingThrowProficiency = self.dexteritySavingThrowProficiencyEnum
        vm.constitutionScore = self.constitutionScore
        vm.constitutionSavingThrowAdvantage = self.constitutionSavingThrowAdvantageEnum
        vm.constitutionSavingThrowProficiency = self.constitutionSavingThrowProficiencyEnum
        vm.intelligenceScore = self.intelligenceScore
        vm.intelligenceSavingThrowAdvantage = self.intelligenceSavingThrowAdvantageEnum
        vm.intelligenceSavingThrowProficiency = self.intelligenceSavingThrowProficiencyEnum
        vm.wisdomScore = self.wisdomScore
        vm.wisdomSavingThrowAdvantage = self.wisdomSavingThrowAdvantageEnum
        vm.wisdomSavingThrowProficiency = self.wisdomSavingThrowProficiencyEnum
        vm.charismaScore = self.charismaScore
        vm.charismaSavingThrowAdvantage = self.charismaSavingThrowAdvantageEnum
        vm.charismaSavingThrowProficiency = self.charismaSavingThrowProficiencyEnum
        vm.telepathy = self.telepathy
        vm.understandsBut = self.understandsBut ?? ""
        vm.challengeRating = self.challengeRatingEnum
        vm.customChallengeRating = self.customChallengeRating ?? ""
        vm.customProficiencyBonus = self.customProficiencyBonus
        vm.isBlind = self.isBlind
        vm.shieldBonus = Int(self.shieldBonus)
        vm.otherArmorDescription = self.otherArmorDescription ?? ""

        // Decode JSON array attributes
        if let jsonStr = self.damageImmunitiesJson, let data = jsonStr.data(using: .utf8),
           let list = try? JSONDecoder().decode([String].self, from: data) {
            vm.damageImmunities = list.map { StringViewModel($0) }
        }
        if let jsonStr = self.damageResistancesJson, let data = jsonStr.data(using: .utf8),
           let list = try? JSONDecoder().decode([String].self, from: data) {
            vm.damageResistances = list.map { StringViewModel($0) }
        }
        if let jsonStr = self.damageVulnerabilitiesJson, let data = jsonStr.data(using: .utf8),
           let list = try? JSONDecoder().decode([String].self, from: data) {
            vm.damageVulnerabilities = list.map { StringViewModel($0) }
        }
        if let jsonStr = self.conditionImmunitiesJson, let data = jsonStr.data(using: .utf8),
           let list = try? JSONDecoder().decode([String].self, from: data) {
            vm.conditionImmunities = list.map { StringViewModel($0) }
        }
        if let jsonStr = self.sensesJson, let data = jsonStr.data(using: .utf8),
           let list = try? JSONDecoder().decode([String].self, from: data) {
            vm.senses = list.map { StringViewModel($0) }
        }
        
        struct JsonSkill: Codable {
            var name: String
            var ability: String
            var prof: String
            var adv: String
        }
        if let jsonStr = self.skillsJson, let data = jsonStr.data(using: .utf8),
           let list = try? JSONDecoder().decode([JsonSkill].self, from: data) {
            vm.skills = list.map {
                SkillViewModel(
                    $0.name,
                    AbilityScore(rawValue: $0.ability) ?? .strength,
                    ProficiencyType(rawValue: $0.prof) ?? .none,
                    AdvantageType(rawValue: $0.adv) ?? .none
                )
            }
        }

        struct JsonLanguage: Codable {
            var name: String
            var speaks: Bool
        }
        if let jsonStr = self.languagesJson, let data = jsonStr.data(using: .utf8),
           let list = try? JSONDecoder().decode([JsonLanguage].self, from: data) {
            vm.languages = list.map { LanguageViewModel($0.name, $0.speaks) }
        }

        struct JsonAbility: Codable {
            var name: String
            var desc: String
        }
        if let jsonStr = self.abilitiesJson, let data = jsonStr.data(using: .utf8),
           let list = try? JSONDecoder().decode([JsonAbility].self, from: data) {
            vm.abilities = list.map { AbilityViewModel($0.name, $0.desc) }
        }
        if let jsonStr = self.actionsJson, let data = jsonStr.data(using: .utf8),
           let list = try? JSONDecoder().decode([JsonAbility].self, from: data) {
            vm.actions = list.map { AbilityViewModel($0.name, $0.desc) }
        }
        if let jsonStr = self.reactionsJson, let data = jsonStr.data(using: .utf8),
           let list = try? JSONDecoder().decode([JsonAbility].self, from: data) {
            vm.reactions = list.map { AbilityViewModel($0.name, $0.desc) }
        }
        if let jsonStr = self.legendaryActionsJson, let data = jsonStr.data(using: .utf8),
           let list = try? JSONDecoder().decode([JsonAbility].self, from: data) {
            vm.legendaryActions = list.map { AbilityViewModel($0.name, $0.desc) }
        }
        if let jsonStr = self.lairActionsJson, let data = jsonStr.data(using: .utf8),
           let list = try? JSONDecoder().decode([JsonAbility].self, from: data) {
            vm.lairActions = list.map { AbilityViewModel($0.name, $0.desc) }
        }
        if let jsonStr = self.regionalActionsJson, let data = jsonStr.data(using: .utf8),
           let list = try? JSONDecoder().decode([JsonAbility].self, from: data) {
            vm.regionalActions = list.map { AbilityViewModel($0.name, $0.desc) }
        }

        return vm
    }
}
