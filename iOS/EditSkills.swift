//
//  EditSkills.swift
//  MonsterCards
//
//  Created by Tom Hicks on 3/21/21.
//

import SwiftUI

struct EditSkills: View {
    @ObservedObject var monsterViewModel: MonsterViewModel
    
    var body: some View {
        List {
            ForEach(monsterViewModel.skills, id: \.self) { skill in
                Text(skill.name)
            }
            .onDelete(perform: { indexSet in
                for index in indexSet {
                    monsterViewModel.skills.remove(at: index)
                }
            })
        }
        .toolbar(content: {
            Button(
                action: {
                    let newSkill = SkillViewModel(nil)
                    newSkill.name = "New Skill"
                    monsterViewModel.skills.append(newSkill)
                },
                label: {
                    Image(systemName: "plus")
                }
            )
        })
    }
}

struct EditSkills_Previews: PreviewProvider {
    static var previews: some View {
        let viewModel = MonsterViewModel(nil)
        EditSkills(monsterViewModel: viewModel)
    }
}
