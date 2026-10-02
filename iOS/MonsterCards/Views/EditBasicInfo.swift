//
//  EditBasicInfo.swift
//  MonsterCards
//
//  Created by Tom Hicks on 3/21/21.
//

import SwiftUI

struct EditBasicInfo: View {
    @ObservedObject var monsterViewModel: MonsterViewModel

    private let sizePresets = ["Tiny", "Small", "Medium", "Large", "Huge", "Gargantuan"]
    private let typePresets = ["Aberration", "Beast", "Celestial", "Construct", "Dragon", "Elemental", "Fey", "Fiend", "Giant", "Humanoid", "Monstrosity", "Ooze", "Plant", "Undead"]
    private let alignmentPresets = ["Lawful Good", "Neutral Good", "Chaotic Good", "Lawful Neutral", "True Neutral", "Chaotic Neutral", "Lawful Evil", "Neutral Evil", "Chaotic Evil", "Unaligned", "Any Alignment"]

    var body: some View {
        List {
            Section("Identification") {
                MCTextField(
                    label: "Name",
                    value: $monsterViewModel.name)

                Picker("Size", selection: $monsterViewModel.size) {
                    ForEach(sizePresets, id: \.self) { size in
                        Text(size).tag(size)
                    }
                }
                .pickerStyle(.menu)

                Picker("Type", selection: $monsterViewModel.type) {
                    ForEach(typePresets, id: \.self) { type in
                        Text(type).tag(type)
                    }
                }
                .pickerStyle(.menu)

                MCTextField(
                    label: "Subtype",
                    value: $monsterViewModel.subType)

                Picker("Alignment", selection: $monsterViewModel.alignment) {
                    ForEach(alignmentPresets, id: \.self) { align in
                        Text(align).tag(align)
                    }
                }
                .pickerStyle(.menu)
            }

            Section("Hit Points & Hit Dice") {
                MCStepperField(
                    label: "Hit Dice Count",
                    value: $monsterViewModel.hitDice)

                Toggle("Custom HP String", isOn: $monsterViewModel.hasCustomHP)

                if monsterViewModel.hasCustomHP {
                    MCTextField(
                        label: "Custom HP",
                        value: $monsterViewModel.customHP)
                } else {
                    HStack {
                        Text("Calculated HP")
                            .foregroundColor(.secondary)
                        Spacer()
                        Text(monsterViewModel.hitPoints)
                            .fontWeight(.semibold)
                    }
                }
            }
        }
        .navigationTitle("Basic Info")
    }
}

struct EditBasicInfo_Previews: PreviewProvider {
    static var previews: some View {
        let viewModel = MonsterViewModel()
        EditBasicInfo(monsterViewModel: viewModel)
    }
}

