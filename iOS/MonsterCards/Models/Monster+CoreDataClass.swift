//
//  Monster+CoreDataClass.swift
//  MonsterCards
//
//  Created by Tom Hicks on 1/15/21.
//
//

import Foundation
import CoreData

@objc(Monster)
public class Monster: NSManagedObject {
    convenience init(context: NSManagedObjectContext, name: String, size: String, type: String, subtype: String, alignment: String) {
        self.init(context:context)
        self.name = name;
        self.size = size;
        self.type = type;
        self.subtype = subtype;
        self.alignment = alignment;
    }
    
    // hasCustomProficiencyBonus
    // telepathy int in json but seems like it should be a bool
    
    let kBaseArmorClassUnarmored = 10;
    let kBaseArmorClassMageArmor = 13;
    let kBaseArmorClassPadded = 11;
    let kBaseArmorClassLeather = 11;
    let kBaseArmorClassStudded = 12;
    let kBaseArmorClassHide = 12;
    let kBaseArmorClassChainShirt = 13;
    let kBaseArmorClassScaleMail = 14;
    let kBaseArmorClassBreastplate = 14;
    let kBaseArmorClassHalfPlate = 15;
    let kBaseArmorClassRingMail = 14;
    let kBaseArmorClassChainMail = 16;
    let kBaseArmorClassSplintMail = 17;
    let kBaseArmorClassPlate = 18;
    
    // MARK: Basic Info
    
    var meta: String {
        get {
            // size type (subtype) alignment
            var parts: [String] = []
            
            if (!(self.size?.isEmpty ?? false)) {
                parts.append(self.size!)
            }
            
            if (!(self.type?.isEmpty ?? false)) {
                parts.append(self.type!)
            }
            
            if (!(self.subtype?.isEmpty ?? false)) {
                parts.append(String.init(format: "(%@)", arguments: [self.subtype!]))
            }
            
            if (!(self.alignment?.isEmpty ?? false)) {
                parts.append(self.alignment!)
            }
            
            return parts.joined(separator: " ")
        }
    }
    
    var sizeEnum: SizeType {
        get {
            return SizeType.init(rawValue: size ?? "") ?? .medium
        }
        set {
            size = newValue.rawValue
        }
    }
    
    var hitPoints: String {
        get {
            if (hasCustomHP) {
                return customHP ?? "";
            } else {
                let dieSize = Double(Monster.hitDieForSize(sizeEnum))
                let conMod = Double(constitutionModifier)
                let level1HP = Double(dieSize + conMod)
                let extraLevels = Double(hitDice - 1)
                let levelNHP = (dieSize + 1.0) / 2.0 + conMod
                let extraLevelsHP = extraLevels * levelNHP
                let hpTotal = Int(ceil(level1HP + extraLevelsHP))
//                let hpTotal = Int(max(1, ceil(dieSize + conMod + (hitDice - 1) * ((dieSize + 1) / 2.0 + conMod))))
                let conBonus = Int(conMod) * Int(hitDice)
                return String(format: "%d (%dd%d%+d)", hpTotal, hitDice, dieSize, conBonus)
            }
        }
    }
    
    var speed: String {
        get {
            if (hasCustomSpeed) {
                return customSpeed ?? ""
            } else {
                var parts: [String] = []
                
                if (baseSpeed > 0) {
                    parts.append("\(baseSpeed) ft.")
                }
                if (burrowSpeed > 0) {
                    parts.append("burrow \(burrowSpeed) ft.")
                }
                if (climbSpeed > 0) {
                    parts.append("climb \(climbSpeed) ft.")
                }
                if (flySpeed > 0) {
                    parts.append("fly \(flySpeed) ft.\(canHover ? " (hover)": "")")
                }
                if (swimSpeed > 0) {
                    parts.append("swim \(swimSpeed) ft.")
                }
                
                return parts.joined(separator: ", ")
            }
        }
    }
    
    class func hitDieForSize(_ size: SizeType) -> Int {
        switch size {
            case .tiny: return 4
            case .small: return 6
            case .medium: return 8
            case .large: return 10
            case .huge: return 12
            case .gargantuan: return 20
        }
    }

