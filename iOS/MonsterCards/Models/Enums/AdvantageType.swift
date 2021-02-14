//
//  AdvantageType.swift
//  MonsterCards
//
//  Created by Tom Hicks on 1/17/21.
//

import Foundation

enum AdvantageType: String, CaseIterable, Identifiable {
    case none = "none"
    case advantage = "advantage"
    case disadvantage = "disadvantage"
    
    var id: AdvantageType { self }
    
    var displayName: String {
        switch self {
        case .none:
            return "None"
        case .advantage:
            return "Advantage"
        case .disadvantage:
            return "Disadvantage"
        }
    }
}
