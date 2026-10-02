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
        if (text != null && !text.trim().isEmpty()) {
            String trimmed = text.trim();
            for (GameSystem gs : GameSystem.values()) {
                if (gs.name().equalsIgnoreCase(trimmed) || gs.displayName.equalsIgnoreCase(trimmed) || gs.shortName.equalsIgnoreCase(trimmed)) {
                    return gs;
                }
            }
            return CUSTOM;
        }
        return DND_5E;
    }

    public static String getSystemDisplayName(GameSystem system, String customSystem) {
        if (system == CUSTOM && customSystem != null && !customSystem.trim().isEmpty()) {
            return customSystem.trim();
        }
        return system != null ? system.getDisplayName() : DND_5E.getDisplayName();
    }

    public static String getSystemShortName(GameSystem system, String customSystem) {
        if (system == CUSTOM && customSystem != null && !customSystem.trim().isEmpty()) {
            return customSystem.trim();
        }
        return system != null ? system.getShortName() : DND_5E.getShortName();
    }

    @NonNull
    @Override
    public String toString() {
        return displayName;
    }
}
