//
//  MonsterDetail.swift
//  MonsterCards
//
//  Created by Tom Hicks on 1/15/21.
//

import SwiftUI

struct LabeledField<Content: View>: View {
    let content: Content
    let label: String
    
    @inlinable public init(
        _ label: String,
        @ViewBuilder content: () -> Content) {
        self.content = content()
        self.label = label
    }
    
    var body: some View {
        HStack(alignment: .top) {
            Text(label)
                .fontWeight(.bold)
            content
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

struct MonsterDetail: View {
    let kTextColor: Color = Color(hex: 0x982818)
    
    var monster: Monster
    
    var body: some View {
        ScrollView {
            VStack (alignment: .leading) {
                // meta: "(large humanoid (elf) lawful evil"
                Text(monster.meta)
                    .font(.subheadline)
                    .foregroundColor(.secondary)
                    
                SectionDivider()

                // AC
                LabeledField("Armor Class") {
                    Text(monster.armorClassDescription)// armor class
                }
                // HP
                LabeledField("Hit Points") {
                    Text(monster.hitPoints) // hit points
                }
                // Speed
                LabeledField("Speed") {
                    Text(monster.speed) // speed
                }

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

                SectionDivider()
                
                let savingThrowsDescription = monster.savingThrowsDescription
                if (!savingThrowsDescription.isEmpty) {
                    LabeledField("Saving Throws") {
                        Text(monster.savingThrowsDescription)
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
