//
//  Search.swift
//  MonsterCards
//
//  Created by Tom Hicks on 1/15/21.
//

import SwiftUI
import CoreData

enum SearchScope: String, CaseIterable, Identifiable {
    case local = "Local Library"
    case open5e = "Open5e Online"

    var id: String { rawValue }
}

struct Search: View {
    @State private var searchText = ""
    @State private var searchScope: SearchScope = .local
    @State private var selectedTypeFilter = "All"
    @State private var selectedCrFilter = "All"

    // Remote Open5e Search State
    @State private var isSearchingRemote = false
    @State private var remoteMonsters: [MonsterViewModel] = []
    @State private var remoteErrorMessage: String? = nil
    @State private var previewedRemoteMonster: MonsterViewModel? = nil
    @State private var importedMonsterNames: Set<String> = []

    @Environment(\.managedObjectContext) var viewContext
    @FetchRequest(
        sortDescriptors: [
            NSSortDescriptor(keyPath: \Monster.name, ascending: true),
        ],
        animation: .default)
    private var allMonsters: FetchedResults<Monster>

    private let quickTypeFilters = ["All", "Beast", "Undead", "Dragon", "Fiend", "Humanoid", "Monstrosity", "Fey", "Elemental"]
    private let crOptions = ["All", "0", "1/8", "1/4", "1/2", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "15", "20"]

