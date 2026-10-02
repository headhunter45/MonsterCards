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
    @State private var isCreateSheetPresented = false
    @State private var editingCollection: Collection? = nil
    @State private var searchText = ""

    @FetchRequest(
        sortDescriptors: [
            NSSortDescriptor(keyPath: \Collection.sortOrder, ascending: true),
            NSSortDescriptor(keyPath: \Collection.name, ascending: true)
        ],
        animation: .default
    )
    private var collections: FetchedResults<Collection>

    private var filteredCollections: [Collection] {
        let q = searchText.trimmingCharacters(in: .whitespacesAndNewlines)
        if q.isEmpty {
            return Array(collections)
        }
        return collections.filter {
            StringHelper.safeContainsCaseInsensitive($0.name, q)
                || StringHelper.safeContainsCaseInsensitive($0.details, q)
        }
    }

    var body: some View {
        NavigationStack {
            List {
                if filteredCollections.isEmpty {
                    ContentUnavailableView(
                        searchText.isEmpty ? "No Collections" : "No Matching Collections",
                        systemImage: searchText.isEmpty ? "folder.badge.plus" : "magnifyingglass",
                        description: Text(searchText.isEmpty ? "Tap '+' to organize your monsters into encounters and campaigns." : "No collections match '\(searchText)'.")
                    )
                } else {
                    ForEach(filteredCollections) { collection in
                        NavigationLink(destination: CollectionDetailView(collection: collection)) {
                            CollectionRowView(collection: collection)
                        }
                        .swipeActions(edge: .trailing, allowsFullSwipe: true) {
                            Button(role: .destructive) {
                                deleteCollection(collection)
                            } label: {
                                Label("Delete", systemImage: "trash")
                            }

                            Button {
                                editingCollection = collection
                            } label: {
                                Label("Edit", systemImage: "pencil")
                            }
                            .tint(.blue)
                        }
                        .swipeActions(edge: .leading) {
                            Button {
                                pushCollectionToDashboard(collection)
                            } label: {
                                Label("Dashboard", systemImage: "rectangle.3.offgrid.fill")
                            }
                            .tint(.orange)
                        }
                        .contextMenu {
                            Button {
                                pushCollectionToDashboard(collection)
                            } label: {
                                Label("Push to Dashboard", systemImage: "rectangle.3.offgrid.fill")
                            }

                            Button {
                                editingCollection = collection
                            } label: {
                                Label("Edit Details", systemImage: "pencil")
                            }

                            Divider()

                            Button(role: .destructive) {
                                deleteCollection(collection)
                            } label: {
                                Label("Delete", systemImage: "trash")
                            }
                        }
                    }
                    .onDelete(perform: deleteCollections)
                }
            }
            .navigationTitle("Collections")
            .searchable(text: $searchText, prompt: "Search collections...")
            .toolbar {
                ToolbarItem(placement: .primaryAction) {
                    Button(action: { isCreateSheetPresented = true }) {
                        Label("Add Collection", systemImage: "plus")
                    }
                }
            }
            .sheet(isPresented: $isCreateSheetPresented) {
                CreateCollectionSheet(isPresented: $isCreateSheetPresented)
            }
            .sheet(item: $editingCollection) { collection in
                EditCollectionSheet(collection: collection, isPresented: Binding(
                    get: { editingCollection != nil },
                    set: { if !$0 { editingCollection = nil } }
                ))
            }
        }
    }

    private func deleteCollection(_ collection: Collection) {
        viewContext.delete(collection)
        try? viewContext.save()
    }

    private func deleteCollections(offsets: IndexSet) {
        for index in offsets {
            let collection = filteredCollections[index]
            viewContext.delete(collection)
        }
        try? viewContext.save()
    }

    private func pushCollectionToDashboard(_ collection: Collection) {
        guard let links = collection.monsters as? Set<CollectionMonster> else { return }
        for link in links {
            if let mId = link.monsterId, let uuid = UUID(uuidString: mId) {
                try? MonsterRepository.shared.addMonsterToDashboard(id: uuid)
            }
        }
    }
}

struct CollectionRowView: View {
    @ObservedObject var collection: Collection

    private var monsterCount: Int {
        (collection.monsters as? Set<CollectionMonster>)?.count ?? 0
    }

    var body: some View {
        HStack(spacing: 12) {
            Image(systemName: "folder.fill")
                .font(.title2)
                .foregroundColor(Theme.dndGold)

            VStack(alignment: .leading, spacing: 3) {
                Text(collection.name ?? "Unnamed Collection")
                    .font(.headline)
                    .foregroundColor(.primary)

                if let details = collection.details, !details.isEmpty {
                    Text(details)
                        .font(.caption)
                        .foregroundColor(.secondary)
                        .lineLimit(1)
                }
            }

            Spacer()

            Text("\(monsterCount)")
                .font(.caption)
                .fontWeight(.bold)
                .padding(.horizontal, 8)
                .padding(.vertical, 3)
                .background(Color.secondary.opacity(0.12))
                .cornerRadius(10)
        }
        .padding(.vertical, 2)
    }
}

struct CreateCollectionSheet: View {
    @Binding var isPresented: Bool
    @Environment(\.managedObjectContext) private var viewContext
    @State private var name = ""
    @State private var details = ""

    var body: some View {
        NavigationStack {
            Form {
                Section("Collection Details") {
                    TextField("Collection Name (e.g. Forest Ambush)", text: $name)
                    TextField("Description / Campaign Notes", text: $details, axis: .vertical)
                        .lineLimit(3...5)
                }
            }
            .navigationTitle("New Collection")
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .cancellationAction) {
                    Button("Cancel") { isPresented = false }
                }
                ToolbarItem(placement: .confirmationAction) {
                    Button("Create") {
                        let col = Collection(context: viewContext)
                        col.name = name.trimmingCharacters(in: .whitespacesAndNewlines)
                        col.details = details.trimmingCharacters(in: .whitespacesAndNewlines)
                        col.sortOrder = 0
                        try? viewContext.save()
                        isPresented = false
                    }
                    .disabled(name.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty)
                }
            }
        }
    }
}

#Preview {
    Collections()
        .environment(\.managedObjectContext, PersistenceController.preview.container.viewContext)
}
