package com.majinnaibu.monstercards.data.converters;

import androidx.room.TypeConverter;

import com.majinnaibu.monstercards.data.enums.ChallengeRating;

public class ChallengeRatingConverter {

    @TypeConverter
    public static String fromChallengeRating(ChallengeRating challengeRating) {
        return challengeRating.stringValue;
    }

    @TypeConverter
    public static ChallengeRating challengeRatingFromStringValue(String stringValue) {
        return ChallengeRating.valueOfString(stringValue);
    }
}
