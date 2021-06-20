package com.majinnaibu.monstercards.data;

import com.majinnaibu.monstercards.data.enums.AbilityScore;
import com.majinnaibu.monstercards.data.enums.AdvantageType;
import com.majinnaibu.monstercards.data.enums.ArmorType;
import com.majinnaibu.monstercards.data.enums.ChallengeRating;
import com.majinnaibu.monstercards.data.enums.ProficiencyType;
import com.majinnaibu.monstercards.models.Language;
import com.majinnaibu.monstercards.models.Monster;
import com.majinnaibu.monstercards.models.Skill;
import com.majinnaibu.monstercards.models.Trait;

@SuppressWarnings("unused")
public final class DevContent {
    public static Monster createSampleMonster() {
        Monster monster = new Monster();
        // Name
        monster.name = "Pixie";
        // Meta
        monster.size = "tiny";
        monster.type = "fey";
        monster.subtype = "";
        monster.alignment = "neutral good";
        monster.armorType = ArmorType.NONE;
        // Armor & Armor Class
        monster.shieldBonus = 0;
        monster.naturalArmorBonus = 7;
        monster.otherArmorDescription = "14";
        // Hit Points
        monster.hitDice = 1;
        monster.hasCustomHP = false;
        monster.customHPDescription = "11 (2d8 + 2)";
        monster.walkSpeed = 10;
        monster.burrowSpeed = 0;
        monster.climbSpeed = 0;
        monster.flySpeed = 30;
        monster.canHover = false;
        monster.swimSpeed = 0;
        monster.hasCustomSpeed = false;
        monster.customSpeedDescription = "30 ft., swim 30 ft.";
        // Ability Scores
        monster.strengthScore = Integer.parseInt("2");
        monster.dexterityScore = Integer.parseInt("20");
        monster.constitutionScore = Integer.parseInt("8");
        monster.intelligenceScore = Integer.parseInt("10");
        monster.wisdomScore = Integer.parseInt("14");
        monster.charismaScore = Integer.parseInt("15");
//        monster.strengthScore = 10;
//        monster.dexterityScore = 10;
//        monster.constitutionScore = 10;
//        monster.intelligenceScore = 10;
//        monster.wisdomScore = 10;
//        monster.charismaScore = 10;

        // Saving Throws
        monster.strengthSavingThrowAdvantage = AdvantageType.NONE;
        monster.strengthSavingThrowProficiency = ProficiencyType.NONE;
        monster.dexteritySavingThrowAdvantage = AdvantageType.ADVANTAGE;
        monster.dexteritySavingThrowProficiency = ProficiencyType.PROFICIENT;
        monster.constitutionSavingThrowAdvantage = AdvantageType.DISADVANTAGE;
        monster.constitutionSavingThrowProficiency = ProficiencyType.EXPERTISE;
        monster.intelligenceSavingThrowAdvantage = AdvantageType.NONE;
        monster.intelligenceSavingThrowProficiency = ProficiencyType.EXPERTISE;
        monster.wisdomSavingThrowAdvantage = AdvantageType.ADVANTAGE;
        monster.wisdomSavingThrowProficiency = ProficiencyType.PROFICIENT;
        monster.charismaSavingThrowAdvantage = AdvantageType.DISADVANTAGE;
        monster.charismaSavingThrowProficiency = ProficiencyType.NONE;
        //Skills
        monster.skills.add(new Skill("perception", AbilityScore.WISDOM));
        monster.skills.add(new Skill("stealth", AbilityScore.DEXTERITY));
        // Damage Types
        monster.damageImmunities.add("force");
        monster.damageImmunities.add("lightning");
        monster.damageImmunities.add("poison");
        monster.damageResistances.add("cold");
        monster.damageResistances.add("fire");
        monster.damageResistances.add("piercing");
        monster.damageVulnerabilities.add("acid");
        monster.damageVulnerabilities.add("bludgeoning");
        monster.damageVulnerabilities.add("necrotic");
        // Condition Immunities
        monster.conditionImmunities.add("blinded");
        // Senses
        monster.senses.add("blindsight 10 ft. (blind beyond this range)");
        monster.senses.add("darkvision 20 ft.");
        monster.senses.add("tremorsense 30 ft.");
        monster.senses.add("truesight 40 ft.");
        monster.telepathyRange = 20;
        monster.understandsButDescription = "doesn't care";
        // Languages
        monster.languages.add(new Language("English", true));
        monster.languages.add(new Language("Steve", false));
        monster.languages.add(new Language("Spanish", true));
        monster.languages.add(new Language("French", true));
        monster.languages.add(new Language("Mermataur", false));
        monster.languages.add(new Language("Goldfish", false));
        // Challenge Rating
        monster.challengeRating = ChallengeRating.CUSTOM;
        monster.customChallengeRatingDescription = "Infinite (0XP)";
        monster.customProficiencyBonus = 4;
        // Abilities
        monster.abilities.add(new Trait("Spellcasting", "The acolyte is a 1st-level spellcaster. Its spellcasting ability is Wisdom (spell save DC [WIS SAVE], [WIS ATK] to hit with spell attacks). The acolyte has following cleric spells prepared:\n\n\n> Cantrips (at will): _light, sacred flame, thaumaturgy_\n> 1st level (3 slots): _bless, cure wounds, sanctuary_"));
        monster.abilities.add(new Trait("Amphibious", "The dragon can breathe air and water."));
        monster.abilities.add(new Trait("Legendary Resistance (3/Day)", "If the dragon fails a saving throw, it can choose to succeed instead."));
        // Actions
        monster.actions.add(new Trait("Club", "_Melee Weapon Attack:_ [STR ATK] to hit, reach 5 ft., one target. _Hit:_ 2 (1d4) bludgeoning damage."));

        return monster;
    }
}
