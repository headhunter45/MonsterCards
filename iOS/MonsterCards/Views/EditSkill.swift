//
//  EditSkill.swift
//  MonsterCards
//
//  Created by Tom Hicks on 3/21/21.
//

import SwiftUI

struct EditSkill: View {
    @ObservedObject var skillViewModel: SkillViewModel
    
    var body: some View {
        List {
            MCTextField(
                label: "Name",
                value: $skillViewModel.name)
                .autocapitalization(.words)
            
            MCAbilityScorePicker(
                label: "Ability Score",
                value: $skillViewModel.abilityScore)
            
            // TODO: Add a version of this layout for wider screens where these two are in an HStack
            MCAdvantagePicker(
                label: "Advantage",
                value: $skillViewModel.advantage)
            
            MCProficiencyPicker(
                label: "Proficiency",
                value: $skillViewModel.proficiency)
        }
        .navigationTitle("Skill")
    }
}

struct EditSkill_Previews: PreviewProvider {
    static var previews: some View {
        let viewModel = SkillViewModel()
        EditSkill(skillViewModel: viewModel)
    }
}
