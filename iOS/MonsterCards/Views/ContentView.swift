//
//  ContentView.swift
//  MonsterCards
//
//  Created by Tom Hicks on 1/15/21.
//

import SwiftUI
import CoreData

struct ContentView: View {
    @Environment(\.managedObjectContext) private var viewContext
    
    var body: some View {
        TabView {
            Search()
                .tabItem {
                    Image(systemName: "magnifyingglass")
                    Text("Search")
                }
            Dashboard()
                .tabItem {
                    Image(systemName: "rectangle.3.offgrid.fill")
                    Text("Dashboard")

                }
            Collections()
                .tabItem {
                    Image(systemName: "tray.full.fill")
                    Text("Collections")
                }
            Library()
                .tabItem {
                    Image(systemName: "book.fill")
                    Text("Library")
                }
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView().environment(\.managedObjectContext, PersistenceController.preview.container.viewContext)
    }
}
