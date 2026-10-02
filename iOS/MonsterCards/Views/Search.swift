//
//  Search.swift
//  MonsterCards
//
//  Created by Tom Hicks on 1/15/21.
//

import SwiftUI
import CoreData

enum SearchScope: String, CaseIterable, Identifiable {
    case local = "My Library"
    case compendiums = "Compendiums"
    case open5e = "Open5e Online"

    var id: String { rawValue }
}

struct Search: View {
    @State private var searchText = ""
    @State private var searchScope: SearchScope = .local
    @State private var selectedSystemFilter: GameSystem? = nil
    @State private var selectedTypeFilter = "All"
    @State private var selectedCrFilter = "All"

    // Remote Open5e Search State
    @State private var isSearchingRemote = false
    @State private var remoteMonsters: [MonsterViewModel] = []
    @State private var remoteErrorMessage: String? = nil
    
    // Preview & Clone State
    @State private var previewedMonsterVM: MonsterViewModel? = nil
    @State private var selectedTargetCollection: Collection? = nil
    @State private var clonedMonsterNames: Set<String> = []
    @State private var cloneConfirmationMessage: String? = nil

    @Environment(\.managedObjectContext) var viewContext
    
    @FetchRequest(
        sortDescriptors: [
            NSSortDescriptor(keyPath: \Monster.name, ascending: true),
        ],
        animation: .default)
    private var allMonsters: FetchedResults<Monster>

    @FetchRequest(
        sortDescriptors: [
            NSSortDescriptor(keyPath: \ReferenceMonster.name, ascending: true),
        ],
        animation: .default)
    private var allReferenceMonsters: FetchedResults<ReferenceMonster>

    @FetchRequest(
        sortDescriptors: [
            NSSortDescriptor(keyPath: \Collection.sortOrder, ascending: true),
            NSSortDescriptor(keyPath: \Collection.name, ascending: true)
        ],
        animation: .default)
    private var availableCollections: FetchedResults<Collection>

    private let quickTypeFilters = ["All", "Beast", "Undead", "Dragon", "Fiend", "Humanoid", "Monstrosity", "Fey", "Elemental"]

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

            let matchesSystem = selectedSystemFilter == nil || monster.gameSystemEnum == selectedSystemFilter
            let matchesCr = selectedCrFilter == "All" || (monster.challengeRating ?? "") == selectedCrFilter
            let matchesType = selectedTypeFilter == "All" || (monster.type ?? "").lowercased() == selectedTypeFilter.lowercased()

