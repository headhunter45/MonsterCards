package com.majinnaibu.monstercards.data.enums;

import androidx.annotation.NonNull;

public enum MonsterOrigin {
    MANUAL("manual", "Manual Entry", "Manual"),
    OPEN5E("open5e.com", "Open5e", "open5e.com"),
    FOUNDRY_PF2E("foundryvtt/pf2e", "Foundry PF2e", "foundryvtt/pf2e"),
    OTHER("other", "Other", "Other");

    private final String value;
    private final String displayName;
    private final String shortName;

    MonsterOrigin(String value, String displayName, String shortName) {
        this.value = value;
        this.displayName = displayName;
        this.shortName = shortName;
    }

    public String getValue() {
        return value;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getShortName() {
        return shortName;
    }

    public static MonsterOrigin fromString(String text) {
        if (text != null && !text.trim().isEmpty()) {
            String trimmed = text.trim();
            for (MonsterOrigin mo : MonsterOrigin.values()) {
                if (mo.name().equalsIgnoreCase(trimmed) 
                        || mo.value.equalsIgnoreCase(trimmed) 
                        || mo.displayName.equalsIgnoreCase(trimmed) 
                        || mo.shortName.equalsIgnoreCase(trimmed)) {
                    return mo;
                }
            }
            return OTHER;
        }
        return MANUAL;
    }

    public static String getOriginDisplayName(MonsterOrigin origin, String customOrigin) {
        if (origin == OTHER && customOrigin != null && !customOrigin.trim().isEmpty()) {
            return customOrigin.trim();
        }
        return origin != null ? origin.getDisplayName() : MANUAL.getDisplayName();
    }

    @NonNull
    @Override
    public String toString() {
        return value;
    }
}
