//
//  TraitDTO.swift
//  MonsterCards
//
//  Created by Tom Hicks on 3/28/21.
//

import Foundation

struct TraitDTO {
    var name: String
    var note: String
    var desc: String
}

private enum TraitDTOCodingKeys: String, CodingKey {
    case name = "name"
    case note = "note"
    case desc = "desc"
}

extension TraitDTO: Codable {
    
    init(from decoder: Decoder) throws {
        
        let container = try decoder.container(keyedBy: TraitDTOCodingKeys.self)
        self.name = (try? container.decode(String.self, forKey: .name)) ?? ""
        self.note = (try? container.decode(String.self, forKey: .note)) ?? ""
        self.desc = (try? container.decode(String.self, forKey: .desc)) ?? ""
    }
        
    func encode(to encoder: Encoder) throws {
    
        var container = encoder.container(keyedBy: TraitDTOCodingKeys.self)
        try container.encode(self.name, forKey: .name)
        try container.encode(self.note, forKey: .note)
        try container.encode(self.desc, forKey: .desc)
    }
}
