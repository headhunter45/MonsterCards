//
//  MonsterDetailWrapper.swift
//  MonsterCards
//
//  Created by Tom Hicks on 4/7/21.
//

import SwiftUI

struct MonsterDetailWrapper: View {
    let kTextColor = Color(hex: 0x982818)
    
    @ObservedObject var monster: Monster
    @StateObject private var viewModel = MonsterViewModel()
    
    var body: some View {
        
        MonsterDetailView(viewModel: viewModel)
            .onAppear(perform: {
                viewModel.copyFromMonster(monster: monster)
            })
            .toolbar(content: {
                ToolbarItem(placement: .primaryAction) {
                    NavigationLink("Edit", destination: EditMonster(monster: monster))
                }
            })
            .navigationTitle(monster.name ?? "")
            .navigationBarTitleDisplayMode(.inline)
    }
    
    private func editMonster() {
        print("Edit Monster pressed")
    }
}

struct MonsterDetailWrapper_Previews: PreviewProvider {
    static var previews: some View {
        let context = PersistenceController.preview.container.viewContext
        let monster = Monster.init(context: context)
        monster.name = "Steve"
        monster.size = "Medium"
        monster.type = "humanoid"
        monster.subtype = "human"
        monster.alignment = "LG"
        monster.hitDice = 6
        monster.hasCustomHP = true
        monster.customHP = "12 (1d10)+2"
        monster.walkSpeed = 5
        monster.burrowSpeed = 10
        monster.climbSpeed = 15
        monster.flySpeed = 20
        monster.swimSpeed = 25
        monster.canHover = true
        monster.hasCustomSpeed = false
        monster.customSpeed = "walk: 5 ft."
        monster.strengthScore = 8
        monster.dexterityScore = 10
        monster.constitutionScore = 12
        monster.intelligenceScore = 14
        monster.wisdomScore = 16
        monster.charismaScore = 18
        monster.strengthSavingThrowAdvantageEnum = AdvantageType.none
        monster.strengthSavingThrowProficiencyEnum = ProficiencyType.none
        monster.dexteritySavingThrowAdvantageEnum = AdvantageType.advantage
        monster.dexteritySavingThrowProficiencyEnum = ProficiencyType.proficient
        monster.constitutionSavingThrowAdvantageEnum = AdvantageType.disadvantage
        monster.constitutionSavingThrowProficiencyEnum = ProficiencyType.expertise
        monster.intelligenceSavingThrowAdvantageEnum = AdvantageType.none
        monster.intelligenceSavingThrowProficiencyEnum = ProficiencyType.expertise
        monster.wisdomSavingThrowAdvantageEnum = AdvantageType.advantage
        monster.wisdomSavingThrowProficiencyEnum = ProficiencyType.proficient
        monster.charismaSavingThrowAdvantageEnum = AdvantageType.disadvantage
        monster.charismaSavingThrowProficiencyEnum = ProficiencyType.none
        monster.telepathy = 1
        monster.languages = [
            LanguageViewModel("English", true),
            LanguageViewModel("French", false)
        ]
        
        return Group {
            MonsterDetailWrapper(monster: monster)
                .environment(\.managedObjectContext, context)
                .previewDevice("iPod touch (7th generation)")
            MonsterDetailWrapper(monster: monster)
                .environment(\.managedObjectContext, context)
                .previewDevice("iPad Pro (11-inch) (2nd generation)")
        }
    }
}
