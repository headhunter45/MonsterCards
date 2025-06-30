//
//  SkillViewModel+CoreData.swift
//  MonsterCards
//
//  Created by Tom Hicks on 4/7/21.
//

import Foundation
import CoreData

extension SkillViewModel {
    func isEqualTo(rawSkill: Skill?) -> Bool {
        if (rawSkill == nil) {
            return false;
        } else if (abilityScore != rawSkill!.wrappedAbilityScore) {
            return false;
        } else if (advantage != rawSkill!.wrappedAdvantage) {
            return false;
        } else if (name != rawSkill!.name) {
            return false;
        } else if (proficiency != rawSkill!.wrappedProficiency) {
            return false;
        } else {
            return true
        }
    }
    
    func copyToSkill(skill: Skill) {
        skill.wrappedAbilityScore = abilityScore
        skill.wrappedAdvantage = advantage
        skill.name = name
        skill.wrappedProficiency = proficiency
    }

    convenience init(_ rawSkill: Skill?) {
        if (rawSkill == nil) {
            self.init()
        } else {
            let skill = rawSkill!
            self.init(
                skill.wrappedName,
                skill.wrappedAbilityScore,
                skill.wrappedProficiency,
                skill.wrappedAdvantage
            )
        }
    }

    func buildRawSkill(context: NSManagedObjectContext?) -> Skill {
        let newSkill = context == nil ? Skill.init() : Skill.init(context: context!)
        newSkill.name = name
        newSkill.wrappedAbilityScore = abilityScore
        newSkill.wrappedProficiency = proficiency
        newSkill.wrappedAdvantage = advantage
        return newSkill
    }
}
