//
//  Theme.swift
//  MonsterCards
//
//  Created by Antigravity on 10/1/26.
//

import SwiftUI

public enum Theme {
    public static let dndRed = Color(hex: 0x58180D)
    public static let dndDarkRed = Color(hex: 0x330000)
    public static let dndGold = Color(hex: 0xC59B27)
    public static let parchmentLight = Color(hex: 0xFDF1DC)
    public static let parchmentDark = Color(hex: 0x1F1D1A)
    public static let statblockBorder = Color(hex: 0xD3A625)

    public static var parchmentBackground: Color {
        Color("ParchmentBackground", bundle: nil)
    }

    public static var statblockHeaderRed: Color {
        dndRed
    }

    public static var statblockText: Color {
        Color(hex: 0x221100)
    }
}

public struct StatblockCardModifier: ViewModifier {
    @Environment(\.colorScheme) var colorScheme

    public func body(content: Content) -> some View {
        content
            .padding()
            .background(
                RoundedRectangle(cornerRadius: 12)
                    .fill(colorScheme == .dark ? Color(hex: 0x24201C) : Color(hex: 0xFDF7EB))
                    .shadow(color: Color.black.opacity(colorScheme == .dark ? 0.4 : 0.15), radius: 6, x: 0, y: 3)
            )
            .overlay(
                RoundedRectangle(cornerRadius: 12)
                    .stroke(Theme.statblockBorder, lineWidth: 1.5)
            )
    }
}

public extension View {
    func statblockCardStyle() -> some View {
        self.modifier(StatblockCardModifier())
    }
}

public struct FilterChip: View {
    public let title: String
    public let isActive: Bool

    public init(title: String, isActive: Bool) {
        self.title = title
        self.isActive = isActive
    }

    public var body: some View {
        Text(title)
            .font(.subheadline)
            .fontWeight(isActive ? .semibold : .regular)
            .padding(.horizontal, 12)
            .padding(.vertical, 6)
            .background(isActive ? Theme.dndRed : Color(.secondarySystemBackground))
            .foregroundColor(isActive ? .white : .primary)
            .cornerRadius(16)
    }
}

