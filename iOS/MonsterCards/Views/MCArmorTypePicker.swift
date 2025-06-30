//
//  MCArmorTypePicker.swift
//  MonsterCards
//
//  Created by Tom Hicks on 2/6/21.
//

import SwiftUI

struct MCArmorTypePicker: View {
    var label: String = ""
    var value: Binding<ArmorType>
    
    var body: some View {
        VStack(alignment: .leading) {
            Text(label)
                .font(.caption2)
            Picker(
                selection: value,
                label: Text(value.wrappedValue.displayName)) {
                    ForEach(ArmorType.allCases) {armorType in
                        Text(armorType.displayName).tag(armorType)
                    }
                }
                .pickerStyle(MenuPickerStyle())
        }
    }
}

struct MCArmorTypePicker_Previews: PreviewProvider {
    static var previews: some View {
        MCArmorTypePicker(
            value: .constant(ArmorType.none)
        )
    }
}
