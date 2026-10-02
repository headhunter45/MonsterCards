//
//  Library.swift
//  MonsterCards
//
//  Created by Tom Hicks on 1/15/21.
//

import SwiftUI
import CoreData

struct Library: View {
    @Environment(\.managedObjectContext) private var viewContext
    @State private var searchText = ""
    @State private var selectedTypeFilter: String = "All"
    @State private var isShowingNewMonsterSheet = false
    @State private var newMonsterViewModel = MonsterViewModel()

    @FetchRequest(
        sortDescriptors: [
            NSSortDescriptor(keyPath: \Monster.name, ascending: true),
        ],
        animation: .default)
    private var allMonsters: FetchedResults<Monster>

    private var filteredMonsters: [Monster] {
        allMonsters.filter { monster in
            let matchesSearch: Bool
            if searchText.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty {
                matchesSearch = true
            } else {
                let q = searchText
                matchesSearch = StringHelper.safeContainsCaseInsensitive(monster.name, q)
                    || StringHelper.safeContainsCaseInsensitive(monster.type, q)
                    || StringHelper.safeContainsCaseInsensitive(monster.subtype, q)
                    || StringHelper.safeContainsCaseInsensitive(monster.alignment, q)
                    || StringHelper.safeContainsCaseInsensitive(monster.challengeRating, q)
            }

            let matchesType: Bool
            if selectedTypeFilter == "All" {
                matchesType = true
            } else {
                matchesType = (monster.type ?? "").lowercased() == selectedTypeFilter.lowercased()
            }

            return matchesSearch && matchesType
        }
    }

    var body: some View {
        NavigationStack {
            List {
                if filteredMonsters.isEmpty {
                    ContentUnavailableView(
                        searchText.isEmpty ? "Library Empty" : "No Matching Monsters",
                        systemImage: searchText.isEmpty ? "book.closed" : "magnifyingglass",
                        description: Text(searchText.isEmpty ? "Add monsters using '+' or import from Open5e / files." : "No monsters match '\(searchText)'.")
                    )
                } else {
                    ForEach(filteredMonsters) { monster in
                        NavigationLink(destination: MonsterDetailWrapper(monster: monster)) {
                            MonsterListRow(monster: monster)
                        }
                        .swipeActions(edge: .trailing, allowsFullSwipe: true) {
                            Button(role: .destructive) {
                                deleteMonster(monster)
                            } label: {
                                Label("Delete", systemImage: "trash")
                            }
                        }
                        .swipeActions(edge: .leading) {
                            Button {
                                addToDashboard(monster)
                            } label: {
                                Label("Dashboard", systemImage: "shield.righthalf.filled")
                            }
                            .tint(.orange)
                        }
                        .contextMenu {
                            Button {
                                addToDashboard(monster)
                            } label: {
                                Label("Add to Dashboard", systemImage: "rectangle.3.offgrid.fill")
                            }
                            Button(role: .destructive) {
                                deleteMonster(monster)
                            } label: {
                                Label("Delete", systemImage: "trash")
                            }
                        }
                    }
                    .onDelete(perform: deleteFromOffset)
                }
            }
            .navigationTitle("Library")
            .searchable(text: $searchText, prompt: "Search by name, type, CR...")
            .toolbar {
                ToolbarItem(placement: .primaryAction) {
                    Button(action: addMonster) {
                        Label("Add Monster", systemImage: "plus")
                    }
                }
            }
        }
    }

    private func addMonster() {
        withAnimation {
            let newItem = Monster(context: viewContext)
            newItem.name = "Unnamed Monster"
            newItem.uuid = UUID()
            newItem.size = "medium"
            newItem.type = "humanoid"
            newItem.strengthScore = 10
            newItem.dexterityScore = 10
            newItem.constitutionScore = 10
            newItem.intelligenceScore = 10
            newItem.wisdomScore = 10
            newItem.charismaScore = 10
            newItem.walkSpeed = 30
            newItem.hitDice = 1

            do {
                try viewContext.save()
            } catch {
                let nsError = error as NSError
                print("Failed to add monster: \(nsError), \(nsError.userInfo)")
            }
        }
    }

    private func deleteMonster(_ monster: Monster) {
        viewContext.delete(monster)
        do {
            try viewContext.save()
        } catch {
            let nsError = error as NSError
            print("Failed to delete monster: \(nsError), \(nsError.userInfo)")
        }
    }

    private func deleteFromOffset(indexSet: IndexSet) {
        for index in indexSet {
            let monster = filteredMonsters[index]
            deleteMonster(monster)
        }
    }

    private func addToDashboard(_ monster: Monster) {
        guard let uuid = monster.uuid else { return }
        Task { @MainActor in
            do {
                try MonsterRepository.shared.addMonsterToDashboard(id: uuid)
            } catch {
                print("Failed to add monster to dashboard: \(error)")
            }
        }
    }
}

struct MonsterListRow: View {
    @ObservedObject var monster: Monster

    var body: some View {
        HStack(spacing: 12) {
            VStack(alignment: .leading, spacing: 3) {
                Text(monster.name ?? "Unnamed Monster")
                    .font(.headline)
                    .foregroundColor(.primary)

                HStack(spacing: 6) {
                    if let size = monster.size, !size.isEmpty {
                        Text(size.capitalized)
                            .font(.caption)
                            .foregroundColor(.secondary)
                    }
                    if let type = monster.type, !type.isEmpty {
                        Text(type.capitalized)
                            .font(.caption)
                            .foregroundColor(.secondary)
                    }
                    if let cr = monster.challengeRating, !cr.isEmpty {
                        Text("CR \(cr)")
                            .font(.caption)
                            .fontWeight(.semibold)
                            .padding(.horizontal, 5)
                            .padding(.vertical, 1)
                            .background(Color.secondary.opacity(0.15))
                            .cornerRadius(4)
                    }
                }
            }
            Spacer()
        }
        .padding(.vertical, 2)
    }
}

#Preview {
    Library()
        .environment(\.managedObjectContext, PersistenceController.preview.container.viewContext)
}