    // MARK: Ability Scores
    class func abilityModifierForScore(_ score: Int) -> Int {
        return Int(floor(Double((score - 10)) / 2.0))
    }
    
    var strengthModifier: Int {
        get {
            return Monster.abilityModifierForScore(Int(strengthScore))
        }
    }
    
    var dexterityModifier: Int {
        get {
            return Monster.abilityModifierForScore(Int(dexterityScore))
        }
    }
    
    var constitutionModifier: Int {
        get {
            return Monster.abilityModifierForScore(Int(constitutionScore))
        }
    }
    
    var intelligenceModifier: Int {
        get {
            return Monster.abilityModifierForScore(Int(intelligenceScore))
        }
    }
    
    var wisdomModifier: Int {
        get {
            return Monster.abilityModifierForScore(Int(wisdomScore))
        }
    }
    
    var charismaModifier: Int {
        get {
            return Monster.abilityModifierForScore(Int(charismaScore))
        }
    }
    
    var strengthDescription: String {
        get {
            return String(format: "%d (%+d)", strengthScore, strengthModifier)
        }
    }
    
    var dexterityDescription: String {
        get {
            return String(format: "%d (%+d)", dexterityScore, dexterityModifier)
        }
    }
    
    var constitutionDescription: String {
        get {
            return String(format: "%d (%+d)", constitutionScore, constitutionModifier)
        }
    }
    
    var intelligenceDescription: String {
        get {
            return String(format: "%d (%+d)", intelligenceScore, intelligenceModifier)
        }
    }
    
    var wisdomDescription: String {
        get {
            return String(format: "%d (%+d)", wisdomScore, wisdomModifier)
        }
    }
    
    var charismaDescription: String {
        get {
            return String(format: "%d (%+d)", charismaScore, charismaModifier)
        }
    }
    
    // MARK: Armor
    
    var armorTypeEnum: ArmorType {
        get {
            return ArmorType.init(rawValue: armorType ?? "none") ?? .none
        }
        set {
            armorType =  newValue.rawValue
        }
    }
    
