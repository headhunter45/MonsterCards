//
//  Search.swift
//  MonsterCards
//
//  Created by Tom Hicks on 1/15/21.
//

import SwiftUI
import CoreData

struct Search: View {
    @State private var searchText = ""
    @State private var selectedCrFilter = "All"
    @State private var selectedTypeFilter = "All"

    @Environment(\.managedObjectContext) var managedObjectContext
    @FetchRequest(
        sortDescriptors: [
            NSSortDescriptor(keyPath: \Monster.name, ascending: true),
        ],
        animation: .default)
    private var allMonsters: FetchedResults<Monster>

    private let crOptions = ["All", "0", "1/8", "1/4", "1/2", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "15", "20"]
    private let typeOptions = ["All", "Aberration", "Beast", "Celestial", "Construct", "Dragon", "Elemental", "Fey", "Fiend", "Giant", "Humanoid", "Monstrosity", "Ooze", "Plant", "Undead"]

    private var searchResults: [Monster] {
        allMonsters.filter { monster in
            let cleanQuery = searchText.trimmingCharacters(in: .whitespacesAndNewlines)

            let matchesQuery: Bool
            if cleanQuery.isEmpty {
                matchesQuery = true
            } else {
                let terms = cleanQuery.components(separatedBy: " ").filter { !$0.isEmpty }
                matchesQuery = terms.allSatisfy { term in
                    StringHelper.safeContainsCaseInsensitive(monster.name, term)
                        || StringHelper.safeContainsCaseInsensitive(monster.size, term)
                        || StringHelper.safeContainsCaseInsensitive(monster.type, term)
                        || StringHelper.safeContainsCaseInsensitive(monster.subtype, term)
                        || StringHelper.safeContainsCaseInsensitive(monster.alignment, term)
                        || StringHelper.safeContainsCaseInsensitive(monster.challengeRating, term)
                }
            }

            let matchesCr = selectedCrFilter == "All" || (monster.challengeRating ?? "") == selectedCrFilter
            let matchesType = selectedTypeFilter == "All" || (monster.type ?? "").lowercased() == selectedTypeFilter.lowercased()

            return matchesQuery && matchesCr && matchesType
        }
    }

    var body: some View {
        NavigationStack {
            List {
                Section {
                    ScrollView(.horizontal, showsIndicators: false) {
                        HStack(spacing: 8) {
                            Menu {
                                ForEach(typeOptions, id: \.self) { opt in
                                    Button(opt) { selectedTypeFilter = opt }
                                }
                            } label: {
                                FilterChip(title: "Type: \(selectedTypeFilter)", isActive: selectedTypeFilter != "All")
                            }

                            Menu {
                                ForEach(crOptions, id: \.self) { opt in
                                    Button(opt) { selectedCrFilter = opt }
                                }
                            } label: {
                                FilterChip(title: "CR: \(selectedCrFilter)", isActive: selectedCrFilter != "All")
                            }

                            if selectedTypeFilter != "All" || selectedCrFilter != "All" {
                                Button("Reset Filters") {
                                    selectedTypeFilter = "All"
                                    selectedCrFilter = "All"
                                }
                                .font(.caption)
                                .foregroundColor(.red)
                            }
                        }
                        .padding(.vertical, 4)
                    }
                }
                .listRowBackground(Color.clear)
                .listRowInsets(EdgeInsets(top: 4, leading: 16, bottom: 4, trailing: 16))

                if searchResults.isEmpty {
                    ContentUnavailableView(
                        "No Monsters Found",
                        systemImage: "magnifyingglass",
                        description: Text("No monsters matched your query or filters.")
                    )
                } else {
                    Section("Results (\(searchResults.count))") {
                        ForEach(searchResults) { monster in
                            NavigationLink(destination: MonsterDetailWrapper(monster: monster)) {
                                MonsterListRow(monster: monster)
                            }
                        }
                    }
                }
            }
            .navigationTitle("Search")
            .searchable(text: $searchText, prompt: "Search monsters, types, sizes...")
        }
    }
}

struct FilterChip: View {
    let title: String
    let isActive: Bool

    var body: some View {
        Text(title)
            .font(.caption)
            .fontWeight(isActive ? .semibold : .regular)
            .padding(.horizontal, 10)
            .padding(.vertical, 6)
            .background(isActive ? Color.accentColor.opacity(0.2) : Color.secondary.opacity(0.12))
            .foregroundColor(isActive ? .accentColor : .primary)
            .cornerRadius(8)
    }
}

#Preview {
    Search()
        .environment(\.managedObjectContext, PersistenceController.preview.container.viewContext)
}