    private var localSearchResults: [Monster] {
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
            VStack(spacing: 0) {
                // Scope picker
                Picker("Search Scope", selection: $searchScope) {
                    ForEach(SearchScope.allCases) { scope in
                        Text(scope.rawValue).tag(scope)
                    }
                }
                .pickerStyle(.segmented)
                .padding(.horizontal)
                .padding(.top, 8)
                .padding(.bottom, 4)

                // Quick Category Filters
                ScrollView(.horizontal, showsIndicators: false) {
                    HStack(spacing: 8) {
                        ForEach(quickTypeFilters, id: \.self) { type in
                            FilterChip(title: type, isActive: selectedTypeFilter == type)
                                .onTapGesture {
                                    selectedTypeFilter = type
                                    if searchScope == .open5e && !searchText.isEmpty {
                                        performRemoteSearch()
                                    }
                                }
                        }
                    }
                    .padding(.horizontal)
                    .padding(.vertical, 6)
                }

                // Results view
                if searchScope == .local {
                    localResultsView
                } else {
                    remoteResultsView
                }
            }
            .navigationTitle("Search")
            .searchable(text: $searchText, prompt: Text(searchScope == .local ? "Search local library..." : "Search Open5e API (e.g. Dragon, Goblin)..."))
            .onChange(of: searchText) {
                if searchScope == .open5e {
                    debounceRemoteSearch(query: searchText)
                }
            }
            .onChange(of: searchScope) {
                if searchScope == .open5e {
                    if !searchText.isEmpty && remoteMonsters.isEmpty {
                        performRemoteSearch()
                    }
                }
            }
            .sheet(item: $previewedRemoteMonster) { monsterVM in
                NavigationStack {
                    VStack {
                        MonsterDetailView(viewModel: monsterVM)
                    }
                    .navigationTitle(monsterVM.name)
                    .navigationBarTitleDisplayMode(.inline)
                    .toolbar {
                        ToolbarItem(placement: .cancellationAction) {
                            Button("Close") {
                                previewedRemoteMonster = nil
                            }
                        }

                        ToolbarItem(placement: .confirmationAction) {
                            Button {
                                importRemoteMonster(monsterVM)
                                previewedRemoteMonster = nil
                            } label: {
                                Label("Import to Library", systemImage: "square.and.arrow.down")
                            }
                        }
                    }
                }
            }
        }
    }

    // MARK: - Local Results View

    private var localResultsView: some View {
        List {
            if localSearchResults.isEmpty {
                ContentUnavailableView(
                    "No Monsters Found",
                    systemImage: "magnifyingglass",
                    description: Text("No local monsters matched '\(searchText)'. Try searching Open5e Online.")
                )
            } else {
                Section("Local Library Results (\(localSearchResults.count))") {
                    ForEach(localSearchResults) { monster in
                        NavigationLink(destination: MonsterDetailWrapper(monster: monster)) {
                            MonsterListRow(monster: monster)
                        }
                    }
                }
            }
        }
    }

    // MARK: - Remote Results View

    private var remoteResultsView: some View {
        List {
            if isSearchingRemote {
                HStack {
                    Spacer()
                    ProgressView("Searching Open5e API...")
                        .padding()
                    Spacer()
                }
                .listRowBackground(Color.clear)
            } else if let err = remoteErrorMessage {
                ContentUnavailableView(
                    "Search Failed",
                    systemImage: "wifi.exclamationmark",
                    description: Text(err)
                )
                Section {
                    Button("Retry Search") {
                        performRemoteSearch()
                    }
                    .frame(maxWidth: .infinity)
                }
            } else if remoteMonsters.isEmpty {
                ContentUnavailableView(
                    searchText.isEmpty ? "Search Open5e Online" : "No Online Results",
                    systemImage: "globe",
                    description: Text(searchText.isEmpty ? "Type a monster name or creature type to query thousands of 5e SRD monsters live." : "No creatures matched '\(searchText)' on Open5e.")
                )
            } else {
                Section("Open5e Results (\(remoteMonsters.count))") {
                    ForEach(remoteMonsters, id: \.name) { monsterVM in
                        HStack {
                            VStack(alignment: .leading, spacing: 3) {
                                Text(monsterVM.name)
                                    .font(.headline)
                                    .foregroundColor(.primary)

                                HStack(spacing: 6) {
                                    if !monsterVM.size.isEmpty {
                                        Text(monsterVM.size.capitalized)
                                            .font(.caption)
                                            .foregroundColor(.secondary)
                                    }
                                    if !monsterVM.type.isEmpty {
                                        Text(monsterVM.type.capitalized)
                                            .font(.caption)
                                            .foregroundColor(.secondary)
                                    }
                                    Text("CR \(monsterVM.challengeRatingDescription)")
                                        .font(.caption2)
                                        .fontWeight(.semibold)
                                        .padding(.horizontal, 4)
                                        .padding(.vertical, 1)
                                        .background(Theme.dndGold.opacity(0.2))
                                        .cornerRadius(3)
                                }
                            }

                            Spacer()

                            if importedMonsterNames.contains(monsterVM.name) {
                                Label("Imported", systemImage: "checkmark.circle.fill")
                                    .font(.caption)
                                    .foregroundColor(.green)
                            } else {
                                Button {
                                    importRemoteMonster(monsterVM)
                                } label: {
                                    Image(systemName: "square.and.arrow.down")
                                        .font(.title3)
                                        .foregroundColor(.accentColor)
                                }
                                .buttonStyle(.borderless)
                            }
                        }
                        .contentShape(Rectangle())
                        .onTapGesture {
                            previewedRemoteMonster = monsterVM
                        }
                    }
                }
            }
        }
    }

    // MARK: - Search Actions

    @State private var searchTask: Task<Void, Never>? = nil

    private func debounceRemoteSearch(query: String) {
        searchTask?.cancel()
        guard !query.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty else {
            remoteMonsters = []
            remoteErrorMessage = nil
            return
        }

        searchTask = Task {
            try? await Task.sleep(nanoseconds: 400_000_000) // 400ms debounce
            if !Task.isCancelled {
                await MainActor.run {
                    performRemoteSearch()
                }
            }
        }
    }

    private func performRemoteSearch() {
        let q = searchText.trimmingCharacters(in: .whitespacesAndNewlines)
        guard !q.isEmpty else { return }

        isSearchingRemote = true
        remoteErrorMessage = nil

        let encodedQ = q.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed) ?? q
        var urlStr = "https://api.open5e.com/v2/creatures/?search=\(encodedQ)"
        if selectedTypeFilter != "All" {
            let encodedType = selectedTypeFilter.lowercased().addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed) ?? ""
            urlStr += "&type=\(encodedType)"
        }

        Task {
            do {
                let pageResult = try await Open5eApiWrapper.fetchPage(urlStr: urlStr)
                var parsed: [MonsterViewModel] = []
                for raw in pageResult.rawResults {
                    if let vm = try? Open5eImporter.parse(raw) {
                        parsed.append(vm)
                    }
                }

                await MainActor.run {
                    self.remoteMonsters = parsed
                    self.isSearchingRemote = false
                }
            } catch {
                await MainActor.run {
                    self.remoteErrorMessage = error.localizedDescription
                    self.isSearchingRemote = false
                }
            }
        }
    }

    private func importRemoteMonster(_ monsterVM: MonsterViewModel) {
        withAnimation {
            let newMonster = Monster(context: viewContext)
            newMonster.uuid = UUID()
            monsterVM.copyToMonster(monster: newMonster)
            do {
                try viewContext.save()
                importedMonsterNames.insert(monsterVM.name)
            } catch {
                print("Failed to save imported monster: \(error)")
            }
        }
    }
}

#Preview {
    Search()
        .environment(\.managedObjectContext, PersistenceController.preview.container.viewContext)
}
