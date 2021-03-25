//
//  LanguageViewModel.swift
//  MonsterCards
//
//  Created by Tom Hicks on 3/24/21.
//

import Foundation

// TODO: split this into separate Model and ViewModel classes later.
public class LanguageViewModel : NSObject, ObservableObject, Comparable, Identifiable, NSSecureCoding {
    public static var supportsSecureCoding = true
    
    public func encode(with coder: NSCoder) {
        coder.encode(self.name, forKey: "name")
        coder.encode(self.speaks, forKey: "speaks")
    }
    
    public required init?(coder: NSCoder) {
        self.name = coder.decodeObject(of: NSString.self, forKey: "name")! as String
        self.speaks = coder.decodeBool(forKey: "speaks")
    }
    
    public static func < (lhs: LanguageViewModel, rhs: LanguageViewModel) -> Bool {
        lhs.name < rhs.name
    }
    
    public static func == (lhs: LanguageViewModel, rhs: LanguageViewModel) -> Bool {
        lhs.name == rhs.name &&
            lhs.speaks == rhs.speaks
    }
    
    @Published var name: String
    @Published var speaks: Bool
    
    init(
        _ name: String = "",
        _ speaks: Bool = true
    ) {
        self.name = name
        self.speaks = speaks
    }
}

// TODO: figure out how to add this to the set of known transformers so it will work with transformer set to NSSecureUnarchiveFromDataTransformerName
@objc(LanguageViewModelValueTransformer)
public final class LanguageViewModelValueTransformer: ValueTransformer {
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
            assertionFailure("Failed to transform `LanguageViewModel` to `Data`")
            return nil
        }
    }

    override public func reverseTransformedValue(_ value: Any?) -> Any? {
        guard let data = value as? NSData else { return nil }

        do {
            let language = try NSKeyedUnarchiver.unarchivedArrayOfObjects(ofClass: LanguageViewModel.self, from: data as Data)
            return language
        } catch {
            assertionFailure("Failed to transform `Data` to `LanguageViewModel`")
            return nil
        }
    }
}
