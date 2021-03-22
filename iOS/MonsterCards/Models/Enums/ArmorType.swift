//
//  ArmorType.swift
//  MonsterCards
//
//  Created by Tom Hicks on 3/21/21.
//

import Foundation

enum ArmorType: String, CaseIterable, Identifiable {
    case none = "none"
    case naturalArmor = "natural armor"
    case mageArmor = "mage armor"
    case padded = "padded"
    case leather = "leather"
    case studdedLeather = "studded"
    case hide = "hide"
    case chainShirt = "chain shirt"
    case scaleMail = "scale mail"
    case breastplate = "breastplate"
    case halfPlate = "half plate"
    case ringMail = "ring mail"
    case chainMail = "chain mail"
    case splintMail = "splint"
    case plateMail = "plate"
    case other = "other"
    
    var id: ArmorType { self }
    
    var displayName: String {
        switch self {
            case .none: return "None"
            case .naturalArmor: return "Natural Armor"
            case .mageArmor: return "Mage Armor"
            case .padded: return "Padded"
            case .leather: return "Leather"
            case .studdedLeather: return "Studded Leather"
            case .hide: return "Hide"
            case .chainShirt: return "Chain Shirt"
            case .scaleMail: return "Scale Mail"
            case .breastplate: return "Breastplate"
            case .halfPlate: return "Half Plate"
            case .ringMail: return "Ring Mail"
            case .chainMail: return "Chain Mail"
            case .splintMail: return "Splint Mail"
            case .plateMail: return "Plate Mail"
            case .other: return "Other"
        }
    }
}
