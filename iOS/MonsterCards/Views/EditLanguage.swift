//
//  EditLanguage.swift
//  MonsterCards
//
//  Created by Tom Hicks on 3/24/21.
//

import SwiftUI

struct EditLanguage: View {
    @ObservedObject var viewModel: LanguageViewModel
    
    var body: some View {
        VStack(alignment: .leading) {
            MCTextField(
                label: "Name",
                value: $viewModel.name)
                .autocapitalization(.none)
            
            Toggle("Speaks", isOn: $viewModel.speaks)
            
            Spacer()
        }
        .padding()
    }
}

struct EditLanguage_Previews: PreviewProvider {
    static var previews: some View {
        let viewModel = LanguageViewModel()
        EditLanguage(viewModel: viewModel)
    }
}
