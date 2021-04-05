//
//  ImportMonster.swift
//  MonsterCards
//
//  Created by Tom Hicks on 4/1/21.
//

import SwiftUI

struct ImportMonster: View {
    @Binding var monster: MonsterViewModel
    @Environment(\.managedObjectContext) private var viewContext
    @Binding var isOpen: Bool
    
    var body: some View {
        VStack{
            HStack {
                Button("Cancel", action: cancelImport)
                Spacer()
                Button("Add to Library", action: saveNewMonster)
            }
            MonsterDetailView(viewModel: monster)
        }
        .padding()
    }
    
    func cancelImport() {
        isOpen = false
    }
    
    func saveNewMonster() {
        print("Saving monster: \(monster.name)")
        withAnimation {
            let newMonster = Monster(context: viewContext)
            monster.copyToMonster(monster: newMonster)
            
            do {
                try viewContext.save()
                isOpen = false
            } catch {
                // Replace this implementation with code to handle the error appropriately.
                // fatalError() causes the application to generate a crash log and terminate. You should not use this function in a shipping application, although it may be useful during development.
                let nsError = error as NSError
                fatalError("Unresolved error \(nsError), \(nsError.userInfo)")
            }
        }
    }
}

struct ImportMonster_Previews: PreviewProvider {
    static var previews: some View {
        let monster = MonsterViewModel()
        monster.name = "Steve"
        monster.size = "Medium"
        monster.type = "dwarf"
        monster.alignment = "chaotic good"
        monster.armorType = .none
        monster.hasShield = true
        monster.hitDice = 4
        monster.strengthScore = 20
        monster.dexterityScore = 14
        monster.constitutionScore = 18
        monster.intelligenceScore = 8
        monster.wisdomScore = 8
        monster.charismaScore = 15
        monster.walkSpeed = 40
        monster.challengeRating = .four
        monster.languages = [LanguageViewModel("Common", true), LanguageViewModel("Giant", true)]
        monster.actions = [AbilityViewModel("Greataxe, +3", "__Badass Attack:___ Hits the other dude on a _3_ or above and does a ton of damage")]
        
        return
            VStack{
                Text("Hello, World!")
            }
            .sheet(isPresented: .constant(true)) {
                ImportMonster(monster: .constant(monster), isOpen: .constant(true))
            }
        
    }
}
