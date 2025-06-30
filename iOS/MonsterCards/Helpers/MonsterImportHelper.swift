//
//  MonsterImportHelper.swift
//  MonsterCards
//
//  Created by Tom Hicks on 4/3/21.
//

import Foundation

extension MonsterViewModel {
    func maybeAddSense(_ name: String, _ distance: Int) {
        if (distance > 0) {
            senses.append(StringViewModel("\(name): \(distance) ft."))
        }
    }
}

class MonsterImportHelper {
    static func import5ESBMonster(_ monsterDTO: MonsterDTO) -> MonsterViewModel {
        let monster = MonsterViewModel()
        
        monster.name = monsterDTO.name
        monster.size = monsterDTO.size
        monster.type = monsterDTO.type
        monster.subType = monsterDTO.tag
        monster.alignment = monsterDTO.alignment
        monster.hitDice = Int64(monsterDTO.hitDice)
        monster.armorType = ArmorType(rawValue: monsterDTO.armorName) ?? .none
        monster.hasShield = monsterDTO.shieldBonus != 0
        monster.naturalArmorBonus = Int64(monsterDTO.natArmorBonus)
        monster.customArmor = monsterDTO.otherArmorDesc
        monster.walkSpeed = Int64(monsterDTO.speed)
        monster.burrowSpeed = Int64(monsterDTO.burrowSpeed)
        monster.climbSpeed = Int64(monsterDTO.climbSpeed)
        monster.flySpeed = Int64(monsterDTO.flySpeed)
        monster.swimSpeed = Int64(monsterDTO.swimSpeed)
        monster.canHover = monsterDTO.hover
        monster.hasCustomHP = monsterDTO.customHP
        monster.customHP = monsterDTO.hpText
        monster.hasCustomSpeed = monsterDTO.customSpeed
        monster.customSpeed = monsterDTO.speedDesc
        monster.strengthScore = Int64(monsterDTO.strPoints)
        monster.dexterityScore = Int64(monsterDTO.dexPoints)
        monster.constitutionScore = Int64(monsterDTO.conPoints)
        monster.intelligenceScore = Int64(monsterDTO.intPoints)
        monster.wisdomScore = Int64(monsterDTO.wisPoints)
        monster.charismaScore = Int64(monsterDTO.chaPoints)
        monster.isBlind = monsterDTO.blind
        monster.maybeAddSense("blindsight", monsterDTO.blindsight)
        monster.maybeAddSense("darkvision", monsterDTO.darkvision)
        monster.maybeAddSense("tremorsense", monsterDTO.tremorsense)
        monster.maybeAddSense("turesight", monsterDTO.truesight)
        monster.telepathy = Int64(monsterDTO.telepathy)
        monster.challengeRating = ChallengeRating(rawValue: monsterDTO.cr) ?? ChallengeRating.one
        monster.customChallengeRating = monsterDTO.customCr
        monster.customProficiencyBonus = Int64(monsterDTO.customProf)
        // TODO: Think about adding legendary properties isLegendary, legendariesDescription, isLair, lairDescription, lairDescriptionEnd, isRegional, regionalDescription, regionalDescriptionEnd
        monster.abilities = monsterDTO.abilities.map({AbilityViewModel($0.name, $0.desc)})
        monster.actions = monsterDTO.actions.map({AbilityViewModel($0.name, $0.desc)})
        monster.reactions = monsterDTO.reactions.map({AbilityViewModel($0.name, $0.desc)})
        monster.legendaryActions = monsterDTO.legendaries.map({AbilityViewModel($0.name, $0.desc)})
        monster.lairActions = monsterDTO.lairs.map({AbilityViewModel($0.name, $0.desc)})
        monster.regionalActions = monsterDTO.regionals.map({AbilityViewModel($0.name, $0.desc)})
        monsterDTO.sthrows.forEach({
            switch $0.name {
                case "str":
                    monster.strengthSavingThrowProficiency = .proficient
                case "dex":
                    monster.dexteritySavingThrowProficiency = .proficient
                case "con":
                    monster.constitutionSavingThrowProficiency = .proficient
                case "int":
                    monster.intelligenceSavingThrowProficiency = .proficient
                case "wis":
                    monster.wisdomSavingThrowProficiency = .proficient
                case "cha":
                    monster.charismaSavingThrowProficiency = .proficient
                default:
                    break
            }
        })
        monster.skills = monsterDTO.skills.map({
            // TODO: consider using a lookup table to make fixing missing stats easier
            SkillViewModel(
                $0.name,
                AbilityScore(rawValue: $0.stat) ?? .dexterity,
                $0.note == " (ex)" ? .expertise : .proficient)
        })
        monster.damageImmunities = monsterDTO.damageTypes
            .filter({$0.type == "i" || $0.type == " (Immune)"})
            .map({StringViewModel($0.name)})
        monster.damageResistances = monsterDTO.damageTypes
            .filter({$0.type == "r" || $0.type == " (Resistant)"})
            .map({StringViewModel($0.name)})
        monster.damageVulnerabilities = monsterDTO.damageTypes
            .filter({$0.type == "v" || $0.type == " (Vulnerable)"})
            .map({StringViewModel($0.name)})
        monster.conditionImmunities = monsterDTO.conditions.map({StringViewModel($0.name)})
        monster.languages = monsterDTO.languages
            .map({
                LanguageViewModel($0.name, $0.speaks)
            })
        monster.understandsBut = monsterDTO.understandsBut
        // TODO: add shortName or nickname
        // monster.shortName = monsterDTO.shortName
        
        // TODO: look into what goes in specialdamage and damage
        
        return monster
    }
}
