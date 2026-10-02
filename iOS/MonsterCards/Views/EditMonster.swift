//
//  EditMonster.swift
//  MonsterCards
//
//  Created by Tom Hicks on 1/16/21.
//

import CoreData
import SwiftUI

struct EditMonster: View {
    @Environment(\.presentationMode) var presentationMode: Binding<PresentationMode>
    @Environment(\.managedObjectContext) private var viewContext

    var monster: Monster

    @StateObject private var monsterViewModel: MonsterViewModel = MonsterViewModel()
    @State private var hasInitializedViewModel = false
    @State private var isShowingPreview = false

    var body: some View {
        List {
            Section("Basic & Health") {
                NavigationLink(
                    "Basic Info",
                    destination: EditBasicInfo(monsterViewModel: monsterViewModel))

                NavigationLink(
                    "Armor & Hit Points",
                    destination: EditArmor(monsterViewModel: monsterViewModel))

                NavigationLink(
                    "Speed & Movement",
                    destination: EditSpeed(monsterViewModel: monsterViewModel))
            }

            Section("Ability Scores & Proficiencies") {
                NavigationLink(
                    "Ability Scores",
                    destination: EditAbilityScores(monsterViewModel: monsterViewModel))

                NavigationLink(
                    "Saving Throws",
                    destination: EditSavingThrows(monsterViewModel: monsterViewModel))

                NavigationLink(
                    "Skills",
                    destination: EditSkills(monsterViewModel: monsterViewModel))
            }

            Section("Defenses, Senses & Languages") {
                NavigationLink(
                    "Condition Immunities",
                    destination: EditStrings(
                        viewModel: monsterViewModel,
                        path: \.conditionImmunities,
                        title: "Condition Immunities"))

                NavigationLink(
                    "Damage Immunities",
                    destination: EditStrings(
                        viewModel: monsterViewModel,
                        path: \.damageImmunities,
                        title: "Damage Immunities"))

                NavigationLink(
                    "Damage Resistances",
                    destination: EditStrings(
                        viewModel: monsterViewModel,
                        path: \.damageResistances,
                        title: "Damage Resistances"))

                NavigationLink(
                    "Damage Vulnerabilities",
                    destination: EditStrings(
                        viewModel: monsterViewModel,
                        path: \.damageVulnerabilities,
                        title: "Damage Vulnerabilities"))

                NavigationLink(
                    "Senses",
                    destination: EditStrings(
                        viewModel: monsterViewModel,
                        path: \.senses,
                        title: "Senses"))

                NavigationLink(
                    "Languages",
                    destination: EditLanguages(viewModel: monsterViewModel))

                NavigationLink(
                    "Challenge Rating",
                    destination: EditChallengeRating(viewModel: monsterViewModel))
            }

            Section("Traits, Actions & Lore") {
                NavigationLink(
                    "Special Traits & Abilities",
                    destination: EditTraits(
                        viewModel: monsterViewModel,
                        path: \.abilities,
                        title: "Special Traits"))

                NavigationLink(
                    "Actions",
                    destination: EditTraits(
                        viewModel: monsterViewModel,
                        path: \.actions,
                        title: "Actions"))

                NavigationLink(
                    "Reactions",
                    destination: EditTraits(
                        viewModel: monsterViewModel,
                        path: \.reactions,
                        title: "Reactions"))

                NavigationLink(
                    "Legendary Actions",
                    destination: EditTraits(
                        viewModel: monsterViewModel,
                        path: \.legendaryActions,
                        title: "Legendary Actions"))

                NavigationLink(
                    "Lair Actions",
                    destination: EditTraits(
                        viewModel: monsterViewModel,
                        path: \.lairActions,
                        title: "Lair Actions"))

                NavigationLink(
                    "Regional Actions",
                    destination: EditTraits(
                        viewModel: monsterViewModel,
                        path: \.regionalActions,
                        title: "Regional Actions"))
            }
        }
        .onAppear(perform: copyMonsterToLocal)
        .toolbar {
            ToolbarItem(placement: .navigationBarLeading) {
                Button {
                    isShowingPreview = true
                } label: {
                    Label("Preview Card", systemImage: "eye")
                }
            }

            ToolbarItem(placement: .confirmationAction) {
                Button("Save", action: saveMonster)
                    .fontWeight(.semibold)
            }
        }
        .sheet(isPresented: $isShowingPreview) {
            NavigationStack {
                MonsterDetailView(viewModel: monsterViewModel)
                    .navigationTitle(monsterViewModel.name.isEmpty ? "Monster Preview" : monsterViewModel.name)
                    .navigationBarTitleDisplayMode(.inline)
                    .toolbar {
                        ToolbarItem(placement: .confirmationAction) {
                            Button("Done") {
                                isShowingPreview = false
                            }
                        }
                    }
            }
        }
        .navigationTitle(monsterViewModel.name.isEmpty ? "Edit Monster" : monsterViewModel.name)
        .navigationBarTitleDisplayMode(.inline)
    }

    private func dismissView() {
        self.presentationMode.wrappedValue.dismiss()
    }

    private func saveMonster() {
        copyLocalToMonster()

        do {
            try viewContext.save()
        } catch {
            print("Failed to save monster: \(error)")
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

