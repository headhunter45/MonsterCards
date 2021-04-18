package com.majinnaibu.monstercards.data.enums;

public enum AdvantageType {
    NONE("none", "None", ""),
    ADVANTAGE("advantage", "Advantage", "A"),
    DISADVANTAGE("disadvantage", "Disadvantage", "D"),
    ;

    public final String displayName;
    public final String stringValue;
    public final String label;

    AdvantageType(String stringValue, String displayName, String label) {
        this.displayName = displayName;
        this.stringValue = stringValue;
        this.label = label;
    }

    public static AdvantageType valueOfString(String string) {
        for (AdvantageType advantageType : values()) {
            if (advantageType.stringValue.equals(string)) {
                return advantageType;
            }
        }
        return AdvantageType.NONE;
    }
}
