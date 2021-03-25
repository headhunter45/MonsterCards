//
//  EditStrings.swift
//  MonsterCards
//
//  Created by Tom Hicks on 3/22/21.
//

import SwiftUI

struct EditStrings: View {
    @ObservedObject var viewModel: MonsterViewModel
    var path: ReferenceWritableKeyPath<MonsterViewModel, [StringViewModel]>
    var title: String
    
    var body: some View {
        List {
            ForEach(viewModel[keyPath: path]) { damageType in
                TextField(
                    "",
                    text: Binding<String>(
                        get: {damageType.name},
                        set: {damageType.name = $0}
                    )
                )
                .autocapitalization(.none)
            }
            .onDelete(perform: { indexSet in
                for index in indexSet {
                    viewModel[keyPath: path].remove(at: index)
                }
            })
        }
        .toolbar(content: {
            Button(
                action: {
                    let newDamageType = StringViewModel()
                    viewModel[keyPath: path].append(newDamageType)
                    viewModel[keyPath: path] = viewModel[keyPath: path].sorted()
                },
                label: {
                    Image(systemName: "plus")
                }
            )
        })
        .onAppear(perform: {
            viewModel[keyPath: path] = viewModel[keyPath: path].sorted()
        }).navigationTitle(title)
    }
}

struct EditStrings_Previews: PreviewProvider {
    static var previews: some View {
        let viewModel = MonsterViewModel()
        EditStrings(viewModel: viewModel, path: \.damageImmunities, title: "Damage Types")
    }
}
