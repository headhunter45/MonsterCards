//
//  SavingThrowDTO.swift
//  MonsterCards
//
//  Created by Tom Hicks on 3/28/21.
//

import Foundation

struct SavingThrowDTO {
    var name: String
    var order: Int
}

private enum SavingThrowDTOCodingKeys: String, CodingKey {
    case name = "name"
    case order = "order"
}

extension SavingThrowDTO: Codable {
    
    init(from decoder: Decoder) throws {
        
        let container = try decoder.container(keyedBy: SavingThrowDTOCodingKeys.self)
        self.name = (try? container.decode(String.self, forKey: .name)) ?? ""
        self.order = (try? container.decode(Int.self, forKey: .order)) ?? 0
    }
    
    func encode(to encoder: Encoder) throws {
    
        var container = encoder.container(keyedBy: SavingThrowDTOCodingKeys.self)
        try container.encode(self.name, forKey: .name)
        try container.encode(self.order, forKey: .order)
    }
}
