//
//  MCStepperField.swift
//  MonsterCards
//
//  Created by Tom Hicks on 1/16/21.
//

import SwiftUI

struct MCStepperField: View {
    var label: String = ""
    var prefix: String = ""
    var step: Int = 1
    var suffix: String = ""
    var value: Binding<Int64>
    
    var body: some View {
        VStack(alignment: .leading) {
            Text(label)
                .font(.caption2)
            Stepper(
                value: value,
                step: step
            ) {
                Text("\(prefix)\(value.wrappedValue)\(suffix)" as String)
            }
        }
    }
}

struct MCStepperField_Previews: PreviewProvider {
    static var previews: some View {
        MCStepperField(
            label: "Hit Dice",
            value: .constant(4))
    }
}
