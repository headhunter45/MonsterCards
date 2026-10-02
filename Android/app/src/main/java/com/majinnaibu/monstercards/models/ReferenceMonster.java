package com.majinnaibu.monstercards.models;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;
import com.majinnaibu.monstercards.data.enums.AdvantageType;
import com.majinnaibu.monstercards.data.enums.ArmorType;
import com.majinnaibu.monstercards.data.enums.ChallengeRating;
import com.majinnaibu.monstercards.data.enums.GameSystem;
import com.majinnaibu.monstercards.data.enums.ProficiencyType;
import com.majinnaibu.monstercards.helpers.StringHelper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity(tableName = "reference_monsters")
@SuppressLint("DefaultLocale")
@SuppressWarnings("unused")
public class ReferenceMonster {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    @SerializedName("id")
    public String id;

    @NonNull
    @ColumnInfo(name = "source_id", defaultValue = "")
    @SerializedName("sourceId")
    public String sourceId;

    @NonNull
    @ColumnInfo(name = "source_label", defaultValue = "")
    @SerializedName("sourceLabel")
    public String sourceLabel;

    @NonNull
    @ColumnInfo(name = "book_source", defaultValue = "")
    @SerializedName("bookSource")
    public String bookSource;

    @NonNull
    @ColumnInfo(name = "game_system", defaultValue = "DND_5E")
    @SerializedName("gameSystem")
    public GameSystem gameSystem;

    @NonNull
    @ColumnInfo(defaultValue = "")
    public String name;

    @NonNull
    @ColumnInfo(defaultValue = "")
    public String size;

    @NonNull
    @ColumnInfo(defaultValue = "")
    public String type;

    @NonNull
    @ColumnInfo(defaultValue = "")
    public String subtype;

    @NonNull
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

    @ColumnInfo(name = "armor_type", defaultValue = "none")
    public ArmorType armorType;

    @ColumnInfo(name = "shield_bonus", defaultValue = "0")
    public int shieldBonus;

    @ColumnInfo(name = "natural_armor_bonus", defaultValue = "0")
    public int naturalArmorBonus;

    @ColumnInfo(name = "other_armor_description", defaultValue = "")
    public String otherArmorDescription;

    @ColumnInfo(name = "hit_dice", defaultValue = "1")
    public int hitDice;

    @ColumnInfo(name = "has_custom_hit_points", defaultValue = "false")
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

    @ColumnInfo(name = "source_url", defaultValue = "")
    public String sourceUrl;

    @ColumnInfo(name = "bonus_actions", defaultValue = "[]")
    public List<Trait> bonusActions;

    @ColumnInfo(name = "mythic_actions", defaultValue = "[]")
    public List<Trait> mythicActions;

    @ColumnInfo(name = "legendary_actions_description", defaultValue = "")
    public String legendaryActionsDescription;

    @ColumnInfo(name = "lair_actions_description", defaultValue = "")
    public String lairActionsDescription;

    @ColumnInfo(name = "lair_actions_end_note", defaultValue = "")
    public String lairActionsEndNote;

    @ColumnInfo(name = "regional_actions_description", defaultValue = "")
    public String regionalActionsDescription;

    @ColumnInfo(name = "regional_actions_end_note", defaultValue = "")
    public String regionalActionsEndNote;

    @ColumnInfo(name = "mythic_actions_description", defaultValue = "")
    public String mythicActionsDescription;

    public ReferenceMonster() {
        id = UUID.randomUUID().toString();
        sourceId = "";
        sourceLabel = "";
        bookSource = "";
        gameSystem = GameSystem.DND_5E;
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
        bonusActions = new ArrayList<>();
        mythicActions = new ArrayList<>();
        sourceUrl = "";
        legendaryActionsDescription = "";
        lairActionsDescription = "";
        lairActionsEndNote = "";
        regionalActionsDescription = "";
        regionalActionsEndNote = "";
        mythicActionsDescription = "";
    }

