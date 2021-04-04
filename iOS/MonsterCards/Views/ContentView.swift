//
//  ContentView.swift
//  MonsterCards
//
//  Created by Tom Hicks on 1/15/21.
//

import SwiftUI
import CoreData

struct ImportInfo {
    var monster: MonsterViewModel = MonsterViewModel()
}

struct ContentView: View {
    @Environment(\.managedObjectContext) private var viewContext
    @State private var importInfo = ImportInfo()
    @State private var isShowingImportDialog = false
        
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
        .onOpenURL(perform: beginImportingMonster)
        .sheet(isPresented: self.$isShowingImportDialog) {
            ImportMonster(monster: $importInfo.monster)
        }
    }
    
    func beginImportingMonster(url: URL) {
        self.importInfo.monster.name = url.absoluteString
        self.isShowingImportDialog = true
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView().environment(\.managedObjectContext, PersistenceController.preview.container.viewContext)
    }
}
