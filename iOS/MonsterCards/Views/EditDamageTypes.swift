//
//  EditDamageTypes.swift
//  MonsterCards
//
//  Created by Tom Hicks on 3/22/21.
//

import SwiftUI

struct EditDamageTypes: View {
    @ObservedObject var viewModel: MonsterViewModel
    var path: ReferenceWritableKeyPath<MonsterViewModel, [DamageTypeViewModel]>
    
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
                    let newDamageType = DamageTypeViewModel()
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
        })
    }
}

struct EditDamageTypes_Previews: PreviewProvider {
    static var previews: some View {
        let viewModel = MonsterViewModel()
        EditDamageTypes(viewModel: viewModel, path: \.damageImmunities)
    }
}
