//
//  Dashboard.swift
//  MonsterCards
//
//  Created by Tom Hicks on 1/15/21.
//

import SwiftUI
import CoreData

struct Dashboard: View {
    @Environment(\.managedObjectContext) private var viewContext
    @State private var isAddSheetPresented = false
    @State private var inspectedMonster: Monster? = nil
    @State private var isClearConfirmationPresented = false

    @FetchRequest(
        sortDescriptors: [NSSortDescriptor(keyPath: \DashboardMonster.ordinal, ascending: true)],
        animation: .default
    )
    private var dashboardMonsters: FetchedResults<DashboardMonster>

    @FetchRequest(
        sortDescriptors: [NSSortDescriptor(keyPath: \Monster.name, ascending: true)],
        animation: .default
    )
    private var allMonsters: FetchedResults<Monster>

    @FetchRequest(
        sortDescriptors: [NSSortDescriptor(keyPath: \Collection.name, ascending: true)],
        animation: .default
    )
    private var allCollections: FetchedResults<Collection>

    var body: some View {
        NavigationStack {
            ScrollView {
                if dashboardMonsters.isEmpty {
                    ContentUnavailableView(
                        "Combat Dashboard Empty",
                        systemImage: "rectangle.3.offgrid",
                        description: Text("Pin monsters from your Library or tap '+' above to track combat encounters in real-time.")
                    )
                    .padding(.top, 40)

                    Button(action: { isAddSheetPresented = true }) {
                        Label("Add Monsters to Dashboard", systemImage: "plus.circle.fill")
                            .font(.headline)
                            .padding(.horizontal, 20)
                            .padding(.vertical, 12)
                    }
                    .buttonStyle(.borderedProminent)
                    .padding(.top, 16)
                } else {
                    LazyVGrid(
                        columns: [GridItem(.adaptive(minimum: 320, maximum: 480), spacing: 16)],
                        spacing: 16
                    ) {
                        ForEach(dashboardMonsters) { dashItem in
                            if let monster = monsterFor(dashItem: dashItem) {
                                CombatMonsterCard(
                                    dashItem: dashItem,
                                    monster: monster,
                                    onInspect: { inspectedMonster = monster },
                                    onRemove: { removeDashboardMonster(dashItem) }
                                )
                            }
                        }
                    }
                    .padding()
                }
            }
            .navigationTitle("Combat Dashboard")
            .toolbar {
                ToolbarItemGroup(placement: .topBarTrailing) {
                    if !dashboardMonsters.isEmpty {
                        Button(role: .destructive) {
                            isClearConfirmationPresented = true
                        } label: {
                            Image(systemName: "trash")
                        }
                    }

                    Button {
                        isAddSheetPresented = true
                    } label: {
                        Image(systemName: "plus")
                    }
                }
            }
            .sheet(isPresented: $isAddSheetPresented) {
                AddDashboardMonstersSheet(
                    allMonsters: Array(allMonsters),
                    allCollections: Array(allCollections),
                    isPresented: $isAddSheetPresented
                )
            }
            .sheet(item: $inspectedMonster) { monster in
                NavigationStack {
                    MonsterDetailWrapper(monster: monster)
                        .toolbar {
                            ToolbarItem(placement: .confirmationAction) {
                                Button("Done") {
                                    inspectedMonster = nil
                                }
                            }
                        }
                }
            }
            .confirmationDialog(
                "Clear Combat Dashboard?",
                isPresented: $isClearConfirmationPresented,
                titleVisibility: .visible
            ) {
                Button("Clear All", role: .destructive) {
                    clearAllDashboard()
                }
                Button("Cancel", role: .cancel) {}
            } message: {
                Text("This will remove all monsters currently pinned to the combat dashboard.")
            }
        }
    }

    private func monsterFor(dashItem: DashboardMonster) -> Monster? {
        guard let idStr = dashItem.monsterId, let uuid = UUID(uuidString: idStr) else { return nil }
        return allMonsters.first(where: { $0.uuid == uuid })
    }

