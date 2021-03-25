//
//  Skill+CoreDataClass.swift
//  MonsterCards
//
//  Created by Tom Hicks on 1/18/21.
//
//

import Foundation
import CoreData

@objc(Skill)
public class Skill: NSManagedObject {

    var wrappedName: String {
        get {
            return name ?? ""
        }
        set {
            name = newValue
        }
    }
    
    var wrappedProficiency: ProficiencyType {
        get {
            return ProficiencyType.init(rawValue: proficiency ?? "") ?? .none
        }
        set {
            proficiency = newValue.rawValue
        }
    }
        
    var wrappedAbilityScore: AbilityScore {
        get {
            return AbilityScore.init(rawValue: abilityScoreName ?? "") ?? .strength
        }
        set {
            abilityScoreName = newValue.rawValue
        }
    }
    
    var wrappedAdvantage: AdvantageType {
        get {
            return AdvantageType.init(rawValue: advantage ?? "") ?? .none
        }
        set {
            advantage = newValue.rawValue
        }
    }
    
    var modifier: Int {
        get {
            let proficiencyBonus = Double(monster?.proficiencyBonus ?? 0)
            let abilityScoreModifier = Double(monster?.abilityModifierForAbilityScore(wrappedAbilityScore) ?? 0)
            switch wrappedProficiency {
            case .none:
                return Int(abilityScoreModifier)
            case .proficient:
                return Int(abilityScoreModifier + proficiencyBonus)
            case .expertise:
                return Int(abilityScoreModifier + 2 * proficiencyBonus)
            }
        }
    }
    
    var skillDescription: String {
        get {
            var advantageLabel = Monster.advantageLabelStringForType(wrappedAdvantage)
            if (advantageLabel != "") {
                advantageLabel = " " + advantageLabel
            }
            return String(format: "%@ %+d%@", name ?? "", modifier, advantageLabel)
        }
    }
}
