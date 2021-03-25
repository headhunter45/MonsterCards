//
//  MonsterDetail.swift
//  MonsterCards
//
//  Created by Tom Hicks on 1/15/21.
//

import SwiftUI
import MarkdownUI

struct LabeledField<Content: View>: View {
    @Environment(\.horizontalSizeClass) var sizeClass
    
    let content: Content
    let label: String
    
    @inlinable public init(
        _ label: String,
        @ViewBuilder content: () -> Content) {
        self.content = content()
        self.label = label
    }
    
    var body: some View {
        if (sizeClass == .compact) {
            VStack(alignment: .leading) {
                Text(label)
                    .fontWeight(.bold)
                content
            }
        } else {
            HStack(alignment: .top) {
                Text(label)
                    .fontWeight(.bold)
                content
            }
        }
    }
}

struct SectionDivider: View {
    var body: some View {
        Image("section-divider") // divider
            .resizable()
            .scaledToFit()
            .padding(.vertical, 4)
    }
}

struct SmallAbilityScore: View {
    var label: String
    var score: Int64
    var modifier: Int

    public init(
        _ label: String,
        _ score: Int64,
        _ modifier: Int) {
        self.label = label
        self.score = score
        self.modifier = modifier
    }

    var body: some View {
        VStack {
            Text(label)
                .fontWeight(.bold)
            Text(String(format: "%d (%+d)", score, modifier))
        }
        .frame(maxWidth: .infinity)
    }
}

struct BasicInfoView: View {
    @ObservedObject var monster: Monster
    
    var body: some View {
        let monsterMeta = monster.meta
        let monsterArmorClassDescription = monster.armorClassDescription
        let monsterHitPoints = monster.hitPoints
        let monsterSpeed = monster.speed
        
        // meta: "(large humanoid (elf) lawful evil"
        if (!monsterMeta.isEmpty) {
            Text(monsterMeta)
                .font(.subheadline)
                .foregroundColor(.secondary)
        }
        
        SectionDivider()

        // AC
        if (!monsterArmorClassDescription.isEmpty) {
            LabeledField("Armor Class") {
                Text(monsterArmorClassDescription)// armor class
            }
        }
        
        // HP
        if (!monsterHitPoints.isEmpty) {
            LabeledField("Hit Points") {
                Text(monsterHitPoints) // hit points
            }
        }
        
        // Speed
        if (!monsterSpeed.isEmpty) {
            LabeledField("Speed") {
                Text(monsterSpeed) // speed
            }
        }
    }
}

struct AbilityScoresView: View {
    @ObservedObject var monster: Monster
    
    var body: some View {
        SectionDivider()
        
        // Ability Scores
        HStack {
            SmallAbilityScore("STR", monster.strengthScore, monster.strengthModifier)
            SmallAbilityScore("DEX", monster.dexterityScore, monster.dexterityModifier)
            SmallAbilityScore("CON", monster.constitutionScore, monster.constitutionModifier)
            SmallAbilityScore("INT", monster.intelligenceScore, monster.intelligenceModifier)
            SmallAbilityScore("WIS", monster.wisdomScore, monster.wisdomModifier)
            SmallAbilityScore("CHA", monster.charismaScore, monster.charismaModifier)
        }
    }
}

struct ResistancesAndImmunitiesView: View {
    @ObservedObject var monster: Monster
    
    var body: some View {
        let monsterDamageVulnerabilitiesDescription = monster.damageVulnerabilitiesDescription
        let monsterDamageResistancesDescription = monster.damageResistancesDescription
        let monsterDamageImmunitiesDescription = monster.damageImmunitiesDescription
        let monsterConditionImmunitiesDescription = monster.conditionImmunitiesDescription
        let monsterSensesDescription = monster.sensesDescription
        
        // Damage Vulnerabilities
        if (!monsterDamageVulnerabilitiesDescription.isEmpty) {
            LabeledField("Damage Vulnerabilities") {
                Text(monsterDamageVulnerabilitiesDescription)
            }
        }
        
        // Damage Resistances
        if (!monsterDamageResistancesDescription.isEmpty) {
            LabeledField("Damage Resistances") {
                Text(monsterDamageResistancesDescription)
            }
        }
        
        // Damage Immunities
        if (!monsterDamageImmunitiesDescription.isEmpty) {
            LabeledField("Damage Immunities") {
                Text(monsterDamageImmunitiesDescription)
            }
        }
        
        // Condition Immunities
        if (!monsterConditionImmunitiesDescription.isEmpty) {
            LabeledField("Condition Immunities") {
                Text(monsterConditionImmunitiesDescription)
            }
        }
        
        // Senses
        if (!monsterSensesDescription.isEmpty) {
            LabeledField("Senses") {
                Text(monsterSensesDescription)
            }
        }
    }
}

