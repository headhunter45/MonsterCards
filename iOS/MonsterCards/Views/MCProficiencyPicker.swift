//
//  MCProficiencyPicker.swift
//  MonsterCards
//
//  Created by Tom Hicks on 1/17/21.
//

import SwiftUI

struct MCProficiencyPicker: View {
    var label: String = ""
    var value: Binding<ProficiencyType>
    
    var body: some View {
        VStack(alignment: .leading) {
            Text(label)
                .font(.caption2)
            Picker(
                selection: value,
                label: Text(label)) {
                ForEach(ProficiencyType.allCases) { profType in
                    Text(profType.displayName).tag(profType)
                }
            }
            .pickerStyle(SegmentedPickerStyle())
        }
    }
}

struct MCProficiencyPicker_Previews: PreviewProvider {
    static var previews: some View {
        MCProficiencyPicker(value: .constant(ProficiencyType.none))
    }
}
