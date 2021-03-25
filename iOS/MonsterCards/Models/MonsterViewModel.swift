//
//  MonsterViewModel.swift
//  MonsterCards
//
//  Created by Tom Hicks on 1/18/21.
//

import Foundation
import CoreData

class MonsterViewModel: ObservableObject {
    
    @Published var name: String
    @Published var size: String
    @Published var type: String
    @Published var subType: String
    @Published var alignment: String
    @Published var hitDice: Int64
    @Published var hasCustomHP: Bool
    @Published var customHP: String
    @Published var armorType: ArmorType
    @Published var hasShield: Bool
    @Published var naturalArmorBonus: Int64
    @Published var customArmor: String
    @Published var baseSpeed: Int64
    @Published var burrowSpeed: Int64
    @Published var climbSpeed: Int64
    @Published var flySpeed: Int64
    @Published var canHover: Bool
    @Published var swimSpeed: Int64
    @Published var hasCustomSpeed: Bool
    @Published var customSpeed: String
    @Published var strengthScore: Int64
    @Published var dexterityScore: Int64
    @Published var constitutionScore: Int64
    @Published var intelligenceScore: Int64
    @Published var wisdomScore: Int64
    @Published var charismaScore: Int64
    @Published var strengthSavingThrowProficiency: ProficiencyType
    @Published var strengthSavingThrowAdvantage: AdvantageType
    @Published var dexteritySavingThrowProficiency: ProficiencyType
    @Published var dexteritySavingThrowAdvantage: AdvantageType
    @Published var constitutionSavingThrowProficiency: ProficiencyType
    @Published var constitutionSavingThrowAdvantage: AdvantageType
    @Published var intelligenceSavingThrowProficiency: ProficiencyType
    @Published var intelligenceSavingThrowAdvantage: AdvantageType
    @Published var wisdomSavingThrowProficiency: ProficiencyType
    @Published var wisdomSavingThrowAdvantage: AdvantageType
    @Published var charismaSavingThrowProficiency: ProficiencyType
    @Published var charismaSavingThrowAdvantage: AdvantageType
    @Published var skills: [SkillViewModel]
    @Published var damageImmunities: [StringViewModel]
    @Published var damageResistances: [StringViewModel]
    @Published var damageVulnerabilities: [StringViewModel]
    @Published var conditionImmunities: [StringViewModel]
    @Published var senses: [StringViewModel]
    @Published var languages: [LanguageViewModel]
    @Published var telepathy: Int64
    @Published var understandsBut: String
    @Published var challengeRating: ChallengeRating
    @Published var customChallengeRating: String
    @Published var customProficiencyBonus: Int64
    @Published var abilities: [AbilityViewModel]
    
    init(_ rawMonster: Monster? = nil) {
        self.name = ""
        self.size = ""
        self.type = ""
        self.subType = ""
        self.alignment = ""
        self.hitDice = 0
        self.hasCustomHP = false
        self.customHP = ""
        self.armorType = .none
        self.hasShield = false
        self.naturalArmorBonus = 0
        self.customArmor = ""
        self.baseSpeed = 0
        self.burrowSpeed = 0
        self.climbSpeed = 0
        self.flySpeed = 0
        self.canHover = false
        self.swimSpeed = 0
        self.hasCustomSpeed = false
        self.customSpeed = ""
        self.strengthScore = 10
        self.strengthSavingThrowAdvantage = .none
        self.strengthSavingThrowProficiency = .none
        self.dexterityScore = 10
        self.dexteritySavingThrowAdvantage = .none
        self.dexteritySavingThrowProficiency = .none
        self.constitutionScore = 10
        self.constitutionSavingThrowAdvantage = .none
        self.constitutionSavingThrowProficiency = .none
        self.intelligenceScore = 10
        self.intelligenceSavingThrowAdvantage = .none
        self.intelligenceSavingThrowProficiency = .none
        self.wisdomScore = 10
        self.wisdomSavingThrowAdvantage = .none
        self.wisdomSavingThrowProficiency = .none
        self.charismaScore = 10
        self.charismaSavingThrowAdvantage = .none
        self.charismaSavingThrowProficiency = .none
        self.skills = []
        self.damageImmunities = []
        self.damageResistances = []
        self.damageVulnerabilities = []
        self.conditionImmunities = []
        self.senses = []
        self.languages = []
        self.telepathy = 0
        self.understandsBut = ""
        self.challengeRating = .one
        self.customChallengeRating = ""
        self.customProficiencyBonus = 0
        self.abilities = []

        if (rawMonster != nil) {
            self.copyFromMonster(monster: rawMonster!)
        }
    }
    
