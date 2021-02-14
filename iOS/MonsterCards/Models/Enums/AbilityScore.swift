//
//  AbilityScore.swift
//  MonsterCards
//
//  Created by Tom Hicks on 1/18/21.
//

import Foundation

enum AbilityScore: String, CaseIterable, Identifiable {
    case strength = "strength"
    case dexterity = "dexterity"
    case constitution = "constitution"
    case intelligence = "intelligence"
    case wisdom = "wisdom"
    case charisma = "charisma"
    
    var id: AbilityScore { self }
    
    var displayName: String {
        switch self {
        case .strength:
            return "Strength"
        case .dexterity:
            return "Dexterity"
        case .constitution:
            return "Constitution"
        case .intelligence:
            return "Intelligence"
        case .wisdom:
            return "Wisdom"
        case .charisma:
            return "Charisma"
        }
    }
    
    var shortDisplayName: String {
        switch self {
        case .strength:
            return "STR"
        case .dexterity:
            return "DEX"
        case .constitution:
            return "CON"
        case .intelligence:
            return "INT"
        case .wisdom:
            return "WIS"
        case .charisma:
            return "CHA"
        }
    }
}
