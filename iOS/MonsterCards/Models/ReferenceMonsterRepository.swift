//
//  ReferenceMonsterRepository.swift
//  MonsterCards
//

import Foundation
import CoreData

final class ReferenceMonsterRepository: @unchecked Sendable {
    static let shared = ReferenceMonsterRepository()
    
    init() {}
    
    // MARK: - Cache Directory
    
    static func getCompendiumCacheDirectory() -> URL {
        let fileManager = FileManager.default
        let paths = fileManager.urls(for: .cachesDirectory, in: .userDomainMask)
        let cacheDir = paths[0].appendingPathComponent("MonsterCardsCompendiums", isDirectory: true)
        if !fileManager.fileExists(atPath: cacheDir.path) {
            try? fileManager.createDirectory(at: cacheDir, withIntermediateDirectories: true, attributes: nil)
        }
        return cacheDir
    }
    
    static func getExtractedJsonDir(sourceId: String) -> URL {
        let dir = getCompendiumCacheDirectory().appendingPathComponent(sourceId, isDirectory: true)
        if !FileManager.default.fileExists(atPath: dir.path) {
            try? FileManager.default.createDirectory(at: dir, withIntermediateDirectories: true, attributes: nil)
        }
        return dir
    }
    
    // MARK: - Querying
    
    func countForSource(sourceId: String, in context: NSManagedObjectContext) -> Int {
        let request = NSFetchRequest<ReferenceMonster>(entityName: "ReferenceMonster")
        request.predicate = NSPredicate(format: "sourceId == %@", sourceId)
        return (try? context.count(for: request)) ?? 0
    }
    
    func totalReferenceCount(in context: NSManagedObjectContext) -> Int {
        let request = NSFetchRequest<ReferenceMonster>(entityName: "ReferenceMonster")
        return (try? context.count(for: request)) ?? 0
    }
    
    func searchReferenceMonsters(
        query: String,
        gameSystem: GameSystem? = nil,
        limit: Int = 100,
        in context: NSManagedObjectContext
    ) -> [ReferenceMonster] {
        let request = NSFetchRequest<ReferenceMonster>(entityName: "ReferenceMonster")
        var predicates: [NSPredicate] = []
        
        let trimmedQuery = query.trimmingCharacters(in: .whitespacesAndNewlines)
        if !trimmedQuery.isEmpty {
            let namePred = NSPredicate(format: "name CONTAINS[cd] %@", trimmedQuery)
            let typePred = NSPredicate(format: "type CONTAINS[cd] %@", trimmedQuery)
            let sourcePred = NSPredicate(format: "sourceLabel CONTAINS[cd] %@", trimmedQuery)
            predicates.append(NSCompoundPredicate(orPredicateWithSubpredicates: [namePred, typePred, sourcePred]))
        }
        
        if let gameSystem = gameSystem {
            predicates.append(NSPredicate(format: "gameSystem == %@", gameSystem.rawValue))
        }
        
        if !predicates.isEmpty {
            request.predicate = NSCompoundPredicate(andPredicateWithSubpredicates: predicates)
        }
        
        request.sortDescriptors = [NSSortDescriptor(key: "name", ascending: true)]
        request.fetchLimit = limit
        
        return (try? context.fetch(request)) ?? []
    }
    
    // MARK: - Batch Ingestion & Atomic Replacement
    
    func deleteSource(sourceId: String, in context: NSManagedObjectContext) throws {
        let fetchRequest: NSFetchRequest<NSFetchRequestResult> = NSFetchRequest(entityName: "ReferenceMonster")
        fetchRequest.predicate = NSPredicate(format: "sourceId == %@", sourceId)
        let deleteRequest = NSBatchDeleteRequest(fetchRequest: fetchRequest)
        deleteRequest.resultType = .resultTypeObjectIDs
        
        let result = try context.execute(deleteRequest) as? NSBatchDeleteResult
        if let objectIDs = result?.result as? [NSManagedObjectID] {
            let changes = [NSDeletedObjectsKey: objectIDs]
            NSManagedObjectContext.mergeChanges(fromRemoteContextSave: changes, into: [context])
        }
    }
    
