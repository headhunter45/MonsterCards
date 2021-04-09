//
//  SkillViewModel.swift
//  MonsterCards
//
//  Created by Tom Hicks on 1/18/21.
//

import Foundation
import CoreData

class SkillViewModel: ObservableObject, Identifiable {
    
    @Published var name: String
    @Published var abilityScore: AbilityScore
    @Published var proficiency: ProficiencyType
    @Published var advantage: AdvantageType
    
    init(_ name: String = "", _ abilityScore: AbilityScore = .dexterity, _ proficiency: ProficiencyType = .proficient, _ advantage: AdvantageType = .none) {
        self.name = name
        self.abilityScore = abilityScore
        self.proficiency = proficiency
        self.advantage = advantage
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
        var advantageLabel = MonsterViewModel.advantageLabelStringForType(advantage)
        if (advantageLabel != "") {
            advantageLabel = " " + advantageLabel
        }
        return String(format: "%@ %+d%@", name, modifier(forMonster: forMonster), advantageLabel)
    }
}

extension SkillViewModel: Hashable {
    func hash(into hasher: inout Hasher) {
        hasher.combine(abilityScore)
        hasher.combine(advantage)
        hasher.combine(name)
        hasher.combine(proficiency)
    }
}

extension SkillViewModel: Comparable {
    static func < (lhs: SkillViewModel, rhs: SkillViewModel) -> Bool {
        return lhs.name < rhs.name
    }
    
    static func == (lhs: SkillViewModel, rhs: SkillViewModel) -> Bool {
        return lhs.abilityScore == rhs.abilityScore
            && lhs.advantage == rhs.advantage
            && lhs.name == rhs.name
            && lhs.proficiency == rhs.proficiency
    }
}
