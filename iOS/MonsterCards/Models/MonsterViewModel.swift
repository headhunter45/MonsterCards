//
//  MonsterViewModel.swift
//  MonsterCards
//
//  Created by Tom Hicks on 1/18/21.
//

import Foundation
import CoreData

class MonsterViewModel: ObservableObject {
    
    private var rawMonster: Monster?
    
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
    
    init(_ rawMonster: Monster?) {
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
        
        if (rawMonster != nil) {
            self.rawMonster = rawMonster
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
        self.skills = (monster.skills?.allObjects.map {SkillViewModel(($0 as! Skill))})!
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

//        // Remove missing skills from raw monster
//        monster.skills?.forEach {s in
//            let skill = s as! Skill
//            let skillVM = skills.first { $0.isEqualTo(rawSkill: skill) }
//            if (skillVM != nil) {
//                skillVM!.copyToSkill(skill: skill)
//            } else {
//                monster.removeFromSkills(skill)
//            }
//        }
//        // Add new skills to raw monster
//        skills.forEach {skillVM in
//            if (!(monster.skills?.contains(
//                    where: {
//                        skillVM.isEqualTo(rawSkill: $0 as? Skill)
//                    }) ?? true)){
//                monster.addToSkills(skillVM.buildRawSkill(context: monster.managedObjectContext))
//            }
//        }
    }
    
    func copyFromRaw() {
        if (self.rawMonster != nil) {
            self.copyFromMonster(monster: self.rawMonster!);
        }
    }
    
    func copyToRaw() {
        
    }
}