    func insertBatch(monsters: [[String: Any]], in context: NSManagedObjectContext) throws {
        guard !monsters.isEmpty else { return }
        let batchInsert = NSBatchInsertRequest(entityName: "ReferenceMonster", objects: monsters)
        batchInsert.resultType = .objectIDs
        let result = try context.execute(batchInsert) as? NSBatchInsertResult
        if let objectIDs = result?.result as? [NSManagedObjectID] {
            let changes = [NSInsertedObjectsKey: objectIDs]
            NSManagedObjectContext.mergeChanges(fromRemoteContextSave: changes, into: [context])
        }
    }
    
    func replaceSource(
        sourceId: String,
        monsters: [[String: Any]],
        in context: NSManagedObjectContext
    ) throws {
        try deleteSource(sourceId: sourceId, in: context)
        try insertBatch(monsters: monsters, in: context)
        if context.hasChanges {
            try context.save()
        }
    }
    
    // MARK: - Mapping Dictionaries
    
    static func dictionaryFromViewModel(
        _ vm: MonsterViewModel,
        id: String = UUID().uuidString,
        sourceId: String,
        sourceLabel: String,
        gameSystem: GameSystem,
        bookSource: String = ""
    ) -> [String: Any] {
        var dict: [String: Any] = [:]
        dict["id"] = id
        dict["sourceId"] = sourceId
        dict["sourceLabel"] = sourceLabel
        dict["gameSystem"] = gameSystem.rawValue
        dict["bookSource"] = bookSource
        dict["name"] = vm.name
        dict["size"] = vm.size
        dict["type"] = vm.type
        dict["subtype"] = vm.subType
        dict["alignment"] = vm.alignment
        dict["challengeRating"] = vm.challengeRating.rawValue
        dict["customChallengeRating"] = vm.customChallengeRating
        dict["customProficiencyBonus"] = Int64(vm.customProficiencyBonus)
        dict["armorType"] = vm.armorType.rawValue
        dict["customArmor"] = vm.customArmor
        dict["naturalArmorBonus"] = Int64(vm.naturalArmorBonus)
        dict["shieldBonus"] = Int64(vm.shieldBonus)
        dict["otherArmorDescription"] = vm.otherArmorDescription
        dict["hasShield"] = vm.hasShield
        dict["hitDice"] = Int64(vm.hitDice)
        dict["hasCustomHP"] = vm.hasCustomHP
        dict["customHP"] = vm.customHP
        dict["walkSpeed"] = Int64(vm.walkSpeed)
        dict["burrowSpeed"] = Int64(vm.burrowSpeed)
        dict["climbSpeed"] = Int64(vm.climbSpeed)
        dict["flySpeed"] = Int64(vm.flySpeed)
        dict["swimSpeed"] = Int64(vm.swimSpeed)
        dict["canHover"] = vm.canHover
        dict["hasCustomSpeed"] = vm.hasCustomSpeed
        dict["customSpeed"] = vm.customSpeed
        dict["strengthScore"] = Int64(vm.strengthScore)
        dict["dexterityScore"] = Int64(vm.dexterityScore)
        dict["constitutionScore"] = Int64(vm.constitutionScore)
        dict["intelligenceScore"] = Int64(vm.intelligenceScore)
        dict["wisdomScore"] = Int64(vm.wisdomScore)
        dict["charismaScore"] = Int64(vm.charismaScore)
        dict["strengthSavingThrowProficiency"] = vm.strengthSavingThrowProficiency.rawValue
        dict["strengthSavingThrowAdvantage"] = vm.strengthSavingThrowAdvantage.rawValue
        dict["dexteritySavingThrowProficiency"] = vm.dexteritySavingThrowProficiency.rawValue
        dict["dexteritySavingThrowAdvantage"] = vm.dexteritySavingThrowAdvantage.rawValue
        dict["constitutionSavingThrowProficiency"] = vm.constitutionSavingThrowProficiency.rawValue
        dict["constitutionSavingThrowAdvantage"] = vm.constitutionSavingThrowAdvantage.rawValue
        dict["intelligenceSavingThrowProficiency"] = vm.intelligenceSavingThrowProficiency.rawValue
        dict["intelligenceSavingThrowAdvantage"] = vm.intelligenceSavingThrowAdvantage.rawValue
        dict["wisdomSavingThrowProficiency"] = vm.wisdomSavingThrowProficiency.rawValue
        dict["wisdomSavingThrowAdvantage"] = vm.wisdomSavingThrowAdvantage.rawValue
        dict["charismaSavingThrowProficiency"] = vm.charismaSavingThrowProficiency.rawValue
        dict["charismaSavingThrowAdvantage"] = vm.charismaSavingThrowAdvantage.rawValue
        dict["telepathy"] = Int64(vm.telepathy)
        dict["understandsBut"] = vm.understandsBut
        dict["isBlind"] = vm.isBlind
        dict["imageURL"] = ""
        dict["sourceURL"] = ""

        // JSON string encodes
        let encoder = JSONEncoder()
        if let data = try? encoder.encode(vm.damageImmunities.map { $0.name }),
           let str = String(data: data, encoding: .utf8) {
            dict["damageImmunitiesJson"] = str
        }
        if let data = try? encoder.encode(vm.damageResistances.map { $0.name }),
           let str = String(data: data, encoding: .utf8) {
            dict["damageResistancesJson"] = str
        }
        if let data = try? encoder.encode(vm.damageVulnerabilities.map { $0.name }),
           let str = String(data: data, encoding: .utf8) {
            dict["damageVulnerabilitiesJson"] = str
        }
        if let data = try? encoder.encode(vm.conditionImmunities.map { $0.name }),
           let str = String(data: data, encoding: .utf8) {
            dict["conditionImmunitiesJson"] = str
        }
        if let data = try? encoder.encode(vm.senses.map { $0.name }),
           let str = String(data: data, encoding: .utf8) {
            dict["sensesJson"] = str
        }

        struct JsonSkill: Codable {
            var name: String
            var ability: String
            var prof: String
            var adv: String
        }
        let skills = vm.skills.map {
            JsonSkill(
                name: $0.name,
                ability: $0.abilityScore.rawValue,
                prof: $0.proficiency.rawValue,
                adv: $0.advantage.rawValue
            )
        }
        if let data = try? encoder.encode(skills),
           let str = String(data: data, encoding: .utf8) {
            dict["skillsJson"] = str
        }

        struct JsonLanguage: Codable {
            var name: String
            var speaks: Bool
        }
        let languages = vm.languages.map { JsonLanguage(name: $0.name, speaks: $0.speaks) }
        if let data = try? encoder.encode(languages),
           let str = String(data: data, encoding: .utf8) {
            dict["languagesJson"] = str
        }

        struct JsonAbility: Codable {
            var name: String
            var desc: String
        }
        let mapAbility: (AbilityViewModel) -> JsonAbility = { JsonAbility(name: $0.name, desc: $0.abilityDescription) }
        
        if let data = try? encoder.encode(vm.abilities.map(mapAbility)), let str = String(data: data, encoding: .utf8) {
            dict["abilitiesJson"] = str
        }
        if let data = try? encoder.encode(vm.actions.map(mapAbility)), let str = String(data: data, encoding: .utf8) {
            dict["actionsJson"] = str
        }
        if let data = try? encoder.encode(vm.reactions.map(mapAbility)), let str = String(data: data, encoding: .utf8) {
            dict["reactionsJson"] = str
        }
        if let data = try? encoder.encode(vm.legendaryActions.map(mapAbility)), let str = String(data: data, encoding: .utf8) {
            dict["legendaryActionsJson"] = str
        }
        if let data = try? encoder.encode(vm.lairActions.map(mapAbility)), let str = String(data: data, encoding: .utf8) {
            dict["lairActionsJson"] = str
        }
        if let data = try? encoder.encode(vm.regionalActions.map(mapAbility)), let str = String(data: data, encoding: .utf8) {
            dict["regionalActionsJson"] = str
        }

        return dict
    }
}
