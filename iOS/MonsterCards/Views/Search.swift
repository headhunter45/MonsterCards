//
//  Search.swift
//  MonsterCards
//
//  Created by Tom Hicks on 1/15/21.
//

import SwiftUI

struct Search: View {
    @State private var searchText = ""
    
    @Environment(\.managedObjectContext) var managedObjectContext
    @FetchRequest(
        sortDescriptors: [
            NSSortDescriptor(keyPath: \Monster.name, ascending: true),
        ],
        animation: .default)
    var allMonsters: FetchedResults<Monster>
    
    var body: some View {
        NavigationView {
            List {
                SearchBar(text: $searchText)
                    .padding(.top, -30)
                
                ForEach(
                    allMonsters.filter(
                        {
                            // TODO: consider splitting search text into words and if each word appears in any of these fields return true e.g, "large demon" would match large in size and demon in type.
                            // TODO: add tags and search by tags
                            // TODO: add a display of what fields matched on each item in the results
                            // TODO: make the criteria configurable from this screen
                            // TODO: find a way to add challenge rating as a search criteria
                            
                            if (searchText.isEmpty) {
                                return true
                            }
                            
                            if (StringHelper.safeContainsCaseInsensitive($0.name, searchText)) {
                                return true
                            }
                            
                            if (StringHelper.safeContainsCaseInsensitive($0.size, searchText)) {
                                return true
                            }
                            
                            if (StringHelper.safeContainsCaseInsensitive($0.type, searchText)) {
                                return true
                            }
                            
                            if (StringHelper.safeContainsCaseInsensitive($0.subtype, searchText)) {
                                return true
                            }
                            
                            if (StringHelper.safeContainsCaseInsensitive($0.alignment, searchText)) {
                                return true
                            }
                            
                            return false
                        })) { monster in
                    NavigationLink(destination: MonsterDetail(monster: monster)) {
                        Text(monster.name ?? "")
                    }
                }
            }
        }.navigationViewStyle(StackNavigationViewStyle())
    }
}

struct Search_Previews: PreviewProvider {
    static var previews: some View {
        Search().environment(\.managedObjectContext, PersistenceController.preview.container.viewContext)
    }
}
