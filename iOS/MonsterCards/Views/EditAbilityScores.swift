//
//  EditAbilityScores.swift
//  MonsterCards
//
//  Created by Tom Hicks on 3/21/21.
//

import SwiftUI

struct EditAbilityScores: View {
    @ObservedObject var monsterViewModel: MonsterViewModel

    var body: some View {
        List {
            Section("Ability Scores") {
                abilityRow(name: "Strength (STR)", score: $monsterViewModel.strengthScore, modifier: monsterViewModel.strengthModifier)
                abilityRow(name: "Dexterity (DEX)", score: $monsterViewModel.dexterityScore, modifier: monsterViewModel.dexterityModifier)
                abilityRow(name: "Constitution (CON)", score: $monsterViewModel.constitutionScore, modifier: monsterViewModel.constitutionModifier)
                abilityRow(name: "Intelligence (INT)", score: $monsterViewModel.intelligenceScore, modifier: monsterViewModel.intelligenceModifier)
                abilityRow(name: "Wisdom (WIS)", score: $monsterViewModel.wisdomScore, modifier: monsterViewModel.wisdomModifier)
                abilityRow(name: "Charisma (CHA)", score: $monsterViewModel.charismaScore, modifier: monsterViewModel.charismaModifier)
            }
        }
        .navigationTitle("Ability Scores")
    }

    private func abilityRow(name: String, score: Binding<Int64>, modifier: Int) -> some View {
        HStack {
            VStack(alignment: .leading, spacing: 2) {
                Text(name)
                    .font(.body)
                Text(modifier >= 0 ? "Modifier: +\(modifier)" : "Modifier: \(modifier)")
                    .font(.caption)
                    .foregroundColor(Theme.dndRed)
                    .fontWeight(.medium)
            }

            Spacer()

            MCStepperField(
                label: "",
                value: score
            )
            .labelsHidden()
        }
        .padding(.vertical, 4)
    }
}

struct EditAbilityScores_Previews: PreviewProvider {
    static var previews: some View {
        let viewModel = MonsterViewModel()
        EditAbilityScores(monsterViewModel: viewModel)
    }
}

