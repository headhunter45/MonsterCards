//
//  MCAdvantagePicker.swift
//  MonsterCards
//
//  Created by Tom Hicks on 1/17/21.
//

import SwiftUI

struct MCAdvantagePicker: View {
    var label: String = ""
    var value: Binding<AdvantageType>

    var body: some View {
        VStack(alignment: .leading) {
            Text(label)
                .font(.caption2)
            Picker(
                selection: value,
                label: Text(label)) {
                ForEach(AdvantageType.allCases) { advType in
                    Text(advType.displayName).tag(advType)
                }
            }
            .pickerStyle(SegmentedPickerStyle())
//            .pickerStyle(SegmentedPickerStyle())
//            .pickerStyle(WheelPickerStyle())
//            .pickerStyle(DefaultPickerStyle())
//            .pickerStyle(InlinePickerStyle())
//            .pickerStyle(MenuPickerStyle())
        }
    }
}

struct MCAdvantagePicker_Previews: PreviewProvider {
    static var previews: some View {
        MCAdvantagePicker(value: .constant(AdvantageType.none))
    }
}