    public static ReferenceMonster fromMonster(@NonNull Monster monster, @NonNull String sourceId, @Nullable String bookSource) {
        ReferenceMonster rm = new ReferenceMonster();
        rm.id = monster.id != null ? monster.id.toString() : UUID.randomUUID().toString();
        rm.sourceId = sourceId;
        rm.sourceLabel = !StringHelper.isNullOrEmpty(monster.sourceLabel) ? monster.sourceLabel : (!StringHelper.isNullOrEmpty(bookSource) ? bookSource : "");
        rm.bookSource = bookSource != null ? bookSource : "";
        rm.gameSystem = monster.gameSystem != null ? monster.gameSystem : GameSystem.DND_5E;
        rm.name = monster.name;
        rm.size = monster.size;
        rm.type = monster.type;
        rm.subtype = monster.subtype;
        rm.alignment = monster.alignment;
        rm.strengthScore = monster.strengthScore;
        rm.dexterityScore = monster.dexterityScore;
        rm.constitutionScore = monster.constitutionScore;
        rm.intelligenceScore = monster.intelligenceScore;
        rm.wisdomScore = monster.wisdomScore;
        rm.charismaScore = monster.charismaScore;
        rm.strengthSavingThrowAdvantage = monster.strengthSavingThrowAdvantage;
        rm.strengthSavingThrowProficiency = monster.strengthSavingThrowProficiency;
        rm.dexteritySavingThrowAdvantage = monster.dexteritySavingThrowAdvantage;
        rm.dexteritySavingThrowProficiency = monster.dexteritySavingThrowProficiency;
        rm.constitutionSavingThrowAdvantage = monster.constitutionSavingThrowAdvantage;
        rm.constitutionSavingThrowProficiency = monster.constitutionSavingThrowProficiency;
        rm.intelligenceSavingThrowAdvantage = monster.intelligenceSavingThrowAdvantage;
        rm.intelligenceSavingThrowProficiency = monster.intelligenceSavingThrowProficiency;
        rm.wisdomSavingThrowAdvantage = monster.wisdomSavingThrowAdvantage;
        rm.wisdomSavingThrowProficiency = monster.wisdomSavingThrowProficiency;
        rm.charismaSavingThrowAdvantage = monster.charismaSavingThrowAdvantage;
        rm.charismaSavingThrowProficiency = monster.charismaSavingThrowProficiency;
        rm.armorType = monster.armorType;
        rm.shieldBonus = monster.shieldBonus;
        rm.naturalArmorBonus = monster.naturalArmorBonus;
        rm.otherArmorDescription = monster.otherArmorDescription;
        rm.hitDice = monster.hitDice;
        rm.hasCustomHP = monster.hasCustomHP;
        rm.customHPDescription = monster.customHPDescription;
        rm.walkSpeed = monster.walkSpeed;
        rm.burrowSpeed = monster.burrowSpeed;
        rm.climbSpeed = monster.climbSpeed;
        rm.flySpeed = monster.flySpeed;
        rm.canHover = monster.canHover;
        rm.swimSpeed = monster.swimSpeed;
        rm.hasCustomSpeed = monster.hasCustomSpeed;
        rm.customSpeedDescription = monster.customSpeedDescription;
        rm.challengeRating = monster.challengeRating;
        rm.customChallengeRatingDescription = monster.customChallengeRatingDescription;
        rm.customProficiencyBonus = monster.customProficiencyBonus;
        rm.telepathyRange = monster.telepathyRange;
        rm.understandsButDescription = monster.understandsButDescription;
        rm.senses = new HashSet<>(monster.senses);
        rm.skills = new HashSet<>(monster.skills);
        rm.damageImmunities = new HashSet<>(monster.damageImmunities);
        rm.damageResistances = new HashSet<>(monster.damageResistances);
        rm.damageVulnerabilities = new HashSet<>(monster.damageVulnerabilities);
        rm.conditionImmunities = new HashSet<>(monster.conditionImmunities);
        rm.languages = new HashSet<>(monster.languages);
        rm.abilities = new ArrayList<>(monster.abilities);
        rm.actions = new ArrayList<>(monster.actions);
        rm.reactions = new ArrayList<>(monster.reactions);
        rm.lairActions = new ArrayList<>(monster.lairActions);
        rm.legendaryActions = new ArrayList<>(monster.legendaryActions);
        rm.regionalActions = new ArrayList<>(monster.regionalActions);
        rm.bonusActions = new ArrayList<>(monster.bonusActions);
        rm.mythicActions = new ArrayList<>(monster.mythicActions);
        rm.sourceUrl = monster.sourceUrl;
        rm.legendaryActionsDescription = monster.legendaryActionsDescription;
        rm.lairActionsDescription = monster.lairActionsDescription;
        rm.lairActionsEndNote = monster.lairActionsEndNote;
        rm.regionalActionsDescription = monster.regionalActionsDescription;
        rm.regionalActionsEndNote = monster.regionalActionsEndNote;
        rm.mythicActionsDescription = monster.mythicActionsDescription;
        return rm;
    }

