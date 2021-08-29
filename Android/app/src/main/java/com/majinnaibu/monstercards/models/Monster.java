package com.majinnaibu.monstercards.models;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.majinnaibu.monstercards.data.enums.AbilityScore;
import com.majinnaibu.monstercards.data.enums.AdvantageType;
import com.majinnaibu.monstercards.data.enums.ArmorType;
import com.majinnaibu.monstercards.data.enums.ChallengeRating;
import com.majinnaibu.monstercards.data.enums.ProficiencyType;
import com.majinnaibu.monstercards.helpers.StringHelper;
import com.majinnaibu.monstercards.utils.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity(tableName = "monsters")
@SuppressLint("DefaultLocale")
@SuppressWarnings("unused")
public class Monster {

    @PrimaryKey
    @NonNull
    public UUID id;

    @NonNull
    @ColumnInfo(defaultValue = "")
    public String name;

    @NonNull()
    @ColumnInfo(defaultValue = "")
    public String size;

    @NonNull()
    @ColumnInfo(defaultValue = "")
    public String type;

    @NonNull()
    @ColumnInfo(defaultValue = "")
    public String subtype;

    @NonNull()
    @ColumnInfo(defaultValue = "")
    public String alignment;

    @ColumnInfo(name = "strength_score", defaultValue = "10")
    public int strengthScore;

    @ColumnInfo(name = "strength_saving_throw_advantage", defaultValue = "none")
    public AdvantageType strengthSavingThrowAdvantage;

    @ColumnInfo(name = "strength_saving_throw_proficiency", defaultValue = "none")
    public ProficiencyType strengthSavingThrowProficiency;

    @ColumnInfo(name = "dexterity_score", defaultValue = "10")
    public int dexterityScore;

    @ColumnInfo(name = "dexterity_saving_throw_advantage", defaultValue = "none")
    public AdvantageType dexteritySavingThrowAdvantage;

    @ColumnInfo(name = "dexterity_saving_throw_proficiency", defaultValue = "none")
    public ProficiencyType dexteritySavingThrowProficiency;

    @ColumnInfo(name = "constitution_score", defaultValue = "10")
    public int constitutionScore;

    @ColumnInfo(name = "constitution_saving_throw_advantage", defaultValue = "none")
    public AdvantageType constitutionSavingThrowAdvantage;

    @ColumnInfo(name = "constitution_saving_throw_proficiency", defaultValue = "none")
    public ProficiencyType constitutionSavingThrowProficiency;

    @ColumnInfo(name = "intelligence_score", defaultValue = "10")
    public int intelligenceScore;

    @ColumnInfo(name = "intelligence_saving_throw_advantage", defaultValue = "none")
    public AdvantageType intelligenceSavingThrowAdvantage;

    @ColumnInfo(name = "intelligence_saving_throw_proficiency", defaultValue = "none")
    public ProficiencyType intelligenceSavingThrowProficiency;

    @ColumnInfo(name = "wisdom_score", defaultValue = "10")
    public int wisdomScore;

    @ColumnInfo(name = "wisdom_saving_throw_advantage", defaultValue = "none")
    public AdvantageType wisdomSavingThrowAdvantage;

    @ColumnInfo(name = "wisdom_saving_throw_proficiency", defaultValue = "none")
    public ProficiencyType wisdomSavingThrowProficiency;

    @ColumnInfo(name = "charisma_score", defaultValue = "10")
    public int charismaScore;

    @ColumnInfo(name = "charisma_saving_throw_advantage", defaultValue = "none")
    public AdvantageType charismaSavingThrowAdvantage;

    @ColumnInfo(name = "charisma_saving_throw_proficiency", defaultValue = "none")
    public ProficiencyType charismaSavingThrowProficiency;

    @ColumnInfo(name = "armor_type", defaultValue = "none"/* ArmorType.NONE.stringValue */)
    public ArmorType armorType;

    @ColumnInfo(name = "shield_bonus", defaultValue = "0")
    public int shieldBonus;

    @ColumnInfo(name = "natural_armor_bonus", defaultValue = "0")
    public int naturalArmorBonus;

    @ColumnInfo(name = "other_armor_description", defaultValue = "")
    public String otherArmorDescription;

    @ColumnInfo(name = "hit_dice", defaultValue = "1")
    public int hitDice;

    @ColumnInfo(name = "has_custom_hit_points", defaultValue = "")
    public boolean hasCustomHP;

    @ColumnInfo(name = "custom_hit_points_description", defaultValue = "")
    public String customHPDescription;

    @ColumnInfo(name = "walk_speed", defaultValue = "0")
    public int walkSpeed;

    @ColumnInfo(name = "burrow_speed", defaultValue = "0")
    public int burrowSpeed;

    @ColumnInfo(name = "climb_speed", defaultValue = "0")
    public int climbSpeed;

    @ColumnInfo(name = "fly_speed", defaultValue = "0")
    public int flySpeed;

    @ColumnInfo(name = "can_hover", defaultValue = "false")
    public boolean canHover;

    @ColumnInfo(name = "swim_speed", defaultValue = "0")
    public int swimSpeed;

    @ColumnInfo(name = "has_custom_speed", defaultValue = "false")
    public boolean hasCustomSpeed;

    @ColumnInfo(name = "custom_speed_description")
    public String customSpeedDescription;

    @ColumnInfo(name = "challenge_rating", defaultValue = "1")
    public ChallengeRating challengeRating;

