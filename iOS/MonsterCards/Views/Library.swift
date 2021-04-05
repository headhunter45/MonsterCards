//
//  Library.swift
//  MonsterCards
//
//  Created by Tom Hicks on 1/15/21.
//

import SwiftUI

struct Library: View {
    @Environment(\.managedObjectContext) private var viewContext

    @FetchRequest(
        sortDescriptors: [
            NSSortDescriptor(keyPath: \Monster.name, ascending: true),
        ],
        animation: .default)
    var allMonsters: FetchedResults<Monster>
    
    var body: some View {
        NavigationView{
            List {
                ForEach(allMonsters) { monster in
                    NavigationLink(destination: MonsterDetailWrapper(monster: monster)) {
                        Text(monster.name ?? "")
                    }
                }
                .onDelete(perform: { indexSet in
                    for index in indexSet {
                        let monster = allMonsters[index]
                        viewContext.delete(monster)
                        do {
                            try viewContext.save()
                        } catch {
                            // Replace this implementation with code to handle the error appropriately.
                            // fatalError() causes the application to generate a crash log and terminate. You should not use this function in a shipping application, although it may be useful during development.
                            let nsError = error as NSError
                            fatalError("Unresolved error \(nsError), \(nsError.userInfo)")
                        }
                    }
                })
            }
            .navigationTitle("Library")
            .navigationBarTitleDisplayMode(.inline)
            .toolbar(content: {
                ToolbarItem(placement: .primaryAction) {
                    Button(action: addMonster) {
                        Image(systemName:"plus")
                    }
                }
            })
        }
    }
    
    private func addMonster() {
        withAnimation {
            let newItem = Monster(context: viewContext)
            newItem.name = "Unnamed Monster"
            
            do {
                try viewContext.save()
            } catch {
                // Replace this implementation with code to handle the error appropriately.
                // fatalError() causes the application to generate a crash log and terminate. You should not use this function in a shipping application, although it may be useful during development.
                let nsError = error as NSError
                fatalError("Unresolved error \(nsError), \(nsError.userInfo)")
            }
        }
    }
}

struct Library_Previews: PreviewProvider {
    static var previews: some View {
        return Library().environment(\.managedObjectContext, PersistenceController.preview.container.viewContext)
    }
}
