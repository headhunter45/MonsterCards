//
//  EditSavingThrows.swift
//  MonsterCards
//
//  Created by Tom Hicks on 3/21/21.
//

import SwiftUI

struct EditSavingThrows: View {
    @ObservedObject var monsterViewModel: MonsterViewModel
    
    var body: some View {
        List {
            // TODO: Add a version of this layout for wider screens where these VStacks with HStacks
            VStack {
                MCAdvantagePicker(
                    label: "Strength Advantage",
                    value: $monsterViewModel.strengthSavingThrowAdvantage)

                MCProficiencyPicker(
                    label: "Strength Proficiency",
                    value: $monsterViewModel.strengthSavingThrowProficiency)
            }
            VStack {
                MCAdvantagePicker(
                    label: "Dexterity Advantage",
                    value: $monsterViewModel.dexteritySavingThrowAdvantage)

                MCProficiencyPicker(
                    label: "Dexterity Proficiency",
                    value: $monsterViewModel.dexteritySavingThrowProficiency)
            }
            VStack {
                MCAdvantagePicker(
                    label: "Constitution Advantage",
                    value: $monsterViewModel.constitutionSavingThrowAdvantage)

                MCProficiencyPicker(
                    label: "Constitution Proficiency",
                    value: $monsterViewModel.constitutionSavingThrowProficiency)
            }
            VStack {
                MCAdvantagePicker(
                    label: "Intelligence Advantage",
                    value: $monsterViewModel.intelligenceSavingThrowAdvantage)

                MCProficiencyPicker(
                    label: "Intelligence Proficiency",
                    value: $monsterViewModel.intelligenceSavingThrowProficiency)
                }
            VStack {
                MCAdvantagePicker(
                    label: "Wisdom Advantage",
                    value: $monsterViewModel.wisdomSavingThrowAdvantage)

                MCProficiencyPicker(
                    label: "Wisdom Proficiency",
                    value: $monsterViewModel.wisdomSavingThrowProficiency)
                }
            VStack {
                MCAdvantagePicker(
                    label: "Charisma Advantage",
                    value: $monsterViewModel.charismaSavingThrowAdvantage)

                MCProficiencyPicker(
                    label: "Charisma Proficiency",
                    value: $monsterViewModel.charismaSavingThrowProficiency)
            }
        }
        .navigationTitle("Saving Throws")
    }
}

struct EditSavingThrows_Previews: PreviewProvider {
    static var previews: some View {
        let viewModel = MonsterViewModel()
        EditSavingThrows(monsterViewModel: viewModel)
    }
}
