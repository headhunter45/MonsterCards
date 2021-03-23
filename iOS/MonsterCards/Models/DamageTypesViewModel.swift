//
//  DamageTypesViewModel.swift
//  MonsterCards
//
//  Created by Tom Hicks on 3/22/21.
//

import Foundation

class DamageTypeViewModel: ObservableObject, Comparable, Identifiable {
    static func < (lhs: DamageTypeViewModel, rhs: DamageTypeViewModel) -> Bool {
        lhs.name < rhs.name
    }
    
    static func == (lhs: DamageTypeViewModel, rhs: DamageTypeViewModel) -> Bool {
        lhs.name == rhs.name
    }
    
    @Published var name: String
    
    init(_ name: String) {
        self.name = name
    }
}
