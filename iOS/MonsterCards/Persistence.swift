//
//  Persistence.swift
//  MonsterCards
//
//  Created by Tom Hicks on 1/15/21.
//

import CoreData

struct PersistenceController {
    static let shared = PersistenceController()

    @MainActor
    static let preview: PersistenceController = {
        let result = PersistenceController(inMemory: true)
        let viewContext = result.container.viewContext
        let monsters: [Monster] = [
            Monster(context:viewContext, name: "Ted", size: "Huge", type: "humanoid", subtype: "any race", alignment: "any alignment"),
            Monster(context:viewContext, name: "Steve", size: "Huge", type: "humanoid", subtype: "any race", alignment: "any alignment"),
            Monster(context:viewContext, name: "Dave", size: "Huge", type: "humanoid", subtype: "any race", alignment: "any alignment")
        ]
        do {
            try viewContext.save()
        } catch {
            // TODO: Replace this implementation with code to handle the error appropriately.
            let nsError = error as NSError
            print("Unresolved error \(nsError), \(nsError.userInfo)")
        }
        return result
    }()

    let container: NSPersistentCloudKitContainer

    init(inMemory: Bool = false) {
        let container = NSPersistentCloudKitContainer(name: "MonsterCards")
        if inMemory {
            container.persistentStoreDescriptions.first!.url = URL(fileURLWithPath: "/dev/null")
        }
        container.viewContext.automaticallyMergesChangesFromParent = true
        container.loadPersistentStores(completionHandler: { (storeDescription, error) in
            if let error = error as NSError? {
                #if DEBUG
                // TODO: Disable wiping database on migration failure before production release.
                // During development, if automatic migration fails (e.g. schema changes),
                // wipe the existing store and reload a fresh database.
                if let storeURL = storeDescription.url, storeURL.path != "/dev/null" {
                    do {
                        try container.persistentStoreCoordinator.destroyPersistentStore(
                            at: storeURL,
                            ofType: storeDescription.type,
                            options: storeDescription.options
                        )
                    } catch {
                        try? FileManager.default.removeItem(at: storeURL)
                        try? FileManager.default.removeItem(atPath: storeURL.path + "-shm")
                        try? FileManager.default.removeItem(atPath: storeURL.path + "-wal")
                    }

                    container.loadPersistentStores { (_, retryError) in
                        if let retryError = retryError as NSError? {
                            fatalError("Unresolved error after wiping database: \(retryError), \(retryError.userInfo)")
                        }
                    }
                    return
                }
                #endif

                // TODO: Handle persistent store load failure gracefully instead of fatalError
                fatalError("Unresolved error \(error), \(error.userInfo)")
            }
        })
        self.container = container
    }
}
