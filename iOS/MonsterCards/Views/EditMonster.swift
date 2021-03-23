//
//  EditMonster.swift
//  MonsterCards
//
//  Created by Tom Hicks on 1/16/21.
//

import CoreData
import SwiftUI

struct EditMonster: View {
    // TODO: add challengeRating/challengeRatingEnum and customChallengeRating maybe in basicInfo
    @Environment(\.presentationMode) var presentationMode: Binding<PresentationMode>
    @Environment(\.managedObjectContext) private var viewContext
    
    var monster: Monster
    
    @StateObject private var monsterViewModel: MonsterViewModel = MonsterViewModel(nil)
    @State private var hasInitializedViewModel = false
        
    var body: some View {
        List {
            NavigationLink(
                "Basic Info",
                destination: EditBasicInfo(monsterViewModel: monsterViewModel))
            
            NavigationLink(
                "Armor",
                destination: EditArmor(monsterViewModel: monsterViewModel))
            
            NavigationLink(
                "Speed",
                destination: EditSpeed(monsterViewModel: monsterViewModel))
            
            NavigationLink(
                "Ability Scores",
                destination: EditAbilityScores(monsterViewModel: monsterViewModel))
            
            NavigationLink(
                "Saving Throws",
                destination: EditSavingThrows(monsterViewModel: monsterViewModel))
            
            NavigationLink(
                "Skills",
                destination: EditSkills(monsterViewModel: monsterViewModel))
            
            NavigationLink(
                "Damage Immunities",
                destination: EditDamageTypes(viewModel: monsterViewModel, path: \.damageImmunities))
            
            NavigationLink(
                "Damage Resistances",
                destination: EditDamageTypes(viewModel: monsterViewModel, path: \.damageResistances))

            NavigationLink(
                "Condition Immunities",
                destination: EditDamageTypes(viewModel: monsterViewModel, path: \.conditionImmunities))
        }
        .onAppear(perform: copyMonsterToLocal)
        .toolbar(content: {
            ToolbarItem(placement: .primaryAction) {
                Button("Save", action: saveMonster)
            }
        })
        .navigationTitle(monsterViewModel.name)
        .navigationBarTitleDisplayMode(.inline)
    }
    
    private func addSkill() {
        print("Add Skill pressed")
//
//        let newSkill = Skill.init(context: viewContext)
//        newSkill.name = "Acrobatics"
//        newSkill.wrappedAbilityScore = .dexterity
//        newSkill.wrappedProficiency = .proficient
//        monster.addToSkills(newSkill);
//        do {
//            try viewContext.save()
//            monsterSkills = monster.skillsArray
//        } catch {
//            print("error")
//        }
    }
    
    private func dismissView() {
        self.presentationMode.wrappedValue.dismiss()
    }
    
    private func saveMonster() {
        copyLocalToMonster()
        
        do {
            // Save core data context
            try viewContext.save()
        } catch {
            // Replace this implementation with code to handle the error appropriately.
            // fatalError() causes the application to generate a crash log and terminate. You should not use this function in a shipping application, although it may be useful during development.
            let nsError = error as NSError
            fatalError("Unresolved error \(nsError), \(nsError.userInfo)")
        }
        dismissView()
    }
    
    private func copyMonsterToLocal() {
        if (!hasInitializedViewModel) {
            monsterViewModel.copyFromMonster(monster: monster)
            hasInitializedViewModel = true
        }
    }
    
    private func copyLocalToMonster() {
        monsterViewModel.copyToMonster(monster: monster)
    }
}

struct EditMonster_Previews: PreviewProvider {
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
        monster.baseSpeed = 5
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
        monster.strengthSavingThrowAdvantage = AdvantageType.none.rawValue
        monster.strengthSavingThrowProficiency = ProficiencyType.none.rawValue
        monster.dexteritySavingThrowAdvantage = AdvantageType.advantage.rawValue
        monster.dexteritySavingThrowProficiency = ProficiencyType.proficient.rawValue
        monster.constitutionSavingThrowAdvantage = AdvantageType.disadvantage.rawValue
        monster.constitutionSavingThrowProficiency = ProficiencyType.expertise.rawValue
        monster.intelligenceSavingThrowAdvantage = AdvantageType.none.rawValue
        monster.intelligenceSavingThrowProficiency = ProficiencyType.expertise.rawValue
        monster.wisdomSavingThrowAdvantage = AdvantageType.advantage.rawValue
        monster.wisdomSavingThrowProficiency = ProficiencyType.proficient.rawValue
        monster.charismaSavingThrowAdvantage = AdvantageType.disadvantage.rawValue
        monster.charismaSavingThrowProficiency = ProficiencyType.none.rawValue
        
        return EditMonster(monster: monster).environment(\.managedObjectContext, context)
    }
}
