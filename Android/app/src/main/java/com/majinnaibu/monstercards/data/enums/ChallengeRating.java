package com.majinnaibu.monstercards.data.enums;

@SuppressWarnings("unused")
public enum ChallengeRating {
    CUSTOM("custom", "Custom", 0),
    ZERO("zero", "0 (10 XP)", 2),
    ONE_EIGHTH("1/8", "1/8 (25 XP)", 2),
    ONE_QUARTER("1/4", "1/4 (50 XP)", 2),
    ONE_HALF("1/2", "1/2 (100 XP)", 2),
    ONE("1", "1 (200 XP)", 2),
    TWO("2", "2 (450 XP)", 2),
    THREE("3", "3 (700 XP)", 2),
    FOUR("4", "4 (1,100 XP)", 2),
    FIVE("5", "5 (1,800 XP)", 3),
    SIX("6", "6 (2,300 XP)", 3),
    SEVEN("7", "7 (2,900 XP)", 3),
    EIGHT("8", "8 (3,900 XP)", 3),
    NINE("9", "9 (5,000 XP)", 4),
    TEN("10", "10 (5,900 XP)", 4),
    ELEVEN("11", "11 (7,200 XP)", 4),
    TWELVE("12", "12 (8,400 XP)", 4),
    THIRTEEN("13", "13 (10,000 XP)", 5),
    FOURTEEN("14", "14 (11,500 XP)", 5),
    FIFTEEN("15", "15 (13,000 XP)", 5),
    SIXTEEN("16", "16 (15,000 XP)", 5),
    SEVENTEEN("17", "17 (18,000 XP)", 6),
    EIGHTEEN("18", "18 (20,000 XP)", 6),
    NINETEEN("19", "19 (22,000 XP)", 6),
    TWENTY("20", "20 (25,000 XP)", 6),
    TWENTY_ONE("21", "21 (33,000 XP)", 7),
    TWENTY_TWO("22", "22 (41,000 XP)", 7),
    TWENTY_THREE("23", "23 (50,000 XP)", 7),
    TWENTY_FOUR("24", "24 (62,000 XP)", 7),
    TWENTY_FIVE("25", "25 (75,000 XP)", 8),
    TWENTY_SIX("26", "26 (90,000 XP)", 8),
    TWENTY_SEVEN("27", "27 (105,000 XP)", 8),
    TWENTY_EIGHT("28", "28 (120,000 XP)", 8),
    TWENTY_NINE("29", "29 (135,000 XP)", 9),
    THIRTY("30", "30 (155,000 XP)", 9),
    ;

    public final String displayName;
    public final String stringValue;
    public final int proficiencyBonus;

    ChallengeRating(String stringValue, String displayName, int proficiencyBonus) {
        this.displayName = displayName;
        this.stringValue = stringValue;
        this.proficiencyBonus = proficiencyBonus;
    }

    public static ChallengeRating valueOfString(String string) {
        for (ChallengeRating challengeRating : values()) {
            if (challengeRating.stringValue.equals(string)) {
                return challengeRating;
            }
        }
        return ChallengeRating.ONE;
    }
}