    var armorClassDescription: String {
        get {
            let hasShield = shieldBonus != 0
            var armorClassTotal = 0
            if (armorTypeEnum == ArmorType.none) {
                // 10 + dexMod + 2 for shieldBonus "15" or "17 (shield)"
                armorClassTotal = kBaseArmorClassUnarmored + dexterityModifier + Int(shieldBonus)
                return  "\(armorClassTotal)\(hasShield ? " (shield)" : "")"
            } else if (armorTypeEnum == .naturalArmor) {
                // 10 + dexMod + naturalArmorBonus + 2 for shieldBonus "16 (natural armor)" or "18 (natural armor, shield)"
                armorClassTotal = kBaseArmorClassUnarmored + dexterityModifier + Int(naturalArmorBonus) + Int(shieldBonus)
                return  "\(armorClassTotal) (natural armor\(hasShield ? " (shield)" : ""))"
            } else if (armorTypeEnum == .mageArmor) {
                // 10 + dexMod + 2 for shield + 3 for mage armor "15 (18 with mage armor)" or 17 (shield, 20 with mage armor)
                armorClassTotal = kBaseArmorClassUnarmored + dexterityModifier + Int(shieldBonus)
                let acWithMageArmor = kBaseArmorClassMageArmor + dexterityModifier + Int(shieldBonus)
                return String(format: "%d (%@%d with mage armor)", armorClassTotal, (hasShield ? "shield, " : ""), acWithMageArmor)
            } else if (armorTypeEnum == .padded) {
                // 11 + dexMod + 2 for shield "18 (padded armor, shield)"
                armorClassTotal = kBaseArmorClassPadded + dexterityModifier + Int(shieldBonus)
                return String(format: "%d (padded%@)", armorClassTotal, (hasShield ? "shield, " : ""))
            } else if (armorTypeEnum == .leather) {
                // 11 + dexMod + 2 for shield "18 (leather, shield)"
                armorClassTotal = kBaseArmorClassLeather + dexterityModifier + Int(shieldBonus)
                return String(format:"%d (leather%@)", armorClassTotal, (hasShield ? "shield, " : ""))
            } else if (armorTypeEnum == .studdedLeather) {
                // 12 + dexMod +2 for shield "17 (studded leather)"
                armorClassTotal = kBaseArmorClassStudded + dexterityModifier + Int(shieldBonus)
                return String(format: "%d (studded leather%@)", armorClassTotal, (hasShield ? "shield, " : ""))
            } else if (armorTypeEnum == .hide) {
                // 12 + Min(2, dexMod) + 2 for shield "12 (hide armor)"
                armorClassTotal = kBaseArmorClassHide + min(2, dexterityModifier) + Int(shieldBonus)
                return String(format: "%d (hide%@)", armorClassTotal, (hasShield ? ", shield" : ""))
            } else if (armorTypeEnum == .chainShirt) {
                // 13 + Min(2, dexMod) + 2 for shield "12 (chain shirt)"
                armorClassTotal = kBaseArmorClassChainShirt + min(2, dexterityModifier) + Int(shieldBonus)
                return String(format: "%d (chain shirt%@)", armorClassTotal, (hasShield ? ", shield" : ""))
            } else if (armorTypeEnum == .scaleMail) {
                // 14 + Min(2, dexMod) + 2 for shield "14 (scale mail)"
                armorClassTotal = kBaseArmorClassScaleMail + min(2, dexterityModifier) + Int(shieldBonus)
                return String(format: "%d (scale mail%@)", armorClassTotal, (hasShield ? ", shield" : ""))
            } else if (armorTypeEnum == .breastplate) {
                // 14 + Min(2, dexMod) + 2 for shield "16 (breastplate)"
                armorClassTotal = kBaseArmorClassBreastplate + min(2, dexterityModifier) + Int(shieldBonus)
                return String(format: "%d (breastplate%@)", armorClassTotal, (hasShield ? ", shield" : ""))
            } else if (armorTypeEnum == .halfPlate) {
                // 15 + Min(2, dexMod) + 2 for shield "17 (half plate)"
                armorClassTotal = kBaseArmorClassHalfPlate + min(2, dexterityModifier) + Int(shieldBonus)
                return String(format: "%d (half plate%@)", armorClassTotal, (hasShield ? ", shield" : ""))
            } else if (armorTypeEnum == .ringMail) {
                // 14 + 2 for shield "14 (ring mail)
                armorClassTotal = kBaseArmorClassRingMail + Int(shieldBonus)
                return String(format: "%d (ring mail%@)", armorClassTotal, (hasShield ? ", shield" : ""))
            } else if (armorTypeEnum == .chainMail) {
                // 16 + 2 for shield "16 (chain mail)"
                armorClassTotal = kBaseArmorClassChainMail + Int(shieldBonus)
                return String(format: "%d (chain mail%@)", armorClassTotal, (hasShield ? ", shield" : ""))
            } else if (armorTypeEnum == .splintMail) {
                // 17 + 2 for shield "17 (splint)"
                armorClassTotal = kBaseArmorClassSplintMail + Int(shieldBonus)
                return String(format: "%d (splint%@)", armorClassTotal, (hasShield ? ", shield" : ""))
            } else if (armorTypeEnum == .plateMail) {
                // 18 + 2 for shield "18 (plate)"
                armorClassTotal = kBaseArmorClassPlate + Int(shieldBonus)
                return String(format: "%d (plate%@)", armorClassTotal, (hasShield ? ", shield" : ""))
            } else if (armorTypeEnum == .other) {
                // pure string value shield check does nothing just copies the string from otherArmorDesc
                return otherArmorDescription ?? "";
            } else {
                return ""
            }
        }
    }
    // MARK: Challenge Rating / Proficiency Bonus
    
    var challengeRatingEnum: ChallengeRating {
        get {
            return ChallengeRating.init(rawValue: challengeRating ?? "1") ?? .one
        }
        set {
            challengeRating = newValue.rawValue
        }
    }
    