    @ColumnInfo(name = "custom_challenge_rating_description", defaultValue = "")
    public String customChallengeRatingDescription;

    @ColumnInfo(name = "custom_proficiency_bonus", defaultValue = "0")
    public int customProficiencyBonus;

    @ColumnInfo(name = "telepathy_range", defaultValue = "0")
    public int telepathyRange;

    @ColumnInfo(name = "understands_but_description", defaultValue = "")
    public String understandsButDescription;

    @ColumnInfo(name = "senses", defaultValue = "[]")
    public Set<String> senses;

    @ColumnInfo(name = "skills", defaultValue = "[]")
    public Set<Skill> skills;

    @ColumnInfo(name = "damage_immunities", defaultValue = "[]")
    public Set<String> damageImmunities;

    @ColumnInfo(name = "damage_resistances", defaultValue = "[]")
    public Set<String> damageResistances;

    @ColumnInfo(name = "damage_vulnerabilities", defaultValue = "[]")
    public Set<String> damageVulnerabilities;

    @ColumnInfo(name = "condition_immunities", defaultValue = "[]")
    public Set<String> conditionImmunities;

    @ColumnInfo(name = "languages", defaultValue = "[]")
    public Set<Language> languages;

    @ColumnInfo(name = "abilities", defaultValue = "[]")
    public List<Trait> abilities;

    @ColumnInfo(name = "actions", defaultValue = "[]")
    public List<Trait> actions;

    @ColumnInfo(name = "reactions", defaultValue = "[]")
    public List<Trait> reactions;

    @ColumnInfo(name = "lair_actions", defaultValue = "[]")
    public List<Trait> lairActions;

    @ColumnInfo(name = "legendary_actions", defaultValue = "[]")
    public List<Trait> legendaryActions;

    @ColumnInfo(name = "regional_actions", defaultValue = "[]")
    public List<Trait> regionalActions;

    public Monster() {
        id = UUID.randomUUID();
        name = "";
        size = "";
        type = "";
        subtype = "";
        alignment = "";
        strengthScore = 10;
        dexterityScore = 10;
        constitutionScore = 10;
        intelligenceScore = 10;
        wisdomScore = 10;
        charismaScore = 10;
        armorType = ArmorType.NONE;
        shieldBonus = 0;
        naturalArmorBonus = 0;
        otherArmorDescription = "";
        hitDice = 1;
        hasCustomHP = false;
        customHPDescription = "";
        walkSpeed = 0;
        burrowSpeed = 0;
        climbSpeed = 0;
        flySpeed = 0;
        canHover = false;
        swimSpeed = 0;
        hasCustomSpeed = false;
        customSpeedDescription = "";
        challengeRating = ChallengeRating.ONE;
        customChallengeRatingDescription = "";
        customProficiencyBonus = 0;
        telepathyRange = 0;
        understandsButDescription = "";
        strengthSavingThrowAdvantage = AdvantageType.NONE;
        strengthSavingThrowProficiency = ProficiencyType.NONE;
        dexteritySavingThrowAdvantage = AdvantageType.NONE;
        dexteritySavingThrowProficiency = ProficiencyType.NONE;
        constitutionSavingThrowAdvantage = AdvantageType.NONE;
        constitutionSavingThrowProficiency = ProficiencyType.NONE;
        intelligenceSavingThrowAdvantage = AdvantageType.NONE;
        intelligenceSavingThrowProficiency = ProficiencyType.NONE;
        wisdomSavingThrowAdvantage = AdvantageType.NONE;
        wisdomSavingThrowProficiency = ProficiencyType.NONE;
        charismaSavingThrowAdvantage = AdvantageType.NONE;
        charismaSavingThrowProficiency = ProficiencyType.NONE;


        skills = new HashSet<>();
        senses = new HashSet<>();
        damageImmunities = new HashSet<>();
        damageResistances = new HashSet<>();
        damageVulnerabilities = new HashSet<>();
        conditionImmunities = new HashSet<>();
        languages = new HashSet<>();
        abilities = new ArrayList<>();
        actions = new ArrayList<>();
        reactions = new ArrayList<>();
        lairActions = new ArrayList<>();
        legendaryActions = new ArrayList<>();
        regionalActions = new ArrayList<>();
    }

    public static boolean areItemsTheSame(@NonNull Monster oldItem, @NonNull Monster newItem) {
        return Objects.equals(oldItem.id, newItem.id);
    }

