package com.majinnaibu.monstercards.ui.monster;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.majinnaibu.monstercards.R;
import com.majinnaibu.monstercards.data.enums.AbilityScore;
import com.majinnaibu.monstercards.data.enums.AdvantageType;
import com.majinnaibu.monstercards.data.enums.ArmorType;
import com.majinnaibu.monstercards.data.enums.ChallengeRating;
import com.majinnaibu.monstercards.data.enums.ProficiencyType;
import com.majinnaibu.monstercards.helpers.CommonMarkHelper;
import com.majinnaibu.monstercards.helpers.StringHelper;
import com.majinnaibu.monstercards.models.Language;
import com.majinnaibu.monstercards.models.Monster;
import com.majinnaibu.monstercards.models.Skill;
import com.majinnaibu.monstercards.models.Trait;

@SuppressWarnings("FieldCanBeLocal")
public class MonsterFragment extends Fragment {

    private MonsterViewModel monsterViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // TODO: remove this block make the monster ID a parameter to the view and get the monster from saved data (sqlite)
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
        monster.blindsightRange = 10;
        monster.isBlindBeyondBlindsightRange = true;
        monster.darkvisionRange = 20;
        monster.tremorsenseRange = 30;
        monster.truesightRange = 40;
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
        // END remove block
        monsterViewModel = new ViewModelProvider(this).get(MonsterViewModel.class);
        View root = inflater.inflate(R.layout.fragment_monster, container, false);
        monsterViewModel.setMonster(monster);

        final TextView monsterName = root.findViewById(R.id.name);
        monsterViewModel.getName().observe(getViewLifecycleOwner(), name -> monsterName.setText(name));

        final TextView monsterMeta = root.findViewById(R.id.meta);
        monsterViewModel.getMeta().observe(getViewLifecycleOwner(), metaText -> monsterMeta.setText(metaText));

        final TextView monsterArmorClass = root.findViewById(R.id.armor_class);
        monsterViewModel.getArmorClass().observe(getViewLifecycleOwner(), armorText -> monsterArmorClass.setText(Html.fromHtml("<b>Armor Class</b> " + armorText)));

        final TextView monsterHitPoints = root.findViewById(R.id.hit_points);
        monsterViewModel.getHitPoints().observe(getViewLifecycleOwner(), hitPoints -> monsterHitPoints.setText(Html.fromHtml("<b>Hit Points</b> " + hitPoints)));

        final TextView monsterSpeed = root.findViewById(R.id.speed);
        monsterViewModel.getSpeed().observe(getViewLifecycleOwner(), speed -> monsterSpeed.setText(Html.fromHtml("<b>Speed</b> " + speed)));

        final TextView monsterStrength = root.findViewById(R.id.strength);
        monsterViewModel.getStrength().observe(getViewLifecycleOwner(), strength -> monsterStrength.setText(strength));

        final TextView monsterDexterity = root.findViewById(R.id.dexterity);
        monsterViewModel.getDexterity().observe(getViewLifecycleOwner(), dexterity -> monsterDexterity.setText(dexterity));

        final TextView monsterConstitution = root.findViewById(R.id.constitution);
        monsterViewModel.getConstitution().observe(getViewLifecycleOwner(), constitution -> monsterConstitution.setText(constitution));

        final TextView monsterIntelligence = root.findViewById(R.id.intelligence);
        monsterViewModel.getIntelligence().observe(getViewLifecycleOwner(), intelligence -> monsterIntelligence.setText(intelligence));

        final TextView monsterWisdom = root.findViewById(R.id.wisdom);
        monsterViewModel.getWisdom().observe(getViewLifecycleOwner(), wisdom -> monsterWisdom.setText(wisdom));

        final TextView monsterCharisma = root.findViewById(R.id.charisma);
        monsterViewModel.getCharisma().observe(getViewLifecycleOwner(), charisma -> monsterCharisma.setText(charisma));

        final TextView monsterSavingThrows = root.findViewById(R.id.saving_throws);
        monsterViewModel.getSavingThrows().observe(getViewLifecycleOwner(), savingThrows -> {
            if (StringHelper.isNullOrEmpty(savingThrows)) {
                monsterSavingThrows.setVisibility(View.GONE);
            } else {
                monsterSavingThrows.setVisibility(View.VISIBLE);
            }
            monsterSavingThrows.setText(Html.fromHtml("<b>Saving Throws</b> " + savingThrows));
        });

        final TextView monsterSkills = root.findViewById(R.id.skills);
        monsterViewModel.getSkills().observe(getViewLifecycleOwner(), skills -> {
            if (StringHelper.isNullOrEmpty(skills)) {
                monsterSkills.setVisibility(View.GONE);
            } else {
                monsterSkills.setVisibility(View.VISIBLE);
            }
            monsterSkills.setText(Html.fromHtml("<b>Skills</b> " + skills));
        });

