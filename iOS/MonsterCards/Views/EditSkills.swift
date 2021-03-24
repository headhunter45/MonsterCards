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
            ForEach(monsterViewModel.skills) { skill in
                NavigationLink(skill.name, destination: EditSkill(skillViewModel: skill))
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
                    let newSkill = SkillViewModel()
                    newSkill.name = ""
                    newSkill.proficiency = .proficient
                    monsterViewModel.skills.append(newSkill)
                },
                label: {
                    Image(systemName: "plus")
                }
            )
        })
        .onAppear(perform: {
            monsterViewModel.skills = monsterViewModel.skills.sorted()
        })
    }
}

struct EditSkills_Previews: PreviewProvider {
    static var previews: some View {
        let viewModel = MonsterViewModel()
        EditSkills(monsterViewModel: viewModel)
    }
}