    public static boolean areContentsTheSame(@NonNull Monster oldItem, @NonNull Monster newItem) {
        return Objects.equals(oldItem.abilities, newItem.abilities) &&
                Objects.equals(oldItem.actions, newItem.actions) &&
                Objects.equals(oldItem.alignment, newItem.alignment) &&
                Objects.equals(oldItem.armorType, newItem.armorType) &&
                Objects.equals(oldItem.burrowSpeed, newItem.burrowSpeed) &&
                Objects.equals(oldItem.canHover, newItem.canHover) &&
                Objects.equals(oldItem.challengeRating, newItem.challengeRating) &&
                Objects.equals(oldItem.charismaSavingThrowAdvantage, newItem.charismaSavingThrowAdvantage) &&
                Objects.equals(oldItem.charismaSavingThrowProficiency, newItem.charismaSavingThrowProficiency) &&
                Objects.equals(oldItem.charismaScore, newItem.charismaScore) &&
                Objects.equals(oldItem.climbSpeed, newItem.climbSpeed) &&
                Objects.equals(oldItem.conditionImmunities, newItem.conditionImmunities) &&
                Objects.equals(oldItem.constitutionSavingThrowAdvantage, newItem.constitutionSavingThrowAdvantage) &&
                Objects.equals(oldItem.constitutionSavingThrowProficiency, newItem.constitutionSavingThrowProficiency) &&
                Objects.equals(oldItem.constitutionScore, newItem.constitutionScore) &&
                Objects.equals(oldItem.customChallengeRatingDescription, newItem.customChallengeRatingDescription) &&
                Objects.equals(oldItem.customHPDescription, newItem.customHPDescription) &&
                Objects.equals(oldItem.customProficiencyBonus, newItem.customProficiencyBonus) &&
                Objects.equals(oldItem.customSpeedDescription, newItem.customSpeedDescription) &&
                Objects.equals(oldItem.damageImmunities, newItem.damageImmunities) &&
                Objects.equals(oldItem.damageResistances, newItem.damageResistances) &&
                Objects.equals(oldItem.damageVulnerabilities, newItem.damageVulnerabilities) &&
                Objects.equals(oldItem.dexteritySavingThrowAdvantage, newItem.dexteritySavingThrowAdvantage) &&
                Objects.equals(oldItem.dexteritySavingThrowProficiency, newItem.dexteritySavingThrowProficiency) &&
                Objects.equals(oldItem.dexterityScore, newItem.dexterityScore) &&
                Objects.equals(oldItem.flySpeed, newItem.flySpeed) &&
                Objects.equals(oldItem.hasCustomHP, newItem.hasCustomHP) &&
                Objects.equals(oldItem.hasCustomSpeed, newItem.hasCustomSpeed) &&
                Objects.equals(oldItem.hitDice, newItem.hitDice) &&
                Objects.equals(oldItem.intelligenceSavingThrowAdvantage, newItem.intelligenceSavingThrowAdvantage) &&
                Objects.equals(oldItem.intelligenceSavingThrowProficiency, newItem.intelligenceSavingThrowProficiency) &&
                Objects.equals(oldItem.intelligenceScore, newItem.intelligenceScore) &&
                Objects.equals(oldItem.lairActions, newItem.lairActions) &&
                Objects.equals(oldItem.languages, newItem.languages) &&
                Objects.equals(oldItem.legendaryActions, newItem.legendaryActions) &&
                Objects.equals(oldItem.name, newItem.name) &&
                Objects.equals(oldItem.naturalArmorBonus, newItem.naturalArmorBonus) &&
                Objects.equals(oldItem.otherArmorDescription, newItem.otherArmorDescription) &&
                Objects.equals(oldItem.reactions, newItem.reactions) &&
                Objects.equals(oldItem.regionalActions, newItem.regionalActions) &&
                Objects.equals(oldItem.senses, newItem.senses) &&
                Objects.equals(oldItem.shieldBonus, newItem.shieldBonus) &&
                Objects.equals(oldItem.size, newItem.size) &&
                Objects.equals(oldItem.skills, newItem.skills) &&
                Objects.equals(oldItem.strengthSavingThrowAdvantage, newItem.strengthSavingThrowAdvantage) &&
                Objects.equals(oldItem.strengthSavingThrowProficiency, newItem.strengthSavingThrowProficiency) &&
                Objects.equals(oldItem.strengthScore, newItem.strengthScore) &&
                Objects.equals(oldItem.subtype, newItem.subtype) &&
                Objects.equals(oldItem.swimSpeed, newItem.swimSpeed) &&
                Objects.equals(oldItem.telepathyRange, newItem.telepathyRange) &&
                Objects.equals(oldItem.type, newItem.type) &&
                Objects.equals(oldItem.understandsButDescription, newItem.understandsButDescription) &&
                Objects.equals(oldItem.wisdomSavingThrowAdvantage, newItem.wisdomSavingThrowAdvantage) &&
                Objects.equals(oldItem.wisdomSavingThrowProficiency, newItem.wisdomSavingThrowProficiency) &&
                Objects.equals(oldItem.wisdomScore, newItem.wisdomScore) &&
                Objects.equals(oldItem.walkSpeed, newItem.walkSpeed);
    }

    public String getMeta() {
        StringBuilder sb = new StringBuilder();
        boolean isFirstOutput = true;
        if (!StringHelper.isNullOrEmpty(size)) {
            sb.append(size);
            isFirstOutput = false;
        }

        if (!StringHelper.isNullOrEmpty(type)) {
            if (!isFirstOutput) {
                sb.append(" ");
            }
            sb.append(type);
            isFirstOutput = false;
        }

        if (!StringHelper.isNullOrEmpty(subtype)) {
            if (!isFirstOutput) {
                sb.append(" ");
            }
            sb.append("(");
            sb.append(subtype);
            sb.append(")");
            isFirstOutput = false;
        }

        if (!StringHelper.isNullOrEmpty(alignment)) {
            if (!isFirstOutput) {
                sb.append(", ");
            }
            sb.append(alignment);
        }

        return sb.toString();
    }

    public int getAbilityScore(@NonNull AbilityScore abilityScore) {
        switch (abilityScore) {
            case STRENGTH:
                return strengthScore;
            case DEXTERITY:
                return dexterityScore;
            case CONSTITUTION:
                return constitutionScore;
            case INTELLIGENCE:
                return intelligenceScore;
            case WISDOM:
                return wisdomScore;
            case CHARISMA:
                return charismaScore;
            default:
                return 10;
        }
    }