    private func removeDashboardMonster(_ dashItem: DashboardMonster) {
        withAnimation {
            viewContext.delete(dashItem)
            try? viewContext.save()
        }
    }

    private func clearAllDashboard() {
        withAnimation {
            for item in dashboardMonsters {
                viewContext.delete(item)
            }
            try? viewContext.save()
        }
    }
}

struct CombatMonsterCard: View {
    @ObservedObject var dashItem: DashboardMonster
    @ObservedObject var monster: Monster
    let onInspect: () -> Void
    let onRemove: () -> Void

    @Environment(\.managedObjectContext) private var viewContext
    @State private var isCustomHpPresented = false
    @State private var hpDeltaText = ""

    private var hpRatio: Double {
        let maxVal = max(1, dashItem.maxHP)
        let current = dashItem.currentHP
        return min(1.0, max(0.0, Double(current) / Double(maxVal)))
    }

    private var healthColor: Color {
        if dashItem.currentHP <= 0 { return .gray }
        if hpRatio > 0.5 { return .green }
        if hpRatio > 0.25 { return .yellow }
        return .red
    }

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            // Header
            HStack(alignment: .top) {
                VStack(alignment: .leading, spacing: 2) {
                    Text(monster.name ?? "Monster")
                        .font(.title3)
                        .fontWeight(.bold)
                        .lineLimit(1)

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
                                .font(.caption2)
                                .fontWeight(.bold)
                                .padding(.horizontal, 4)
                                .padding(.vertical, 1)
                                .background(Theme.dndGold.opacity(0.2))
                                .cornerRadius(3)
                        }
                    }
                }

                Spacer()

                Menu {
                    Button(action: onInspect) {
                        Label("Inspect Full StatBlock", systemImage: "doc.text.magnifyingglass")
                    }
                    Button(action: resetHP) {
                        Label("Reset HP to Max", systemImage: "arrow.counterclockwise")
                    }
                    Divider()
                    Button(role: .destructive, action: onRemove) {
                        Label("Remove from Dashboard", systemImage: "xmark.circle")
                    }
                } label: {
                    Image(systemName: "ellipsis.circle")
                        .font(.title3)
                        .foregroundColor(.secondary)
                }
            }

            // Quick Combat Stats: AC, Speed, Passive Perception
            HStack(spacing: 12) {
                StatBadge(icon: "shield.fill", label: "AC", value: "\(monster.armorClassValue)")
                StatBadge(icon: "figure.run", label: "Speed", value: "\(monster.walkSpeed) ft.")
                StatBadge(icon: "eye.fill", label: "Perception", value: "\(monster.passivePerception)")
            }

            Divider()

            // Hit Points Tracker
            VStack(alignment: .leading, spacing: 6) {
                HStack {
                    Text("Hit Points")
                        .font(.subheadline)
                        .fontWeight(.semibold)

                    Spacer()

                    HStack(spacing: 4) {
                        Text("\(dashItem.currentHP)")
                            .font(.headline)
                            .foregroundColor(healthColor)
                        Text("/ \(dashItem.maxHP)")
                            .font(.subheadline)
                            .foregroundColor(.secondary)

                        if dashItem.tempHP > 0 {
                            Text("(+\(dashItem.tempHP) Temp)")
                                .font(.caption)
                                .fontWeight(.bold)
                                .foregroundColor(.blue)
                        }
                    }
                }

                // Progress Bar
                GeometryReader { geo in
                    ZStack(alignment: .leading) {
                        RoundedRectangle(cornerRadius: 4)
                            .fill(Color.secondary.opacity(0.2))
                            .frame(height: 8)

                        RoundedRectangle(cornerRadius: 4)
                            .fill(healthColor)
                            .frame(width: geo.size.width * CGFloat(hpRatio), height: 8)
                            .animation(.easeInOut(duration: 0.25), value: hpRatio)
                    }
                }
                .frame(height: 8)

                // Steppers & Quick Buttons
                HStack(spacing: 8) {
                    Button("-5") { adjustHP(by: -5) }
                        .buttonStyle(HpButtonStyle(color: .red))
                    Button("-1") { adjustHP(by: -1) }
                        .buttonStyle(HpButtonStyle(color: .red))
                    Button("+1") { adjustHP(by: 1) }
                        .buttonStyle(HpButtonStyle(color: .green))
                    Button("+5") { adjustHP(by: 5) }
                        .buttonStyle(HpButtonStyle(color: .green))

                    Spacer()

                    Button {
                        isCustomHpPresented = true
                    } label: {
                        Text("Custom")
                            .font(.caption)
                            .fontWeight(.medium)
                            .padding(.horizontal, 8)
                            .padding(.vertical, 4)
                            .background(Color.secondary.opacity(0.15))
                            .cornerRadius(6)
                    }
                }
                .padding(.top, 2)
            }

            // Inline Condition / Notes Field
            HStack {
                Image(systemName: "pencil.and.list.clipboard")
                    .foregroundColor(.secondary)
                    .font(.caption)
                TextField("Combat notes, status effects...", text: Binding(
                    get: { dashItem.notes ?? "" },
                    set: {
                        dashItem.notes = $0
                        try? viewContext.save()
                    }
                ))
                .font(.caption)
            }
            .padding(6)
            .background(Color.secondary.opacity(0.08))
            .cornerRadius(6)
        }
        .statblockCardStyle()
        .popover(isPresented: $isCustomHpPresented) {
            CustomHpAdjustmentView(
                currentHp: dashItem.currentHP,
                maxHp: dashItem.maxHP,
                tempHp: dashItem.tempHP,
                onApply: { delta, isTemp in
                    if isTemp {
                        dashItem.tempHP = max(0, Int64(delta))
                    } else {
                        adjustHP(by: delta)
                    }
                    try? viewContext.save()
                    isCustomHpPresented = false
                }
            )
            .padding()
            .frame(width: 260)
        }
    }

    private func adjustHP(by amount: Int) {
        var remainingDelta = Int64(amount)
        if remainingDelta < 0 && dashItem.tempHP > 0 {
            let dmg = -remainingDelta
            if dashItem.tempHP >= dmg {
                dashItem.tempHP -= dmg
                remainingDelta = 0
            } else {
                remainingDelta += dashItem.tempHP
                dashItem.tempHP = 0
            }
        }

        dashItem.currentHP = max(0, min(dashItem.maxHP + 100, dashItem.currentHP + remainingDelta))
        try? viewContext.save()
    }

    private func resetHP() {
        dashItem.currentHP = dashItem.maxHP
        dashItem.tempHP = 0
        try? viewContext.save()
    }
}

