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

                ForEach(allMonsters.filter({searchText.isEmpty ? true : $0.name?.containsCaseInsensitive(searchText) ?? false })) { monster in
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
