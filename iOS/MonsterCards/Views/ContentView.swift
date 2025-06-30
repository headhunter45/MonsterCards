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
        .sheet(isPresented: $isShowingImportDialog) {
            ImportMonster(monster: $importInfo.monster, isOpen: $isShowingImportDialog)
        }
    }
    
    func beginImportingMonster(url: URL) {

        // TOOD: only do this if the file name ends in .json or .monster
        
        let decoder = JSONDecoder()
        do {
            let isAccessing = url.startAccessingSecurityScopedResource()
            defer {
                if (isAccessing) {
                    url.stopAccessingSecurityScopedResource()
                }
            }
            let data = try Data(contentsOf: url)
            let monsterDTO = try decoder.decode(MonsterDTO.self, from: data)
            // TODO: check for some minimal set of properties to ensure this is the expected json schema
            self.importInfo.monster = MonsterImportHelper.import5ESBMonster(monsterDTO)
            // TODO: throw or set an err here and don't set isShowingImportDialog to true if the file didn't match any of our supported monster schemas.
            self.isShowingImportDialog = true
        } catch let error as NSError {
            // TODO: show an error message to the user that we were unable to open the file and maybe why.
            print(error)
        }
    }

}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView().environment(\.managedObjectContext, PersistenceController.preview.container.viewContext)
    }
}
