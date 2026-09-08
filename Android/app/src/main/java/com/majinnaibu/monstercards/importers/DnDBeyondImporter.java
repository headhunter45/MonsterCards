package com.majinnaibu.monstercards.importers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.majinnaibu.monstercards.data.enums.AbilityScore;
import com.majinnaibu.monstercards.data.enums.AdvantageType;
import com.majinnaibu.monstercards.data.enums.ProficiencyType;
import com.majinnaibu.monstercards.models.Language;
import com.majinnaibu.monstercards.models.Monster;
import com.majinnaibu.monstercards.models.Skill;
import com.majinnaibu.monstercards.models.Trait;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

public class DnDBeyondImporter implements EntityImporter<Monster> {

    private static final Pattern DNDBEYOND_URL_PATTERN = Pattern.compile("https?://(?:www\\.)?dndbeyond\\.com/characters/(\\d+)(?:/([a-zA-Z0-9]+))?", Pattern.CASE_INSENSITIVE);
    private static final Pattern CHARACTER_ID_PATTERN = Pattern.compile("^\\d+$");

    @Override
    public boolean canImport(@NonNull String input) {
        String trimmed = input.trim();
        if (trimmed.isEmpty()) {
            return false;
        }
        if (DNDBEYOND_URL_PATTERN.matcher(trimmed).find() || CHARACTER_ID_PATTERN.matcher(trimmed).matches()) {
            return true;
        }
        try {
            JsonElement element = JsonParser.parseString(trimmed);
            if (element.isJsonObject()) {
                JsonObject root = element.getAsJsonObject();
                if (root.has("data") && root.getAsJsonObject("data").has("baseHitPoints")) {
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
        String trimmed = input.trim();
        JsonObject characterData;
        String sourceUrl = "";

        if (trimmed.startsWith("{")) {
            JsonObject root = JsonParser.parseString(trimmed).getAsJsonObject();
            characterData = root.has("data") ? root.getAsJsonObject("data") : root;
        } else {
            String characterId = extractCharacterId(trimmed);
            if (characterId == null) {
                throw new IllegalArgumentException("Could not extract D&D Beyond character ID from input: " + input);
            }
            sourceUrl = trimmed.startsWith("http") ? trimmed : "https://www.dndbeyond.com/characters/" + characterId;
            String jsonPayload = fetchCharacterJson(characterId);
            JsonObject root = JsonParser.parseString(jsonPayload).getAsJsonObject();
            if (!root.has("data") || root.get("data").isJsonNull()) {
                throw new IllegalArgumentException("D&D Beyond response did not contain character data.");
            }
            characterData = root.getAsJsonObject("data");
        }

        return buildMonsterFromCharacterData(characterData, sourceUrl);
    }

    @Nullable
    public static String extractCharacterId(@NonNull String input) {
        String trimmed = input.trim();
        Matcher urlMatcher = DNDBEYOND_URL_PATTERN.matcher(trimmed);
        if (urlMatcher.find()) {
            return urlMatcher.group(1);
        }
        if (CHARACTER_ID_PATTERN.matcher(trimmed).matches()) {
            return trimmed;
        }
        return null;
    }

    @NonNull
    private String fetchCharacterJson(@NonNull String characterId) throws Exception {
        String serviceUrl = "https://character-service.dndbeyond.com/character/v5/character/" + characterId + "?includeCustomItems=true";
        URL url = new URL(serviceUrl);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Android; Mobile)");
        conn.setRequestProperty("Accept", "application/json");
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(10000);

        int code = conn.getResponseCode();
        if (code != 200) {
            throw new IllegalStateException("D&D Beyond service returned HTTP " + code);
        }

        StringBuilder sb = new StringBuilder();
        try (InputStream in = conn.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        return sb.toString();
    }

    @NonNull
    private Monster buildMonsterFromCharacterData(@NonNull JsonObject data, @NonNull String sourceUrl) {
        Monster monster = new Monster();
        monster.sourceUrl = sourceUrl;

        String rawName = getString(data, "name");
        if (rawName.startsWith("\"") && rawName.endsWith("\"") && rawName.length() > 1) {
            rawName = rawName.substring(1, rawName.length() - 1);
        }
        monster.name = rawName.isEmpty() ? "D&D Beyond Character" : rawName;

        monster.size = "Medium";
        monster.type = "Humanoid";

        parseClassesAndLevel(data, monster);
        parseStats(data, monster);
        parseHitPoints(data, monster);
        parseSpeedAndSenses(data, monster);
        parseModifiers(data, monster);
        parseTraitsAndFeatures(data, monster);
        parseActionsAndAttacks(data, monster);

        return monster;
    }

    private void parseClassesAndLevel(@NonNull JsonObject data, @NonNull Monster monster) {
        StringBuilder classSummary = new StringBuilder();
        int totalLevel = 0;
        if (data.has("classes") && data.get("classes").isJsonArray()) {
            JsonArray classes = data.getAsJsonArray("classes");
            for (int i = 0; i < classes.size(); i++) {
                JsonObject cls = classes.get(i).getAsJsonObject();
                int level = getInt(cls, "level", 1);
                totalLevel += level;
                String className = "";
                if (cls.has("definition") && cls.get("definition").isJsonObject()) {
                    className = getString(cls.getAsJsonObject("definition"), "name");
                }
                String subclassName = "";
                if (cls.has("subclassDefinition") && cls.get("subclassDefinition").isJsonObject()) {
                    subclassName = getString(cls.getAsJsonObject("subclassDefinition"), "name");
                }
                if (classSummary.length() > 0) {
                    classSummary.append(" / ");
                }
                classSummary.append(className);
                if (!subclassName.isEmpty()) {
                    classSummary.append(" (").append(subclassName).append(")");
                }
                classSummary.append(" ").append(level);
            }
        }
        monster.hitDice = totalLevel > 0 ? totalLevel : 1;
        String raceName = "";
        if (data.has("race") && data.get("race").isJsonObject()) {
            JsonObject race = data.getAsJsonObject("race");
            raceName = getString(race, "fullName");
            if (raceName.isEmpty()) {
                raceName = getString(race, "baseRaceName");
            }
        }
        StringBuilder subtypeSb = new StringBuilder();
        if (totalLevel > 0) {
            subtypeSb.append("Level ").append(totalLevel);
        }
        if (!raceName.isEmpty()) {
            if (subtypeSb.length() > 0) subtypeSb.append(" ");
            subtypeSb.append(raceName);
        }
        if (classSummary.length() > 0) {
            if (subtypeSb.length() > 0) subtypeSb.append(" ");
            subtypeSb.append(classSummary);
        }
        monster.subtype = subtypeSb.toString();
    }

    private void parseStats(@NonNull JsonObject data, @NonNull Monster monster) {
        if (data.has("stats") && data.get("stats").isJsonArray()) {
            JsonArray stats = data.getAsJsonArray("stats");
            for (int i = 0; i < stats.size(); i++) {
                JsonObject stat = stats.get(i).getAsJsonObject();
                int id = getInt(stat, "id");
                int val = getInt(stat, "value", 10);
                switch (id) {
                    case 1: monster.strengthScore = val; break;
                    case 2: monster.dexterityScore = val; break;
                    case 3: monster.constitutionScore = val; break;
                    case 4: monster.intelligenceScore = val; break;
                    case 5: monster.wisdomScore = val; break;
                    case 6: monster.charismaScore = val; break;
                }
            }
        }
    }

    private void parseHitPoints(@NonNull JsonObject data, @NonNull Monster monster) {
        int baseHp = getInt(data, "baseHitPoints");
        int overrideHp = getInt(data, "overrideHitPoints");
        int maxHp = overrideHp > 0 ? overrideHp : baseHp;
        if (maxHp > 0) {
            monster.hasCustomHP = true;
            monster.customHPDescription = String.format(Locale.ROOT, "%d (%dd%d)", maxHp, monster.hitDice, 8);
        }
    }

    private void parseSpeedAndSenses(@NonNull JsonObject data, @NonNull Monster monster) {
        if (data.has("race") && data.get("race").isJsonObject()) {
            JsonObject race = data.getAsJsonObject("race");
            if (race.has("weightSpeeds") && race.get("weightSpeeds").isJsonObject()) {
                JsonObject weightSpeeds = race.getAsJsonObject("weightSpeeds");
                if (weightSpeeds.has("normal") && weightSpeeds.get("normal").isJsonObject()) {
                    JsonObject normal = weightSpeeds.getAsJsonObject("normal");
                    monster.walkSpeed = getInt(normal, "walk", 30);
                    monster.flySpeed = getInt(normal, "fly", 0);
                    monster.burrowSpeed = getInt(normal, "burrow", 0);
                    monster.swimSpeed = getInt(normal, "swim", 0);
                    monster.climbSpeed = getInt(normal, "climb", 0);
                }
            }
        }
        if (monster.walkSpeed == 0) {
            monster.walkSpeed = 30;
        }
    }

    private void parseModifiers(@NonNull JsonObject data, @NonNull Monster monster) {
        if (!data.has("modifiers") || !data.get("modifiers").isJsonObject()) {
            return;
        }
        JsonObject modifiersObj = data.getAsJsonObject("modifiers");
        String[] categories = new String[]{"race", "class", "background", "item", "feat"};

        for (String cat : categories) {
            if (modifiersObj.has(cat) && modifiersObj.get(cat).isJsonArray()) {
                JsonArray mods = modifiersObj.getAsJsonArray(cat);
                for (int i = 0; i < mods.size(); i++) {
                    JsonObject mod = mods.get(i).getAsJsonObject();
                    String type = getString(mod, "type");
                    String subType = getString(mod, "subType");
                    int fixedVal = getInt(mod, "fixedValue");

                    if ("set-base".equals(type) && "darkvision".equals(subType)) {
                        int dist = fixedVal > 0 ? fixedVal : 60;
                        monster.senses.add("darkvision " + dist + " ft.");
                    } else if ("language".equals(type)) {
                        String langName = capitalize(subType.replace("-", " "));
                        monster.languages.add(new Language(langName, true));
                    } else if ("proficiency".equals(type)) {
                        if (subType.endsWith("-saving-throws")) {
                            String stat = subType.replace("-saving-throws", "");
                            applySavingThrowProficiency(monster, stat);
                        } else {
                            applySkillProficiency(monster, subType, ProficiencyType.PROFICIENT);
                        }
                    } else if ("expertise".equals(type)) {
                        applySkillProficiency(monster, subType, ProficiencyType.EXPERTISE);
                    } else if ("immunity".equals(type)) {
                        monster.damageImmunities.add(capitalize(subType.replace("-", " ")));
                    } else if ("resistance".equals(type)) {
                        monster.damageResistances.add(capitalize(subType.replace("-", " ")));
                    } else if ("vulnerability".equals(type)) {
                        monster.damageVulnerabilities.add(capitalize(subType.replace("-", " ")));
                    }
                }
            }
        }
    }

    private void applySavingThrowProficiency(@NonNull Monster monster, String stat) {
        if ("strength".equals(stat) || "str".equals(stat)) monster.strengthSavingThrowProficiency = ProficiencyType.PROFICIENT;
        else if ("dexterity".equals(stat) || "dex".equals(stat)) monster.dexteritySavingThrowProficiency = ProficiencyType.PROFICIENT;
        else if ("constitution".equals(stat) || "con".equals(stat)) monster.constitutionSavingThrowProficiency = ProficiencyType.PROFICIENT;
        else if ("intelligence".equals(stat) || "int".equals(stat)) monster.intelligenceSavingThrowProficiency = ProficiencyType.PROFICIENT;
        else if ("wisdom".equals(stat) || "wis".equals(stat)) monster.wisdomSavingThrowProficiency = ProficiencyType.PROFICIENT;
        else if ("charisma".equals(stat) || "cha".equals(stat)) monster.charismaSavingThrowProficiency = ProficiencyType.PROFICIENT;
    }

    private void applySkillProficiency(@NonNull Monster monster, String subType, ProficiencyType profType) {
        String skillName = capitalize(subType.replace("-", " "));
        AbilityScore abilityScore = AbilityScore.STRENGTH;
        if ("Acrobatics".equalsIgnoreCase(skillName) || "Sleight Of Hand".equalsIgnoreCase(skillName) || "Stealth".equalsIgnoreCase(skillName)) {
            abilityScore = AbilityScore.DEXTERITY;
        } else if ("Arcana".equalsIgnoreCase(skillName) || "History".equalsIgnoreCase(skillName) || "Investigation".equalsIgnoreCase(skillName) || "Nature".equalsIgnoreCase(skillName) || "Religion".equalsIgnoreCase(skillName)) {
            abilityScore = AbilityScore.INTELLIGENCE;
        } else if ("Animal Handling".equalsIgnoreCase(skillName) || "Insight".equalsIgnoreCase(skillName) || "Medicine".equalsIgnoreCase(skillName) || "Perception".equalsIgnoreCase(skillName) || "Survival".equalsIgnoreCase(skillName)) {
            abilityScore = AbilityScore.WISDOM;
        } else if ("Deception".equalsIgnoreCase(skillName) || "Intimidation".equalsIgnoreCase(skillName) || "Performance".equalsIgnoreCase(skillName) || "Persuasion".equalsIgnoreCase(skillName)) {
            abilityScore = AbilityScore.CHARISMA;
        } else if ("Athletics".equalsIgnoreCase(skillName)) {
            abilityScore = AbilityScore.STRENGTH;
        }
        monster.skills.add(new Skill(skillName, abilityScore, AdvantageType.NONE, profType));
    }

    private void parseTraitsAndFeatures(@NonNull JsonObject data, @NonNull Monster monster) {
        if (data.has("race") && data.get("race").isJsonObject()) {
            JsonObject race = data.getAsJsonObject("race");
            if (race.has("racialTraits") && race.get("racialTraits").isJsonArray()) {
                JsonArray traits = race.getAsJsonArray("racialTraits");
                for (int i = 0; i < traits.size(); i++) {
                    JsonObject tr = traits.get(i).getAsJsonObject();
                    if (tr.has("definition") && tr.get("definition").isJsonObject()) {
                        JsonObject def = tr.getAsJsonObject("definition");
                        String name = getString(def, "name");
                        String desc = stripHtml(getString(def, "snippet"));
                        if (desc.isEmpty()) {
                            desc = stripHtml(getString(def, "description"));
                        }
                        if (!name.isEmpty() && !desc.isEmpty()) {
                            monster.abilities.add(new Trait(name, desc));
                        }
                    }
                }
            }
        }
    }

    private void parseActionsAndAttacks(@NonNull JsonObject data, @NonNull Monster monster) {
        if (data.has("actions") && data.get("actions").isJsonObject()) {
            JsonObject actionsObj = data.getAsJsonObject("actions");
            String[] categories = new String[]{"class", "race", "background", "item", "feat"};
            for (String cat : categories) {
                if (actionsObj.has(cat) && actionsObj.get(cat).isJsonArray()) {
                    JsonArray acts = actionsObj.getAsJsonArray(cat);
                    for (int i = 0; i < acts.size(); i++) {
                        JsonObject act = acts.get(i).getAsJsonObject();
                        String name = getString(act, "name");
                        String desc = stripHtml(getString(act, "snippet"));
                        if (desc.isEmpty()) {
                            desc = stripHtml(getString(act, "description"));
                        }
                        int actType = 1;
                        if (act.has("activation") && act.get("activation").isJsonObject()) {
                            actType = getInt(act.getAsJsonObject("activation"), "activationType", 1);
                        }
                        if (!name.isEmpty()) {
                            Trait trait = new Trait(name, desc);
                            if (actType == 3) {
                                monster.bonusActions.add(trait);
                            } else if (actType == 4) {
                                monster.reactions.add(trait);
                            } else {
                                monster.actions.add(trait);
                            }
                        }
                    }
                }
            }
        }
    }

    private static String getString(@NonNull JsonObject obj, String key) {
        return getString(obj, key, "");
    }

    private static String getString(@NonNull JsonObject obj, String key, String defaultVal) {
        if (obj.has(key) && !obj.get(key).isJsonNull()) {
            return obj.get(key).getAsString();
        }
        return defaultVal;
    }

    private static int getInt(@NonNull JsonObject obj, String key) {
        return getInt(obj, key, 0);
    }

    private static int getInt(@NonNull JsonObject obj, String key, int defaultVal) {
        if (obj.has(key) && !obj.get(key).isJsonNull()) {
            try {
                return obj.get(key).getAsInt();
            } catch (Exception ignored) {
            }
        }
        return defaultVal;
    }

    private static String stripHtml(String html) {
        if (html == null) return "";
        return html.replaceAll("<[^>]*>", "").replaceAll("&nbsp;", " ").trim();
    }

    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        String[] words = str.split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (String w : words) {
            if (!w.isEmpty()) {
                if (sb.length() > 0) sb.append(" ");
                sb.append(Character.toUpperCase(w.charAt(0))).append(w.substring(1).toLowerCase(Locale.ROOT));
            }
        }
        return sb.toString();
    }
}