    var proficiencyBonus: Int {
        return 5
//        switch challengeRatingEnum {
//        case .custom:
//            return Int(customProficiencyBonus)
//        case .zero:
//            fallthrough
//        case .oneEighth:
//            fallthrough
//        case .oneQuarter:
//            fallthrough
//        case .oneHalf:
//            fallthrough
//        case .one:
//            fallthrough
//        case .two:
//            fallthrough
//        case .three:
//            fallthrough
//        case .four:
//            return 2
//        case .five:
//            fallthrough
//        case .six:
//            fallthrough
//        case .seven:
//            fallthrough
//        case .eight:
//            return 3
//        case .nine:
//            fallthrough
//        case .ten:
//            fallthrough
//        case .eleven:
//            fallthrough
//        case .twelve:
//            return 4
//        case .thirteen:
//            fallthrough
//        case .fourteen:
//            fallthrough
//        case .fifteen:
//            fallthrough
//        case .sixteen:
//            return 5
//        case .seventeen:
//            fallthrough
//        case .eighteen:
//            fallthrough
//        case .nineteen:
//            fallthrough
//        case .twenty:
//            return 6
//        case .twentyOne:
//            fallthrough
//        case .twentyTwo:
//            fallthrough
//        case .twentyThree:
//            fallthrough
//        case .twentyFour:
//            return 7
//        case .twentyFive:
//            fallthrough
//        case .twentySix:
//            fallthrough
//        case .twentySeven:
//            fallthrough
//        case .twentyEight:
//            return 8
//        case .twentyNine:
//            fallthrough
//        case .thirty:
//            return 9
//        }
    }
    
    func proficiencyBonusForType(_ profType: ProficiencyType) -> Int {
        switch profType {
        case .none:
            return 0
        case .proficient:
            return proficiencyBonus
        case .expertise:
            return proficiencyBonus * 2
        }
    }

    // MARK: Saving Throws
    
    var savingThrowsDescription: String {
        get {
            // TODO: port from objective-c
            var parts: [String] = []
            var name: String
            var advantage: String
            var bonus: Int
            
            if (strengthSavingThrowAdvantageEnum != .none || strengthSavingThrowProficiencyEnum != .none) {
                name = "Strength"
                bonus = strengthModifier + proficiencyBonusForType(strengthSavingThrowProficiencyEnum)
                advantage = Monster.advantageLabelStringForType(strengthSavingThrowAdvantageEnum)
                if (!advantage.isEmpty) {
                    advantage = " " + advantage
                }
                parts.append(String(format: "%@ %+d%@", name, bonus, advantage))
            }
            
            if (dexteritySavingThrowAdvantageEnum != .none || dexteritySavingThrowProficiencyEnum != .none) {
                name = "Dexterity"
                bonus = dexterityModifier + proficiencyBonusForType(dexteritySavingThrowProficiencyEnum)
                advantage = Monster.advantageLabelStringForType(dexteritySavingThrowAdvantageEnum)
                if (!advantage.isEmpty) {
                    advantage = " " + advantage
                }
                parts.append(String(format: "%@ %+d%@", name, bonus, advantage))
            }
            
            if (constitutionSavingThrowAdvantageEnum != .none || constitutionSavingThrowProficiencyEnum != .none) {
                name = "Constitution"
                bonus = constitutionModifier + proficiencyBonusForType(constitutionSavingThrowProficiencyEnum)
                advantage = Monster.advantageLabelStringForType(constitutionSavingThrowAdvantageEnum)
                if (!advantage.isEmpty) {
                    advantage = " " + advantage
                }
                parts.append(String(format: "%@ %+d%@", name, bonus, advantage))
            }
            
            if (intelligenceSavingThrowAdvantageEnum != .none || intelligenceSavingThrowProficiencyEnum != .none) {
                name = "Intelligence"
                bonus = intelligenceModifier + proficiencyBonusForType(intelligenceSavingThrowProficiencyEnum)
                advantage = Monster.advantageLabelStringForType(intelligenceSavingThrowAdvantageEnum)
                if (!advantage.isEmpty) {
                    advantage = " " + advantage
                }
                parts.append(String(format: "%@ %+d%@", name, bonus, advantage))
            }
            
            if (wisdomSavingThrowAdvantageEnum != .none || wisdomSavingThrowProficiencyEnum != .none) {
                name = "Wisdom"
                bonus = wisdomModifier + proficiencyBonusForType(wisdomSavingThrowProficiencyEnum)
                advantage = Monster.advantageLabelStringForType(wisdomSavingThrowAdvantageEnum)
                if (!advantage.isEmpty) {
                    advantage = " " + advantage
                }
                parts.append(String(format: "%@ %+d%@", name, bonus, advantage))
            }
            
            if (charismaSavingThrowAdvantageEnum != .none || charismaSavingThrowProficiencyEnum != .none) {
                name = "Charisma"
                bonus = charismaModifier + proficiencyBonusForType(charismaSavingThrowProficiencyEnum)
                advantage = Monster.advantageLabelStringForType(charismaSavingThrowAdvantageEnum)
                if (!advantage.isEmpty) {
                    advantage = " " + advantage
                }
                parts.append(String(format: "%@ %+d%@", name, bonus, advantage))
            }
            
            return parts.joined(separator: ", ")
        }
    }
    
