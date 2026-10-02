//
//  Library.swift
//  MonsterCards
//
//  Created by Tom Hicks on 1/15/21.
//

import SwiftUI
import CoreData

enum MonsterSortOption: String, CaseIterable, Identifiable {
    case nameAscending = "Name (A-Z)"
    case nameDescending = "Name (Z-A)"
    case crAscending = "CR (Lowest First)"
    case crDescending = "CR (Highest First)"

    var id: String { rawValue }
}

struct Library: View {
    @Environment(\.managedObjectContext) private var viewContext
    @State private var searchText = ""
    @State private var selectedTypeFilter = "All"
    @State private var selectedSizeFilter = "All"
    @State private var selectedCrFilter = "All"
    @State private var sortOption: MonsterSortOption = .nameAscending
    @State private var isFilterSheetPresented = false
    @State private var selectedMonsterIds = Set<NSManagedObjectID>()
    @State private var isAddToCollectionPresented = false
    @State private var editMode: EditMode = .inactive

    @FetchRequest(
        sortDescriptors: [
            NSSortDescriptor(keyPath: \Monster.name, ascending: true),
        ],
        animation: .default)
    private var allMonsters: FetchedResults<Monster>

    @FetchRequest(
        sortDescriptors: [NSSortDescriptor(keyPath: \Collection.name, ascending: true)],
        animation: .default
    )
    private var allCollections: FetchedResults<Collection>

    private let typeOptions = ["All", "Aberration", "Beast", "Celestial", "Construct", "Dragon", "Elemental", "Fey", "Fiend", "Giant", "Humanoid", "Monstrosity", "Ooze", "Plant", "Undead"]
    private let sizeOptions = ["All", "Tiny", "Small", "Medium", "Large", "Huge", "Gargantuan"]
    private let crOptions = ["All", "0", "1/8", "1/4", "1/2", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "15", "20"]

    private var filteredAndSortedMonsters: [Monster] {
        let filtered = allMonsters.filter { monster in
            let matchesSearch: Bool
            let q = searchText.trimmingCharacters(in: .whitespacesAndNewlines)
            if q.isEmpty {
                matchesSearch = true
            } else {
                matchesSearch = StringHelper.safeContainsCaseInsensitive(monster.name, q)
                    || StringHelper.safeContainsCaseInsensitive(monster.type, q)
                    || StringHelper.safeContainsCaseInsensitive(monster.subtype, q)
                    || StringHelper.safeContainsCaseInsensitive(monster.alignment, q)
                    || StringHelper.safeContainsCaseInsensitive(monster.challengeRating, q)
            }

            let matchesType = selectedTypeFilter == "All" || (monster.type ?? "").lowercased() == selectedTypeFilter.lowercased()
            let matchesSize = selectedSizeFilter == "All" || (monster.size ?? "").lowercased() == selectedSizeFilter.lowercased()
            let matchesCr = selectedCrFilter == "All" || (monster.challengeRating ?? "") == selectedCrFilter

            return matchesSearch && matchesType && matchesSize && matchesCr
        }

        return filtered.sorted { m1, m2 in
            switch sortOption {
            case .nameAscending:
                return (m1.name ?? "").localizedCaseInsensitiveCompare(m2.name ?? "") == .orderedAscending
            case .nameDescending:
                return (m1.name ?? "").localizedCaseInsensitiveCompare(m2.name ?? "") == .orderedDescending
            case .crAscending:
                return crNumericValue(m1.challengeRating) < crNumericValue(m2.challengeRating)
            case .crDescending:
                return crNumericValue(m1.challengeRating) > crNumericValue(m2.challengeRating)
            }
        }
    }

