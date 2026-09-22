//
//  EntityImporter.swift
//  MonsterCards
//
//  Imported from Android commit 044202c — refactors monster importers to a common interface.
//

import Foundation

/// Protocol that all entity importers implement, matching the Java `EntityImporter<T>` interface.
/// Each importer decides whether it can handle the input and then parses it into domain objects.
protocol EntityImporter {
    /// Determines whether this importer can handle the given input string or payload.
    func canImport(_ input: String) -> Bool
    
    /// Parses the raw input string/JSON into a `MonsterViewModel`.
    func parse(_ input: String) throws -> MonsterViewModel
}

/// Registry type that dispatches parsing to the correct importer based on format sniffing,
/// mirroring the Android `MonsterImportHelper` dispatcher logic.
enum ImporterRegistry {
    
    private static let importers: [EntityImporter] = [
        MonsterJsonImporter(),
        DnDBeyondImporter(),
        Open5eImporter(),
        BinderImporter(),
    ]
    
    /// Detects which importer handles the input and delegates parse to it.
    @MainActor
    static func importMonster(from input: String) -> MonsterViewModel? {
        let trimmed = input.trimmingCharacters(in: .whitespacesAndNewlines)
        guard !trimmed.isEmpty else { return nil }
        
        for importer in importers {
            if importer.canImport(input) {
                do {
                    let monster = try importer.parse(input)
                    return monster
                } catch {
                    print("Importer \(type(of: importer)) error: \(error)")
                    continue
                }
            }
        }
        return nil
    }
}