struct StatBadge: View {
    let icon: String
    let label: String
    let value: String

    var body: some View {
        HStack(spacing: 4) {
            Image(systemName: icon)
                .font(.caption2)
                .foregroundColor(Theme.dndRed)
            VStack(alignment: .leading, spacing: 0) {
                Text(label)
                    .font(.system(size: 9))
                    .foregroundColor(.secondary)
                Text(value)
                    .font(.caption)
                    .fontWeight(.bold)
            }
        }
        .padding(.horizontal, 6)
        .padding(.vertical, 3)
        .background(Color.secondary.opacity(0.1))
        .cornerRadius(6)
    }
}

struct HpButtonStyle: ButtonStyle {
    let color: Color

    func makeBody(configuration: Configuration) -> some View {
        configuration.label
            .font(.caption)
            .fontWeight(.bold)
            .padding(.horizontal, 10)
            .padding(.vertical, 5)
            .background(color.opacity(configuration.isPressed ? 0.3 : 0.15))
            .foregroundColor(color)
            .cornerRadius(6)
    }
}

struct CustomHpAdjustmentView: View {
    let currentHp: Int64
    let maxHp: Int64
    let tempHp: Int64
    let onApply: (Int, Bool) -> Void

    @State private var amountString = ""
    @State private var isTempHpMode = false

