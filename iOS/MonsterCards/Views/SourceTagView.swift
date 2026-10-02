//
//  SourceTagView.swift
//  MonsterCards
//

import SwiftUI

struct SourceTagView: View {
    var gameSystem: GameSystem
    var sourceLabel: String?
    
    var tagText: String {
        let systemShort = gameSystem.shortName
        if let label = sourceLabel?.trimmingCharacters(in: .whitespacesAndNewlines), !label.isEmpty {
            return "\(systemShort) | \(label)"
        }
        return systemShort
    }
    
    var body: some View {
        Text(tagText)
            .font(.caption2.weight(.bold))
            .foregroundColor(.white)
            .padding(.horizontal, 8)
            .padding(.vertical, 3)
            .background(gameSystem.badgeColor)
            .clipShape(Capsule())
    }
}

struct SourceTagView_Previews: PreviewProvider {
    static var previews: some View {
        VStack(spacing: 8) {
            SourceTagView(gameSystem: .dnd5e, sourceLabel: "SRD 5.1")
            SourceTagView(gameSystem: .pf2e, sourceLabel: "Bestiary")
            SourceTagView(gameSystem: .sf2e, sourceLabel: "Alien Archive")
            SourceTagView(gameSystem: .custom, sourceLabel: "Cyberpunk")
        }
        .padding()
    }
}
