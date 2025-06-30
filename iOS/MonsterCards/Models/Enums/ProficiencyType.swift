//
//  ProficiencyType.swift
//  MonsterCards
//
//  Created by Tom Hicks on 1/17/21.
//

import Foundation

enum ProficiencyType: String, CaseIterable, Identifiable {
    case none = "none"
    case proficient = "proficient"
    case expertise = "expertise"
    
    var id: ProficiencyType { self }
    
    var displayName: String {
        switch self {
        case .none:
            return "None"
        case .proficient:
            return "Proficient"
        case .expertise:
            return "Expertise"
        }
    }
}
