//
//  CollectionDetailView.swift
//  MonsterCards
//
//  Created by Antigravity on 10/1/26.
//

import SwiftUI
import CoreData

struct CollectionDetailView: View {
    @ObservedObject var collection: Collection
    @Environment(\.managedObjectContext) private var viewContext
    @State private var isAddMonstersSheetPresented = false
    @State private var isEditDetailsPresented = false
    @State private var searchText = ""

    @FetchRequest(
        sortDescriptors: [NSSortDescriptor(keyPath: \Monster.name, ascending: true)],
        animation: .default
    )
    private var allMonsters: FetchedResults<Monster>

    private var assignedMonsters: [Monster] {
        guard let links = collection.monsters as? Set<CollectionMonster> else { return [] }
        let sortedLinks = links.sorted { $0.ordinal < $1.ordinal }
        var result: [Monster] = []
        for link in sortedLinks {
            if let mId = link.monsterId, let uuid = UUID(uuidString: mId),
               let found = allMonsters.first(where: { $0.uuid == uuid }) {
                result.append(found)
            }
        }
        if searchText.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty {
            return result
        }
        return result.filter { StringHelper.safeContainsCaseInsensitive($0.name, searchText) }
    }

    // Encounter metrics
    private var totalBaseXP: Int {
        assignedMonsters.reduce(0) { $0 + xpForCr($1.challengeRating) }
    }

    private var encounterMultiplier: Double {
        let count = assignedMonsters.count
        if count == 0 { return 1.0 }
        if count == 1 { return 1.0 }
        if count == 2 { return 1.5 }
        if count <= 6 { return 2.0 }
        if count <= 10 { return 2.5 }
        if count <= 14 { return 3.0 }
        return 4.0
    }

    private var adjustedXP: Int {
        Int(Double(totalBaseXP) * encounterMultiplier)
    }

    private var averageCr: String {
        guard !assignedMonsters.isEmpty else { return "0" }
        let sum = assignedMonsters.reduce(0.0) { $0 + crNumeric($1.challengeRating) }
        let avg = sum / Double(assignedMonsters.count)
        return String(format: "%.1f", avg)
    }

