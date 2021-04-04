//
//  DamageTypeDTO.swift
//  MonsterCards
//
//  Created by Tom Hicks on 3/28/21.
//

import Foundation

struct DamageTypeDTO {
    var name: String
    var note: String
    var type: String
}

private enum DamageTypeDTOCodingKeys: String, CodingKey {
    case name = "name"
    case note = "note"
    case type = "type"
}

extension DamageTypeDTO: Decodable {
    
    init(from decoder: Decoder) throws {
        
        let container = try decoder.container(keyedBy: DamageTypeDTOCodingKeys.self)
        self.name = (try? container.decode(String.self, forKey: .name)) ?? ""
        self.note = (try? container.decode(String.self, forKey: .note)) ?? ""
        self.type = (try? container.decode(String.self, forKey: .type)) ?? ""
    }
}

extension DamageTypeDTO: Encodable {
    
    func encode(to encoder: Encoder) throws {
    
        var container = encoder.container(keyedBy: DamageTypeDTOCodingKeys.self)
        try container.encode(self.name, forKey: .name)
        try container.encode(self.note, forKey: .note)
        try container.encode(self.type, forKey: .type)
    }
}
