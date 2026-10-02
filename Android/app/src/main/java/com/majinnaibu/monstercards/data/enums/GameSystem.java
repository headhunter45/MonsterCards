package com.majinnaibu.monstercards.data.enums;

import androidx.annotation.NonNull;

public enum GameSystem {
    DND_5E("D&D 5e", "5e"),
    PF_2E("Pathfinder 2e", "PF2e"),
    SF_2E("Starfinder 2e", "SF2e"),
    CUSTOM("Custom", "Custom");

    private final String displayName;
    private final String shortName;

    GameSystem(String displayName, String shortName) {
        this.displayName = displayName;
        this.shortName = shortName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getShortName() {
        return shortName;
    }

    public static GameSystem fromString(String text) {
        if (text != null) {
            for (GameSystem gs : GameSystem.values()) {
                if (gs.name().equalsIgnoreCase(text) || gs.displayName.equalsIgnoreCase(text) || gs.shortName.equalsIgnoreCase(text)) {
                    return gs;
                }
            }
        }
        return DND_5E;
    }

    @NonNull
    @Override
    public String toString() {
        return displayName;
    }
}
