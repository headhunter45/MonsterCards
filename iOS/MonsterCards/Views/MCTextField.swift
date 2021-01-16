//
//  MCTextField.swift
//  MonsterCards
//
//  Created by Tom Hicks on 1/16/21.
//

import SwiftUI

struct MCTextField: View {
    var label: String
    var value: Binding<String>
    var body: some View {
        VStack(alignment: .leading) {
            Text(label)
                .font(.caption2)
            TextField(label, text: value)
                .autocapitalization(/*@START_MENU_TOKEN@*/.none/*@END_MENU_TOKEN@*/)
//                .padding(.top, -4)
        }
    }
}

struct MCTextField_Previews: PreviewProvider {
    static var previews: some View {
        MCTextField(label: "Name", value: .constant("Ted"))
    }
}
