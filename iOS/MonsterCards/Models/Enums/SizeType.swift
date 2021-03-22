//
//  SizeType.swift
//  MonsterCards
//
//  Created by Tom Hicks on 3/21/21.
//

import Foundation

enum SizeType: String, CaseIterable, Identifiable {
    case tiny = "tiny"
    case small = "small"
    case medium = "medium"
    case large = "large"
    case huge = "huge"
    case gargantuan = "gargantuan"
    
    var id: SizeType { self }
    
    var displayName: String {
        switch self {
            case .tiny: return "Tiny"
            case .small: return "Small"
            case .medium: return "Medium"
            case .large: return "Large"
            case .huge: return "Huge"
        case .gargantuan: return "gargantuan"
        }
    }
}