            return matchesQuery && matchesSystem && matchesCr && matchesType
        }
    }

    private var compendiumSearchResults: [ReferenceMonster] {
        allReferenceMonsters.filter { refMonster in
            let cleanQuery = searchText.trimmingCharacters(in: .whitespacesAndNewlines)

            let matchesQuery: Bool
            if cleanQuery.isEmpty {
                matchesQuery = true
            } else {
                let terms = cleanQuery.components(separatedBy: " ").filter { !$0.isEmpty }
                matchesQuery = terms.allSatisfy { term in
                    StringHelper.safeContainsCaseInsensitive(refMonster.name, term)
                        || StringHelper.safeContainsCaseInsensitive(refMonster.size, term)
                        || StringHelper.safeContainsCaseInsensitive(refMonster.type, term)
                        || StringHelper.safeContainsCaseInsensitive(refMonster.subtype, term)
                        || StringHelper.safeContainsCaseInsensitive(refMonster.sourceLabel, term)
                        || StringHelper.safeContainsCaseInsensitive(refMonster.challengeRating, term)
                }
            }

            let matchesSystem = selectedSystemFilter == nil || refMonster.gameSystemEnum == selectedSystemFilter
            let matchesType = selectedTypeFilter == "All" || (refMonster.type ?? "").lowercased() == selectedTypeFilter.lowercased()

            return matchesQuery && matchesSystem && matchesType
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

                // System & Category Filter Bar
                ScrollView(.horizontal, showsIndicators: false) {
                    HStack(spacing: 8) {
                        // System filter chip
                        Menu {
                            Button("All Game Systems") {
                                selectedSystemFilter = nil
                            }
                            ForEach(GameSystem.allCases) { sys in
                                Button(sys.displayName) {
                                    selectedSystemFilter = sys
                                }
                            }
                        } label: {
                            HStack(spacing: 4) {
                                Image(systemName: "slider.horizontal.3")
                                Text(selectedSystemFilter?.displayName ?? "All Systems")
                                Image(systemName: "chevron.down")
                                    .font(.caption2)
                            }
                            .font(.caption.weight(.medium))
                            .padding(.horizontal, 10)
                            .padding(.vertical, 6)
                            .background(selectedSystemFilter != nil ? Color.accentColor : Color(uiColor: .secondarySystemFill))
                            .foregroundColor(selectedSystemFilter != nil ? .white : .primary)
                            .cornerRadius(14)
                        }

                        Divider()
                            .frame(height: 20)

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
                switch searchScope {
                case .local:
                    localResultsView
                case .compendiums:
                    compendiumResultsView
                case .open5e:
                    remoteResultsView
                }
            }
            .navigationTitle("Search")
            .searchable(text: $searchText, prompt: Text(searchPromptText))
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
            .sheet(item: $previewedMonsterVM) { monsterVM in
                NavigationStack {
                    VStack(spacing: 0) {
                        if !availableCollections.isEmpty {
                            HStack {
                                Text("Destination:")
                                    .font(.subheadline)
                                    .foregroundColor(.secondary)
                                
                                Menu {
                                    Button("None (Root Library)") {
                                        selectedTargetCollection = nil
                                    }
                                    ForEach(availableCollections) { col in
                                        Button(col.name ?? "Unnamed") {
                                            selectedTargetCollection = col
                                        }
                                    }
                                } label: {
                                    HStack(spacing: 4) {
                                        Image(systemName: "folder")
                                        Text(selectedTargetCollection?.name ?? "None (Root Library)")
                                        Image(systemName: "chevron.down")
                                            .font(.caption2)
                                    }
                                    .font(.subheadline.weight(.medium))
                                    .padding(.horizontal, 10)
                                    .padding(.vertical, 5)
                                    .background(Color(uiColor: .secondarySystemFill))
                                    .cornerRadius(8)
                                }
                                
                                Spacer()
                            }
                            .padding(.horizontal)
                            .padding(.vertical, 8)
                            .background(Color(uiColor: .secondarySystemBackground))
                        }

                        MonsterDetailView(viewModel: monsterVM)
                    }
                    .navigationTitle(monsterVM.name)
                    .navigationBarTitleDisplayMode(.inline)
                    .toolbar {
                        ToolbarItem(placement: .cancellationAction) {
                            Button("Close") {
                                previewedMonsterVM = nil
                                selectedTargetCollection = nil
                            }
                        }

                        ToolbarItem(placement: .confirmationAction) {
                            Button {
                                cloneMonsterToLibrary(monsterVM, into: selectedTargetCollection)
                                previewedMonsterVM = nil
                                selectedTargetCollection = nil
                            } label: {
                                Label("Clone to Library", systemImage: "square.and.arrow.down")
                            }
                        }
                    }
                }
            }
            .alert("Imported to Library", isPresented: Binding(
                get: { cloneConfirmationMessage != nil },
                set: { if !$0 { cloneConfirmationMessage = nil } }
            )) {
                Button("OK") { cloneConfirmationMessage = nil }
            } message: {
                Text(cloneConfirmationMessage ?? "")
            }
        }
    }

    private var searchPromptText: String {
        switch searchScope {
        case .local:
            return "Search local library…"
        case .compendiums:
            return "Search offline compendiums (e.g. Owlbear, Goblin)…"
        case .open5e:
            return "Search Open5e REST API…"
        }
    }

    // MARK: - Local Results View

    private var localResultsView: some View {
        List {
            if localSearchResults.isEmpty {
                ContentUnavailableView(
                    "No Monsters Found",
                    systemImage: "magnifyingglass",
                    description: Text(searchText.isEmpty ? "Your library is empty. Try creating or cloning a monster." : "No local monsters matched '\(searchText)'. Try searching Compendiums.")
                )
            } else {
                Section("My Library (\(localSearchResults.count))") {
                    ForEach(localSearchResults) { monster in
                        NavigationLink(destination: MonsterDetailWrapper(monster: monster)) {
                            MonsterListRow(monster: monster)
                        }
                    }
                }
            }
        }
    }

    // MARK: - Compendium Results View

    private var compendiumResultsView: some View {
        List {
            if allReferenceMonsters.isEmpty {
                ContentUnavailableView(
                    "No Compendiums Downloaded",
                    systemImage: "books.vertical",
                    description: Text("Go to Compendium Sources in Settings to download OGL/ORC 3rd-party community reference packs.")
                )
            } else if compendiumSearchResults.isEmpty {
                ContentUnavailableView(
                    "No Reference Monsters Found",
                    systemImage: "magnifyingglass",
                    description: Text("No compendium creatures matched '\(searchText)'.")
                )
            } else {
                Section("Compendium Monsters (\(compendiumSearchResults.count))") {
                    ForEach(compendiumSearchResults) { refMonster in
                        HStack {
                            VStack(alignment: .leading, spacing: 3) {
                                HStack(spacing: 6) {
                                    Text(refMonster.name ?? "")
                                        .font(.headline)
                                        .foregroundColor(.primary)

                                    SourceTagView(
                                        gameSystem: refMonster.gameSystemEnum,
                                        sourceLabel: refMonster.sourceLabel ?? refMonster.gameSystemEnum.displayName
                                    )
                                }

                                HStack(spacing: 6) {
                                    if let size = refMonster.size, !size.isEmpty {
                                        Text(size.capitalized)
                                            .font(.caption)
                                            .foregroundColor(.secondary)
                                    }
                                    if let type = refMonster.type, !type.isEmpty {
                                        Text(type.capitalized)
                                            .font(.caption)
                                            .foregroundColor(.secondary)
                                    }
                                    if let cr = refMonster.challengeRating, !cr.isEmpty {
                                        Text("CR \(cr)")
                                            .font(.caption2)
                                            .fontWeight(.semibold)
                                            .padding(.horizontal, 4)
                                            .padding(.vertical, 1)
                                            .background(Theme.dndGold.opacity(0.2))
                                            .cornerRadius(3)
                                    }
                                }
                            }

                            Spacer()

                            let mName = refMonster.name ?? ""
                            if clonedMonsterNames.contains(mName) {
                                Label("Cloned", systemImage: "checkmark.circle.fill")
                                    .font(.caption)
                                    .foregroundColor(.green)
                            } else {
                                Button {
                                    cloneReferenceMonster(refMonster)
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
                            previewedMonsterVM = refMonster.toViewModel()
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
                                HStack(spacing: 6) {
                                    Text(monsterVM.name)
                                        .font(.headline)
                                        .foregroundColor(.primary)

                                    SourceTagView(gameSystem: monsterVM.gameSystem, sourceLabel: monsterVM.sourceLabel.isEmpty ? "Open5e" : monsterVM.sourceLabel)
                                }

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

                            if clonedMonsterNames.contains(monsterVM.name) {
                                Label("Cloned", systemImage: "checkmark.circle.fill")
                                    .font(.caption)
                                    .foregroundColor(.green)
                            } else {
                                Button {
                                    cloneMonsterToLibrary(monsterVM)
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
                            previewedMonsterVM = monsterVM
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

    private func cloneReferenceMonster(_ refMonster: ReferenceMonster, into collection: Collection? = nil) {
        let vm = refMonster.toViewModel()
        cloneMonsterToLibrary(vm, into: collection)
    }

    private func cloneMonsterToLibrary(_ monsterVM: MonsterViewModel, into collection: Collection? = nil) {
        withAnimation {
            let newMonster = Monster(context: viewContext)
            let newUuid = UUID()
            newMonster.uuid = newUuid
            monsterVM.copyToMonster(monster: newMonster)
            
            if let col = collection {
                let link = CollectionMonster(context: viewContext)
                link.collectionId = col.name ?? ""
                link.monsterId = newUuid.uuidString
            }

            do {
                try viewContext.save()
                clonedMonsterNames.insert(monsterVM.name)
                if let colName = collection?.name {
                    cloneConfirmationMessage = "Cloned '\(monsterVM.name)' into '\(colName)'!"
                } else {
                    cloneConfirmationMessage = "Cloned '\(monsterVM.name)' into your library!"
                }
            } catch {
                print("Failed to clone monster to library: \(error)")
            }
        }
    }
}

#Preview {
    Search()
        .environment(\.managedObjectContext, PersistenceController.preview.container.viewContext)
}