    public int getAbilityModifier(@NonNull AbilityScore abilityScore) {
        switch (abilityScore) {
            case STRENGTH:
                return getStrengthModifier();
            case DEXTERITY:
                return getDexterityModifier();
            case CONSTITUTION:
                return getConstitutionModifier();
            case INTELLIGENCE:
                return getIntelligenceModifier();
            case WISDOM:
                return getWisdomModifier();
            case CHARISMA:
                return getCharismaModifier();
            default:
                return 0;
        }
    }

    public AdvantageType getSavingThrowAdvantageType(@NonNull AbilityScore abilityScore) {
        switch (abilityScore) {
            case STRENGTH:
                return strengthSavingThrowAdvantage;
            case DEXTERITY:
                return dexteritySavingThrowAdvantage;
            case CONSTITUTION:
                return constitutionSavingThrowAdvantage;
            case INTELLIGENCE:
                return intelligenceSavingThrowAdvantage;
            case WISDOM:
                return wisdomSavingThrowAdvantage;
            case CHARISMA:
                return charismaSavingThrowAdvantage;
            default:
                return AdvantageType.NONE;
        }
    }

    public ProficiencyType getSavingThrowProficiencyType(@NonNull AbilityScore abilityScore) {
        switch (abilityScore) {
            case STRENGTH:
                return strengthSavingThrowProficiency;
            case DEXTERITY:
                return dexteritySavingThrowProficiency;
            case CONSTITUTION:
                return constitutionSavingThrowProficiency;
            case INTELLIGENCE:
                return intelligenceSavingThrowProficiency;
            case WISDOM:
                return wisdomSavingThrowProficiency;
            case CHARISMA:
                return charismaSavingThrowProficiency;
            default:
                return ProficiencyType.NONE;
        }
    }

    public int getStrengthModifier() {
        return Helpers.getAbilityModifierForScore(strengthScore);
    }

    public int getDexterityModifier() {
        return Helpers.getAbilityModifierForScore(dexterityScore);
    }

    public int getConstitutionModifier() {
        return Helpers.getAbilityModifierForScore(constitutionScore);
    }

    public int getIntelligenceModifier() {
        return Helpers.getAbilityModifierForScore(intelligenceScore);
    }

    public int getWisdomModifier() {
        return Helpers.getAbilityModifierForScore(wisdomScore);
    }

    public int getCharismaModifier() {
        return Helpers.getAbilityModifierForScore(charismaScore);
    }

    public String getArmorClass() {
        boolean hasShield = shieldBonus != 0;
        ArmorType armorType = this.armorType != null ? this.armorType : ArmorType.NONE;
        switch (armorType) {
            case NONE:
                // 10 + dexMod + 2 for shieldBonus "15" or "17 (shield)"
                return String.format("%d%s", armorType.baseArmorClass + getDexterityModifier() + shieldBonus, hasShield ? " (shield)" : "");
            case NATURAL_ARMOR:
                // 10 + dexMod + naturalArmorBonus + 2 for shieldBonus "16 (natural armor)" or "18 (natural armor, shield)"
                return String.format("%d (natural armor%s)", armorType.baseArmorClass + getDexterityModifier() + naturalArmorBonus + shieldBonus, hasShield ? ", shield" : "");
            case MAGE_ARMOR:
                // 10 + dexMod + 2 for shield + 3 for mage armor "15 (18 with mage armor)" or 17 (shield, 20 with mage armor)
                return String.format("%d (%s%d with mage armor)", armorType.baseArmorClass + getDexterityModifier() + shieldBonus, hasShield ? "shield, " : "", armorType.baseArmorClass + 3 + getDexterityModifier() + shieldBonus);
            case PADDED:
                // 11 + dexMod + 2 for shield "18 (padded armor, shield)"
                return String.format("%d (padded%s)", armorType.baseArmorClass + getDexterityModifier() + shieldBonus, hasShield ? ", shield" : "");
            case LEATHER:
                // 11 + dexMod + 2 for shield "18 (leather, shield)"
                return String.format("%d (leather%s)", armorType.baseArmorClass + getDexterityModifier() + shieldBonus, hasShield ? ", shield" : "");
            case STUDDED_LEATHER:
                // 12 + dexMod +2 for shield "17 (studded leather)"
                return String.format("%d (studded leather%s)", armorType.baseArmorClass + getDexterityModifier() + shieldBonus, hasShield ? ", shield" : "");
            case HIDE:
                // 12 + Min(2, dexMod) + 2 for shield "12 (hide armor)"
                return String.format("%d (hide%s)", armorType.baseArmorClass + Math.min(2, getDexterityModifier()) + shieldBonus, hasShield ? ", shield" : "");
            case CHAIN_SHIRT:
                // 13 + Min(2, dexMod) + 2 for shield "12 (chain shirt)"
                return String.format("%d (chain shirt%s)", armorType.baseArmorClass + Math.min(2, getDexterityModifier()) + shieldBonus, hasShield ? ", shield" : "");
            case SCALE_MAIL:
                // 14 + Min(2, dexMod) + 2 for shield "14 (scale mail)"
                return String.format("%d (scale mail%s)", armorType.baseArmorClass + Math.min(2, getDexterityModifier()) + shieldBonus, hasShield ? ", shield" : "");
            case BREASTPLATE:
                // 14 + Min(2, dexMod) + 2 for shield "16 (breastplate)"
                return String.format("%d (breastplate%s)", armorType.baseArmorClass + Math.min(2, getDexterityModifier()) + shieldBonus, hasShield ? ", shield" : "");
            case HALF_PLATE:
                // 15 + Min(2, dexMod) + 2 for shield "17 (half plate)"
                return String.format("%d (half plate%s)", armorType.baseArmorClass + Math.min(2, getDexterityModifier()) + shieldBonus, hasShield ? ", shield" : "");
            case RING_MAIL:
                // 14 + 2 for shield "14 (ring mail)
                return String.format("%d (ring mail%s)", armorType.baseArmorClass + shieldBonus, hasShield ? ", shield" : "");
            case CHAIN_MAIL:
                // 16 + 2 for shield "16 (chain mail)"
                return String.format("%d (chain mail%s)", armorType.baseArmorClass + shieldBonus, hasShield ? ", shield" : "");
            case SPLINT_MAIL:
                // 17 + 2 for shield "17 (splint)"
                return String.format("%d (splint%s)", armorType.baseArmorClass + shieldBonus, hasShield ? ", shield" : "");
            case PLATE_MAIL:
                // 18 + 2 for shield "18 (plate)"
                return String.format("%d (plate%s)", armorType.baseArmorClass + shieldBonus, hasShield ? ", shield" : "");
            case OTHER:
                // pure string value shield check does nothing just copies the string from otherArmorDesc
                return otherArmorDescription;
            default:
                return "";
        }
    }

