//
//  SkillViewModel.swift
//  MonsterCards
//
//  Created by Tom Hicks on 1/18/21.
//

import Foundation

class SkillViewModel: ObservableObject {
    
    init(_ rawSkill: Skill?) {
        if (rawSkill != nil) {
            self.rawSkill = rawSkill
            _name = rawSkill!.name ?? ""
        } else {
            _name = ""
        }
    }
    
    var rawSkill: Skill?
    
    private var _name: String = ""
    var name: String {
        get {
            return _name
        }
        set {
            if (newValue != _name) {
                _name = newValue
                // Notify changed name
            }
            if (rawSkill != nil) {
                rawSkill!.name = newValue
            }
        }
    }
}