    public Monster toMonster() {
        Monster monster = new Monster();
        monster.id = UUID.randomUUID();
        monster.name = name;
        monster.size = size;
        monster.type = type;
        monster.subtype = subtype;
        monster.alignment = alignment;
        monster.gameSystem = gameSystem;
        monster.sourceLabel = sourceLabel;
        monster.strengthScore = strengthScore;
        monster.dexterityScore = dexterityScore;
        monster.constitutionScore = constitutionScore;
        monster.intelligenceScore = intelligenceScore;
        monster.wisdomScore = wisdomScore;
        monster.charismaScore = charismaScore;
        monster.strengthSavingThrowAdvantage = strengthSavingThrowAdvantage;
        monster.strengthSavingThrowProficiency = strengthSavingThrowProficiency;
        monster.dexteritySavingThrowAdvantage = dexteritySavingThrowAdvantage;
        monster.dexteritySavingThrowProficiency = dexteritySavingThrowProficiency;
        monster.constitutionSavingThrowAdvantage = constitutionSavingThrowAdvantage;
        monster.constitutionSavingThrowProficiency = constitutionSavingThrowProficiency;
        monster.intelligenceSavingThrowAdvantage = intelligenceSavingThrowAdvantage;
        monster.intelligenceSavingThrowProficiency = intelligenceSavingThrowProficiency;
        monster.wisdomSavingThrowAdvantage = wisdomSavingThrowAdvantage;
        monster.wisdomSavingThrowProficiency = wisdomSavingThrowProficiency;
        monster.charismaSavingThrowAdvantage = charismaSavingThrowAdvantage;
        monster.charismaSavingThrowProficiency = charismaSavingThrowProficiency;
        monster.armorType = armorType;
        monster.shieldBonus = shieldBonus;
        monster.naturalArmorBonus = naturalArmorBonus;
        monster.otherArmorDescription = otherArmorDescription;
        monster.hitDice = hitDice;
        monster.hasCustomHP = hasCustomHP;
        monster.customHPDescription = customHPDescription;
        monster.walkSpeed = walkSpeed;
        monster.burrowSpeed = burrowSpeed;
        monster.climbSpeed = climbSpeed;
        monster.flySpeed = flySpeed;
        monster.canHover = canHover;
        monster.swimSpeed = swimSpeed;
        monster.hasCustomSpeed = hasCustomSpeed;
        monster.customSpeedDescription = customSpeedDescription;
        monster.challengeRating = challengeRating;
        monster.customChallengeRatingDescription = customChallengeRatingDescription;
        monster.customProficiencyBonus = customProficiencyBonus;
        monster.telepathyRange = telepathyRange;
        monster.understandsButDescription = understandsButDescription;
        monster.senses = new HashSet<>(senses);
        monster.skills = new HashSet<>(skills);
        monster.damageImmunities = new HashSet<>(damageImmunities);
        monster.damageResistances = new HashSet<>(damageResistances);
        monster.damageVulnerabilities = new HashSet<>(damageVulnerabilities);
        monster.conditionImmunities = new HashSet<>(conditionImmunities);
        monster.languages = new HashSet<>(languages);
        monster.abilities = new ArrayList<>(abilities);
        monster.actions = new ArrayList<>(actions);
        monster.reactions = new ArrayList<>(reactions);
        monster.lairActions = new ArrayList<>(lairActions);
        monster.legendaryActions = new ArrayList<>(legendaryActions);
        monster.regionalActions = new ArrayList<>(regionalActions);
        monster.bonusActions = new ArrayList<>(bonusActions);
        monster.mythicActions = new ArrayList<>(mythicActions);
        monster.sourceUrl = sourceUrl;
        monster.legendaryActionsDescription = legendaryActionsDescription;
        monster.lairActionsDescription = lairActionsDescription;
        monster.lairActionsEndNote = lairActionsEndNote;
        monster.regionalActionsDescription = regionalActionsDescription;
        monster.regionalActionsEndNote = regionalActionsEndNote;
        monster.mythicActionsDescription = mythicActionsDescription;
        return monster;
    }

    public String getSourceTag() {
        String systemName = (gameSystem != null) ? gameSystem.getShortName() : "5e";
        if (!StringHelper.isNullOrEmpty(sourceLabel)) {
            return systemName + " | " + sourceLabel;
        }
        return systemName;
    }

    public String getChallengeRatingDescription() {
        ChallengeRating challengeRating = this.challengeRating != null ? this.challengeRating : ChallengeRating.ONE;
        if (challengeRating == ChallengeRating.CUSTOM) {
            return customChallengeRatingDescription;
        } else {
            return challengeRating.displayName;
        }
    }
}
