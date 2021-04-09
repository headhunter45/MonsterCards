//
//  MonsterViewModel+CoreData.swift
//  MonsterCards
//
//  Created by Tom Hicks on 4/7/21.
//

import Foundation
import CoreData

extension MonsterViewModel {
    convenience init(_ rawMonster: Monster?) {
        self.init()

        // Call the copy constructor
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
        self.walkSpeed = monster.walkSpeed
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
        self.isBlind = monster.isBlind
        
        self.skills = (monster.skills?.allObjects.map {
            let skill = $0 as! Skill
            return SkillViewModel(
                skill.name ?? "",
                AbilityScore(rawValue: skill.abilityScoreName ?? "") ?? .dexterity,
                ProficiencyType(rawValue: skill.proficiency ?? "") ?? .none,
                AdvantageType(rawValue: skill.advantage ?? "") ?? .none
            )
        })!.sorted()
        
        //            self.name = rawSkill!.name ?? ""
        //            self.abilityScore = AbilityScore(rawValue: rawSkill!.abilityScoreName ?? "") ?? .strength
        //            self.proficiency = ProficiencyType(rawValue: rawSkill!.proficiency ?? "") ?? .none
        //            self.advantage = AdvantageType(rawValue: rawSkill!.advantage ?? "") ?? .none

    
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
        
        self.actions = (monster.actions ?? [])
            .map {AbilityViewModel($0.name, $0.abilityDescription)}
        
        self.legendaryActions = (monster.legendaryActions ?? [])
            .map {AbilityViewModel($0.name, $0.abilityDescription)}
        
        self.lairActions = (monster.lairActions ?? [])
            .map {AbilityViewModel($0.name, $0.abilityDescription)}

        self.regionalActions = (monster.regionalActions ?? [])
            .map {AbilityViewModel($0.name, $0.abilityDescription)}

        self.reactions = (monster.reactions ?? [])
            .map {AbilityViewModel($0.name, $0.abilityDescription)}

        // Private fields
        
        self.shieldBonus = Int(monster.shieldBonus)
        self.otherArmorDescription = monster.otherArmorDescription ?? ""
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
        monster.walkSpeed = walkSpeed
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
        monster.isBlind = isBlind

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
        
        monster.actions = actions.map {AbilityViewModel($0.name, $0.abilityDescription)}
        
        monster.legendaryActions = legendaryActions.map {AbilityViewModel($0.name, $0.abilityDescription)}
        
        monster.lairActions = lairActions.map {AbilityViewModel($0.name, $0.abilityDescription)}
        
        monster.regionalActions = regionalActions.map {AbilityViewModel($0.name, $0.abilityDescription)}
        
        monster.reactions = reactions.map {AbilityViewModel($0.name, $0.abilityDescription)}
        
        monster.shieldBonus = Int64(shieldBonus)
        monster.otherArmorDescription = otherArmorDescription
    }

}
