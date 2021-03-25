//
//  Persistence.swift
//  MonsterCards
//
//  Created by Tom Hicks on 1/15/21.
//

import CoreData

struct PersistenceController {
    static let shared = PersistenceController()

    static var preview: PersistenceController = {
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
            // Replace this implementation with code to handle the error appropriately.
            // fatalError() causes the application to generate a crash log and terminate. You should not use this function in a shipping application, although it may be useful during development.
            let nsError = error as NSError
//            fatalError("TOMHICKS_Unresolved error \(nsError), \(nsError.userInfo)")
        }
        return result
    }()

    let container: NSPersistentCloudKitContainer

    init(inMemory: Bool = false) {
        container = NSPersistentCloudKitContainer(name: "MonsterCards")
        if inMemory {
            container.persistentStoreDescriptions.first!.url = URL(fileURLWithPath: "/dev/null")
        }
        container.loadPersistentStores(completionHandler: { (storeDescription, error) in
            if let error = error as NSError? {
                fatalError("Unresolved error \(error), \(error.userInfo)")
            }
        })
    }
}
