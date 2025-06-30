//
//  ChallengeRating.swift
//  MonsterCards
//
//  Created by Tom Hicks on 3/21/21.
//

import Foundation

enum ChallengeRating: String, CaseIterable, Identifiable {
    case custom = "Custom"
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
        
    var id: ChallengeRating { self }
    
    var displayName: String {
        switch(self) {
        case .custom:
            return "Custom"
        case .zero:
            return "0 (10 XP)"
        case .oneEighth:
            return "1/8 (25 XP)"
        case .oneQuarter:
            return "1/4 (50 XP)"
        case .oneHalf:
            return "1/2 (100 XP)"
        case .one:
            return "1 (200 XP)"
        case .two:
            return "2 (450 XP)"
        case .three:
            return "3 (700 XP)"
        case .four:
            return "4 (1,100 XP)"
        case .five:
            return "5 (1,800 XP)"
        case .six:
            return "6 (2,300 XP)"
        case .seven:
            return "7 (2,900 XP)"
        case .eight:
            return "8 (3,900 XP)"
        case .nine:
            return "9 (5,000 XP)"
        case .ten:
            return "10 (5,900 XP)"
        case .eleven:
            return "11 (7,200 XP)"
        case .twelve:
            return "12 (8,400 XP)"
        case .thirteen:
            return "13 (10,000 XP)"
        case .fourteen:
            return "14 (11,500 XP)"
        case .fifteen:
            return "15 (13,000 XP)"
        case .sixteen:
            return "16 (15,000 XP)"
        case .seventeen:
            return "17 (18,000 XP)"
        case .eighteen:
            return "18 (20,000 XP)"
        case .nineteen:
            return "19 (22,000 XP)"
        case .twenty:
            return "20 (25,000 XP)"
        case .twentyOne:
            return "21 (33,000 XP)"
        case .twentyTwo:
            return "22 (41,000 XP)"
        case .twentyThree:
            return "23 (50,000 XP)"
        case .twentyFour:
            return "24 (62,000 XP)"
        case .twentyFive:
            return "25 (75,000 XP)"
        case .twentySix:
            return "26 (90,000 XP)"
        case .twentySeven:
            return "27 (105,000 XP)"
        case .twentyEight:
            return "28 (120,000 XP)"
        case .twentyNine:
            return "29 (135,000 XP)"
        case .thirty:
            return "30 (155,000 XP)"
        }
    }
}
