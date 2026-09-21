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
        if (string == null) {
            return AbilityScore.STRENGTH;
        }
        String lower = string.trim().toLowerCase(java.util.Locale.ROOT);
        for (AbilityScore abilityScore : values()) {
            if (abilityScore.stringValue.equalsIgnoreCase(lower)
                    || abilityScore.shortDisplayName.equalsIgnoreCase(lower)
                    || abilityScore.name().equalsIgnoreCase(lower)) {
                return abilityScore;
            }
        }
        if (lower.startsWith("str")) return STRENGTH;
        if (lower.startsWith("dex")) return DEXTERITY;
        if (lower.startsWith("con")) return CONSTITUTION;
        if (lower.startsWith("int")) return INTELLIGENCE;
        if (lower.startsWith("wis")) return WISDOM;
        if (lower.startsWith("cha")) return CHARISMA;
        return AbilityScore.STRENGTH;
    }
}
