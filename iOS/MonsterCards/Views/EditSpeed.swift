//
//  EditSpeed.swift
//  MonsterCards
//
//  Created by Tom Hicks on 3/21/21.
//

import SwiftUI

struct EditSpeed: View {
    @ObservedObject var monsterViewModel: MonsterViewModel
    
    var body: some View {
        List {
            // Number bound to monster.baseSpeed
            MCStepperField(
                label: "Base",
                step: 5,
                suffix: " ft.",
                value: $monsterViewModel.baseSpeed)
            
            // Number bound to monster.burrowSpeed
            MCStepperField(
                label: "Burrow",
                step: 5,
                suffix: " ft.",
                value: $monsterViewModel.burrowSpeed)
            
            // Number bound to monster.climbSpeed
            MCStepperField(
                label: "Climb",
                step: 5,
                suffix: " ft.",
                value: $monsterViewModel.climbSpeed)
            
            // Number bound to monster.flySpeed
            MCStepperField(
                label: "Fly",
                step: 5,
                suffix: " ft.",
                value: $monsterViewModel.flySpeed)
            
            // Toggle bound to monster.canHover
            Toggle(
                "Can Hover",
                isOn: $monsterViewModel.canHover)
            
            // Number bound to monster.swimSpeed
            MCStepperField(
                label: "Swim",
                step: 5,
                suffix: " ft.",
                value: $monsterViewModel.swimSpeed)
            
            // Toggle bound to monster.hasCustomSpeed
            Toggle(
                "Has Custom Speed",
                isOn: $monsterViewModel.hasCustomSpeed)
            
            // Editable Text field bound to monster.customSpeedText
            MCTextField(
                label: "Custom Speed",
                value: $monsterViewModel.customSpeed)
                .autocapitalization(.none)
        }
        .navigationTitle("Speed")
    }
}

struct EditSpeed_Previews: PreviewProvider {
    static var previews: some View {
        let viewModel = MonsterViewModel()
        EditSpeed(monsterViewModel: viewModel)
    }
}