    func copyFromMonster(monster: Monster) {
        self.name = monster.name ?? ""
        self.size = monster.size ?? ""
        self.type = monster.type ?? ""
        self.subType = monster.subtype ?? ""
        self.alignment = monster.alignment ?? ""
        self.hitDice = monster.hitDice
        self.hasCustomHP = monster.hasCustomHP
        self.customHP = monster.customHP ?? ""
        self.armorType = monster.armorTypeEnum
        self.hasShield = monster.hasShield
        self.naturalArmorBonus = monster.naturalArmorBonus
        self.customArmor = monster.customArmor ?? ""
        self.baseSpeed = monster.baseSpeed
        self.burrowSpeed = monster.burrowSpeed
        self.climbSpeed = monster.climbSpeed
        self.flySpeed = monster.flySpeed
        self.canHover = monster.canHover
        self.swimSpeed = monster.swimSpeed
        self.hasCustomSpeed = monster.hasCustomSpeed
        self.customSpeed = monster.customSpeed ?? ""
        self.strengthScore = monster.strengthScore
        self.strengthSavingThrowAdvantage = monster.strengthSavingThrowAdvantageEnum
        self.strengthSavingThrowProficiency = monster.strengthSavingThrowProficiencyEnum
        self.dexterityScore = monster.dexterityScore
        self.dexteritySavingThrowAdvantage = monster.dexteritySavingThrowAdvantageEnum
        self.dexteritySavingThrowProficiency = monster.dexteritySavingThrowProficiencyEnum
        self.constitutionScore = monster.constitutionScore
        self.constitutionSavingThrowAdvantage = monster.constitutionSavingThrowAdvantageEnum
        self.constitutionSavingThrowProficiency = monster.constitutionSavingThrowProficiencyEnum
        self.intelligenceScore = monster.intelligenceScore
        self.intelligenceSavingThrowAdvantage = monster.intelligenceSavingThrowAdvantageEnum
        self.intelligenceSavingThrowProficiency = monster.intelligenceSavingThrowProficiencyEnum
        self.wisdomScore = monster.wisdomScore
        self.wisdomSavingThrowAdvantage = monster.wisdomSavingThrowAdvantageEnum
        self.wisdomSavingThrowProficiency = monster.wisdomSavingThrowProficiencyEnum
        self.charismaScore = monster.charismaScore
        self.charismaSavingThrowAdvantage = monster.charismaSavingThrowAdvantageEnum
        self.charismaSavingThrowProficiency = monster.charismaSavingThrowProficiencyEnum
        self.telepathy = monster.telepathy
        self.understandsBut = monster.understandsBut ?? ""
        self.challengeRating = monster.challengeRatingEnum
        self.customChallengeRating = monster.customChallengeRating ?? ""
        self.customProficiencyBonus = monster.customProficiencyBonus
        
        self.skills = (monster.skills?.allObjects.map {SkillViewModel(($0 as! Skill))})!.sorted()
    
        self.damageImmunities = (monster.damageImmunities ?? [])
            .map {StringViewModel($0)}
            .sorted()

        self.damageResistances = (monster.damageResistances ?? [])
            .map {StringViewModel($0)}
            .sorted()
        
        self.damageVulnerabilities = (monster.damageVulnerabilities ?? [])
            .map {StringViewModel($0)}
            .sorted()
        
        self.conditionImmunities = (monster.conditionImmunities ?? [])
            .map {StringViewModel($0)}
            .sorted()
        
        self.senses = (monster.senses ?? [])
            .map {StringViewModel($0)}
            .sorted()
        
        self.languages = (monster.languages ?? [])
            .map {LanguageViewModel($0.name, $0.speaks)}
            .sorted()
        
        // These are manually sorted in the UI
        self.abilities = (monster.abilities ?? [])
            .map {AbilityViewModel($0.name, $0.abilityDescription)}
    }
    
