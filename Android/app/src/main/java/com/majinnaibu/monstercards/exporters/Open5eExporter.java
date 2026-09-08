package com.majinnaibu.monstercards.exporters;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.majinnaibu.monstercards.data.enums.AbilityScore;
import com.majinnaibu.monstercards.data.enums.ArmorType;
import com.majinnaibu.monstercards.data.enums.ProficiencyType;
import com.majinnaibu.monstercards.models.Language;
import com.majinnaibu.monstercards.models.Monster;
import com.majinnaibu.monstercards.models.Skill;
import com.majinnaibu.monstercards.models.Trait;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class Open5eExporter {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    @NonNull
    public String exportMonster(@NonNull Monster monster) {
        JsonObject root = new JsonObject();
        root.addProperty("$schema", "../../../schema/entity.json");
        root.addProperty("uuid", monster.id != null ? monster.id.toString() : UUID.randomUUID().toString());
        root.addProperty("ruleset_id", "open5e");
        root.addProperty("entity_type", "character");
        root.addProperty("display_name", monster.name != null && !monster.name.isEmpty() ? monster.name : "Unnamed Entity");
        root.addProperty("description", buildDescription(monster));
        root.addProperty("version", "1.0.0");
        root.addProperty("template_id", "stat_block");

        JsonObject props = new JsonObject();
        props.addProperty("name", monster.name != null ? monster.name : "");
        props.addProperty("size", monster.size != null ? monster.size : "");
        props.addProperty("type", monster.type != null ? monster.type : "");
        props.addProperty("subtype", monster.subtype != null ? monster.subtype : "");
        props.addProperty("alignment", monster.alignment != null ? monster.alignment : "");
        props.addProperty("armor_class", monster.getArmorClassValue());
        props.addProperty("armor_description", monster.getArmorClass());

        if (monster.armorType != null && !monster.armorType.equals(ArmorType.NONE)) {
            JsonObject armorObj = new JsonObject();
            armorObj.addProperty("id", monster.armorType.stringValue);
            armorObj.addProperty("name", monster.armorType.displayName);
            armorObj.addProperty("armor_category", getArmorCategory(monster.armorType));
            armorObj.addProperty("ac_base", monster.armorType.baseArmorClass);

            Integer dexCap = getDexCap(monster.armorType);
            if (dexCap != null) {
                armorObj.addProperty("dex_cap", dexCap);
            } else {
                armorObj.add("dex_cap", JsonNull.INSTANCE);
            }
            armorObj.addProperty("stealth_disadvantage", isStealthDisadvantage(monster.armorType));
            props.add("equipped_armor", armorObj);
        } else {
            props.add("equipped_armor", JsonNull.INSTANCE);
        }

        if (monster.shieldBonus > 0) {
            JsonObject shieldObj = new JsonObject();
            shieldObj.addProperty("id", "shield");
            shieldObj.addProperty("name", "Shield");
            shieldObj.addProperty("ac_bonus", monster.shieldBonus);
            props.add("equipped_shield", shieldObj);
        } else {
            props.add("equipped_shield", JsonNull.INSTANCE);
        }

        JsonObject hpObj = new JsonObject();
        hpObj.addProperty("max", monster.getHitPointsValue());
        hpObj.addProperty("current", monster.getHitPointsValue());
        hpObj.addProperty("formula", monster.hasCustomHP && !monster.customHPDescription.isEmpty() ? monster.customHPDescription : monster.getHitPoints());
        hpObj.addProperty("hit_dice", monster.hitDice + "d8");
        props.add("hit_points", hpObj);

        props.addProperty("speed", monster.walkSpeed);
        props.addProperty("speed_desc", monster.getSpeedText());
        props.addProperty("challenge_rating", monster.getChallengeRatingDescription());
        props.addProperty("cr", monster.challengeRating != null ? monster.challengeRating.stringValue : "1");

        JsonObject abilitiesObj = new JsonObject();
        abilitiesObj.addProperty("strength", monster.strengthScore);
        abilitiesObj.addProperty("dexterity", monster.dexterityScore);
        abilitiesObj.addProperty("constitution", monster.constitutionScore);
        abilitiesObj.addProperty("intelligence", monster.intelligenceScore);
        abilitiesObj.addProperty("wisdom", monster.wisdomScore);
        abilitiesObj.addProperty("charisma", monster.charismaScore);
        props.add("abilities", abilitiesObj);

        if (monster.playerName != null && !monster.playerName.isEmpty()) props.addProperty("player_name", monster.playerName);
        if (monster.background != null && !monster.background.isEmpty()) props.addProperty("background", monster.background);
        if (monster.personalityTraits != null && !monster.personalityTraits.isEmpty()) props.addProperty("personality_traits", monster.personalityTraits);
        if (monster.ideals != null && !monster.ideals.isEmpty()) props.addProperty("ideals", monster.ideals);
        if (monster.bonds != null && !monster.bonds.isEmpty()) props.addProperty("bonds", monster.bonds);
        if (monster.flaws != null && !monster.flaws.isEmpty()) props.addProperty("flaws", monster.flaws);

        if (hasDetails(monster)) {
            JsonObject detailsObj = new JsonObject();
            if (monster.age != null && !monster.age.isEmpty()) detailsObj.addProperty("age", monster.age);
            if (monster.height != null && !monster.height.isEmpty()) detailsObj.addProperty("height", monster.height);
            if (monster.weight != null && !monster.weight.isEmpty()) detailsObj.addProperty("weight", monster.weight);
            if (monster.eyes != null && !monster.eyes.isEmpty()) detailsObj.addProperty("eyes", monster.eyes);
            if (monster.skin != null && !monster.skin.isEmpty()) detailsObj.addProperty("skin", monster.skin);
            if (monster.hair != null && !monster.hair.isEmpty()) detailsObj.addProperty("hair", monster.hair);
            if (monster.appearance != null && !monster.appearance.isEmpty()) detailsObj.addProperty("appearance", monster.appearance);
            if (monster.backstory != null && !monster.backstory.isEmpty()) detailsObj.addProperty("backstory", monster.backstory);
            if (monster.alliesAndOrganizations != null && !monster.alliesAndOrganizations.isEmpty()) detailsObj.addProperty("allies_and_organizations", monster.alliesAndOrganizations);
            props.add("character_details", detailsObj);
        }

        JsonArray savesArr = buildSavingThrowsArray(monster);
        if (savesArr.size() > 0) props.add("saving_throws", savesArr);

        JsonArray skillsArr = buildSkillsArray(monster);
        if (skillsArr.size() > 0) props.add("skills", skillsArr);

        if (monster.damageImmunities != null && !monster.damageImmunities.isEmpty()) props.add("damage_immunities", toJsonArray(monster.damageImmunities));
        if (monster.damageResistances != null && !monster.damageResistances.isEmpty()) props.add("damage_resistances", toJsonArray(monster.damageResistances));
        if (monster.damageVulnerabilities != null && !monster.damageVulnerabilities.isEmpty()) props.add("damage_vulnerabilities", toJsonArray(monster.damageVulnerabilities));
        if (monster.conditionImmunities != null && !monster.conditionImmunities.isEmpty()) props.add("condition_immunities", toJsonArray(monster.conditionImmunities));

        props.addProperty("senses", monster.getSensesDescription());

        if (monster.languages != null && !monster.languages.isEmpty()) {
            JsonArray langArr = new JsonArray();
            for (Language l : monster.languages) {
                if (l != null && l.getName() != null && !l.getName().isEmpty()) {
                    langArr.add(l.getName());
                }
            }
            props.add("languages", langArr);
        }

        if (monster.abilities != null && !monster.abilities.isEmpty()) props.add("traits", traitsToJsonArray(monster.abilities));
        if (monster.actions != null && !monster.actions.isEmpty()) props.add("actions", traitsToJsonArray(monster.actions));
        if (monster.bonusActions != null && !monster.bonusActions.isEmpty()) props.add("bonus_actions", traitsToJsonArray(monster.bonusActions));
        if (monster.reactions != null && !monster.reactions.isEmpty()) props.add("reactions", traitsToJsonArray(monster.reactions));
        if (monster.legendaryActions != null && !monster.legendaryActions.isEmpty()) props.add("legendary_actions", traitsToJsonArray(monster.legendaryActions));
        if (monster.legendaryActionsDescription != null && !monster.legendaryActionsDescription.isEmpty()) props.addProperty("legendary_desc", monster.legendaryActionsDescription);
        if (monster.lairActions != null && !monster.lairActions.isEmpty()) props.add("lair_actions", traitsToJsonArray(monster.lairActions));
        if (monster.lairActionsDescription != null && !monster.lairActionsDescription.isEmpty()) props.addProperty("lair_desc", monster.lairActionsDescription);
        if (monster.regionalActions != null && !monster.regionalActions.isEmpty()) props.add("regional_effects", traitsToJsonArray(monster.regionalActions));
        if (monster.regionalActionsDescription != null && !monster.regionalActionsDescription.isEmpty()) props.addProperty("regional_desc", monster.regionalActionsDescription);
        if (monster.mythicActions != null && !monster.mythicActions.isEmpty()) props.add("mythic_actions", traitsToJsonArray(monster.mythicActions));
        if (monster.mythicActionsDescription != null && !monster.mythicActionsDescription.isEmpty()) props.addProperty("mythic_desc", monster.mythicActionsDescription);

        if (monster.sourceUrl != null && !monster.sourceUrl.isEmpty()) props.addProperty("source_url", monster.sourceUrl);

        root.add("properties", props);
        return GSON.toJson(root);
    }

    private String getArmorCategory(ArmorType armorType) {
        switch (armorType) {
            case PADDED:
            case LEATHER:
            case STUDDED_LEATHER:
                return "light";
            case HIDE:
            case CHAIN_SHIRT:
            case SCALE_MAIL:
            case BREASTPLATE:
            case HALF_PLATE:
                return "medium";
            case RING_MAIL:
            case CHAIN_MAIL:
            case SPLINT_MAIL:
            case PLATE_MAIL:
                return "heavy";
            default:
                return "natural";
        }
    }

    private Integer getDexCap(ArmorType armorType) {
        switch (armorType) {
            case HIDE:
            case CHAIN_SHIRT:
            case SCALE_MAIL:
            case BREASTPLATE:
            case HALF_PLATE:
                return 2;
            case RING_MAIL:
            case CHAIN_MAIL:
            case SPLINT_MAIL:
            case PLATE_MAIL:
                return 0;
            default:
                return null;
        }
    }

    private boolean isStealthDisadvantage(ArmorType armorType) {
        switch (armorType) {
            case PADDED:
            case SCALE_MAIL:
            case HALF_PLATE:
            case RING_MAIL:
            case CHAIN_MAIL:
            case SPLINT_MAIL:
            case PLATE_MAIL:
                return true;
            default:
                return false;
        }
    }

    private boolean hasDetails(Monster monster) {
        return (monster.age != null && !monster.age.isEmpty()) ||
               (monster.height != null && !monster.height.isEmpty()) ||
               (monster.weight != null && !monster.weight.isEmpty()) ||
               (monster.eyes != null && !monster.eyes.isEmpty()) ||
               (monster.skin != null && !monster.skin.isEmpty()) ||
               (monster.hair != null && !monster.hair.isEmpty()) ||
               (monster.appearance != null && !monster.appearance.isEmpty()) ||
               (monster.backstory != null && !monster.backstory.isEmpty()) ||
               (monster.alliesAndOrganizations != null && !monster.alliesAndOrganizations.isEmpty());
    }

    private String buildDescription(Monster monster) {
        StringBuilder sb = new StringBuilder();
        if (monster.size != null && !monster.size.isEmpty()) sb.append(monster.size).append(" ");
        if (monster.type != null && !monster.type.isEmpty()) sb.append(monster.type);
        if (monster.subtype != null && !monster.subtype.isEmpty()) sb.append(" (").append(monster.subtype).append(")");
        if (monster.alignment != null && !monster.alignment.isEmpty()) sb.append(", ").append(monster.alignment);
        return sb.toString().trim();
    }

    private JsonArray buildSavingThrowsArray(Monster monster) {
        JsonArray arr = new JsonArray();
        checkSave(arr, "Strength", monster.getSavingThrowProficiencyType(AbilityScore.STRENGTH), monster.getAbilityModifier(AbilityScore.STRENGTH) + monster.getProficiencyBonus(monster.getSavingThrowProficiencyType(AbilityScore.STRENGTH)));
        checkSave(arr, "Dexterity", monster.getSavingThrowProficiencyType(AbilityScore.DEXTERITY), monster.getAbilityModifier(AbilityScore.DEXTERITY) + monster.getProficiencyBonus(monster.getSavingThrowProficiencyType(AbilityScore.DEXTERITY)));
        checkSave(arr, "Constitution", monster.getSavingThrowProficiencyType(AbilityScore.CONSTITUTION), monster.getAbilityModifier(AbilityScore.CONSTITUTION) + monster.getProficiencyBonus(monster.getSavingThrowProficiencyType(AbilityScore.CONSTITUTION)));
        checkSave(arr, "Intelligence", monster.getSavingThrowProficiencyType(AbilityScore.INTELLIGENCE), monster.getAbilityModifier(AbilityScore.INTELLIGENCE) + monster.getProficiencyBonus(monster.getSavingThrowProficiencyType(AbilityScore.INTELLIGENCE)));
        checkSave(arr, "Wisdom", monster.getSavingThrowProficiencyType(AbilityScore.WISDOM), monster.getAbilityModifier(AbilityScore.WISDOM) + monster.getProficiencyBonus(monster.getSavingThrowProficiencyType(AbilityScore.WISDOM)));
        checkSave(arr, "Charisma", monster.getSavingThrowProficiencyType(AbilityScore.CHARISMA), monster.getAbilityModifier(AbilityScore.CHARISMA) + monster.getProficiencyBonus(monster.getSavingThrowProficiencyType(AbilityScore.CHARISMA)));
        return arr;
    }

    private void checkSave(JsonArray arr, String statName, ProficiencyType prof, int bonus) {
        if (prof != null && !prof.equals(ProficiencyType.NONE)) {
            arr.add(String.format(Locale.ROOT, "%s %+d", statName, bonus));
        }
    }

    private JsonArray buildSkillsArray(Monster monster) {
        JsonArray arr = new JsonArray();
        if (monster.skills != null) {
            for (Skill s : monster.skills) {
                if (s != null && s.proficiencyType != null && !s.proficiencyType.equals(ProficiencyType.NONE)) {
                    int bonus = s.getSkillBonus(monster);
                    arr.add(String.format(Locale.ROOT, "%s %+d", s.name.toLowerCase(Locale.ROOT), bonus));
                }
            }
        }
        return arr;
    }

    private JsonArray toJsonArray(Collection<String> items) {
        JsonArray arr = new JsonArray();
        for (String s : items) {
            if (s != null && !s.isEmpty()) arr.add(s);
        }
        return arr;
    }

    private JsonArray traitsToJsonArray(List<Trait> traits) {
        JsonArray arr = new JsonArray();
        for (Trait t : traits) {
            if (t != null && t.name != null && !t.name.isEmpty()) {
                JsonObject obj = new JsonObject();
                obj.addProperty("name", t.name);
                obj.addProperty("desc", t.description != null ? t.description : "");
                arr.add(obj);
            }
        }
        return arr;
    }
}
