//
//  MCChallengeRatingPicker.swift
//  MonsterCards
//
//  Created by Tom Hicks on 3/24/21.
//

import SwiftUI

struct MCChallengeRatingPicker: View {
    var label: String = ""
    var value: Binding<ChallengeRating>
    var body: some View {
        VStack(alignment: .leading) {
            Text(label)
                .font(.caption2)
            Picker(
                selection: value,
                label: Text(value.wrappedValue.displayName)) {
                ForEach(ChallengeRating.allCases) {abilityScore in
                        Text(abilityScore.displayName).tag(abilityScore)
                    }
                }
                .pickerStyle(MenuPickerStyle())
        }
    }
}

struct MCChallengeRatingPicker_Previews: PreviewProvider {
    static var previews: some View {
        MCChallengeRatingPicker(
            label: "Rating",
            value: .constant(ChallengeRating.ten))
    }
}
