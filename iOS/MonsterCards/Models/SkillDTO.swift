//
//  SkillDTO.swift
//  MonsterCards
//
//  Created by Tom Hicks on 3/28/21.
//

import Foundation

struct SkillDTO {
    var name: String
    var stat: String
    var note: String
}

private enum SkillDTOCodingKeys: String, CodingKey {
    case name = "name"
    case stat = "stat"
    case note = "note"
}

extension SkillDTO: Decodable {
    
    init(from decoder: Decoder) throws {
        
        let container = try decoder.container(keyedBy: SkillDTOCodingKeys.self)
        self.name = (try? container.decode(String.self, forKey: .name)) ?? ""
        self.note = (try? container.decode(String.self, forKey: .note)) ?? ""
        self.stat = (try? container.decode(String.self, forKey: .stat)) ?? ""
    }
}

extension SkillDTO: Encodable {
    
    func encode(to encoder: Encoder) throws {
    
        var container = encoder.container(keyedBy: SkillDTOCodingKeys.self)
        try container.encode(self.name, forKey: .name)
        try container.encode(self.note, forKey: .note)
        try container.encode(self.stat, forKey: .stat)
    }
}