    var body: some View {
        List {
            // Encounter Metrics Summary Banner
            Section {
                VStack(alignment: .leading, spacing: 10) {
                    if let details = collection.details, !details.isEmpty {
                        Text(details)
                            .font(.subheadline)
                            .foregroundColor(.secondary)
                    }

                    HStack(spacing: 12) {
                        MetricCard(title: "Monsters", value: "\(assignedMonsters.count)", icon: "person.3.fill", color: .blue)
                        MetricCard(title: "Avg CR", value: averageCr, icon: "flame.fill", color: Theme.dndGold)
                        MetricCard(title: "Total XP", value: "\(totalBaseXP)", icon: "star.fill", color: .purple)
                        MetricCard(title: "Adj XP", value: "\(adjustedXP)", icon: "bolt.fill", color: .orange)
                    }

                    HStack(spacing: 12) {
                        Button {
                            pushAllToDashboard()
                        } label: {
                            Label("Push All to Dashboard", systemImage: "rectangle.3.offgrid.fill")
                                .font(.caption)
                                .fontWeight(.semibold)
                                .frame(maxWidth: .infinity)
                                .padding(.vertical, 6)
                        }
                        .buttonStyle(.borderedProminent)
                        .tint(.orange)
                        .disabled(assignedMonsters.isEmpty)

                        Button {
                            exportCollection()
                        } label: {
                            Label("Export", systemImage: "square.and.arrow.up")
                                .font(.caption)
                                .fontWeight(.semibold)
                                .frame(maxWidth: .infinity)
                                .padding(.vertical, 6)
                        }
                        .buttonStyle(.bordered)
                        .disabled(assignedMonsters.isEmpty)
                    }
                }
                .padding(.vertical, 4)
            }
            .listRowBackground(Color.clear)
            .listRowInsets(EdgeInsets(top: 4, leading: 16, bottom: 8, trailing: 16))

            // Monster list
            Section("Monsters in Collection (\(assignedMonsters.count))") {
                if assignedMonsters.isEmpty {
                    ContentUnavailableView(
                        "No Monsters Assigned",
                        systemImage: "folder.badge.plus",
                        description: Text("Tap '+' to add monsters from your library.")
                    )
                } else {
                    ForEach(assignedMonsters) { monster in
                        NavigationLink(destination: MonsterDetailWrapper(monster: monster)) {
                            MonsterListRow(monster: monster)
                        }
                        .swipeActions(edge: .trailing, allowsFullSwipe: true) {
                            Button(role: .destructive) {
                                removeMonster(monster)
                            } label: {
                                Label("Remove", systemImage: "minus.circle")
                            }
                        }
                        .swipeActions(edge: .leading) {
                            Button {
                                addMonsterToDashboard(monster)
                            } label: {
                                Label("Dashboard", systemImage: "rectangle.3.offgrid.fill")
                            }
                            .tint(.orange)
                        }
                    }
                }
            }
        }
        .navigationTitle(collection.name ?? "Collection")
        .searchable(text: $searchText, prompt: "Search monsters in collection...")
        .toolbar {
            ToolbarItemGroup(placement: .topBarTrailing) {
                Button {
                    isEditDetailsPresented = true
                } label: {
                    Image(systemName: "pencil")
                }

                Button {
                    isAddMonstersSheetPresented = true
                } label: {
                    Image(systemName: "plus")
                }
            }
        }
        .sheet(isPresented: $isAddMonstersSheetPresented) {
            AddMonstersToCollectionSheet(
                collection: collection,
                allMonsters: Array(allMonsters),
                assignedMonsterIds: Set(assignedMonsters.compactMap { $0.uuid }),
                isPresented: $isAddMonstersSheetPresented
            )
        }
        .sheet(isPresented: $isEditDetailsPresented) {
            EditCollectionSheet(collection: collection, isPresented: $isEditDetailsPresented)
        }
    }

    private func removeMonster(_ monster: Monster) {
        guard let uuid = monster.uuid,
              let links = collection.monsters as? Set<CollectionMonster> else { return }
        for link in links where link.monsterId == uuid.uuidString {
            viewContext.delete(link)
        }
        try? viewContext.save()
    }

    private func addMonsterToDashboard(_ monster: Monster) {
        guard let uuid = monster.uuid else { return }
        try? MonsterRepository.shared.addMonsterToDashboard(id: uuid)
    }

    private func pushAllToDashboard() {
        for m in assignedMonsters {
            addMonsterToDashboard(m)
        }
    }

    private func exportCollection() {
        var cardArray: [String] = []
        for m in assignedMonsters {
            let vm = MonsterViewModel(m)
            if let json = try? MonsterCardExporter.exportCardJSON(vm) {
                cardArray.append(json)
            }
        }
        let joined = "[\n" + cardArray.joined(separator: ",\n") + "\n]"
        let av = UIActivityViewController(activityItems: [joined], applicationActivities: nil)
        if let windowScene = UIApplication.shared.connectedScenes.first as? UIWindowScene,
           let rootVC = windowScene.windows.first?.rootViewController {
            rootVC.present(av, animated: true)
        }
    }

    private func xpForCr(_ cr: String?) -> Int {
        switch cr {
        case "0": return 10
        case "1/8": return 25
        case "1/4": return 50
        case "1/2": return 100
        case "1": return 200
        case "2": return 450
        case "3": return 700
        case "4": return 1100
        case "5": return 1800
        case "6": return 2300
        case "7": return 2900
        case "8": return 3900
        case "9": return 5000
        case "10": return 5900
        case "11": return 7200
        case "12": return 8400
        case "13": return 10000
        case "14": return 11500
        case "15": return 13000
        case "16": return 15000
        case "17": return 18000
        case "18": return 20000
        case "19": return 22000
        case "20": return 25000
        case "21": return 33000
        case "22": return 41000
        case "23": return 50000
        case "24": return 62000
        case "25": return 75000
        case "26": return 90000
        case "27": return 105000
        case "28": return 120000
        case "29": return 135000
        case "30": return 155000
        default: return 0
        }
    }

