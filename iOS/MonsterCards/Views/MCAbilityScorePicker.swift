//
//  MCAbilityScorePicker.swift
//  MonsterCards
//
//  Created by Tom Hicks on 2/15/21.
//

import SwiftUI

struct MCAbilityScorePicker: View {
    var label: String = ""
    var value: Binding<AbilityScore>
    var body: some View {
        VStack(alignment: .leading) {
            Text(label)
                .font(.caption2)
            Picker(
                selection: value,
                label: Text(value.wrappedValue.displayName)) {
                ForEach(AbilityScore.allCases) {abilityScore in
                        Text(abilityScore.displayName).tag(abilityScore)
                    }
                }
                .pickerStyle(MenuPickerStyle())
        }
    }
}

struct MCAbilityScorePicker_Previews: PreviewProvider {
    static var previews: some View {
        MCAbilityScorePicker(
            label: "Ability Score",
            value: .constant(AbilityScore.strength))
    }
}