    func copyToMonster(monster: Monster) {
        monster.name = name
        monster.size = size
        monster.type = type
        monster.subtype = subType
        monster.alignment = alignment
        monster.hitDice = hitDice
        monster.hasCustomHP = hasCustomHP
        monster.customHP = customHP
        monster.armorTypeEnum = armorType
        monster.hasShield = hasShield
        monster.naturalArmorBonus = naturalArmorBonus
        monster.customArmor = customArmor
        monster.baseSpeed = baseSpeed
        monster.burrowSpeed = burrowSpeed
        monster.climbSpeed = climbSpeed
        monster.flySpeed = flySpeed
        monster.canHover = canHover
        monster.swimSpeed = swimSpeed
        monster.hasCustomSpeed = hasCustomSpeed
        monster.customSpeed = customSpeed
        monster.strengthScore = strengthScore
        monster.strengthSavingThrowAdvantageEnum = strengthSavingThrowAdvantage
        monster.strengthSavingThrowProficiencyEnum = strengthSavingThrowProficiency
        monster.dexterityScore = dexterityScore
        monster.dexteritySavingThrowAdvantageEnum = dexteritySavingThrowAdvantage
        monster.dexteritySavingThrowProficiencyEnum = dexteritySavingThrowProficiency
        monster.constitutionScore = constitutionScore
        monster.constitutionSavingThrowAdvantageEnum = constitutionSavingThrowAdvantage
        monster.constitutionSavingThrowProficiencyEnum = constitutionSavingThrowProficiency
        monster.intelligenceScore = intelligenceScore
        monster.intelligenceSavingThrowAdvantageEnum = intelligenceSavingThrowAdvantage
        monster.intelligenceSavingThrowProficiencyEnum = intelligenceSavingThrowProficiency
        monster.wisdomScore = wisdomScore
        monster.wisdomSavingThrowAdvantageEnum = wisdomSavingThrowAdvantage
        monster.wisdomSavingThrowProficiencyEnum = wisdomSavingThrowProficiency
        monster.charismaScore = charismaScore
        monster.charismaSavingThrowAdvantageEnum = charismaSavingThrowAdvantage
        monster.charismaSavingThrowProficiencyEnum = charismaSavingThrowProficiency
        monster.telepathy = telepathy
        monster.understandsBut = understandsBut
        monster.challengeRatingEnum = challengeRating
        monster.customChallengeRating = customChallengeRating
        monster.customProficiencyBonus = customProficiencyBonus

        // Remove missing skills from raw monster
        monster.skills?.forEach {s in
            let skill = s as! Skill
            let skillVM = skills.first { $0.isEqualTo(rawSkill: skill) }
            if (skillVM != nil) {
                skillVM!.copyToSkill(skill: skill)
            } else {
                monster.removeFromSkills(skill)
            }
        }
        // Add new skills to raw monster
        skills.forEach {skillVM in
            if (!(monster.skills?.contains(
                    where: {
                        skillVM.isEqualTo(rawSkill: $0 as? Skill)
                    }) ?? true)){
                monster.addToSkills(skillVM.buildRawSkill(context: monster.managedObjectContext))
            }
        }
        
        monster.conditionImmunities = conditionImmunities.map {$0.name}
        monster.damageImmunities = damageImmunities.map {$0.name}
        monster.damageResistances = damageResistances.map {$0.name}
        monster.damageVulnerabilities = damageVulnerabilities.map {$0.name}
        monster.senses = senses.map {$0.name}
        
        // This is necessary so core data sees the language objects as changed. Without it they won't be persisted.
        monster.languages = languages.map {LanguageViewModel($0.name, $0.speaks)}
        
        monster.abilities = abilities.map {AbilityViewModel($0.name, $0.abilityDescription)}
    }
}