    private func crNumeric(_ cr: String?) -> Double {
        guard let cr = cr else { return 0 }
        if cr == "1/8" { return 0.125 }
        if cr == "1/4" { return 0.25 }
        if cr == "1/2" { return 0.5 }
        return Double(cr) ?? 0
    }
}

struct MetricCard: View {
    let title: String
    let value: String
    let icon: String
    let color: Color

    var body: some View {
        VStack(spacing: 3) {
            Image(systemName: icon)
                .font(.caption2)
                .foregroundColor(color)
            Text(value)
                .font(.headline)
                .fontWeight(.bold)
            Text(title)
                .font(.system(size: 10))
                .foregroundColor(.secondary)
        }
        .frame(maxWidth: .infinity)
        .padding(.vertical, 8)
        .background(Color.secondary.opacity(0.1))
        .cornerRadius(8)
    }
}

struct AddMonstersToCollectionSheet: View {
    let collection: Collection
    let allMonsters: [Monster]
    let assignedMonsterIds: Set<UUID>
    @Binding var isPresented: Bool
    @Environment(\.managedObjectContext) private var viewContext
    @State private var searchText = ""

    private var availableMonsters: [Monster] {
        allMonsters.filter { m in
            guard let uuid = m.uuid, !assignedMonsterIds.contains(uuid) else { return false }
            if searchText.isEmpty { return true }
            return StringHelper.safeContainsCaseInsensitive(m.name, searchText)
        }
    }

    var body: some View {
        NavigationStack {
            List {
                if availableMonsters.isEmpty {
                    ContentUnavailableView(
                        "No Available Monsters",
                        systemImage: "checkmark.circle",
                        description: Text("All monsters in your library have already been added.")
                    )
                } else {
                    ForEach(availableMonsters) { monster in
                        Button {
                            addMonster(monster)
                        } label: {
                            HStack {
                                MonsterListRow(monster: monster)
                                Spacer()
                                Image(systemName: "plus.circle")
                                    .foregroundColor(.accentColor)
                            }
                        }
                    }
                }
            }
            .navigationTitle("Add to \(collection.name ?? "Collection")")
            .searchable(text: $searchText, prompt: "Search monsters...")
            .toolbar {
                ToolbarItem(placement: .cancellationAction) {
                    Button("Done") {
                        isPresented = false
                    }
                }
            }
        }
    }

    private func addMonster(_ monster: Monster) {
        guard let uuid = monster.uuid else { return }
        let link = CollectionMonster(context: viewContext)
        link.collection = collection
        link.collectionId = collection.name ?? ""
        link.monsterId = uuid.uuidString
        let count = (collection.monsters as? Set<CollectionMonster>)?.count ?? 0
        link.ordinal = Int64(count)
        try? viewContext.save()
    }
}

struct EditCollectionSheet: View {
    @ObservedObject var collection: Collection
    @Binding var isPresented: Bool
    @Environment(\.managedObjectContext) private var viewContext
    @State private var name: String = ""
    @State private var details: String = ""

    var body: some View {
        NavigationStack {
            Form {
                Section("Collection Information") {
                    TextField("Collection Name", text: $name)
                    TextField("Description / Notes", text: $details, axis: .vertical)
                        .lineLimit(3...6)
                }
            }
            .navigationTitle("Edit Collection")
            .navigationBarTitleDisplayMode(.inline)
            .onAppear {
                name = collection.name ?? ""
                details = collection.details ?? ""
            }
            .toolbar {
                ToolbarItem(placement: .cancellationAction) {
                    Button("Cancel") { isPresented = false }
                }
                ToolbarItem(placement: .confirmationAction) {
                    Button("Save") {
                        collection.name = name.trimmingCharacters(in: .whitespacesAndNewlines)
                        collection.details = details.trimmingCharacters(in: .whitespacesAndNewlines)
                        try? viewContext.save()
                        isPresented = false
                    }
                    .disabled(name.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty)
                }
            }
        }
    }
}
