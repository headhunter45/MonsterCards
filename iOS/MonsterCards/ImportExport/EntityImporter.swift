//
//  EntityImporter.swift
//  MonsterCards
//
//  Created by Tom Hicks on 4/3/21.
//  Last Modified by Tom Hicks on 9/23/26.
//

import Foundation

/// Protocol that all entity importers implement.
/// Each importer decides whether it can handle the input and then parses it into domain objects.
protocol EntityImporter {
    /// Determines whether this importer can handle the given input string or payload.
    static func canImport(_ input: String) -> Bool
    
    /// Parses the raw input string/JSON into a `MonsterViewModel`.
    @MainActor
    static func parse(_ input: String) throws -> MonsterViewModel
}

/// Registry type that dispatches parsing to the correct importer based on format sniffing.
enum ImporterRegistry {
    
    @MainActor
    private static var importers: [any EntityImporter.Type] {
        [
            TetraCubeMonsterImporter.self,
            BinderImporter.self,
        ]
    }
    
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
                    print("Importer \(importer) error: \(error)")
                    continue
                }
            }
        }
        return nil
    }
}
