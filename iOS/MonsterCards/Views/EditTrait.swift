//
//  EditTrait.swift
//  MonsterCards
//
//  Created by Tom Hicks on 3/25/21.
//

import SwiftUI

struct EditTrait: View {
    @ObservedObject var viewModel: AbilityViewModel
    
    var body: some View {
        VStack {
            MCTextField(
                label: "Name",
                value: $viewModel.name)
            
            TextEditor(text: $viewModel.abilityDescription)
        }
    }
}

struct EditTrait_Previews: PreviewProvider {
    static var previews: some View {
        let viewModel = AbilityViewModel()
        EditTrait(viewModel: viewModel)
    }
}
