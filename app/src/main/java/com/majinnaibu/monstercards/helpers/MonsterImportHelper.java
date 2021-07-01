package com.majinnaibu.monstercards.helpers;

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

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MonsterImportHelper {
    public static Monster fromJSON(String json) {
        JsonParser parser = new JsonParser();
        JsonObject rootDict = parser.parse(json).getAsJsonObject();

        Monster monster = new Monster();
        monster.name = Helpers.getString(rootDict, "name");
        monster.size = Helpers.getString(rootDict, "size");
        monster.type = Helpers.getString(rootDict, "type");
        monster.subtype = Helpers.getString(rootDict, "tag");
        monster.alignment = Helpers.getString(rootDict, "alignment");
        monster.hitDice = Helpers.getInt(rootDict, "hitDice");
        monster.armorType = ArmorTypeConverter.armorTypeFromStringValue(Helpers.getString(rootDict, "armorName"));
        monster.shieldBonus = Helpers.getInt(rootDict, "shieldBonus");
        monster.naturalArmorBonus = Helpers.getInt(rootDict, "natArmorBonus");
        monster.otherArmorDescription = Helpers.getString(rootDict, "otherArmorDesc");
        monster.walkSpeed = Helpers.getInt(rootDict, "speed");
        monster.burrowSpeed = Helpers.getInt(rootDict, "burrowSpeed");
        monster.climbSpeed = Helpers.getInt(rootDict, "climbSpeed");
        monster.flySpeed = Helpers.getInt(rootDict, "flySpeed");
        monster.canHover = Helpers.getBool(rootDict, "hover");
        monster.swimSpeed = Helpers.getInt(rootDict, "swimSpeed");
        monster.hasCustomHP = Helpers.getBool(rootDict, "customHP");
        monster.hasCustomSpeed = Helpers.getBool(rootDict, "customSpeed");
        monster.customHPDescription = Helpers.getString(rootDict, "hpText");
        monster.customSpeedDescription = Helpers.getString(rootDict, "speedDesc");
        monster.strengthScore = Helpers.getInt(rootDict, "strPoints");
        monster.dexterityScore = Helpers.getInt(rootDict, "dexPoints");
        monster.constitutionScore = Helpers.getInt(rootDict, "conPoints");
        monster.intelligenceScore = Helpers.getInt(rootDict, "intPoints");
        monster.wisdomScore = Helpers.getInt(rootDict, "wisPoints");
        monster.charismaScore = Helpers.getInt(rootDict, "chaPoints");
        Helpers.addSense(monster, rootDict, "blindsight");
        // Helpers.getBool(rootDict, "blind");
        Helpers.addSense(monster, rootDict, "darkvision");
        Helpers.addSense(monster, rootDict, "tremorsense");
        Helpers.addSense(monster, rootDict, "truesight");
        monster.telepathyRange = Helpers.getInt(rootDict, "telepathy");
        monster.challengeRating = ChallengeRatingConverter.challengeRatingFromStringValue(Helpers.getString(rootDict, "cr"));
        monster.customChallengeRatingDescription = Helpers.getString(rootDict, "customCr");
        monster.customProficiencyBonus = Helpers.getInt(rootDict, "customProf");
        // Helpers.getBool(rootDict, "isLegendary");
        // Helpers.getString(rootDict, "legendariesDescription");
        // Helpers.getBool(rootDict, "isLair");
        // Helpers.getString(rootDict, "lairDescription");
        // Helpers.getString(rootDict, "lairDescriptionEnd");
        // Helpers.getBool(rootDict, "isRegional");
        // Helpers.getString(rootDict, "regionalDescription");
        // Helpers.getString(rootDict, "regionalDescriptionEnd");
        // properties: []
        monster.abilities = Helpers.getListOfTraits(rootDict, "abilities");
        monster.actions = Helpers.getListOfTraits(rootDict, "actions");
        monster.reactions = Helpers.getListOfTraits(rootDict, "reactions");
        monster.legendaryActions = Helpers.getListOfTraits(rootDict, "legendaries");
        monster.lairActions = Helpers.getListOfTraits(rootDict, "lairs");
        monster.regionalActions = Helpers.getListOfTraits(rootDict, "regionals");
        Helpers.addSavingThrows(monster, rootDict);
        // skills: []
        monster.skills = Helpers.getSetOfSkills(rootDict);
        // damagetypes: []
        // specialdamage: []
        monster.damageImmunities = Helpers.getSetOfDamageTypes(rootDict, "damageTypes", "i");
        monster.damageImmunities.addAll(Helpers.getSetOfDamageTypes(rootDict, "specialdamage", "i"));
        monster.damageResistances = Helpers.getSetOfDamageTypes(rootDict, "damageTypes", "r");
        monster.damageResistances.addAll(Helpers.getSetOfDamageTypes(rootDict, "specialdamage", "r"));
        monster.damageVulnerabilities = Helpers.getSetOfDamageTypes(rootDict, "damageTypes", "v");
        monster.damageVulnerabilities.addAll(Helpers.getSetOfDamageTypes(rootDict, "specialdamage", "v"));
        // conditions: []
        monster.conditionImmunities = Helpers.getSetOfDamageTypes(rootDict, "conditions");
        // languages: []
        monster.languages = Helpers.getSetOfLanguages(rootDict, "languages");
        // understandsBut: ""
        monster.understandsButDescription = Helpers.getString(rootDict, "understandsBut");
        // shortName: ""
        // doubleColumns: true
        // separationPoint: -1
        // damage: []
        // pluralName: ""

        return monster;
    }


    public static class Helpers {
        public static String getString(JsonObject dict, String name) {
            return getString(dict, name, "");
        }

        public static String getString(@NotNull JsonObject dict, String name, String defaultValue) {
            if (dict.has(name)) {
                return dict.get(name).getAsString();
            }

            return defaultValue;
        }

        public static int getInt(JsonObject dict, String name) {
            return getInt(dict, name, 0);
        }

        public static int getInt(@NotNull JsonObject dict, String name, int defaultValue) {
            if (dict.has(name)) {
                JsonElement element = dict.get(name);
                if (element.isJsonPrimitive()) {
                    JsonPrimitive rawValue = element.getAsJsonPrimitive();//dict.getAsJsonPrimitive(name);
                    if (rawValue.isNumber()) {
                        return rawValue.getAsInt();
                    }
                }
            }
            return defaultValue;
        }

        public static boolean getBool(JsonObject dict, String name) {
            return getBool(dict, name, false);
        }

        public static boolean getBool(@NotNull JsonObject dict, String name, boolean defaultValue) {
            if (dict.has(name)) {
                JsonElement element = dict.get(name);
                if (element.isJsonPrimitive()) {
                    JsonPrimitive rawValue = element.getAsJsonPrimitive();
                    if (rawValue.isBoolean()) {
                        return rawValue.getAsBoolean();
                    }
                }
            }
            return defaultValue;
        }

        @NotNull
        public static String formatDistance(String name, int distance) {
            return String.format("%s %d ft.", name, distance);
        }

        public static void addSense(Monster monster, JsonObject root, String name) {
            int distance = Helpers.getInt(root, name);
            if (distance > 0) {
                monster.senses.add(Helpers.formatDistance(name, distance));
            }
        }

        @NotNull
        public static List<Trait> getListOfTraits(@NotNull JsonObject dict, String name) {
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
                            String traitName = Helpers.getString(jsonObject, "name");
                            String description = Helpers.getString(jsonObject, "desc");
                            Trait trait = new Trait(traitName, description);
                            traits.add(trait);
                        }
                    }
                }
            }
            return traits;
        }

        public static void addSavingThrows(Monster monster, JsonObject root) {
            if (root.has("sthrows")) {
                JsonElement arrayElement = root.get("sthrows");
                if (arrayElement.isJsonArray()) {
                    JsonArray array = arrayElement.getAsJsonArray();
                    int size = array.size();
                    for (int index = 0; index < size; index++) {
                        JsonElement jsonElement = array.get(index);
                        if (jsonElement.isJsonObject()) {
                            JsonObject jsonObject = jsonElement.getAsJsonObject();
                            String name = Helpers.getString(jsonObject, "name");
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

        public static Set<Skill> getSetOfSkills(JsonObject root) {
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
                            String name = Helpers.getString(jsonObject, "name");
                            String stat = Helpers.getString(jsonObject, "stat");
                            String note = Helpers.getString(jsonObject, "note");

                            Skill skill = new Skill(name, AbilityScore.valueOfString(stat), AdvantageType.NONE, " (ex)".equals(note) ? ProficiencyType.EXPERTISE : ProficiencyType.PROFICIENT);
                            skills.add(skill);
                        }
                    }
                }
            }
            return skills;
        }

        public static Set<String> getSetOfDamageTypes(JsonObject rootDict, String name) {
            return getSetOfDamageTypes(rootDict, name, null);
        }

        public static Set<String> getSetOfDamageTypes(JsonObject root, String name, String type) {
            HashSet<String> damageTypes = new HashSet<>();
            if (root.has(name)) {
                JsonElement arrayElement = root.get(name);
                if (arrayElement.isJsonArray()) {
                    JsonArray array = arrayElement.getAsJsonArray();
                    int size = array.size();
                    for (int index = 0; index < size; index++) {
                        JsonElement jsonElement = array.get(index);
                        if (jsonElement.isJsonObject()) {
                            JsonObject jsonObject = jsonElement.getAsJsonObject();
                            String dtName = Helpers.getString(jsonObject, "name");
                            String dtType = Helpers.getString(jsonObject, "type");
                            if (type == null || type.equals(dtType)) {
                                damageTypes.add(dtName);
                            }
                        }
                    }
                }
            }
            return damageTypes;
        }

        public static Set<Language> getSetOfLanguages(JsonObject root, String name) {
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
                            String languageName = Helpers.getString(jsonObject, "name");
                            boolean canSpeak = Helpers.getBool(jsonObject, "speaks");
                            Language language = new Language(languageName, canSpeak);
                            languages.add(language);
                        }
                    }
                }
            }
            return languages;
        }
    }
}
