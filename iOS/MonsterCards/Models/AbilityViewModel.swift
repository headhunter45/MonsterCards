//
//  AbilityViewModel.swift
//  MonsterCards
//
//  Created by Tom Hicks on 3/25/21.
//

import Foundation

public class AbilityViewModel: NSObject, ObservableObject, Identifiable, NSSecureCoding {
    public static var supportsSecureCoding = true
    
    public func encode(with coder: NSCoder) {
        coder.encode(self.name, forKey: "name")
        coder.encode(self.abilityDescription, forKey: "abilityDescription")
    }
    
    public required init?(coder: NSCoder) {
        self.name = coder.decodeObject(of: NSString.self, forKey: "name")! as String
        self.abilityDescription = coder.decodeObject(of: NSString.self, forKey: "abilityDescription")! as String
    }
    
    @Published public var name: String
    @Published public var abilityDescription: String
    
    public init(_ name: String = "", _ abilityDescription: String = "") {
        self.name = name
        self.abilityDescription = abilityDescription
    }
    
    public var fullText: String {
        get {
            return String(format: "___%@:___ %@", name, abilityDescription)
        }
    }
    
    func renderedText(_ monster: MonsterViewModel) -> String {
        let strSave = monster.strengthModifier + monster.proficiencyBonus + 8
        let dexSave = monster.dexterityModifier + monster.proficiencyBonus + 8
        let conSave = monster.constitutionModifier + monster.proficiencyBonus + 8
        let intSave = monster.intelligenceModifier + monster.proficiencyBonus + 8
        let wisSave = monster.wisdomModifier + monster.proficiencyBonus + 8
        let chaSave = monster.charismaModifier + monster.proficiencyBonus + 8
        let strAttack = monster.strengthModifier + monster.proficiencyBonus
        let dexAttack = monster.dexterityModifier + monster.proficiencyBonus
        let conAttack = monster.constitutionModifier + monster.proficiencyBonus
        let intAttack = monster.intelligenceModifier + monster.proficiencyBonus
        let wisAttack = monster.wisdomModifier + monster.proficiencyBonus
        let chaAttack = monster.charismaModifier + monster.proficiencyBonus
        
        // TODO: find the other options and implement them [WIS], [WIS STAT], [WIS DMG], [WIS STAT 1d12]
        
        let finalText = fullText
            .replacingOccurrences(of: "[STR SAVE]", with: String(strSave))
            .replacingOccurrences(of: "[DEX SAVE]", with: String(dexSave))
            .replacingOccurrences(of: "[CON SAVE]", with: String(conSave))
            .replacingOccurrences(of: "[INT SAVE]", with: String(intSave))
            .replacingOccurrences(of: "[WIS SAVE]", with: String(wisSave))
            .replacingOccurrences(of: "[CHA SAVE]", with: String(chaSave))
            .replacingOccurrences(of: "[STR ATK]", with: String(strAttack))
            .replacingOccurrences(of: "[DEX ATK]", with: String(dexAttack))
            .replacingOccurrences(of: "[CON ATK]", with: String(conAttack))
            .replacingOccurrences(of: "[INT ATK]", with: String(intAttack))
            .replacingOccurrences(of: "[WIS ATK]", with: String(wisAttack))
            .replacingOccurrences(of: "[CHA ATK]", with: String(chaAttack))
        
        return finalText
    }
}

extension AbilityViewModel: Comparable {
    public static func < (lhs: AbilityViewModel, rhs: AbilityViewModel) -> Bool {
        lhs.name < rhs.name
    }
    
    public static func == (lhs: AbilityViewModel, rhs: AbilityViewModel) -> Bool {
        lhs.name == rhs.name &&
        lhs.abilityDescription == rhs.abilityDescription
    }
}

// TODO: figure out how to add this to the set of known transformers so it will work with transformer set to NSSecureUnarchiveFromDataTransformerName
@objc(AbilityViewModelValueTransformer)
public final class AbilityViewModelValueTransformer: ValueTransformer {
    override public class func transformedValueClass() -> AnyClass {
        return NSArray.self
    }

    override public class func allowsReverseTransformation() -> Bool {
        return true
    }

    override public func transformedValue(_ value: Any?) -> Any? {
        guard let language = value as? NSArray else { return nil }

        do {
            let data = try NSKeyedArchiver.archivedData(withRootObject: language, requiringSecureCoding: true)
            return data
        } catch {
            assertionFailure("Failed to transform `AbilityViewModel` to `Data`")
            return nil
        }
    }

    override public func reverseTransformedValue(_ value: Any?) -> Any? {
        guard let data = value as? NSData else { return nil }

        do {
            let language = try NSKeyedUnarchiver.unarchivedArrayOfObjects(ofClass: AbilityViewModel.self, from: data as Data)
            return language
        } catch {
            assertionFailure("Failed to transform `Data` to `AbilityViewModel`")
            return nil
        }
    }
}
