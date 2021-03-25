//
//  StringHelper.swift
//  MonsterCards
//
//  Created by Tom Hicks on 3/21/21.
//

import Foundation

class StringHelper {
    static func oxfordJoin(
        _ strings: [String],
        _ separator: String = ", ",
        _ lastSeparator: String = ", and ",
        _ onlySeparator: String =  " and "
    ) -> String {
        let numStrings = strings.count
        if (numStrings < 1) {
            return "";
        } else if (numStrings == 2) {
            return strings[0] + onlySeparator + strings[1]
        } else {
            var joined = ""
            var index = 0
            let lastIndex = numStrings - 1
            
            strings.forEach {
                if index > 0 && index < lastIndex {
                    joined.append(separator)
                } else if (index > 0 && index >= lastIndex) {
                    joined.append(lastSeparator)
                }
                joined.append($0)
                index = index + 1
            }
            
            return joined
        }
    }
    
    static func safeContainsCaseInsensitive(_ str: String?, _ match: String) -> Bool {
        if (str == nil) {
            return false
        }
        
        return str!.containsCaseInsensitive(match)
    }
    
    static func safeEqualsIgnoreCase(_ str: String?, _ match: String) -> Bool {
        if (str == nil) {
            return false
        }
        
        return str!.lowercased() == match.lowercased()
    }
}
