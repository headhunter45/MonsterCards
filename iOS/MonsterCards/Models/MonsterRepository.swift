//
//  MonsterRepository.swift
//  MonsterCards
//
//  Created by Antigravity on 10/1/26.
//

import CoreData
import Foundation

@MainActor
public protocol MonsterRepositoryProtocol {
    func fetchMonsters(query: String?, type: String?, cr: String?, sortAscending: Bool) throws -> [Monster]
    func fetchMonster(by id: UUID) throws -> Monster?
    func deleteMonster(id: UUID) throws
    func fetchCollections() throws -> [Collection]
    func fetchMonsters(for collection: Collection) throws -> [Monster]
    func fetchDashboardMonsters() throws -> [DashboardMonster]
    func addMonsterToDashboard(id: UUID) throws
    func removeMonsterFromDashboard(id: UUID) throws
    func updateDashboardMonsterHP(id: UUID, currentHP: Int64, tempHP: Int64, notes: String?) throws
}

@MainActor
public final class MonsterRepository: MonsterRepositoryProtocol, ObservableObject {
    public static let shared = MonsterRepository(persistenceController: .shared)

    private let persistenceController: PersistenceController

    public init(persistenceController: PersistenceController = .shared) {
        self.persistenceController = persistenceController
    }

    private var context: NSManagedObjectContext {
        persistenceController.container.viewContext
    }

    public func fetchMonsters(query: String? = nil, type: String? = nil, cr: String? = nil, sortAscending: Bool = true) throws -> [Monster] {
        let request: NSFetchRequest<Monster> = Monster.fetchRequest()
        var predicates: [NSPredicate] = []

        if let q = query, !q.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty {
            let cleanQ = q.trimmingCharacters(in: .whitespacesAndNewlines)
            predicates.append(NSPredicate(format: "name CONTAINS[cd] %@ OR type CONTAINS[cd] %@ OR subtype CONTAINS[cd] %@ OR alignment CONTAINS[cd] %@", cleanQ, cleanQ, cleanQ, cleanQ))
        }

        if let t = type, !t.isEmpty, t.lowercased() != "all" {
            predicates.append(NSPredicate(format: "type ==[cd] %@", t))
        }

        if let c = cr, !c.isEmpty, c.lowercased() != "all" {
            predicates.append(NSPredicate(format: "challengeRating ==[cd] %@", c))
        }

        if !predicates.isEmpty {
            request.predicate = NSCompoundPredicate(andPredicateWithSubpredicates: predicates)
        }

        request.sortDescriptors = [NSSortDescriptor(key: "name", ascending: sortAscending)]
        return try context.fetch(request)
    }

    public func fetchMonster(by id: UUID) throws -> Monster? {
        let request: NSFetchRequest<Monster> = Monster.fetchRequest()
        request.predicate = NSPredicate(format: "uuid == %@", id as CVarArg)
        request.fetchLimit = 1
        return try context.fetch(request).first
    }

    public func deleteMonster(id: UUID) throws {
        let request: NSFetchRequest<Monster> = Monster.fetchRequest()
        request.predicate = NSPredicate(format: "uuid == %@", id as CVarArg)
        let monsters = try context.fetch(request)
        for m in monsters {
            context.delete(m)
        }
        if context.hasChanges {
            try context.save()
        }
    }

    public func fetchCollections() throws -> [Collection] {
        let request: NSFetchRequest<Collection> = Collection.fetchRequest()
        request.sortDescriptors = [NSSortDescriptor(key: "sortOrder", ascending: true), NSSortDescriptor(key: "name", ascending: true)]
        return try context.fetch(request)
    }

    public func fetchMonsters(for collection: Collection) throws -> [Monster] {
        guard let links = collection.monsters as? Set<CollectionMonster> else { return [] }
        let sortedLinks = links.sorted { $0.ordinal < $1.ordinal }
        var result: [Monster] = []
        for link in sortedLinks {
            if let mIdStr = link.monsterId, let uuid = UUID(uuidString: mIdStr) {
                let request: NSFetchRequest<Monster> = Monster.fetchRequest()
                request.predicate = NSPredicate(format: "uuid == %@", uuid as CVarArg)
                if let found = try context.fetch(request).first {
                    result.append(found)
                }
            }
        }
        return result
    }

    public func fetchDashboardMonsters() throws -> [DashboardMonster] {
        let request: NSFetchRequest<DashboardMonster> = NSFetchRequest<DashboardMonster>(entityName: "DashboardMonster")
        request.sortDescriptors = [NSSortDescriptor(key: "ordinal", ascending: true)]
        return try context.fetch(request)
    }

    public func addMonsterToDashboard(id: UUID) throws {
        let request: NSFetchRequest<DashboardMonster> = NSFetchRequest<DashboardMonster>(entityName: "DashboardMonster")
        request.predicate = NSPredicate(format: "monsterId == %@", id.uuidString)
        if try context.fetch(request).first == nil {
            let countReq: NSFetchRequest<DashboardMonster> = NSFetchRequest<DashboardMonster>(entityName: "DashboardMonster")
            let count = try context.count(for: countReq)
            let item = DashboardMonster(context: context)
            item.monsterId = id.uuidString
            item.ordinal = Int64(count)

            // Populate initial HP from monster if available
            let mReq: NSFetchRequest<Monster> = Monster.fetchRequest()
            mReq.predicate = NSPredicate(format: "uuid == %@", id as CVarArg)
            if let m = try context.fetch(mReq).first {
                let conMod = Int64((m.constitutionScore - 10) / 2)
                let baseHp = max(1, m.hitDice * 4 + (m.hitDice * conMod))
                item.currentHP = baseHp
                item.maxHP = baseHp
            }

            try context.save()
        }
    }

    public func removeMonsterFromDashboard(id: UUID) throws {
        let request: NSFetchRequest<DashboardMonster> = NSFetchRequest<DashboardMonster>(entityName: "DashboardMonster")
        request.predicate = NSPredicate(format: "monsterId == %@", id.uuidString)
        let items = try context.fetch(request)
        for item in items {
            context.delete(item)
        }
        if context.hasChanges {
            try context.save()
        }
    }

    public func updateDashboardMonsterHP(id: UUID, currentHP: Int64, tempHP: Int64, notes: String? = nil) throws {
        let request: NSFetchRequest<DashboardMonster> = NSFetchRequest<DashboardMonster>(entityName: "DashboardMonster")
        request.predicate = NSPredicate(format: "monsterId == %@", id.uuidString)
        if let item = try context.fetch(request).first {
            item.currentHP = currentHP
            item.tempHP = tempHP
            if let n = notes {
                item.notes = n
            }
            try context.save()
        }
    }
}
