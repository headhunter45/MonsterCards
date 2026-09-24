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
        container = NSPersistentCloudKitContainer(name: "MonsterCards")
        if inMemory {
            container.persistentStoreDescriptions.first!.url = URL(fileURLWithPath: "/dev/null")
        }
        container.viewContext.automaticallyMergesChangesFromParent = true
        container.loadPersistentStores(completionHandler: { (storeDescription, error) in
            if let error = error as NSError? {
                // TODO: Handle persistent store load failure gracefully instead of fatalError
                fatalError("Unresolved error \(error), \(error.userInfo)")
            }
        })
    }
}
