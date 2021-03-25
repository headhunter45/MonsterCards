//
//  EditChallengeRating.swift
//  MonsterCards
//
//  Created by Tom Hicks on 3/24/21.
//

import SwiftUI

struct EditChallengeRating: View {
    @ObservedObject var viewModel: MonsterViewModel
    
    var body: some View {
        let isUsingCustomProficiencyBonus = viewModel.challengeRating == ChallengeRating.custom
        
        VStack(alignment: .leading) {
            MCChallengeRatingPicker(
                label: "Rating",
                value: $viewModel.challengeRating)
            
            MCTextField(
                label: "Custom Text",
                value: $viewModel.customChallengeRating)
                .disabled(!isUsingCustomProficiencyBonus)

            MCStepperField(
                label: "Custom Proficiency Bonus",
                value: $viewModel.customProficiencyBonus)
                .disabled(!isUsingCustomProficiencyBonus)
            Spacer()
        }
        .padding()
    }
}

struct EditChallengeRating_Previews: PreviewProvider {
    static var previews: some View {
        let viewModel = MonsterViewModel()
        EditChallengeRating(viewModel: viewModel)
    }
}
