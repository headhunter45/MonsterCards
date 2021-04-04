//
//  ImportMonster.swift
//  MonsterCards
//
//  Created by Tom Hicks on 4/1/21.
//

import SwiftUI

struct ImportMonster: View {
    @Binding var monster: MonsterViewModel
    
    var body: some View {
        MonsterDetailView(viewModel: monster)
    }
}

struct ImportMonster_Previews: PreviewProvider {
    static var previews: some View {
        ImportMonster(
            monster: .constant(MonsterViewModel())
        )
    }
}