    public int getArmorClassValue() {
        boolean hasShield = shieldBonus != 0;
        ArmorType armorType = this.armorType != null ? this.armorType : ArmorType.NONE;
        switch (armorType) {
            case NATURAL_ARMOR:
                // 10 + dexMod + naturalArmorBonus + 2 for shieldBonus "16 (natural armor)" or "18 (natural armor, shield)"
                return armorType.baseArmorClass + getDexterityModifier() + naturalArmorBonus + shieldBonus;
            case MAGE_ARMOR:
                // 10 + dexMod + 2 for shield + 3 for mage armor "15 (18 with mage armor)" or 17 (shield, 20 with mage armor)
                return armorType.baseArmorClass + 3 + getDexterityModifier() + shieldBonus;
            case NONE:
                // 10 + dexMod + 2 for shieldBonus "15" or "17 (shield)"
            case PADDED:
                // 11 + dexMod + 2 for shield "18 (padded armor, shield)"
            case LEATHER:
                // 11 + dexMod + 2 for shield "18 (leather, shield)"
            case STUDDED_LEATHER:
                // 12 + dexMod +2 for shield "17 (studded leather)"
                return armorType.baseArmorClass + getDexterityModifier() + shieldBonus;
            case HIDE:
                // 12 + Min(2, dexMod) + 2 for shield "12 (hide armor)"
            case CHAIN_SHIRT:
                // 13 + Min(2, dexMod) + 2 for shield "12 (chain shirt)"
            case SCALE_MAIL:
                // 14 + Min(2, dexMod) + 2 for shield "14 (scale mail)"
            case BREASTPLATE:
                // 14 + Min(2, dexMod) + 2 for shield "16 (breastplate)"
            case HALF_PLATE:
                // 15 + Min(2, dexMod) + 2 for shield "17 (half plate)"
                return armorType.baseArmorClass + Math.min(2, getDexterityModifier()) + shieldBonus;
            case RING_MAIL:
                // 14 + 2 for shield "14 (ring mail)
            case CHAIN_MAIL:
                // 16 + 2 for shield "16 (chain mail)"
            case SPLINT_MAIL:
                // 17 + 2 for shield "17 (splint)"
            case PLATE_MAIL:
                // 18 + 2 for shield "18 (plate)"
                return armorType.baseArmorClass + shieldBonus;
            case OTHER:
                // pure string value shield check does nothing just copies the string from otherArmorDesc
                return 0;
            default:
                Logger.logUnimplementedFeature(String.format("Getting the armor class value with an unknown armor type %s", armorType));
                return -1;
        }
    }

    public String getHitPoints() {
        if (hasCustomHP) {
            return customHPDescription;
        } else {
            int dieSize = Helpers.getHitDieForSize(size);
            int conMod = getConstitutionModifier();
            // For PC style calculations use this
            //int hpTotal = (int) Math.max(1, Math.ceil(dieSize + conMod + (hitDice - 1) * ((dieSize + 1) / 2.0 + conMod)));
            // For monster style calculations use this
            int hpTotal = (int) Math.max(1, Math.ceil(hitDice * ((dieSize + 1) / 2.0 + conMod)));
            return String.format("%d (%dd%d %+d)", hpTotal, hitDice, dieSize, conMod * hitDice);
        }
    }

    public int getHitPointsValue() {
        if (hasCustomHP) {
            return 0;
        } else {
            int dieSize = Helpers.getHitDieForSize(size);
            int conMod = getConstitutionModifier();
            // For PC style calculations use this
            //return (int) Math.max(1, Math.ceil(dieSize + conMod + (hitDice - 1) * ((dieSize + 1) / 2.0 + conMod)));
            // For monster style calculations use this
            return (int) Math.max(1, Math.ceil(hitDice * ((dieSize + 1) / 2.0 + conMod)));
        }
    }

