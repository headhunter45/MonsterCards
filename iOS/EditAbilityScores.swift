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
        List {MCStepperField(
            label: "STR",
            value: $monsterViewModel.strengthScore)
        MCStepperField(
            label: "DEX",
            value: $monsterViewModel.dexterityScore)
        MCStepperField(
            label: "CON",
            value: $monsterViewModel.constitutionScore)
        MCStepperField(
            label: "INT",
            value: $monsterViewModel.intelligenceScore)
        MCStepperField(
            label: "WIS",
            value: $monsterViewModel.wisdomScore)
        MCStepperField(
            label: "CHA",
            value: $monsterViewModel.charismaScore)
    }
    .textCase(nil)
    }
}

struct EditAbilityScores_Previews: PreviewProvider {
    static var previews: some View {
        let viewModel = MonsterViewModel(nil)
        EditAbilityScores(monsterViewModel: viewModel)
    }
}
