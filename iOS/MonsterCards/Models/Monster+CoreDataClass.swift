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
   // MARK: Armor
    
    var armorTypeEnum: ArmorType {
        get {
            return ArmorType.init(rawValue: armorType ?? "none") ?? .none
        }
        set {
            armorType =  newValue.rawValue
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
    

    // MARK: Saving Throws
    
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
    
    // MARK: End

}