    public String getSpeedText() {
        if (hasCustomSpeed) {
            return customSpeedDescription;
        } else {
            ArrayList<String> speedParts = new ArrayList<>();
            if (walkSpeed > 0) {
                speedParts.add(String.format("%d ft.", walkSpeed));
            }
            if (burrowSpeed > 0) {
                speedParts.add(String.format("burrow %d ft.", burrowSpeed));
            }
            if (climbSpeed > 0) {
                speedParts.add(String.format("climb %d ft.", climbSpeed));
            }
            if (flySpeed > 0) {
                speedParts.add(String.format("fly %d ft.%s", flySpeed, canHover ? " (hover)" : ""));
            }
            if (swimSpeed > 0) {
                speedParts.add(String.format("swim %d ft.", swimSpeed));
            }
            return StringHelper.join(", ", speedParts);
        }
    }

    public String getStrengthDescription() {
        return String.format("%d (%+d)", strengthScore, getStrengthModifier());
    }

    public String getDexterityDescription() {
        return String.format("%d (%+d)", dexterityScore, getDexterityModifier());
    }

    public String getConstitutionDescription() {
        return String.format("%d (%+d)", constitutionScore, getConstitutionModifier());
    }

    public String getIntelligenceDescription() {
        return String.format("%d (%+d)", intelligenceScore, getIntelligenceModifier());
    }

    public String getWisdomDescription() {
        return String.format("%d (%+d)", wisdomScore, getWisdomModifier());
    }

    public String getCharismaDescription() {
        return String.format("%d (%+d)", charismaScore, getCharismaModifier());
    }

    public String getSavingThrowsDescription() {
        List<String> parts = new ArrayList<>();
        for (AbilityScore abilityScore : AbilityScore.values()) {
            AdvantageType advantage = getSavingThrowAdvantageType(abilityScore);
            ProficiencyType proficiency = getSavingThrowProficiencyType(abilityScore);
            if (advantage != AdvantageType.NONE || proficiency != ProficiencyType.NONE) {
                int bonus = getAbilityModifier(abilityScore) + getProficiencyBonus(proficiency);
                String part = String.format("%s %+d%s", abilityScore.displayName, bonus, advantage != AdvantageType.NONE ? " " + advantage.label : "");
                parts.add(part);
            }
        }
        return StringHelper.join(", ", parts);
    }

    public int getProficiencyBonus() {
        ChallengeRating challengeRating = this.challengeRating != null ? this.challengeRating : ChallengeRating.ONE;
        if (challengeRating == ChallengeRating.CUSTOM) {
            return customProficiencyBonus;
        } else {
            return challengeRating.proficiencyBonus;
        }
    }

    public int getProficiencyBonus(@NonNull ProficiencyType proficiencyType) {
        switch (proficiencyType) {
            case PROFICIENT:
                return getProficiencyBonus();
            case EXPERTISE:
                return getProficiencyBonus() * 2;
            case NONE:
            default:
                return 0;
        }

    }

    public String getSkillsDescription() {
        Skill[] elements = new Skill[skills.size()];
        elements = skills.toArray(elements);
        Arrays.sort(elements);
        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        for (Skill skill : elements) {
            if (!isFirst) {
                sb.append(", ");
            }
            sb.append(skill.getText(this));
            isFirst = false;
        }
        return sb.toString();
    }

    public String getDamageVulnerabilitiesDescription() {
        ArrayList<String> vulnerabilities = new ArrayList<>();
        for (String damageType : damageVulnerabilities) {
            if (!StringHelper.isNullOrEmpty(damageType)) {
                vulnerabilities.add(damageType);
            }
        }
        Collections.sort(vulnerabilities);
        return StringHelper.oxfordJoin(", ", ", and ", " and ", vulnerabilities);
    }

    public String getDamageResistancesDescription() {
        ArrayList<String> resistances = new ArrayList<>();
        for (String damageType : damageResistances) {
            if (!StringHelper.isNullOrEmpty(damageType)) {
                resistances.add(damageType);
            }
        }
        Collections.sort(resistances);
        return StringHelper.oxfordJoin(", ", ", and ", " and ", resistances);
    }

    public String getDamageImmunitiesDescription() {
        ArrayList<String> immunities = new ArrayList<>();
        for (String damageType : damageImmunities) {
            if (!StringHelper.isNullOrEmpty(damageType)) {
                immunities.add(damageType);
            }
        }
        Collections.sort(immunities);
        return StringHelper.oxfordJoin(", ", ", and ", " and ", immunities);
    }

    public String getConditionImmunitiesDescription() {
        ArrayList<String> immunities = new ArrayList<>(conditionImmunities);
        Collections.sort(immunities);
        return StringHelper.oxfordJoin(", ", ", and ", " and ", immunities);
    }

    public String getSensesDescription() {
        ArrayList<String> parts = new ArrayList<>(senses);
        parts.add(String.format("passive Perception %d", 10 + getWisdomModifier()));
        return StringHelper.join(", ", parts);
    }

