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
    
    @State private var monsterName: String = ""
    @State private var monsterSize: String = ""
    @State private var monsterType: String = ""
    @State private var monsterSubtype: String = ""
    @State private var monsterAlignment: String = ""
    @State private var monsterHitDice: Int64 = 0
    @State private var monsterHasCustomHP: Bool = false
    @State private var monsterCustomHP: String = ""
    @State private var monsterArmorType: String = ""
    @State private var monsterHasShield: Bool = false
    @State private var monsterNaturalArmorBonus: Int64 = 0
    @State private var monsterCustomArmor: String = ""
    @State private var monsterBaseSpeed: Int64 = 0
    @State private var monsterBurrowSpeed: Int64 = 0
    @State private var monsterClimbSpeed: Int64 = 0
    @State private var monsterFlySpeed: Int64 = 0
    @State private var monsterCanHover: Bool = false
    @State private var monsterSwimSpeed: Int64 = 0
    @State private var monsterHasCustomSpeed: Bool = false
    @State private var monsterCustomSpeed: String = ""
    @State private var monsterStrengthScore: Int64 = 10
    @State private var monsterDexterityScore: Int64 = 10
    @State private var monsterConstitutionScore: Int64 = 10
    @State private var monsterIntelligenceScore: Int64 = 10
    @State private var monsterWisdomScore: Int64 = 10
    @State private var monsterCharismaScore: Int64 = 10
    
    @State private var monsterStrengthSavingThrowProficiency: ProficiencyType = .none
    @State private var monsterStrengthSavingThrowAdvantage: AdvantageType = .none
    @State private var monsterDexteritySavingThrowProficiency: ProficiencyType = .none
    @State private var monsterDexteritySavingThrowAdvantage: AdvantageType = .none
    @State private var monsterConstitutionSavingThrowProficiency: ProficiencyType = .none
    @State private var monsterConstitutionSavingThrowAdvantage: AdvantageType = .none
    @State private var monsterIntelligenceSavingThrowProficiency: ProficiencyType = .none
    @State private var monsterIntelligenceSavingThrowAdvantage: AdvantageType = .none
    @State private var monsterWisdomSavingThrowProficiency: ProficiencyType = .none
    @State private var monsterWisdomSavingThrowAdvantage: AdvantageType = .none
    @State private var monsterCharismaSavingThrowProficiency: ProficiencyType = .none
    @State private var monsterCharismaSavingThrowAdvantage: AdvantageType = .none
    

    var body: some View {
        List {
            Section(header: Text("Basic Info")) {
                // Editable Text field bound to monster.name
                MCTextField(
                    label: "Name",
                    value: $monsterName)
                
                // Editable Text field bound to monster.size
                MCTextField(
                    label: "Size",
                    value: $monsterSize)
                
                // Editable Text field bound to monster.type
                MCTextField(
                    label: "Type",
                    value: $monsterType)
                
                // Editable Text field bound to monster.subType
                MCTextField(
                    label: "Subtype",
                    value: $monsterSubtype)
                                
                // Editable Text field bound to monster.alignment
                MCTextField(
                    label: "Alignment",
                    value: $monsterAlignment)

                // Number with -/+ buttons bound to monster.hitDice
                MCStepperField(
                    label: "Hit Dice",
                    value: $monsterHitDice)
                
                // Toggle bound to monster.hasCustomHP?
                Toggle(
                    "Has Custom HP",
                    isOn:$monsterHasCustomHP)
                
                // Editable Text field bound to monster.customHpText?
                MCTextField(
                    label: "Custom HP",
                    value: $monsterCustomHP)
                
            }
            .textCase(nil)
            
            Section(header: Text("Armor")) {
                // Armor Type select bound to monster.armorType?
                // TODO: this should be a select/dropdown
                MCTextField(
                    label: "Armor Type",
                    value: $monsterArmorType)
                
                // Toggle bound to monster.hasShield?
                Toggle(
                    "Has Shield",
                    isOn: $monsterHasShield)
                
                // Number with -/+ buttons bound to monster.naturalArmorBonus
                MCStepperField(
                    label: "Natural Armor Bonus",
                    value: $monsterNaturalArmorBonus)
                
                // Editable Text field bound to monster.customArmorText?
                MCTextField(
                    label: "Custom Armor",
                    value: $monsterCustomArmor)
                
            }
            .textCase(nil)
            
            Section(header: Text("Speed")) {
                // Number bound to monster.baseSpeed
                MCStepperField(
                    label: "Base",
                    step: 5,
                    suffix: " ft.",
                    value: $monsterBaseSpeed)
                
                // Number bound to monster.burrowSpeed
                MCStepperField(
                    label: "Burrow",
                    step: 5,
                    suffix: " ft.",
                    value: $monsterBurrowSpeed)
                
                // Number bound to monster.climbSpeed
                MCStepperField(
                    label: "Climb",
                    step: 5,
                    suffix: " ft.",
                    value: $monsterClimbSpeed)
                
                // Number bound to monster.flySpeed
                MCStepperField(
                    label: "Fly",
                    step: 5,
                    suffix: " ft.",
                    value: $monsterFlySpeed)
                
                // Toggle bound to monster.canHover
                Toggle(
                    "Can Hover",
                    isOn: $monsterCanHover)
                
                // Number bound to monster.swimSpeed
                MCStepperField(
                    label: "Swim",
                    step: 5,
                    suffix: " ft.",
                    value: $monsterSwimSpeed)
                
                // Toggle bound to monster.hasCustomSpeed
                Toggle(
                    "Has Custom Speed",
                    isOn: $monsterHasCustomSpeed)
                
                // Editable Text field bound to monster.customSpeedText
                MCTextField(
                    label: "Custom Speed",
                    value: $monsterCustomSpeed)
            }
            .textCase(nil)
            
            Section(header: Text("Ability Scores")) {
                MCStepperField(
                    label: "STR",
                    value: $monsterStrengthScore)
                MCStepperField(
                    label: "DEX",
                    value: $monsterDexterityScore)
                MCStepperField(
                    label: "CON",
                    value: $monsterConstitutionScore)
                MCStepperField(
                    label: "INT",
                    value: $monsterIntelligenceScore)
                MCStepperField(
                    label: "WIS",
                    value: $monsterWisdomScore)
                MCStepperField(
                    label: "CHA",
                    value: $monsterCharismaScore)
            }
            .textCase(nil)
            
            Section(header: Text("Saving Throws")) {
                VStack {
                    MCAdvantagePicker(
                        label: "Strength Advantage",
                        value: $monsterStrengthSavingThrowAdvantage)

                    MCProficiencyPicker(
                        label: "Strength Proficiency",
                        value: $monsterStrengthSavingThrowProficiency)
                }
                VStack {
                    MCAdvantagePicker(
                        label: "Dexterity Advantage",
                        value: $monsterDexteritySavingThrowAdvantage)

                    MCProficiencyPicker(
                        label: "Dexterity Proficiency",
                        value: $monsterDexteritySavingThrowProficiency)
                }
                VStack {
                    MCAdvantagePicker(
                        label: "Constitution Advantage",
                        value: $monsterConstitutionSavingThrowAdvantage)

                    MCProficiencyPicker(
                        label: "Constitution Proficiency",
                        value: $monsterConstitutionSavingThrowProficiency)
                }
                VStack {
                    MCAdvantagePicker(
                        label: "Intelligence Advantage",
                        value: $monsterIntelligenceSavingThrowAdvantage)

                    MCProficiencyPicker(
                        label: "Intelligence Proficiency",
                        value: $monsterIntelligenceSavingThrowProficiency)
                    }
                VStack {
                    MCAdvantagePicker(
                        label: "Wisdom Advantage",
                        value: $monsterWisdomSavingThrowAdvantage)

                    MCProficiencyPicker(
                        label: "Wisdom Proficiency",
                        value: $monsterWisdomSavingThrowProficiency)
                    }
                VStack {
                    MCAdvantagePicker(
                        label: "Charisma Advantage",
                        value: $monsterCharismaSavingThrowAdvantage)

                    MCProficiencyPicker(
                        label: "Charisma Proficiency",
                        value: $monsterCharismaSavingThrowProficiency)
                }
            }
            .textCase(nil)
        }
        .onAppear(perform: copyMonsterToLocal)
        .toolbar(content: {
            ToolbarItem(placement: .primaryAction) {
                Button("Save", action: saveMonster)
            }
            ToolbarItem(placement: ToolbarItemPlacement.cancellationAction) {
                Button("Cancel", action: cancel)
            }
        })
        .navigationTitle(monster.name ?? "")
        .navigationBarTitleDisplayMode(.inline)
        .navigationBarBackButtonHidden(true)
    }
    
    private func dismissView() {
        self.presentationMode.wrappedValue.dismiss()
    }
    
    private func saveMonster() {
        copyLocalToMonster()
        
        do {
            try viewContext.save()
        } catch {
            // Replace this implementation with code to handle the error appropriately.
            // fatalError() causes the application to generate a crash log and terminate. You should not use this function in a shipping application, although it may be useful during development.
            let nsError = error as NSError
            fatalError("Unresolved error \(nsError), \(nsError.userInfo)")
        }
        // TODO: save coredata context
        dismissView()
    }
    
    private func cancel() {
        dismissView()
    }
    
    private func copyMonsterToLocal() {
        monsterName = monster.name ?? ""
        monsterSize = monster.size ?? ""
        monsterType = monster.type ?? ""
        monsterSubtype = monster.subtype ?? ""
        monsterAlignment = monster.alignment ?? ""
        monsterHitDice = monster.hitDice
        monsterHasCustomHP = monster.hasCustomHP
        monsterCustomHP = monster.customHP ?? ""
        monsterArmorType = monster.armorType ?? ""
        monsterHasShield = monster.hasShield
        monsterNaturalArmorBonus = monster.naturalArmorBonus
        monsterCustomArmor = monster.customArmor ?? ""
        monsterBaseSpeed = monster.baseSpeed
        monsterBurrowSpeed = monster.burrowSpeed
        monsterClimbSpeed = monster.climbSpeed
        monsterFlySpeed = monster.flySpeed
        monsterCanHover = monster.canHover
        monsterSwimSpeed = monster.swimSpeed
        monsterHasCustomSpeed = monster.hasCustomSpeed
        monsterCustomSpeed = monster.customSpeed ?? ""
        monsterStrengthScore = monster.strengthScore
        monsterDexterityScore = monster.dexterityScore
        monsterConstitutionScore = monster.constitutionScore
        monsterIntelligenceScore = monster.intelligenceScore
        monsterWisdomScore = monster.wisdomScore
        monsterCharismaScore = monster.charismaScore
        monsterStrengthSavingThrowProficiency = ProficiencyType.init(rawValue: monster.strengthSavingThrowProficiency ?? "") ?? .none
        monsterStrengthSavingThrowAdvantage = AdvantageType(rawValue: monster.strengthSavingThrowAdvantage ?? "") ?? .none
        monsterDexteritySavingThrowProficiency = ProficiencyType(rawValue: monster.dexteritySavingThrowProficiency ?? "") ?? .none
        monsterDexteritySavingThrowAdvantage = AdvantageType(rawValue: monster.dexteritySavingThrowAdvantage ?? "") ?? .none
        monsterConstitutionSavingThrowProficiency = ProficiencyType(rawValue: monster.constitutionSavingThrowProficiency ?? "") ?? .none
        monsterConstitutionSavingThrowAdvantage = AdvantageType(rawValue: monster.constitutionSavingThrowAdvantage ?? "") ?? .none
        monsterIntelligenceSavingThrowProficiency = ProficiencyType(rawValue: monster.intelligenceSavingThrowProficiency ?? "") ?? .none
        monsterIntelligenceSavingThrowAdvantage = AdvantageType(rawValue: monster.intelligenceSavingThrowAdvantage ?? "") ?? .none
        monsterWisdomSavingThrowProficiency = ProficiencyType(rawValue: monster.wisdomSavingThrowProficiency ?? "") ?? .none
        monsterWisdomSavingThrowAdvantage = AdvantageType(rawValue: monster.wisdomSavingThrowAdvantage ?? "") ?? .none
        monsterCharismaSavingThrowProficiency = ProficiencyType(rawValue: monster.charismaSavingThrowProficiency ?? "") ?? .none
        monsterCharismaSavingThrowAdvantage = AdvantageType(rawValue: monster.charismaSavingThrowAdvantage ?? "") ?? .none
    }
    
    private func copyLocalToMonster() {
        monster.name = monsterName
        monster.size = monsterSize
        monster.type = monsterType
        monster.subtype = monsterSubtype
        monster.alignment = monsterAlignment
        monster.hitDice = monsterHitDice
        monster.hasCustomHP = monsterHasCustomHP
        monster.customHP = monsterCustomHP
        monster.armorType = monsterArmorType
        monster.hasShield = monsterHasShield
        monster.naturalArmorBonus = monsterNaturalArmorBonus
        monster.customArmor = monsterCustomArmor
        monster.baseSpeed = monsterBaseSpeed
        monster.burrowSpeed = monsterBurrowSpeed
        monster.climbSpeed = monsterClimbSpeed
        monster.flySpeed = monsterFlySpeed
        monster.canHover = monsterCanHover
        monster.swimSpeed = monsterSwimSpeed
        monster.hasCustomSpeed = monsterHasCustomSpeed
        monster.customSpeed = monsterCustomSpeed
        monster.strengthScore = monsterStrengthScore
        monster.dexterityScore = monsterDexterityScore
        monster.constitutionScore = monsterConstitutionScore
        monster.intelligenceScore = monsterIntelligenceScore
        monster.wisdomScore = monsterWisdomScore
        monster.charismaScore = monsterCharismaScore
        monster.strengthSavingThrowProficiency = monsterStrengthSavingThrowProficiency.rawValue
        monster.strengthSavingThrowAdvantage = monsterStrengthSavingThrowAdvantage.rawValue
        monster.dexteritySavingThrowProficiency = monsterDexteritySavingThrowProficiency.rawValue
        monster.dexteritySavingThrowAdvantage = monsterDexteritySavingThrowAdvantage.rawValue
        monster.constitutionSavingThrowProficiency = monsterConstitutionSavingThrowProficiency.rawValue
        monster.constitutionSavingThrowAdvantage = monsterConstitutionSavingThrowAdvantage.rawValue
        monster.intelligenceSavingThrowProficiency = monsterIntelligenceSavingThrowProficiency.rawValue
        monster.intelligenceSavingThrowAdvantage = monsterIntelligenceSavingThrowAdvantage.rawValue
        monster.wisdomSavingThrowProficiency = monsterWisdomSavingThrowProficiency.rawValue
        monster.wisdomSavingThrowAdvantage = monsterWisdomSavingThrowAdvantage.rawValue
        monster.charismaSavingThrowProficiency = monsterCharismaSavingThrowProficiency.rawValue
        monster.charismaSavingThrowAdvantage = monsterCharismaSavingThrowAdvantage.rawValue
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