    var strengthSavingThrowProficiencyEnum: ProficiencyType {
        get {
            return ProficiencyType.init(rawValue: strengthSavingThrowProficiency ?? "") ?? .none
        }
        set {
            strengthSavingThrowProficiency = newValue.rawValue
        }
    }

    var strengthSavingThrowAdvantageEnum: AdvantageType {
        get {
            return AdvantageType.init(rawValue: strengthSavingThrowAdvantage ?? "") ?? .none
        }
        set {
            strengthSavingThrowAdvantage = newValue.rawValue
        }
    }
    
    var dexteritySavingThrowProficiencyEnum: ProficiencyType {
        get {
            return ProficiencyType.init(rawValue: dexteritySavingThrowProficiency ?? "") ?? .none
        }
        set {
            dexteritySavingThrowProficiency = newValue.rawValue
        }
    }

    var dexteritySavingThrowAdvantageEnum: AdvantageType {
        get {
            return AdvantageType.init(rawValue: dexteritySavingThrowAdvantage ?? "") ?? .none
        }
        set {
            dexteritySavingThrowAdvantage = newValue.rawValue
        }
    }
    
    var constitutionSavingThrowProficiencyEnum: ProficiencyType {
        get {
            return ProficiencyType.init(rawValue: constitutionSavingThrowProficiency ?? "") ?? .none
        }
        set {
            constitutionSavingThrowProficiency = newValue.rawValue
        }
    }

    var constitutionSavingThrowAdvantageEnum: AdvantageType {
        get {
            return AdvantageType.init(rawValue: constitutionSavingThrowAdvantage ?? "") ?? .none
        }
        set {
            constitutionSavingThrowAdvantage = newValue.rawValue
        }
    }
    
    var intelligenceSavingThrowProficiencyEnum: ProficiencyType {
        get {
            return ProficiencyType.init(rawValue: intelligenceSavingThrowProficiency ?? "") ?? .none
        }
        set {
            intelligenceSavingThrowProficiency = newValue.rawValue
        }
    }

    var intelligenceSavingThrowAdvantageEnum: AdvantageType {
        get {
            return AdvantageType.init(rawValue: intelligenceSavingThrowAdvantage ?? "") ?? .none
        }
        set {
            intelligenceSavingThrowAdvantage = newValue.rawValue
        }
    }
    
    var wisdomSavingThrowProficiencyEnum: ProficiencyType {
        get {
            return ProficiencyType.init(rawValue: wisdomSavingThrowProficiency ?? "") ?? .none
        }
        set {
            wisdomSavingThrowProficiency = newValue.rawValue
        }
    }