    public String getLanguagesDescription() {
        ArrayList<String> spokenLanguages = new ArrayList<>();
        ArrayList<String> understoodLanguages = new ArrayList<>();
        for (Language language : languages) {
            if (language != null) {
                if (language.getSpeaks()) {
                    spokenLanguages.add(language.getName());
                } else {
                    understoodLanguages.add(language.getName());
                }
            }
        }
        Collections.sort(spokenLanguages);
        Collections.sort(understoodLanguages);

        String spokenLanguagesString = StringHelper.oxfordJoin(", ", ", and ", " and ", spokenLanguages);
        String understoodLanguagesString = StringHelper.oxfordJoin(", ", ", and ", " and ", understoodLanguages);

        boolean hasUnderstandsBut = understandsButDescription.length() > 0;
        boolean hasTelepathy = telepathyRange > 0;
        String telepathyString = String.format(", telepathy %d ft.", telepathyRange);

        if (spokenLanguages.size() > 0) {
            if (understoodLanguages.size() > 0) {
                return String.format(
                        "%s, understands %s%s%s",
                        spokenLanguagesString,
                        understoodLanguagesString,
                        hasUnderstandsBut ? " but " + understandsButDescription : "",
                        hasTelepathy ? telepathyString : "");
            } else {
                return String.format(
                        "%s%s%s",
                        spokenLanguagesString,
                        hasUnderstandsBut ? " but " + understandsButDescription : "",
                        hasTelepathy ? telepathyString : "");
            }
        } else {
            if (understoodLanguages.size() > 0) {
                return String.format(
                        "understands %s%s%s",
                        understoodLanguagesString,
                        hasUnderstandsBut ? " but " + understandsButDescription : "",
                        hasTelepathy ? telepathyString : "");
            } else {
                return String.format(
                        "%S%s",
                        hasUnderstandsBut ? "none but " + understandsButDescription : "",
                        hasTelepathy ? telepathyString : "");
            }
        }
    }

    public String getChallengeRatingDescription() {
        ChallengeRating challengeRating = this.challengeRating != null ? this.challengeRating : ChallengeRating.ONE;
        if (challengeRating == ChallengeRating.CUSTOM) {
            return customChallengeRatingDescription;
        } else {
            return challengeRating.displayName;
        }
    }

    public List<String> getAbilityDescriptions() {
        ArrayList<String> abilityDescriptions = new ArrayList<>();
        for (Trait ability : abilities) {
            abilityDescriptions.add(getPlaceholderReplacedText(String.format("__%s__ %s", ability.name, ability.description)));
        }
        return abilityDescriptions;
    }

    public String getPlaceholderReplacedText(@NonNull String rawText) {
        return rawText
                .replaceAll("\\[STR SAVE]", String.format("%+d", getSpellSaveDC(AbilityScore.STRENGTH)))
                .replaceAll("\\[STR ATK]", String.format("%+d", getAttackBonus(AbilityScore.STRENGTH)))
                .replaceAll("\\[DEX SAVE]", String.format("%+d", getSpellSaveDC(AbilityScore.DEXTERITY)))
                .replaceAll("\\[DEX ATK]", String.format("%+d", getAttackBonus(AbilityScore.DEXTERITY)))
                .replaceAll("\\[CON SAVE]", String.format("%+d", getSpellSaveDC(AbilityScore.CONSTITUTION)))
                .replaceAll("\\[CON ATK]", String.format("%+d", getAttackBonus(AbilityScore.CONSTITUTION)))
                .replaceAll("\\[INT SAVE]", String.format("%+d", getSpellSaveDC(AbilityScore.INTELLIGENCE)))
                .replaceAll("\\[INT ATK]", String.format("%+d", getAttackBonus(AbilityScore.INTELLIGENCE)))
                .replaceAll("\\[WIS SAVE]", String.format("%+d", getSpellSaveDC(AbilityScore.WISDOM)))
                .replaceAll("\\[WIS ATK]", String.format("%+d", getAttackBonus(AbilityScore.WISDOM)))
                .replaceAll("\\[CHA SAVE]", String.format("%+d", getSpellSaveDC(AbilityScore.CHARISMA)))
                .replaceAll("\\[CHA ATK]", String.format("%+d", getAttackBonus(AbilityScore.CHARISMA)));
    }

    public int getSpellSaveDC(AbilityScore abilityScore) {
        return 8 + getProficiencyBonus() + getAbilityModifier(abilityScore);
    }

    public int getAttackBonus(AbilityScore abilityScore) {
        return getProficiencyBonus() + getAbilityModifier(abilityScore);
    }

    public List<String> getActionDescriptions() {
        ArrayList<String> actionDescriptions = new ArrayList<>();
        for (Trait action : actions) {
            actionDescriptions.add(getPlaceholderReplacedText(String.format("__%s__ %s", action.name, action.description)));
        }
        return actionDescriptions;
    }

    public List<String> getReactionDescriptions() {
        ArrayList<String> actionDescriptions = new ArrayList<>();
        for (Trait action : reactions) {
            actionDescriptions.add(getPlaceholderReplacedText(String.format("__%s__ %s", action.name, action.description)));
        }
        return actionDescriptions;
    }

    public List<String> getLegendaryActionDescriptions() {
        ArrayList<String> actionDescriptions = new ArrayList<>();
        for (Trait action : legendaryActions) {
            actionDescriptions.add(getPlaceholderReplacedText(String.format("__%s__ %s", action.name, action.description)));
        }
        return actionDescriptions;
    }

    public List<String> getLairActionDescriptions() {
        ArrayList<String> actionDescriptions = new ArrayList<>();
        for (Trait action : lairActions) {
            actionDescriptions.add(getPlaceholderReplacedText(String.format("__%s__ %s", action.name, action.description)));
        }
        return actionDescriptions;
    }

