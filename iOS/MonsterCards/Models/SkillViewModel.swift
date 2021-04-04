//
//  SkillViewModel.swift
//  MonsterCards
//
//  Created by Tom Hicks on 1/18/21.
//

import Foundation
import CoreData

class SkillViewModel: ObservableObject, Comparable, Hashable, Identifiable {
    static func < (lhs: SkillViewModel, rhs: SkillViewModel) -> Bool {
        return lhs.name < rhs.name
    }
    
    static func == (lhs: SkillViewModel, rhs: SkillViewModel) -> Bool {
        return lhs.abilityScore == rhs.abilityScore
            && lhs.advantage == rhs.advantage
            && lhs.name == rhs.name
            && lhs.proficiency == rhs.proficiency
    }
    
    func hash(into hasher: inout Hasher) {
        hasher.combine(abilityScore)
        hasher.combine(advantage)
        hasher.combine(name)
        hasher.combine(proficiency)
    }
    
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

    init(_ rawSkill: Skill? = nil) {
        if (rawSkill != nil) {
            _name = rawSkill!.name ?? ""
            _abilityScore = AbilityScore(rawValue: rawSkill!.abilityScoreName ?? "") ?? .strength
            _proficiency = ProficiencyType(rawValue: rawSkill!.proficiency ?? "") ?? .none
            _advantage = AdvantageType(rawValue: rawSkill!.advantage ?? "") ?? .none
            _advantage = .none
        } else {
            _name = ""
            _abilityScore = .strength
            _proficiency = .none
            _advantage = .none
        }
    }
    
    init(_ name: String, _ abilityScore: AbilityScore, _ proficiency: ProficiencyType = .proficient, _ advantage: AdvantageType = .none) {
        _name = name
        _abilityScore = abilityScore
        _proficiency = proficiency
        _advantage = advantage
    }
    
    private var _name: String = ""
    var name: String {
        get {
            return _name
        }
        set {
            if (newValue != _name) {
                _name = newValue
                // Notify changed name
            }
        }
    }
    
    private var _abilityScore: AbilityScore
    var abilityScore: AbilityScore {
        get {
            return _abilityScore
        }
        set {
            if (newValue != _abilityScore) {
                _abilityScore = newValue
                // Notify changed
            }
        }
    }
    
    private var _proficiency: ProficiencyType
    var proficiency: ProficiencyType {
        get {
            return _proficiency
        }
        set {
            if (newValue != _proficiency) {
                _proficiency = newValue
                // Notify changed
            }
        }
    }
    
    private var _advantage: AdvantageType
    var advantage: AdvantageType {
        get {
            return _advantage
        }
        set {
            if (newValue != _advantage) {
                _advantage = newValue
                // Notify changed
            }
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
    
    func modifier(forMonster: MonsterViewModel) -> Int {
        let proficiencyBonus = Double(forMonster.proficiencyBonus)
        let abilityScoreModifier = Double(forMonster.abilityModifierForAbilityScore(abilityScore))
        switch proficiency {
        case .none:
            return Int(abilityScoreModifier)
        case .proficient:
            return Int(abilityScoreModifier + proficiencyBonus)
        case .expertise:
            return Int(abilityScoreModifier + 2 * proficiencyBonus)
        }
    }
    
    func skillDescription(forMonster: MonsterViewModel) -> String {
        var advantageLabel = Monster.advantageLabelStringForType(advantage)
        if (advantageLabel != "") {
            advantageLabel = " " + advantageLabel
        }
        return String(format: "%@ %+d%@", name, modifier(forMonster: forMonster), advantageLabel)
    }
}
