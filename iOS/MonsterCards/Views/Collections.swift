//
//  Collections.swift
//  MonsterCards
//
//  Created by Tom Hicks on 1/15/21.
//

import SwiftUI
import CoreData

struct Collections: View {
    @Environment(\.managedObjectContext) private var viewContext

    @FetchRequest(
        sortDescriptors: [NSSortDescriptor(keyPath: \Collection.sortOrder, ascending: true)],
        animation: .default
    )
    private var collections: FetchedResults<Collection>

    // TODO: Add state for showing 'Create Collection' dialog or sheet
    // TODO: Add state for search/filter within collections

    var body: some View {
        NavigationStack {
            List {
                if collections.isEmpty {
                    ContentUnavailableView(
                        "No Collections",
                        systemImage: "folder.badge.plus",
                        description: Text("Tap '+' to create your first monster collection.")
                    )
                } else {
                    ForEach(collections) { collection in
                        NavigationLink {
                            // TODO: Implement CollectionDetailView to show monsters in this collection
                            Text(collection.name ?? "Unnamed Collection")
                        } label: {
                            VStack(alignment: .leading) {
                                Text(collection.name ?? "Unnamed Collection")
                                    .font(.headline)
                                if let details = collection.details, !details.isEmpty {
                                    Text(details)
                                        .font(.subheadline)
                                        .foregroundColor(.secondary)
                                }
                            }
                        }
                    }
                    .onDelete(perform: deleteCollections)
                }
            }
            .navigationTitle("Collections")
            .toolbar {
                ToolbarItem(placement: .primaryAction) {
                    Button(action: addCollection) {
                        Label("Add Collection", systemImage: "plus")
                    }
                }
            }
        }
    }

    private func addCollection() {
        // TODO: Implement collection creation logic (e.g. presenting sheet or creating new Collection managed object)
    }

    private func deleteCollections(offsets: IndexSet) {
        // TODO: Implement collection deletion logic with context saving
    }
}

#Preview {
    Collections()
        .environment(\.managedObjectContext, PersistenceController.preview.container.viewContext)
}