        final TextView monsterDamageVulnerabilities = root.findViewById(R.id.damage_vulnerabilities);
        monsterViewModel.getDamageVulnerabilities().observe(getViewLifecycleOwner(), damageType -> {
            if (StringHelper.isNullOrEmpty(damageType)) {
                monsterDamageVulnerabilities.setVisibility(View.GONE);
            } else {
                monsterDamageVulnerabilities.setVisibility(View.VISIBLE);
            }
            monsterDamageVulnerabilities.setText(Html.fromHtml("<b>Damage Vulnerabilities</b> " + damageType));
        });

        final TextView monsterDamageResistances = root.findViewById(R.id.damage_resistances);
        monsterViewModel.getDamageResistances().observe(getViewLifecycleOwner(), damageType -> {
            if (StringHelper.isNullOrEmpty(damageType)) {
                monsterDamageResistances.setVisibility(View.GONE);
            } else {
                monsterDamageResistances.setVisibility(View.VISIBLE);
            }
            monsterDamageResistances.setText(Html.fromHtml("<b>Damage Resistances</b> " + damageType));
        });

        final TextView monsterDamageImmunities = root.findViewById(R.id.damage_immunities);
        monsterViewModel.getDamageImmunities().observe(getViewLifecycleOwner(), damageType -> {
            if (StringHelper.isNullOrEmpty(damageType)) {
                monsterDamageImmunities.setVisibility(View.GONE);
            } else {
                monsterDamageImmunities.setVisibility(View.VISIBLE);
            }
            monsterDamageImmunities.setText(Html.fromHtml("<b>Damage Immunities</b> " + damageType));
        });

        final TextView monsterConditionImmunities = root.findViewById(R.id.condition_immunities);
        monsterViewModel.getConditionImmunities().observe(getViewLifecycleOwner(), conditionImmunities -> {
            if (StringHelper.isNullOrEmpty(conditionImmunities)) {
                monsterConditionImmunities.setVisibility(View.GONE);
            } else {
                monsterConditionImmunities.setVisibility(View.VISIBLE);
            }
            monsterConditionImmunities.setText(Html.fromHtml("<b>Condition Immunities</b> " + conditionImmunities));
        });

        final TextView monsterSenses = root.findViewById(R.id.senses);
        monsterViewModel.getSenses().observe(getViewLifecycleOwner(), senses -> {
            if (StringHelper.isNullOrEmpty(senses)) {
                monsterSenses.setVisibility(View.GONE);
            } else {
                monsterSenses.setVisibility(View.VISIBLE);
            }
            monsterSenses.setText(Html.fromHtml("<b>Senses</b> " + senses));
        });

        final TextView monsterLanguages = root.findViewById(R.id.languages);
        monsterViewModel.getLanguages().observe(getViewLifecycleOwner(), languages -> {
            if (StringHelper.isNullOrEmpty(languages)) {
                monsterLanguages.setVisibility(View.GONE);
            } else {
                monsterLanguages.setVisibility(View.VISIBLE);
            }
            monsterLanguages.setText(Html.fromHtml("<b>Languages</b> " + languages));
        });

        final TextView monsterChallenge = root.findViewById(R.id.challenge);
        monsterViewModel.getChallenge().observe(getViewLifecycleOwner(), challengeRating -> monsterChallenge.setText(Html.fromHtml("<b>Challenge</b> " + challengeRating)));

        final LinearLayout monsterAbilities = root.findViewById(R.id.abilities);
        monsterViewModel.getAbilities().observe(getViewLifecycleOwner(), abilities -> {
            Context context = getContext();
            DisplayMetrics displayMetrics = null;
            if (context != null) {
                Resources resources = context.getResources();
                if (resources != null) {
                    displayMetrics = resources.getDisplayMetrics();
                }
            }
            monsterAbilities.removeAllViews();
            if (abilities != null) {
                for (String ability : abilities) {
                    TextView tvAbility = new TextView(context);
                    // TODO: Handle multiline block quotes specially so they stay multiline.
                    // TODO: Replace QuoteSpans in the result of fromHtml with something like this https://stackoverflow.com/questions/7717567/how-to-style-blockquotes-in-android-textviews to make them indent as expected
                    Spanned spannedText = Html.fromHtml(CommonMarkHelper.toHtml(ability));
                    tvAbility.setText(spannedText);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams.topMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, displayMetrics);
                    tvAbility.setLayoutParams(layoutParams);
                    monsterAbilities.addView(tvAbility);
                }
            }
        });

        final LinearLayout monsterActions = root.findViewById(R.id.actions);
        monsterViewModel.getActions().observe(getViewLifecycleOwner(), actions -> {
            Context context = getContext();
            DisplayMetrics displayMetrics = null;
            if (context != null) {
                Resources resources = context.getResources();
                if (resources != null) {
                    displayMetrics = resources.getDisplayMetrics();
                }
            }
            monsterActions.removeAllViews();
            if (actions != null) {
                for (String action : actions) {
                    TextView tvAction = new TextView(getContext());
                    tvAction.setText(Html.fromHtml(CommonMarkHelper.toHtml(action)));
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams.topMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, displayMetrics);
                    tvAction.setLayoutParams(layoutParams);
                    monsterActions.addView(tvAction);
                }
            }
        });

        return root;
    }
}
