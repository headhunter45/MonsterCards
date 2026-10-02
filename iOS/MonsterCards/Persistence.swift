//
//  Persistence.swift
//  MonsterCards
//
//  Created by Tom Hicks on 1/15/21.
//

import CoreData
import Foundation

public struct PersistenceController: @unchecked Sendable {
    public static let shared = PersistenceController()

    @MainActor
    public static let preview: PersistenceController = {
        let controller = PersistenceController(inMemory: true)
        let viewContext = controller.container.viewContext
        DevContent.seedPreviewData(into: viewContext)
        return controller
    }()

    nonisolated(unsafe) private static let model: NSManagedObjectModel = {
        if let modelURL = Bundle(for: Monster.self).url(forResource: "MonsterCards", withExtension: "momd") ?? Bundle.main.url(forResource: "MonsterCards", withExtension: "momd"),
           let model = NSManagedObjectModel(contentsOf: modelURL) {
            return model
        }
        return NSPersistentContainer(name: "MonsterCards").managedObjectModel
    }()

    public let container: NSPersistentCloudKitContainer

    public init(inMemory: Bool = false) {
        container = NSPersistentCloudKitContainer(name: "MonsterCards", managedObjectModel: PersistenceController.model)

        guard let description = container.persistentStoreDescriptions.first else {
            fatalError("Failed to retrieve persistent store description.")
        }

        if inMemory {
            description.url = URL(fileURLWithPath: "/dev/null")
        } else {
            // Configure CloudKit container options if not in-memory
            description.setOption(true as NSNumber, forKey: NSPersistentHistoryTrackingKey)
            description.setOption(true as NSNumber, forKey: NSPersistentStoreRemoteChangeNotificationPostOptionKey)
        }

        // Merge policy favoring in-memory changes or remote updates gracefully
        container.viewContext.automaticallyMergesChangesFromParent = true
        container.viewContext.mergePolicy = NSMergePolicy(merge: .mergeByPropertyObjectTrumpMergePolicyType)

        container.loadPersistentStores { storeDescription, error in
            if let error = error as NSError? {
                // Log the persistent store load failure instead of a fatal crash
                print("Warning: Failed to load persistent store \(storeDescription): \(error), \(error.userInfo)")
            }
        }
    }

    public func newBackgroundContext() -> NSManagedObjectContext {
        let context = container.newBackgroundContext()
        context.automaticallyMergesChangesFromParent = true
        context.mergePolicy = NSMergePolicy(merge: .mergeByPropertyObjectTrumpMergePolicyType)
        return context
    }
}

public enum DevContent {
    @MainActor
    public static func seedPreviewData(into context: NSManagedObjectContext) {
        let pixie = Monster(context: context)
        pixie.name = "Pixie"
        pixie.size = "tiny"
        pixie.type = "fey"
        pixie.subtype = ""
        pixie.alignment = "neutral good"
        pixie.challengeRating = "1/4"
        pixie.walkSpeed = 10
        pixie.flySpeed = 30
        pixie.strengthScore = 2
        pixie.dexterityScore = 20
        pixie.constitutionScore = 8
        pixie.intelligenceScore = 10
        pixie.wisdomScore = 14
        pixie.charismaScore = 15
        pixie.uuid = UUID()

        let dragon = Monster(context: context)
        dragon.name = "Ancient Black Dragon"
        dragon.size = "gargantuan"
        dragon.type = "dragon"
        dragon.alignment = "chaotic evil"
        dragon.challengeRating = "21"
        dragon.walkSpeed = 40
        dragon.flySpeed = 80
        dragon.swimSpeed = 40
        dragon.strengthScore = 27
        dragon.dexterityScore = 14
        dragon.constitutionScore = 25
        dragon.intelligenceScore = 16
        dragon.wisdomScore = 15
        dragon.charismaScore = 19
        dragon.uuid = UUID()

        let goblin = Monster(context: context)
        goblin.name = "Goblin"
        goblin.size = "small"
        goblin.type = "humanoid"
        goblin.subtype = "goblinoid"
        goblin.alignment = "neutral evil"
        goblin.challengeRating = "1/4"
        goblin.walkSpeed = 30
        goblin.strengthScore = 8
        goblin.dexterityScore = 14
        goblin.constitutionScore = 10
        goblin.intelligenceScore = 10
        goblin.wisdomScore = 8
        goblin.charismaScore = 8
        goblin.uuid = UUID()

        let sampleCollection = Collection(context: context)
        sampleCollection.name = "Forest Encounter"
        sampleCollection.details = "Monsters for Session 1 in Whisperwood"
        sampleCollection.sortOrder = 0

        let link1 = CollectionMonster(context: context)
        link1.collection = sampleCollection
        link1.collectionId = sampleCollection.name ?? ""
        link1.monsterId = pixie.uuid?.uuidString ?? ""
        link1.ordinal = 0

        let link2 = CollectionMonster(context: context)
        link2.collection = sampleCollection
        link2.collectionId = sampleCollection.name ?? ""
        link2.monsterId = goblin.uuid?.uuidString ?? ""
        link2.ordinal = 1

        let dash1 = DashboardMonster(context: context)
        dash1.monsterId = goblin.uuid?.uuidString ?? ""
        dash1.ordinal = 0
        dash1.currentHP = 7
        dash1.maxHP = 7
        dash1.tempHP = 0
        dash1.notes = "Stealth ambush"

        do {
            try context.save()
        } catch {
            let nsError = error as NSError
            print("DevContent preview save error: \(nsError), \(nsError.userInfo)")
        }
    }
}