    var body: some View {
        NavigationStack {
            List(selection: $selectedMonsterIds) {
                if filteredAndSortedMonsters.isEmpty {
                    ContentUnavailableView(
                        searchText.isEmpty ? "Library Empty" : "No Matching Monsters",
                        systemImage: searchText.isEmpty ? "book.closed" : "magnifyingglass",
                        description: Text(searchText.isEmpty ? "Add monsters using '+' or seed demo content below." : "No monsters match your active search and filter criteria.")
                    )
                    if allMonsters.isEmpty {
                        Section {
                            Button(action: seedPreviewData) {
                                Label("Seed Demo Monsters & Collections", systemImage: "sparkles")
                                    .frame(maxWidth: .infinity)
                                    .padding(.vertical, 8)
                            }
                            .buttonStyle(.borderedProminent)
                        }
                        .listRowBackground(Color.clear)
                    }
                } else {
                    ForEach(filteredAndSortedMonsters) { monster in
                        NavigationLink(destination: MonsterDetailWrapper(monster: monster)) {
                            MonsterListRow(monster: monster)
                        }
                        .swipeActions(edge: .trailing, allowsFullSwipe: true) {
                            Button(role: .destructive) {
                                deleteMonster(monster)
                            } label: {
                                Label("Delete", systemImage: "trash")
                            }
                            Button {
                                duplicateMonster(monster)
                            } label: {
                                Label("Duplicate", systemImage: "doc.on.doc")
                            }
                            .tint(.blue)
                        }
                        .swipeActions(edge: .leading) {
                            Button {
                                addToDashboard(monster)
                            } label: {
                                Label("Dashboard", systemImage: "rectangle.3.offgrid.fill")
                            }
                            .tint(.orange)
                        }
                        .contextMenu {
                            Button {
                                addToDashboard(monster)
                            } label: {
                                Label("Add to Dashboard", systemImage: "rectangle.3.offgrid.fill")
                            }
                            Button {
                                duplicateMonster(monster)
                            } label: {
                                Label("Duplicate", systemImage: "doc.on.doc")
                            }
                            Button {
                                exportMonsterText(monster)
                            } label: {
                                Label("Share / Export", systemImage: "square.and.arrow.up")
                            }
                            Divider()
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
            .searchable(text: $searchText, prompt: "Search monsters, types, CR...")
            .toolbar {
                ToolbarItem(placement: .topBarLeading) {
                    if !allMonsters.isEmpty {
                        EditButton()
                    }
                }

                ToolbarItemGroup(placement: .topBarTrailing) {
                    Menu {
                        Section("Sort By") {
                            ForEach(MonsterSortOption.allCases) { opt in
                                Button {
                                    sortOption = opt
                                } label: {
                                    HStack {
                                        Text(opt.rawValue)
                                        if sortOption == opt {
                                            Image(systemName: "checkmark")
                                        }
                                    }
                                }
                            }
                        }

                        Section("Filter") {
                            Button {
                                isFilterSheetPresented = true
                            } label: {
                                Label("Filter Options...", systemImage: "line.3.horizontal.decrease.circle")
                            }
                        }
                    } label: {
                        Image(systemName: hasActiveFilters ? "line.3.horizontal.decrease.circle.fill" : "arrow.up.arrow.down.circle")
                    }

                    Button(action: addMonster) {
                        Image(systemName: "plus")
                    }
                }

                if editMode.isEditing && !selectedMonsterIds.isEmpty {
                    ToolbarItemGroup(placement: .bottomBar) {
                        Button(role: .destructive, action: deleteSelectedMonsters) {
                            Label("Delete (\(selectedMonsterIds.count))", systemImage: "trash")
                        }
                        Spacer()
                        Button(action: { isAddToCollectionPresented = true }) {
                            Label("Add to Collection", systemImage: "folder.badge.plus")
                        }
                    }
                }
            }
            .environment(\.editMode, $editMode)
            .sheet(isPresented: $isFilterSheetPresented) {
                FilterSheetView(
                    selectedType: $selectedTypeFilter,
                    selectedSize: $selectedSizeFilter,
                    selectedCr: $selectedCrFilter,
                    typeOptions: typeOptions,
                    sizeOptions: sizeOptions,
                    crOptions: crOptions
                )
            }
            .sheet(isPresented: $isAddToCollectionPresented) {
                AddToCollectionSheet(
                    selectedMonsterIds: selectedMonsterIds,
                    collections: Array(allCollections),
                    isPresented: $isAddToCollectionPresented
                )
            }
        }
    }

    private var hasActiveFilters: Bool {
        selectedTypeFilter != "All" || selectedSizeFilter != "All" || selectedCrFilter != "All"
    }

    private func crNumericValue(_ cr: String?) -> Double {
        guard let cr = cr, !cr.isEmpty else { return 0 }
        if cr == "1/8" { return 0.125 }
        if cr == "1/4" { return 0.25 }
        if cr == "1/2" { return 0.5 }
        return Double(cr) ?? 0
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
            newItem.challengeRating = "1"

            do {
                try viewContext.save()
            } catch {
                let nsError = error as NSError
                print("Failed to add monster: \(nsError), \(nsError.userInfo)")
            }
        }
    }

    private func duplicateMonster(_ monster: Monster) {
        withAnimation {
            let copy = Monster(context: viewContext)
            copy.uuid = UUID()
            copy.name = "\(monster.name ?? "Monster") (Copy)"
            copy.size = monster.size
            copy.type = monster.type
            copy.subtype = monster.subtype
            copy.alignment = monster.alignment
            copy.hitDice = monster.hitDice
            copy.walkSpeed = monster.walkSpeed
            copy.burrowSpeed = monster.burrowSpeed
            copy.climbSpeed = monster.climbSpeed
            copy.flySpeed = monster.flySpeed
            copy.swimSpeed = monster.swimSpeed
            copy.strengthScore = monster.strengthScore
            copy.dexterityScore = monster.dexterityScore
            copy.constitutionScore = monster.constitutionScore
            copy.intelligenceScore = monster.intelligenceScore
            copy.wisdomScore = monster.wisdomScore
            copy.charismaScore = monster.charismaScore
            copy.challengeRating = monster.challengeRating
            copy.abilities = monster.abilities
            copy.actions = monster.actions
            copy.reactions = monster.reactions
            copy.legendaryActions = monster.legendaryActions

            do {
                try viewContext.save()
            } catch {
                print("Failed to duplicate monster: \(error)")
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
            let monster = filteredAndSortedMonsters[index]
            deleteMonster(monster)
        }
    }

    private func deleteSelectedMonsters() {
        for objId in selectedMonsterIds {
            if let obj = try? viewContext.existingObject(with: objId) {
                viewContext.delete(obj)
            }
        }
        selectedMonsterIds.removeAll()
        do {
            try viewContext.save()
        } catch {
            print("Failed to delete selected monsters: \(error)")
        }
    }

    private func addToDashboard(_ monster: Monster) {
        guard let uuid = monster.uuid else { return }
        do {
            try MonsterRepository.shared.addMonsterToDashboard(id: uuid)
        } catch {
            print("Failed to add monster to dashboard: \(error)")
        }
    }

    private func exportMonsterText(_ monster: Monster) {
        let vm = MonsterViewModel(monster)
        let md = MonsterCardExporter.exportMarkdown(vm)
        let av = UIActivityViewController(activityItems: [md], applicationActivities: nil)
        if let windowScene = UIApplication.shared.connectedScenes.first as? UIWindowScene,
           let rootVC = windowScene.windows.first?.rootViewController {
            rootVC.present(av, animated: true)
        }
    }

    private func seedPreviewData() {
        DevContent.seedPreviewData(into: viewContext)
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

struct FilterSheetView: View {
    @Binding var selectedType: String
    @Binding var selectedSize: String
    @Binding var selectedCr: String
    let typeOptions: [String]
    let sizeOptions: [String]
    let crOptions: [String]
    @Environment(\.dismiss) private var dismiss

    var body: some View {
        NavigationStack {
            Form {
                Section("Monster Type") {
                    Picker("Type", selection: $selectedType) {
                        ForEach(typeOptions, id: \.self) { opt in
                            Text(opt).tag(opt)
                        }
                    }
                    .pickerStyle(.menu)
                }

                Section("Size") {
                    Picker("Size", selection: $selectedSize) {
                        ForEach(sizeOptions, id: \.self) { opt in
                            Text(opt).tag(opt)
                        }
                    }
                    .pickerStyle(.menu)
                }

                Section("Challenge Rating") {
                    Picker("CR", selection: $selectedCr) {
                        ForEach(crOptions, id: \.self) { opt in
                            Text(opt).tag(opt)
                        }
                    }
                    .pickerStyle(.menu)
                }

                Section {
                    Button("Reset All Filters") {
                        selectedType = "All"
                        selectedSize = "All"
                        selectedCr = "All"
                    }
                    .foregroundColor(.red)
                }
            }
            .navigationTitle("Filter Monsters")
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .confirmationAction) {
                    Button("Done") {
                        dismiss()
                    }
                }
            }
        }
    }
}

struct AddToCollectionSheet: View {
    let selectedMonsterIds: Set<NSManagedObjectID>
    let collections: [Collection]
    @Binding var isPresented: Bool
    @Environment(\.managedObjectContext) private var viewContext

    var body: some View {
        NavigationStack {
            List {
                if collections.isEmpty {
                    Text("No collections available. Create one in the Collections tab first.")
                        .foregroundColor(.secondary)
                } else {
                    ForEach(collections) { col in
                        Button {
                            addSelectedToCollection(col)
                        } label: {
                            VStack(alignment: .leading) {
                                Text(col.name ?? "Unnamed Collection")
                                    .font(.headline)
                                    .foregroundColor(.primary)
                                if let details = col.details, !details.isEmpty {
                                    Text(details)
                                        .font(.caption)
                                        .foregroundColor(.secondary)
                                }
                            }
                        }
                    }
                }
            }
            .navigationTitle("Add to Collection")
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .cancellationAction) {
                    Button("Cancel") {
                        isPresented = false
                    }
                }
            }
        }
    }

    private func addSelectedToCollection(_ col: Collection) {
        for objId in selectedMonsterIds {
            if let monster = try? viewContext.existingObject(with: objId) as? Monster,
               let uuid = monster.uuid {
                let link = CollectionMonster(context: viewContext)
                link.collection = col
                link.collectionId = col.name ?? ""
                link.monsterId = uuid.uuidString
            }
        }
        try? viewContext.save()
        isPresented = false
    }
}

#Preview {
    Library()
        .environment(\.managedObjectContext, PersistenceController.preview.container.viewContext)
}
