//
//  LanguageDTO.swift
//  MonsterCards
//
//  Created by Tom Hicks on 3/28/21.
//

import Foundation

struct LanguageDTO {
    var name: String
    var speaks: Bool
}

private enum LanguageDTOCodingKeys: String, CodingKey {
    case name = "name"
    case speaks = "speaks"
}

extension LanguageDTO: Codable {
        
    init(from decoder: Decoder) throws {
        
        let container = try decoder.container(keyedBy: LanguageDTOCodingKeys.self)
        self.name = (try? container.decode(String.self, forKey: .name)) ?? ""
        self.speaks = (try? container.decode(Bool.self, forKey: .speaks)) ?? false
    }
    
    func encode(to encoder: Encoder) throws {
    
        var container = encoder.container(keyedBy: LanguageDTOCodingKeys.self)
        try container.encode(self.name, forKey: .name)
        try container.encode(self.speaks, forKey: .speaks)
    }
}
