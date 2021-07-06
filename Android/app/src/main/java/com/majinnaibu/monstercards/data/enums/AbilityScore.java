package com.majinnaibu.monstercards.data.enums;

@SuppressWarnings("unused")
public enum AbilityScore {
    STRENGTH("strength", "Strength", "STR"),
    DEXTERITY("dexterity", "Dexterity", "DEX"),
    CONSTITUTION("constitution", "Constitution", "CON"),
    INTELLIGENCE("intelligence", "Intelligence", "INT"),
    WISDOM("wisdom", "Wisdom", "WIS"),
    CHARISMA("charisma", "Charisma", "CHA"),
    ;

    public final String displayName;
    public final String shortDisplayName;
    public final String stringValue;

    AbilityScore(String stringValue, String displayName, String shortDisplayName) {
        this.displayName = displayName;
        this.stringValue = stringValue;
        this.shortDisplayName = shortDisplayName;
    }

    public static AbilityScore valueOfString(String string) {
        for (AbilityScore abilityScore : values()) {
            if (abilityScore.stringValue.equals(string)) {
                return abilityScore;
            }
        }
        return AbilityScore.STRENGTH;
    }
}
