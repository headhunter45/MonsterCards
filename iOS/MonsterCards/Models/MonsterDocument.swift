//
//  Document.swift
//  MonsterCards
//
//  Created by Tom Hicks on 4/7/21.
//

import UIKit
import SceneKit

class MonsterDocument: UIDocument {

    var monsterDTO: MonsterDTO?
    var error: Error?
    
    override func contents(forType typeName: String) throws -> Any {
        let encoder = JSONEncoder()
        do {
            let data = try encoder.encode(monsterDTO)
            return data
        } catch {
            return Data()
        }
    }
    
    override func load(fromContents contents: Any, ofType typeName: String?) throws {
        let decoder = JSONDecoder()
        do {
            let data = contents as! Data
            monsterDTO = try decoder.decode(MonsterDTO.self, from: data)
        } catch {
            monsterDTO = MonsterDTO()
        }
    }
    
}

