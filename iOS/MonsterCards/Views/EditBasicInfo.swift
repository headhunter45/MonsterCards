//
//  EditBasicInfo.swift
//  MonsterCards
//
//  Created by Tom Hicks on 3/21/21.
//

import SwiftUI

struct EditBasicInfo: View {
    
    @ObservedObject var monsterViewModel: MonsterViewModel
    
    var body: some View {
        List {
            // Editable Text field bound to monster.name
            MCTextField(
                label: "Name",
                value: $monsterViewModel.name)
                .autocapitalization(.words)
            
            // Editable Text field bound to monster.size
            MCTextField(
                label: "Size",
                value: $monsterViewModel.size)
                .autocapitalization(.words)
            
            // Editable Text field bound to monster.type
            MCTextField(
                label: "Type",
                value: $monsterViewModel.type)
                .autocapitalization(.none)
            
            // Editable Text field bound to monster.subType
            MCTextField(
                label: "Subtype",
                value: $monsterViewModel.subType)
                .autocapitalization(.none)
            
            // Editable Text field bound to monster.alignment
            MCTextField(
                label: "Alignment",
                value: $monsterViewModel.alignment)
                .autocapitalization(.none)
            
            // Number with -/+ buttons bound to monster.hitDice
            MCStepperField(
                label: "Hit Dice",
                value: $monsterViewModel.hitDice)
            
            // Toggle bound to monster.hasCustomHP?
            Toggle(
                "Has Custom HP",
                isOn:$monsterViewModel.hasCustomHP)
            
            // Editable Text field bound to monster.customHpText?
            MCTextField(
                label: "Custom HP",
                value: $monsterViewModel.customHP)
                .autocapitalization(.none)
        }
    }
}

struct EditBasicInfo_Previews: PreviewProvider {
    static var previews: some View {
        let viewModel = MonsterViewModel.init(nil)
        EditBasicInfo(monsterViewModel: viewModel)
    }
}
