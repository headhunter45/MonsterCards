package com.majinnaibu.monstercards.importers;

import androidx.annotation.NonNull;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.majinnaibu.monstercards.data.converters.ArmorTypeConverter;
import com.majinnaibu.monstercards.data.converters.ChallengeRatingConverter;
import com.majinnaibu.monstercards.data.enums.AbilityScore;
import com.majinnaibu.monstercards.data.enums.AdvantageType;
import com.majinnaibu.monstercards.data.enums.ProficiencyType;
import com.majinnaibu.monstercards.models.Language;
import com.majinnaibu.monstercards.models.Monster;
import com.majinnaibu.monstercards.models.Skill;
import com.majinnaibu.monstercards.models.Trait;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class TetraCubeMonsterImporter implements EntityImporter<Monster> {

    @Override
    public boolean canImport(@NonNull String input) {
        if (input.trim().isEmpty()) {
            return false;
        }
        try {
            JsonElement element = JsonParser.parseString(input);
            if (!element.isJsonObject()) {
                return false;
            }
            JsonObject rootDict = element.getAsJsonObject();
            return rootDict.has("hitDice") || rootDict.has("armorName") || rootDict.has("strPoints");
        } catch (Exception e) {
            return false;
        }
    }

    @NonNull
    @Override
    public Monster parse(@NonNull String input) throws Exception {
        JsonObject rootDict = JsonParser.parseString(input).getAsJsonObject();

        Monster monster = new Monster();
        monster.name = getString(rootDict, "name");
        monster.size = getString(rootDict, "size");
        monster.type = getString(rootDict, "type");
        monster.subtype = getString(rootDict, "tag");
        monster.alignment = getString(rootDict, "alignment");
        monster.hitDice = getInt(rootDict, "hitDice");
        monster.armorType = ArmorTypeConverter.armorTypeFromStringValue(getString(rootDict, "armorName"));
        monster.shieldBonus = getInt(rootDict, "shieldBonus");
        monster.naturalArmorBonus = getInt(rootDict, "natArmorBonus");
        monster.otherArmorDescription = getString(rootDict, "otherArmorDesc");
        monster.walkSpeed = getInt(rootDict, "speed");
        monster.burrowSpeed = getInt(rootDict, "burrowSpeed");
        monster.climbSpeed = getInt(rootDict, "climbSpeed");
        monster.flySpeed = getInt(rootDict, "flySpeed");
        monster.canHover = getBool(rootDict, "hover");
        monster.swimSpeed = getInt(rootDict, "swimSpeed");
        monster.hasCustomHP = getBool(rootDict, "customHP");
        monster.hasCustomSpeed = getBool(rootDict, "customSpeed");
        monster.customHPDescription = getString(rootDict, "hpText");
        monster.customSpeedDescription = getString(rootDict, "speedDesc");
        monster.strengthScore = getInt(rootDict, "strPoints");
        monster.dexterityScore = getInt(rootDict, "dexPoints");
        monster.constitutionScore = getInt(rootDict, "conPoints");
        monster.intelligenceScore = getInt(rootDict, "intPoints");
        monster.wisdomScore = getInt(rootDict, "wisPoints");
        monster.charismaScore = getInt(rootDict, "chaPoints");
        addSense(monster, rootDict, "blindsight");
        addSense(monster, rootDict, "darkvision");
        addSense(monster, rootDict, "tremorsense");
        addSense(monster, rootDict, "truesight");
        monster.telepathyRange = getInt(rootDict, "telepathy");
        monster.challengeRating = ChallengeRatingConverter.challengeRatingFromStringValue(getString(rootDict, "cr"));
        monster.customChallengeRatingDescription = getString(rootDict, "customCr");
        monster.customProficiencyBonus = getInt(rootDict, "customProf");

        monster.abilities = getListOfTraits(rootDict, "abilities");
        monster.actions = getListOfTraits(rootDict, "actions");
        monster.reactions = getListOfTraits(rootDict, "reactions");
        monster.legendaryActions = getListOfTraits(rootDict, "legendaries");
        monster.lairActions = getListOfTraits(rootDict, "lairs");
        monster.regionalActions = getListOfTraits(rootDict, "regionals");
        addSavingThrows(monster, rootDict);
        monster.skills = getSetOfSkills(rootDict);
        monster.damageImmunities = getSetOfDamageTypes(rootDict, "damageTypes", "i");
        monster.damageImmunities.addAll(getSetOfDamageTypes(rootDict, "specialdamage", "i"));
        monster.damageResistances = getSetOfDamageTypes(rootDict, "damageTypes", "r");
        monster.damageResistances.addAll(getSetOfDamageTypes(rootDict, "specialdamage", "r"));
        monster.damageVulnerabilities = getSetOfDamageTypes(rootDict, "damageTypes", "v");
        monster.damageVulnerabilities.addAll(getSetOfDamageTypes(rootDict, "specialdamage", "v"));
        monster.conditionImmunities = getSetOfDamageTypes(rootDict, "conditions");
        monster.languages = getSetOfLanguages(rootDict, "languages");
        monster.understandsButDescription = getString(rootDict, "understandsBut");

        return monster;
    }

    private static String getString(JsonObject dict, String name) {
        return getString(dict, name, "");
    }

    private static String getString(@NonNull JsonObject dict, String name, String defaultValue) {
        if (dict.has(name)) {
            return dict.get(name).getAsString();
        }
        return defaultValue;
    }

    private static int getInt(JsonObject dict, String name) {
        return getInt(dict, name, 0);
    }

    private static int getInt(@NonNull JsonObject dict, String name, int defaultValue) {
        if (dict.has(name)) {
            JsonElement element = dict.get(name);
            if (element.isJsonPrimitive()) {
                JsonPrimitive rawValue = element.getAsJsonPrimitive();
                if (rawValue.isNumber()) {
                    return rawValue.getAsInt();
                } else {
                    try {
                        return rawValue.getAsInt();
                    } catch (Exception ex) {
                        return defaultValue;
                    }
                }
            }
        }
        return defaultValue;
    }

    private static boolean getBool(JsonObject dict, String name) {
        return getBool(dict, name, false);
    }

    private static boolean getBool(@NonNull JsonObject dict, String name, boolean defaultValue) {
        if (dict.has(name)) {
            JsonElement element = dict.get(name);
            if (element.isJsonPrimitive()) {
                JsonPrimitive rawValue = element.getAsJsonPrimitive();
                if (rawValue.isBoolean()) {
                    return rawValue.getAsBoolean();
                } else {
                    try {
                        return rawValue.getAsBoolean();
                    } catch (Exception ex) {
                        return defaultValue;
                    }
                }
            }
        }
        return defaultValue;
    }

    @NonNull
    private static String formatDistance(String name, int distance) {
        return String.format(Locale.getDefault(), "%s %d ft.", name, distance);
    }

    private static void addSense(Monster monster, JsonObject root, String name) {
        int distance = getInt(root, name);
        if (distance > 0) {
            monster.senses.add(formatDistance(name, distance));
        }
    }

    @NonNull
    private static List<Trait> getListOfTraits(@NonNull JsonObject dict, String name) {
        ArrayList<Trait> traits = new ArrayList<>();
        if (dict.has(name)) {
            JsonElement arrayElement = dict.get(name);
            if (arrayElement.isJsonArray()) {
                JsonArray array = arrayElement.getAsJsonArray();
                int size = array.size();
                for (int index = 0; index < size; index++) {
                    JsonElement jsonElement = array.get(index);
                    if (jsonElement.isJsonObject()) {
                        JsonObject jsonObject = jsonElement.getAsJsonObject();
                        String traitName = getString(jsonObject, "name");
                        String description = getString(jsonObject, "desc");
                        Trait trait = new Trait(traitName, description);
                        traits.add(trait);
                    }
                }
            }
        }
        return traits;
    }

    private static void addSavingThrows(Monster monster, @NonNull JsonObject root) {
        if (root.has("sthrows")) {
            JsonElement arrayElement = root.get("sthrows");
            if (arrayElement.isJsonArray()) {
                JsonArray array = arrayElement.getAsJsonArray();
                int size = array.size();
                for (int index = 0; index < size; index++) {
                    JsonElement jsonElement = array.get(index);
                    if (jsonElement.isJsonObject()) {
                        JsonObject jsonObject = jsonElement.getAsJsonObject();
                        String name = getString(jsonObject, "name");
                        if ("str".equals(name)) {
                            monster.strengthSavingThrowProficiency = ProficiencyType.PROFICIENT;
                        } else if ("dex".equals(name)) {
                            monster.dexteritySavingThrowProficiency = ProficiencyType.PROFICIENT;
                        } else if ("con".equals(name)) {
                            monster.constitutionSavingThrowProficiency = ProficiencyType.PROFICIENT;
                        } else if ("int".equals(name)) {
                            monster.intelligenceSavingThrowProficiency = ProficiencyType.PROFICIENT;
                        } else if ("wis".equals(name)) {
                            monster.wisdomSavingThrowProficiency = ProficiencyType.PROFICIENT;
                        } else if ("cha".equals(name)) {
                            monster.charismaSavingThrowProficiency = ProficiencyType.PROFICIENT;
                        }
                    }
                }
            }
        }
    }

    @NonNull
    private static Set<Skill> getSetOfSkills(@NonNull JsonObject root) {
        HashSet<Skill> skills = new HashSet<>();
        if (root.has("skills")) {
            JsonElement arrayElement = root.get("skills");
            if (arrayElement.isJsonArray()) {
                JsonArray array = arrayElement.getAsJsonArray();
                int size = array.size();
                for (int index = 0; index < size; index++) {
                    JsonElement jsonElement = array.get(index);
                    if (jsonElement.isJsonObject()) {
                        JsonObject jsonObject = jsonElement.getAsJsonObject();
                        String name = getString(jsonObject, "name");
                        String stat = getString(jsonObject, "stat");
                        String note = getString(jsonObject, "note");

                        Skill skill = new Skill(name, AbilityScore.valueOfString(stat), AdvantageType.NONE, " (ex)".equals(note) ? ProficiencyType.EXPERTISE : ProficiencyType.PROFICIENT);
                        skills.add(skill);
                    }
                }
            }
        }
        return skills;
    }

    @NonNull
    private static Set<String> getSetOfDamageTypes(JsonObject rootDict, String name) {
        return getSetOfDamageTypes(rootDict, name, null);
    }

    @NonNull
    private static Set<String> getSetOfDamageTypes(JsonObject rootDict, String name, String type) {
        HashSet<String> damageTypes = new HashSet<>();
        if (rootDict.has(name)) {
            JsonElement arrayElement = rootDict.get(name);
            if (arrayElement.isJsonArray()) {
                JsonArray array = arrayElement.getAsJsonArray();
                int size = array.size();
                for (int index = 0; index < size; index++) {
                    JsonElement jsonElement = array.get(index);
                    if (jsonElement.isJsonObject()) {
                        JsonObject jsonObject = jsonElement.getAsJsonObject();
                        String dtName = getString(jsonObject, "name");
                        String dtType = getString(jsonObject, "type");
                        if (type == null || type.equals(dtType)) {
                            damageTypes.add(dtName);
                        }
                    }
                }
            }
        }
        return damageTypes;
    }

    @NonNull
    private static Set<Language> getSetOfLanguages(@NonNull JsonObject root, String name) {
        HashSet<Language> languages = new HashSet<>();
        if (root.has(name)) {
            JsonElement arrayElement = root.get(name);
            if (arrayElement.isJsonArray()) {
                JsonArray array = arrayElement.getAsJsonArray();
                int size = array.size();
                for (int index = 0; index < size; index++) {
                    JsonElement jsonElement = array.get(index);
                    if (jsonElement.isJsonObject()) {
                        JsonObject jsonObject = jsonElement.getAsJsonObject();
                        String languageName = getString(jsonObject, "name");
                        boolean canSpeak = getBool(jsonObject, "speaks");
                        Language language = new Language(languageName, canSpeak);
                        languages.add(language);
                    }
                }
            }
        }
        return languages;
    }
}
