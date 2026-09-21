package com.majinnaibu.monstercards.importers;

import androidx.annotation.NonNull;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.majinnaibu.monstercards.data.enums.AbilityScore;
import com.majinnaibu.monstercards.data.enums.AdvantageType;
import com.majinnaibu.monstercards.data.enums.ArmorType;
import com.majinnaibu.monstercards.data.enums.ChallengeRating;
import com.majinnaibu.monstercards.data.enums.ProficiencyType;
import com.majinnaibu.monstercards.models.Language;
import com.majinnaibu.monstercards.models.Monster;
import com.majinnaibu.monstercards.models.Skill;
import com.majinnaibu.monstercards.models.Trait;

import java.util.Locale;

public class Pf2eImporter implements EntityImporter<Monster> {
    @Override
    public boolean canImport(@NonNull String input) {
        String trimmed = input.trim();
        if (!trimmed.startsWith("{") || !trimmed.endsWith("}")) {
            return false;
        }
        try {
            JsonObject root = JsonParser.parseString(trimmed).getAsJsonObject();
            if (root.has("type") && "npc".equals(root.get("type").getAsString())) {
                if (root.has("system") && root.getAsJsonObject("system").has("attributes")) {
                    return true;
                }
            }
        } catch (Exception ignored) {
        }
        return false;
    }

    @NonNull
    @Override
    public Monster parse(@NonNull String input) throws Exception {
        JsonObject root = JsonParser.parseString(input.trim()).getAsJsonObject();
        JsonObject system = root.has("system") ? root.getAsJsonObject("system") : new JsonObject();
        
        Monster monster = new Monster();
        
        // Name
        if (root.has("name") && !root.get("name").isJsonNull()) {
            monster.name = root.get("name").getAsString();
        } else {
            monster.name = "Unknown PF2e Monster";
        }
        
        // Type / Size / Alignment
        if (system.has("traits") && system.get("traits").isJsonObject()) {
            JsonObject traits = system.getAsJsonObject("traits");
            if (traits.has("size") && traits.get("size").isJsonObject()) {
                monster.size = mapPf2eSize(getString(traits.getAsJsonObject("size"), "value"));
            }
            if (traits.has("value") && traits.get("value").isJsonArray()) {
                // PF2e holds type/alignment tags in traits array
                StringBuilder typeSb = new StringBuilder();
                for (JsonElement el : traits.getAsJsonArray("value")) {
                    String trait = el.getAsString();
                    if (typeSb.length() > 0) typeSb.append(", ");
                    typeSb.append(capitalize(trait));
                }
                monster.type = typeSb.toString();
            }
        }
        
        // Ability Scores
        if (system.has("abilities") && system.get("abilities").isJsonObject()) {
            JsonObject abilities = system.getAsJsonObject("abilities");
            monster.strengthScore = 10 + (getInt(abilities.getAsJsonObject("str"), "mod") * 2);
            monster.dexterityScore = 10 + (getInt(abilities.getAsJsonObject("dex"), "mod") * 2);
            monster.constitutionScore = 10 + (getInt(abilities.getAsJsonObject("con"), "mod") * 2);
            monster.intelligenceScore = 10 + (getInt(abilities.getAsJsonObject("int"), "mod") * 2);
            monster.wisdomScore = 10 + (getInt(abilities.getAsJsonObject("wis"), "mod") * 2);
            monster.charismaScore = 10 + (getInt(abilities.getAsJsonObject("cha"), "mod") * 2);
        }
        
        // Attributes (HP, AC, Speed, Level)
        if (system.has("attributes") && system.get("attributes").isJsonObject()) {
            JsonObject attributes = system.getAsJsonObject("attributes");
            
            if (attributes.has("ac") && attributes.get("ac").isJsonObject()) {
                monster.armorType = ArmorType.NONE;
                monster.otherArmorDescription = String.valueOf(getInt(attributes.getAsJsonObject("ac"), "value"));
            }
            
            if (attributes.has("hp") && attributes.get("hp").isJsonObject()) {
                monster.hasCustomHP = true;
                monster.customHPDescription = String.valueOf(getInt(attributes.getAsJsonObject("hp"), "max"));
            }
            
            if (attributes.has("speed") && attributes.get("speed").isJsonObject()) {
                monster.walkSpeed = getInt(attributes.getAsJsonObject("speed"), "value");
            }
        }
        
        // Level (Mapped to CR)
        if (system.has("details") && system.get("details").isJsonObject()) {
            JsonObject details = system.getAsJsonObject("details");
            if (details.has("level") && details.get("level").isJsonObject()) {
                int level = getInt(details.getAsJsonObject("level"), "value");
                monster.challengeRating = mapLevelToCr(level);
            }
            
            if (details.has("languages") && details.get("languages").isJsonObject()) {
                JsonObject langObj = details.getAsJsonObject("languages");
                if (langObj.has("value") && langObj.get("value").isJsonArray()) {
                    for (JsonElement el : langObj.getAsJsonArray("value")) {
                        monster.languages.add(new Language(capitalize(el.getAsString()), true));
                    }
                }
            }
            
            if (details.has("publicNotes") && !details.get("publicNotes").isJsonNull()) {
                String notes = details.get("publicNotes").getAsString().replaceAll("<[^>]*>", "").trim();
                monster.backstory = notes;
            }
        }
        
        return monster;
    }

    private String mapPf2eSize(String pf2eSize) {
        if (pf2eSize == null) return "Medium";
        switch (pf2eSize.toLowerCase(Locale.ROOT)) {
            case "tiny": return "Tiny";
            case "sm": return "Small";
            case "med": return "Medium";
            case "lg": return "Large";
            case "huge": return "Huge";
            case "grg": return "Gargantuan";
            default: return "Medium";
        }
    }

    private ChallengeRating mapLevelToCr(int level) {
        if (level <= 0) return ChallengeRating.ZERO;
        if (level > 30) return ChallengeRating.THIRTY;
        return ChallengeRating.valueOfString(String.valueOf(level));
    }

    private static String getString(JsonObject obj, String key) {
        if (obj != null && obj.has(key) && !obj.get(key).isJsonNull()) {
            return obj.get(key).getAsString();
        }
        return "";
    }

    private static int getInt(JsonObject obj, String key) {
        if (obj != null && obj.has(key) && !obj.get(key).isJsonNull()) {
            try {
                return obj.get(key).getAsInt();
            } catch (Exception ignored) {}
        }
        return 0;
    }

    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase(Locale.ROOT) + str.substring(1).toLowerCase(Locale.ROOT);
    }
}
