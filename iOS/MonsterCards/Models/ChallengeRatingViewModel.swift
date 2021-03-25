//
//  ChallengeRatingViewModel.swift
//  MonsterCards
//
//  Created by Tom Hicks on 3/24/21.
//

import Foundation

class ChallengeRatingViewModel: ObservableObject/*, Comparable*/, Identifiable {
    
    func encode(with coder: NSCoder) {
        coder.encode(self.rating.rawValue, forKey: "rating")

    }
        
    static func == (lhs: ChallengeRatingViewModel, rhs: ChallengeRatingViewModel) -> Bool {
         lhs.rating == rhs.rating &&
            lhs.customText == rhs.customText &&
            lhs.customProficiencyBonus == rhs.customProficiencyBonus
    }
    
    @Published var rating: ChallengeRating
    @Published var customText: String
    @Published var customProficiencyBonus: Int64

    init(
        _ rating: ChallengeRating = .one,
        _ customText: String = "",
        _ customProficiencyBonus: Int64 = 0
    ) {
        self.rating = rating
        self.customText = customText
        self.customProficiencyBonus = customProficiencyBonus
    }
    
    init(
        _ rating: String = ChallengeRating.one.rawValue,
        _ customText: String = "",
        _ customProficiencyBonus: Int64 = 0
    ) {
        self.rating = ChallengeRating(rawValue: rating) ?? .one
        self.customText = customText
        self.customProficiencyBonus = customProficiencyBonus
    }
}