    var body: some View {
        VStack(spacing: 12) {
            Text(isTempHpMode ? "Set Temp HP" : "Apply Damage / Heal")
                .font(.headline)

            Picker("Mode", selection: $isTempHpMode) {
                Text("Damage / Heal").tag(false)
                Text("Temp HP").tag(true)
            }
            .pickerStyle(.segmented)

            TextField("Amount (e.g. 12)", text: $amountString)
                .textFieldStyle(.roundedBorder)
                .keyboardType(.numberPad)

            if !isTempHpMode {
                HStack(spacing: 12) {
                    Button(role: .destructive) {
                        if let val = Int(amountString), val > 0 {
                            onApply(-val, false)
                        }
                    } label: {
                        Text("Damage")
                            .frame(maxWidth: .infinity)
                    }
                    .buttonStyle(.borderedProminent)
                    .tint(.red)

                    Button {
                        if let val = Int(amountString), val > 0 {
                            onApply(val, false)
                        }
                    } label: {
                        Text("Heal")
                            .frame(maxWidth: .infinity)
                    }
                    .buttonStyle(.borderedProminent)
                    .tint(.green)
                }
            } else {
                Button("Set Temp HP") {
                    if let val = Int(amountString) {
                        onApply(val, true)
                    }
                }
                .buttonStyle(.borderedProminent)
            }
        }
    }
}

struct AddDashboardMonstersSheet: View {
    let allMonsters: [Monster]
    let allCollections: [Collection]
    @Binding var isPresented: Bool
    @Environment(\.managedObjectContext) private var viewContext
    @State private var selectedTab = 0

    var body: some View {
        NavigationStack {
            VStack {
                Picker("Source", selection: $selectedTab) {
                    Text("Monsters").tag(0)
                    Text("Collections").tag(1)
                }
                .pickerStyle(.segmented)
                .padding()

                if selectedTab == 0 {
                    List(allMonsters) { monster in
                        Button {
                            addMonsterToDashboard(monster)
                        } label: {
                            HStack {
                                MonsterListRow(monster: monster)
                                Spacer()
                                Image(systemName: "plus.circle")
                                    .foregroundColor(.accentColor)
                            }
                        }
                    }
                } else {
                    List(allCollections) { col in
                        Button {
                            addCollectionToDashboard(col)
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
            .navigationTitle("Add to Dashboard")
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .cancellationAction) {
                    Button("Done") {
                        isPresented = false
                    }
                }
            }
        }
    }

    private func addMonsterToDashboard(_ monster: Monster) {
        guard let uuid = monster.uuid else { return }
        try? MonsterRepository.shared.addMonsterToDashboard(id: uuid)
        isPresented = false
    }

    private func addCollectionToDashboard(_ col: Collection) {
        guard let links = col.monsters as? Set<CollectionMonster> else { return }
        for link in links {
            if let mId = link.monsterId, let uuid = UUID(uuidString: mId) {
                try? MonsterRepository.shared.addMonsterToDashboard(id: uuid)
            }
        }
        isPresented = false
    }
}

// Helpers on Monster for quick combat access
extension Monster {
    var armorClassValue: Int {
        // Base approximation or calculated from armor
        let dexMod = Int((dexterityScore - 10) / 2)
        if armorTypeEnum == .none {
            return 10 + dexMod + Int(shieldBonus)
        }
        return Int(armorTypeEnum.baseArmorClass) + min(dexMod, 2) + Int(shieldBonus)
    }

    var passivePerception: Int {
        let wisMod = Int((wisdomScore - 10) / 2)
        return 10 + wisMod
    }
}

#Preview {
    Dashboard()
        .environment(\.managedObjectContext, PersistenceController.preview.container.viewContext)
}