    var wisdomSavingThrowAdvantageEnum: AdvantageType {
        get {
            return AdvantageType.init(rawValue: wisdomSavingThrowAdvantage ?? "") ?? .none
        }
        set {
            wisdomSavingThrowAdvantage = newValue.rawValue
        }
    }
    
    var charismaSavingThrowProficiencyEnum: ProficiencyType {
        get {
            return ProficiencyType.init(rawValue: charismaSavingThrowProficiency ?? "") ?? .none
        }
        set {
            charismaSavingThrowProficiency = newValue.rawValue
        }
    }

    var charismaSavingThrowAdvantageEnum: AdvantageType {
        get {
            return AdvantageType.init(rawValue: charismaSavingThrowAdvantage ?? "") ?? .none
        }
        set {
            charismaSavingThrowAdvantage = newValue.rawValue
        }
    }
    

    // MARK: Misc Helpers

    class func advantageLabelStringForType(_ advType: AdvantageType) -> String {
        switch advType {
        case .none:
            return ""
        case .advantage:
            return "(A)"
        case .disadvantage:
            return "(D)"
        }
    }
    
    // MARK: End

}

enum ArmorType: String, CaseIterable, Identifiable {
    case none = "none"
    case naturalArmor = "natural armor"
    case mageArmor = "mage armor"
    case padded = "padded"
    case leather = "leather"
    case studdedLeather = "studded"
    case hide = "hide"
    case chainShirt = "chain shirt"
    case scaleMail = "scale mail"
    case breastplate = "breastplate"
    case halfPlate = "half plate"
    case ringMail = "ring mail"
    case chainMail = "chain mail"
    case splintMail = "splint"
    case plateMail = "plate"
    case other = "other"
    
    var id: ArmorType { self }
    
    var displayName: String {
        switch self {
            case .none: return "None"
            case .naturalArmor: return "Natural Armor"
            case .mageArmor: return "Mage Armor"
            case .padded: return "Padded"
            case .leather: return "Leather"
            case .studdedLeather: return "Studded Leather"
            case .hide: return "Hide"
            case .chainShirt: return "Chain Shirt"
            case .scaleMail: return "Scale Mail"
            case .breastplate: return "Breastplate"
            case .halfPlate: return "Half Plate"
            case .ringMail: return "Ring Mail"
            case .chainMail: return "Chain Mail"
            case .splintMail: return "Splint Mail"
            case .plateMail: return "Plate Mail"
            case .other: return "Other"
        }
    }
}

enum SizeType: String, CaseIterable, Identifiable {
    case tiny = "tiny"
    case small = "small"
    case medium = "medium"
    case large = "large"
    case huge = "huge"
    case gargantuan = "gargantuan"
    
    var id: SizeType { self }
    
    var displayName: String {
        switch self {
            case .tiny: return "Tiny"
            case .small: return "Small"
            case .medium: return "Medium"
            case .large: return "Large"
            case .huge: return "Huge"
        case .gargantuan: return "gargantuan"
        }
    }
}

enum ChallengeRating: String, CaseIterable, Identifiable {
    case zero = "0"
    case oneEighth = "1/8"
    case oneQuarter = "1/4"
    case oneHalf = "1/2"
    case one = "1"
    case two = "2"
    case three = "3"
    case four = "4"
    case five = "5"
    case six = "6"
    case seven = "7"
    case eight = "8"
    case nine = "9"
    case ten = "10"
    case eleven = "11"
    case twelve = "12"
    case thirteen = "13"
    case fourteen = "14"
    case fifteen = "15"
    case sixteen = "16"
    case seventeen = "17"
    case eighteen = "18"
    case nineteen = "19"
    case twenty = "20"
    case twentyOne = "21"
    case twentyTwo = "22"
    case twentyThree = "23"
    case twentyFour = "24"
    case twentyFive = "25"
    case twentySix = "26"
    case twentySeven = "27"
    case twentyEight = "28"
    case twentyNine = "29"
    case thirty = "30"
    case custom = "*"
        
    var id: ChallengeRating { self }
    
    // Probably don't need this
    var displayName: String {
        return rawValue
    }
}
