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
        VStack(alignment: .leading) {
            MCTextField(
                label: "Name",
                value: $viewModel.name)
            
            Text("Description")
                .font(.caption2)
        
            TextEditor(text: $viewModel.abilityDescription)
                
        }
        .padding()
    }
}

struct EditTrait_Previews: PreviewProvider {
    static var previews: some View {
        let viewModel = AbilityViewModel()
        EditTrait(viewModel: viewModel)
    }
}
