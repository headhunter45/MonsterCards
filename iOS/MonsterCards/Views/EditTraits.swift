//
//  EditTraits.swift
//  MonsterCards
//
//  Created by Tom Hicks on 3/25/21.
//

import SwiftUI

struct EditTraits: View {
    @ObservedObject var viewModel: MonsterViewModel
    var path: ReferenceWritableKeyPath<MonsterViewModel, [AbilityViewModel]>
    var title: String
    
    var body: some View {
        List {
            ForEach(viewModel[keyPath: path]) { ability in
                NavigationLink(
                    ability.name,
                    destination: EditTrait(viewModel: ability))
            }
            .onDelete(perform: { indexSet in
                for index in indexSet {
                    viewModel[keyPath: path].remove(at: index)
                }
            })
            .onMove(perform: { indices, newOffset in
                viewModel[keyPath: path].move(fromOffsets: indices, toOffset: newOffset)
                
            })
        }
        .toolbar(content: {
            ToolbarItemGroup(placement: .navigationBarTrailing) {
                EditButton()
                
                Button(
                    action: {
                        let newAbility = AbilityViewModel()
                        viewModel[keyPath: path].append(newAbility)
                        viewModel[keyPath: path] = viewModel[keyPath: path].sorted()
                    },
                    label: {
                        Image(systemName: "plus")
                    }
                )
            }
        })
        .onAppear(perform: {
            viewModel[keyPath: path] = viewModel[keyPath: path].sorted()
        })
        .navigationTitle(title)
        
    }
}

struct EditTraits_Previews: PreviewProvider {
    static var previews: some View {
        let viewModel = MonsterViewModel()
        EditTraits(
            viewModel: viewModel,
            path: \.abilities,
            title: "Abilities")
    }
}
