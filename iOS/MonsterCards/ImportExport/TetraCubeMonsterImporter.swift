//
//  TetraCubeMonsterImporter.swift
//  MonsterCards
//
//  Created by Tom Hicks on 9/23/26.
//

import Foundation

/// Importer for TetraCube monster generator JSON format.
struct TetraCubeMonsterImporter: EntityImporter {
    
    // MARK: - canImport
    
    static func canImport(_ input: String) -> Bool {
        guard let data = input.data(using: .utf8),
              let root = try? JSONSerialization.jsonObject(with: data, options: []) as? [String: Any] else {
            return false
        }
        
        // Detect TetraCube specific keys
        return root["name"] != nil && root["strPoints"] != nil
    }
    
    // MARK: - parse
    
    @MainActor
    static func parse(_ input: String) throws -> MonsterViewModel {
        guard let data = input.data(using: .utf8) else {
            throw TetraCubeParseError.unsupportedEncoding
        }
        
        let decoder = JSONDecoder()
        let monsterDTO = try decoder.decode(MonsterDTO.self, from: data)
        return MonsterImportHelper.import5ESBMonster(monsterDTO)
    }
}

// MARK: - Error Type

enum TetraCubeParseError: LocalizedError {
    case unsupportedEncoding
    case invalidFormat(String)
    
    var errorDescription: String? {
        switch self {
        case .unsupportedEncoding:
            return "TetraCube import requires UTF-8 JSON"
        case .invalidFormat(let msg):
            return "Invalid TetraCube format: \(msg)"
        }
    }
}
