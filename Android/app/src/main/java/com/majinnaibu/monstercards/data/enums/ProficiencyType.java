package com.majinnaibu.monstercards.data.enums;

public enum ProficiencyType {
    NONE("none", "None", ""),
    PROFICIENT("proficient", "Proficient", "P"),
    EXPERTISE("expertise", "Expertise", "Ex"),
    ;

    public final String displayName;
    public final String stringValue;
    public final String label;

    ProficiencyType(String stringValue, String displayName, String label) {
        this.displayName = displayName;
        this.stringValue = stringValue;
        this.label = label;
    }

    public static ProficiencyType valueOfString(String string) {
        for (ProficiencyType proficiencyType : values()) {
            if (proficiencyType.stringValue.equals(string)) {
                return proficiencyType;
            }
        }
        return ProficiencyType.NONE;
    }
}
