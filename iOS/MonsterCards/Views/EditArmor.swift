//
//  EditArmor.swift
//  MonsterCards
//
//  Created by Tom Hicks on 3/21/21.
//

import SwiftUI

struct EditArmor: View {
    @ObservedObject var monsterViewModel: MonsterViewModel
    
    var body: some View {
        List {
            // Armor Type select bound to monster.armorTypeEnum
            MCArmorTypePicker(
                label: "Armor Type",
                value: $monsterViewModel.armorType)
            
            // Toggle bound to monster.hasShield?
            Toggle(
                "Has Shield",
                isOn: $monsterViewModel.hasShield)
            
            // Number with -/+ buttons bound to monster.naturalArmorBonus
            MCStepperField(
                label: "Natural Armor Bonus",
                value: $monsterViewModel.naturalArmorBonus)
            
            // Editable Text field bound to monster.customArmorText?
            MCTextField(
                label: "Custom Armor",
                value: $monsterViewModel.customArmor)
                .autocapitalization(.none)
        }
        .navigationTitle("Armor")
    }
}

struct EditArmor_Previews: PreviewProvider {
    static var previews: some View {
        let viewModel = MonsterViewModel()
        EditArmor(monsterViewModel: viewModel)
    }
}
