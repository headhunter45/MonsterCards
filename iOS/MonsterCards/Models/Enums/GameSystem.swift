//
//  GameSystem.swift
//  MonsterCards
//

import Foundation
import SwiftUI

enum GameSystem: String, CaseIterable, Identifiable, Codable {
    case dnd5e = "DND_5E"
    case pf2e = "PF_2E"
    case sf2e = "SF_2E"
    case custom = "CUSTOM"
    
    var id: GameSystem { self }
    
    var displayName: String {
        switch self {
        case .dnd5e:
            return "D&D 5e"
        case .pf2e:
            return "Pathfinder 2e"
        case .sf2e:
            return "Starfinder 2e"
        case .custom:
            return "Custom / Other"
        }
    }
    
    var shortName: String {
        switch self {
        case .dnd5e:
            return "5e"
        case .pf2e:
            return "PF2e"
        case .sf2e:
            return "SF2e"
        case .custom:
            return "Custom"
        }
    }
    
    var badgeColor: Color {
        switch self {
        case .dnd5e:
            return Color(red: 0.5, green: 0.11, blue: 0.11)
        case .pf2e:
            return Color(red: 0.12, green: 0.23, blue: 0.54)
        case .sf2e:
            return Color(red: 0.3, green: 0.11, blue: 0.58)
        case .custom:
            return Color(red: 0.2, green: 0.25, blue: 0.33)
        }
    }
    
    static func fromRawValue(_ raw: String?) -> GameSystem {
        guard let raw = raw?.uppercased().trimmingCharacters(in: .whitespacesAndNewlines), !raw.isEmpty else {
            return .dnd5e
        }
        if raw == "DND_5E" || raw == "DND5E" || raw == "5E" || raw == "D&D 5E" {
            return .dnd5e
        } else if raw == "PF_2E" || raw == "PF2E" || raw == "PATHFINDER 2E" || raw == "PF2" {
            return .pf2e
        } else if raw == "SF_2E" || raw == "SF2E" || raw == "STARFINDER 2E" || raw == "SF2" {
            return .sf2e
        } else if raw == "CUSTOM" {
            return .custom
        }
        return GameSystem(rawValue: raw) ?? .dnd5e
    }
}
