package com.majinnaibu.monstercards.importers;

import androidx.annotation.NonNull;

import com.google.gson.JsonArray;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Open5eImporter implements EntityImporter<Monster> {

    private static final Pattern HIT_DICE_PATTERN = Pattern.compile("(\\d+)d(\\d+)");

    @Override
    public boolean canImport(@NonNull String input) {
        String trimmed = input.trim();
        if (!trimmed.startsWith("{") || !trimmed.endsWith("}")) {
            return false;
        }
        try {
            JsonElement element = JsonParser.parseString(trimmed);
            if (!element.isJsonObject()) {
                return false;
            }
            JsonObject root = element.getAsJsonObject();
            if (root.has("ruleset_id") && root.get("ruleset_id").getAsString().equals("open5e")) {
                return true;
            }
            if (root.has("properties") && root.getAsJsonObject("properties").has("abilities")) {
                return true;
            }
        } catch (Exception ignored) {
        }
        return false;
    }

    @NonNull
    @Override
    public Monster parse(@NonNull String input) throws Exception {
        JsonObject root = JsonParser.parseString(input.trim()).getAsJsonObject();

        Monster monster = new Monster();

        if (root.has("uuid") && !root.get("uuid").isJsonNull()) {
            try {
                monster.id = UUID.fromString(root.get("uuid").getAsString());
            } catch (Exception ignored) {
            }
        }

        if (root.has("display_name") && !root.get("display_name").isJsonNull()) {
            monster.name = root.get("display_name").getAsString();
        }

        JsonObject props = root.has("properties") ? root.getAsJsonObject("properties") : root;

        if (monster.name == null || monster.name.isEmpty()) {
            monster.name = getString(props, "name", "Unnamed Entity");
        }

        monster.size = getString(props, "size");
        monster.type = getString(props, "type");
        monster.subtype = getString(props, "subtype");
        monster.alignment = getString(props, "alignment");

        parseArmorClass(props, monster);
        parseHitPoints(props, monster);

        monster.walkSpeed = getInt(props, "speed", 30);
        if (props.has("speed_desc")) {
            String speedDesc = getString(props, "speed_desc");
            if (!speedDesc.isEmpty()) {
                monster.hasCustomSpeed = true;
                monster.customSpeedDescription = speedDesc;
            }
        }

        parseChallengeRating(props, monster);
        parseAbilities(props, monster);
        parseIdentityFields(props, monster);

        parseSavingThrows(props, monster);
        parseSkills(props, monster);
        parseSenses(props, monster);
        parseLanguages(props, monster);
        parseDamageAndConditions(props, monster);

        parseTraitsAndActions(props, monster);

        if (props.has("source_url")) {
            monster.sourceUrl = getString(props, "source_url");
        }

        return monster;
    }

    private void parseArmorClass(JsonObject props, Monster monster) {
        if (props.has("equipped_armor") && props.get("equipped_armor").isJsonObject()) {
            JsonObject armorObj = props.getAsJsonObject("equipped_armor");
            String armorId = getString(armorObj, "id");
            ArmorType armorType = ArmorType.valueOfString(armorId);
            if (armorType != ArmorType.NONE) {
                monster.armorType = armorType;
            }
        }

        if (props.has("equipped_shield") && props.get("equipped_shield").isJsonObject()) {
            JsonObject shieldObj = props.getAsJsonObject("equipped_shield");
            monster.shieldBonus = getInt(shieldObj, "ac_bonus", 2);
        }

        if (monster.armorType == ArmorType.NONE && props.has("armor_description")) {
            String armorDesc = getString(props, "armor_description");
            if (!armorDesc.isEmpty()) {
                monster.otherArmorDescription = armorDesc;
            }
        }
    }

    private void parseHitPoints(JsonObject props, Monster monster) {
        if (props.has("hit_points") && props.get("hit_points").isJsonObject()) {
            JsonObject hpObj = props.getAsJsonObject("hit_points");
            int maxHp = getInt(hpObj, "max", getInt(hpObj, "current", 10));
            if (hpObj.has("hit_dice") && !hpObj.get("hit_dice").isJsonNull()) {
                String hitDiceStr = hpObj.get("hit_dice").getAsString();
                Matcher m = HIT_DICE_PATTERN.matcher(hitDiceStr);
                if (m.find()) {
                    monster.hitDice = Integer.parseInt(m.group(1));
                }
            }
            if (hpObj.has("formula") && !hpObj.get("formula").isJsonNull()) {
                monster.hasCustomHP = true;
                monster.customHPDescription = String.format(Locale.ROOT, "%d (%s)", maxHp, hpObj.get("formula").getAsString());
            }
        }
    }

    private void parseChallengeRating(JsonObject props, Monster monster) {
        String crStr = "";
        if (props.has("cr") && !props.get("cr").isJsonNull()) {
            crStr = props.get("cr").getAsString();
        } else if (props.has("challenge_rating") && !props.get("challenge_rating").isJsonNull()) {
            crStr = props.get("challenge_rating").getAsString();
        }
        if (!crStr.isEmpty()) {
            monster.challengeRating = ChallengeRating.valueOfString(crStr);
        }
    }

    private void parseAbilities(JsonObject props, Monster monster) {
        if (!props.has("abilities") || !props.get("abilities").isJsonObject()) {
            return;
        }
        JsonObject abs = props.getAsJsonObject("abilities");
        monster.strengthScore = extractAbilityScore(abs, "strength");
        monster.dexterityScore = extractAbilityScore(abs, "dexterity");
        monster.constitutionScore = extractAbilityScore(abs, "constitution");
        monster.intelligenceScore = extractAbilityScore(abs, "intelligence");
        monster.wisdomScore = extractAbilityScore(abs, "wisdom");
        monster.charismaScore = extractAbilityScore(abs, "charisma");
    }

    private int extractAbilityScore(JsonObject abs, String key) {
        if (!abs.has(key) || abs.get(key).isJsonNull()) {
            return 10;
        }
        JsonElement el = abs.get(key);
        if (el.isJsonPrimitive()) {
            return el.getAsInt();
        } else if (el.isJsonObject() && el.getAsJsonObject().has("value")) {
            return el.getAsJsonObject().get("value").getAsInt();
        }
        return 10;
    }

    private void parseIdentityFields(JsonObject props, Monster monster) {
        monster.playerName = getString(props, "player_name");
        monster.background = getString(props, "background");
        monster.personalityTraits = getString(props, "personality_traits");
        monster.ideals = getString(props, "ideals");
        monster.bonds = getString(props, "bonds");
        monster.flaws = getString(props, "flaws");

        if (props.has("character_details") && props.get("character_details").isJsonObject()) {
            JsonObject details = props.getAsJsonObject("character_details");
            monster.age = getString(details, "age");
            monster.height = getString(details, "height");
            monster.weight = getString(details, "weight");
            monster.eyes = getString(details, "eyes");
            monster.skin = getString(details, "skin");
            monster.hair = getString(details, "hair");
            monster.appearance = getString(details, "appearance");
            monster.backstory = getString(details, "backstory");
            monster.alliesAndOrganizations = getString(details, "allies_and_organizations");
        }
    }

    private void parseSavingThrows(JsonObject props, Monster monster) {
        if (!props.has("saving_throws")) return;
        JsonElement savesEl = props.get("saving_throws");
        if (savesEl.isJsonArray()) {
            JsonArray arr = savesEl.getAsJsonArray();
            for (int i = 0; i < arr.size(); i++) {
                String saveStr = arr.get(i).getAsString().toLowerCase(Locale.ROOT);
                applySaveProficiency(monster, saveStr);
            }
        }
    }

    private void applySaveProficiency(Monster monster, String saveStr) {
        if (saveStr.contains("str")) monster.strengthSavingThrowProficiency = ProficiencyType.PROFICIENT;
        else if (saveStr.contains("dex")) monster.dexteritySavingThrowProficiency = ProficiencyType.PROFICIENT;
        else if (saveStr.contains("con")) monster.constitutionSavingThrowProficiency = ProficiencyType.PROFICIENT;
        else if (saveStr.contains("int")) monster.intelligenceSavingThrowProficiency = ProficiencyType.PROFICIENT;
        else if (saveStr.contains("wis")) monster.wisdomSavingThrowProficiency = ProficiencyType.PROFICIENT;
        else if (saveStr.contains("cha")) monster.charismaSavingThrowProficiency = ProficiencyType.PROFICIENT;
    }

    private void parseSkills(JsonObject props, Monster monster) {
        if (!props.has("skills")) return;
        JsonElement skillsEl = props.get("skills");
        if (skillsEl.isJsonArray()) {
            JsonArray arr = skillsEl.getAsJsonArray();
            for (int i = 0; i < arr.size(); i++) {
                String skillStr = arr.get(i).getAsString();
                String skillName = skillStr.replaceAll("[+-]\\d+", "").trim();
                if (!skillName.isEmpty()) {
                    AbilityScore stat = getAbilityForSkill(skillName);
                    monster.skills.add(new Skill(capitalize(skillName), stat, AdvantageType.NONE, ProficiencyType.PROFICIENT));
                }
            }
        }
    }

    private AbilityScore getAbilityForSkill(String skillName) {
        String lower = skillName.toLowerCase(Locale.ROOT);
        if (lower.contains("acro") || lower.contains("sleight") || lower.contains("stealth")) return AbilityScore.DEXTERITY;
        if (lower.contains("arca") || lower.contains("hist") || lower.contains("inve") || lower.contains("natu") || lower.contains("reli")) return AbilityScore.INTELLIGENCE;
        if (lower.contains("anim") || lower.contains("insi") || lower.contains("medi") || lower.contains("perc") || lower.contains("surv")) return AbilityScore.WISDOM;
        if (lower.contains("dece") || lower.contains("inti") || lower.contains("perf") || lower.contains("pers")) return AbilityScore.CHARISMA;
        return AbilityScore.STRENGTH;
    }

    private void parseSenses(JsonObject props, Monster monster) {
        if (props.has("senses")) {
            String sensesStr = getString(props, "senses");
            if (!sensesStr.isEmpty()) {
                String[] parts = sensesStr.split(",");
                for (String p : parts) {
                    if (!p.trim().isEmpty()) {
                        monster.senses.add(p.trim());
                    }
                }
            }
        }
    }

    private void parseLanguages(JsonObject props, Monster monster) {
        if (props.has("languages")) {
            JsonElement langEl = props.get("languages");
            if (langEl.isJsonArray()) {
                JsonArray arr = langEl.getAsJsonArray();
                for (int i = 0; i < arr.size(); i++) {
                    String langName = arr.get(i).getAsString();
                    if (!langName.isEmpty()) {
                        monster.languages.add(new Language(langName, true));
                    }
                }
            } else if (langEl.isJsonPrimitive()) {
                String langStr = langEl.getAsString();
                for (String l : langStr.split(",")) {
                    if (!l.trim().isEmpty()) {
                        monster.languages.add(new Language(l.trim(), true));
                    }
                }
            }
        }
    }

    private void parseDamageAndConditions(JsonObject props, Monster monster) {
        parseStringSet(props, "damage_immunities", monster.damageImmunities);
        parseStringSet(props, "damage_resistances", monster.damageResistances);
        parseStringSet(props, "damage_vulnerabilities", monster.damageVulnerabilities);
        parseStringSet(props, "condition_immunities", monster.conditionImmunities);
    }

    private void parseStringSet(JsonObject props, String key, java.util.Set<String> set) {
        if (!props.has(key)) return;
        JsonElement el = props.get(key);
        if (el.isJsonArray()) {
            JsonArray arr = el.getAsJsonArray();
            for (int i = 0; i < arr.size(); i++) {
                String val = arr.get(i).getAsString();
                if (!val.isEmpty()) set.add(capitalize(val));
            }
        } else if (el.isJsonPrimitive()) {
            for (String val : el.getAsString().split(",")) {
                if (!val.trim().isEmpty()) set.add(capitalize(val.trim()));
            }
        }
    }

    private void parseTraitsAndActions(JsonObject props, Monster monster) {
        parseTraitList(props, "traits", monster.abilities);
        parseTraitList(props, "actions", monster.actions);
        parseTraitList(props, "bonus_actions", monster.bonusActions);
        parseTraitList(props, "reactions", monster.reactions);
        parseTraitList(props, "legendary_actions", monster.legendaryActions);
        parseTraitList(props, "lair_actions", monster.lairActions);
        parseTraitList(props, "regional_effects", monster.regionalActions);
        parseTraitList(props, "mythic_actions", monster.mythicActions);

        if (props.has("legendary_desc")) monster.legendaryActionsDescription = getString(props, "legendary_desc");
        if (props.has("lair_desc")) monster.lairActionsDescription = getString(props, "lair_desc");
        if (props.has("lair_end_note")) monster.lairActionsEndNote = getString(props, "lair_end_note");
        if (props.has("regional_desc")) monster.regionalActionsDescription = getString(props, "regional_desc");
        if (props.has("regional_end_note")) monster.regionalActionsEndNote = getString(props, "regional_end_note");
        if (props.has("mythic_desc")) monster.mythicActionsDescription = getString(props, "mythic_desc");
    }

    private void parseTraitList(JsonObject props, String key, List<Trait> list) {
        if (!props.has(key) || !props.get(key).isJsonArray()) return;
        JsonArray arr = props.getAsJsonArray(key);
        for (int i = 0; i < arr.size(); i++) {
            JsonElement el = arr.get(i);
            if (el.isJsonObject()) {
                JsonObject obj = el.getAsJsonObject();
                String name = getString(obj, "name");
                String desc = getString(obj, "desc");
                if (desc.isEmpty()) desc = getString(obj, "description");
                if (!name.isEmpty()) {
                    list.add(new Trait(name, desc));
                }
            }
        }
    }

    private static String getString(JsonObject obj, String key) {
        return getString(obj, key, "");
    }

    private static String getString(JsonObject obj, String key, String defaultVal) {
        if (obj.has(key) && !obj.get(key).isJsonNull()) {
            return obj.get(key).getAsString();
        }
        return defaultVal;
    }

    private static int getInt(JsonObject obj, String key, int defaultVal) {
        if (obj.has(key) && !obj.get(key).isJsonNull()) {
            try {
                return obj.get(key).getAsInt();
            } catch (Exception ignored) {
            }
        }
        return defaultVal;
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