    public List<String> getRegionalActionDescriptions() {
        ArrayList<String> actionDescriptions = new ArrayList<>();
        for (Trait action : regionalActions) {
            actionDescriptions.add(getPlaceholderReplacedText(String.format("__%s__ %s", action.name, action.description)));
        }
        return actionDescriptions;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Monster)) {
            return false;
        }
        Monster other = (Monster) obj;
        if (!challengeRating.equals(other.challengeRating)) {
            return false;
        }
        if (!understandsButDescription.equals(other.understandsButDescription)) {
            return false;
        }
        if (!languages.equals(other.languages)) {
            return false;
        }
        if (!damageVulnerabilities.equals(other.damageVulnerabilities)) {
            return false;
        }
        if (!damageResistances.equals(other.damageResistances)) {
            return false;
        }
        if (!damageImmunities.equals(other.damageImmunities)) {
            return false;
        }
        if (!conditionImmunities.equals(other.conditionImmunities)) {
            return false;
        }
        if (!charismaSavingThrowProficiency.equals(other.charismaSavingThrowProficiency)) {
            return false;
        }
        if (!wisdomSavingThrowProficiency.equals(other.wisdomSavingThrowProficiency)) {
            return false;
        }
        if (!intelligenceSavingThrowProficiency.equals(other.intelligenceSavingThrowProficiency)) {
            return false;
        }
        if (constitutionSavingThrowProficiency.equals(other.constitutionSavingThrowProficiency)) {
            return false;
        }

        if (dexteritySavingThrowProficiency.equals(other.dexteritySavingThrowProficiency)) {
            return false;
        }

        if (strengthSavingThrowProficiency.equals(other.strengthSavingThrowProficiency)) {
            return false;
        }

        if (legendaryActions.equals(other.legendaryActions)) {
            return false;
        }

        if (customChallengeRatingDescription.equals(other.customChallengeRatingDescription)) {
            return false;
        }

        if (customSpeedDescription.equals(other.customSpeedDescription)) {
            return false;
        }

        if (customHPDescription.equals(other.customHPDescription)) {
            return false;
        }

        if (otherArmorDescription.equals(other.otherArmorDescription)) {
            return false;
        }

        if (alignment.equals(other.alignment)) {
            return false;
        }

        if (subtype.equals(other.subtype)) {
            return false;
        }

        if (abilities.equals(other.abilities)) {
            return false;
        }

        if (actions.equals(other.actions)) {
            return false;
        }

        if (armorType.equals(other.armorType)) {
            return false;
        }

        if (charismaSavingThrowAdvantage.equals(other.charismaSavingThrowAdvantage)) {
            return false;
        }

        if (constitutionSavingThrowAdvantage.equals(other.constitutionSavingThrowAdvantage)) {
            return false;
        }

        if (dexteritySavingThrowAdvantage.equals(other.dexteritySavingThrowAdvantage)) {
            return false;
        }

        if (hitDice == other.hitDice) {
            return false;
        }

        if (id.equals(other.id)) {
            return false;
        }

        if (intelligenceSavingThrowAdvantage.equals(other.intelligenceSavingThrowAdvantage)) {
            return false;
        }

        if (lairActions.equals(other.lairActions)) {
            return false;
        }

        if (name.equals(other.name)) {
            return false;
        }

        if (reactions.equals(other.reactions)) {
            return false;
        }

        if (regionalActions.equals(other.regionalActions)) {
            return false;
        }

        if (senses.equals(other.senses)) {
            return false;
        }

        if (shieldBonus == other.shieldBonus) {
            return false;
        }

        if (size.equals(other.size)) {
            return false;
        }

        if (skills.equals(other.skills)) {
            return false;
        }

        if (strengthSavingThrowAdvantage.equals(other.strengthSavingThrowAdvantage)) {
            return false;
        }

        if (strengthScore == other.strengthScore) {
            return false;
        }

        if (type.equals(other.type)) {
            return false;
        }

        if (wisdomSavingThrowAdvantage.equals(other.wisdomSavingThrowAdvantage)) {
            return false;
        }

        if (wisdomScore == other.wisdomScore) {
            return false;
        }
        if (customProficiencyBonus == other.customProficiencyBonus) {
            return false;
        }

        if (telepathyRange == other.telepathyRange) {
            return false;
        }

        if (intelligenceScore == other.intelligenceScore) {
            return false;
        }

        if (constitutionScore == other.constitutionScore) {
            return false;
        }

        if (dexterityScore == other.dexterityScore) {
            return false;
        }

        if (hasCustomSpeed == other.hasCustomSpeed) {
            return false;
        }

        if (hasCustomHP == other.hasCustomHP) {
            return false;
        }

        if (swimSpeed == other.swimSpeed) {
            return false;
        }

        if (flySpeed == other.flySpeed) {
            return false;
        }

        if (climbSpeed == other.climbSpeed) {
            return false;
        }

        if (burrowSpeed == other.burrowSpeed) {
            return false;
        }

        if (walkSpeed == other.walkSpeed) {
            return false;
        }

        if (naturalArmorBonus == other.naturalArmorBonus) {
            return false;
        }

        if (canHover == other.canHover) {
            return false;
        }

        if (charismaScore == other.charismaScore) {
            return false;
        }

        return true;
    }

    public static class Helpers {
        public static int getAbilityModifierForScore(int score) {
            return (int) Math.floor((score - 10) / 2.0);
        }

        public static int getHitDieForSize(String size) {
            if ("tiny".equals(size)) {
                return 4;
            } else if ("small".equals(size)) {
                return 6;
            } else if ("medium".equals(size)) {
                return 8;
            } else if ("large".equals(size)) {
                return 10;
            } else if ("huge".equals(size)) {
                return 12;
            } else if ("gargantuan".equals(size)) {
                return 20;
            } else {
                return 8;
            }
        }
    }
}
