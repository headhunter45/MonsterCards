//
//  EditLanguages.swift
//  MonsterCards
//
//  Created by Tom Hicks on 3/24/21.
//

import SwiftUI

struct EditLanguages: View {
    @ObservedObject var viewModel: MonsterViewModel
    
    var body: some View {
        let sortedLanguages = viewModel.languages.sorted()
        List {
            MCTextField(
                label: "Understands But",
                value: $viewModel.understandsBut)
            MCStepperField(label: "Telepathy", prefix: "", step: 5, suffix: " ft.", value: $viewModel.telepathy)
            ForEach(sortedLanguages/*viewModel.languages*/) { language in
                NavigationLink(language.name, destination: EditLanguage(viewModel: language))
            }
            .onDelete(perform: { indexSet in
                for index in indexSet {
                    viewModel.languages.remove(at: index)
                }
            })
        }
        .toolbar(content: {
            ToolbarItemGroup(placement: .navigationBarTrailing) {
                EditButton()
                
                Button(
                    action: {
                        let newLanguage = LanguageViewModel("English")
                        viewModel.languages.append(newLanguage)
                        viewModel.languages = viewModel.languages.sorted()
                    },
                    label: {
                        Image(systemName: "plus")
                    }
                )
            }
        })
        .onAppear(perform: {
            viewModel.languages = viewModel.languages.sorted()
        })
    }
}

struct EditLanguages_Previews: PreviewProvider {
    static var previews: some View {
        let viewModel = MonsterViewModel()
        EditLanguages(viewModel: viewModel)
    }
}