struct SavingThrowsAndSkillsView: View {
    @ObservedObject var monster: Monster
    
    var body: some View {
        let savingThrowsDescription = monster.savingThrowsDescription
        let skillsDescription = monster.skillsDescription
        
        // Saving Throws
        if (!savingThrowsDescription.isEmpty) {
            LabeledField("Saving Throws") {
                Text(savingThrowsDescription)
            }
        }

        // Skills
        if (!skillsDescription.isEmpty) {
            LabeledField("Skills") {
                Text(skillsDescription)
            }
        }
    }
}

struct MonsterDetail: View {
    let kTextColor: Color = Color(hex: 0x982818)
    
    @ObservedObject var monster: Monster
    
    var body: some View {
        ScrollView {
            // TODO: Consider adding an inmage here at the top
            VStack (alignment: .leading) {
                let monsterLanguagesDescription = monster.languagesDescription
                let monsterChallengeRatingDescription = monster.challengeRatingDescription
                let monsterAbilities: [AbilityViewModel] = monster.abilities ?? []
                let monsterActions: [AbilityViewModel] = monster.actions ?? []
                let monsterLegendaryActions: [AbilityViewModel] = monster.legendaryActions ?? []
                
                BasicInfoView(monster: monster)
                
                // TODO: Find a way to hide unnecessarry dividiers.
                // if sections 0, 1, 2, and 3 are present there should be a divider between each of them
                // if section 1 is not present there should be one and only one divider between sections 0 and 2 as well as the one between 2 and 3
                // if sections 1 and 2 are not present there should be a single divider between sections 0 and 3
                

                AbilityScoresView(monster: monster)

                SectionDivider()
                
                SavingThrowsAndSkillsView(monster: monster)
                
                ResistancesAndImmunitiesView(monster: monster)
                
                // Languages
                if (!monsterLanguagesDescription.isEmpty) {
                    LabeledField("Languages") {
                        Text(monsterLanguagesDescription)
                    }
                }
                
                // Challenge Rating
                if (!monsterChallengeRatingDescription.isEmpty) {
                    LabeledField("Challenge") {
                        Text(monsterChallengeRatingDescription)
                    }
                    
                    // TODO: add proficiency bonus
                }
                
                // Abilities
                if (monsterAbilities.count > 0) {
                    ForEach(monsterAbilities) { ability in
                        VStack {
                            Markdown(Document(ability.renderedText(monster)))
                            Divider()
                        }
                    }
                }

                // Actions
                if (monsterActions.count > 0) {
                    VStack(alignment: .leading) {
                        Text("Actions")
                            .fontWeight(.bold)
                        ForEach(monsterActions) { action in
                            VStack {
                                Markdown(Document(action.renderedText(monster)))
                                Divider()
                            }
                        }
                    }
                }

                // Legendary Actions
                if (monsterLegendaryActions.count > 0) {
                    VStack(alignment: .leading) {
                        Text("Legendary Actions")
                            .fontWeight(.bold)
                        ForEach(monsterLegendaryActions) { action in
                            VStack {
                                Markdown(Document(action.renderedText(monster)))
                                Divider()
                            }
                        }
                    }
                }
                
            }
            .padding(.horizontal)
            .foregroundColor(kTextColor)
        }
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

struct MonsterDetail_Previews: PreviewProvider {
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
            MonsterDetail(monster: monster)
                .environment(\.managedObjectContext, context)
                .previewDevice("iPod touch (7th generation)")
            MonsterDetail(monster: monster)
                .environment(\.managedObjectContext, context)
                .previewDevice("iPad Pro (11-inch) (2nd generation)")
        }
    }
}
