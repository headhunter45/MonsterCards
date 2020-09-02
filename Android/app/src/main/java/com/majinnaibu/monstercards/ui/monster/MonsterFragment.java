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
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.majinnaibu.monstercards.R;
import com.majinnaibu.monstercards.helpers.CommonMarkHelper;
import com.majinnaibu.monstercards.helpers.StringHelper;
import com.majinnaibu.monstercards.models.Ability;
import com.majinnaibu.monstercards.models.DamageType;
import com.majinnaibu.monstercards.models.Language;
import com.majinnaibu.monstercards.models.Monster;
import com.majinnaibu.monstercards.models.SavingThrow;
import com.majinnaibu.monstercards.models.Skill;

import java.util.List;

@SuppressWarnings("FieldCanBeLocal")
public class MonsterFragment extends Fragment {

    private MonsterViewModel monsterViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // TODO: remove this block make the monster ID a parameter to the view and get the monster from saved data (sqlite)
        Monster monster = new Monster();
        // Name
        monster.setName("Pixie");
        // Meta
        monster.setSize("tiny");
        monster.setType("fey");
        monster.setTag("");
        monster.setAlignment("neutral good");
        // Armor & Armor Class
        monster.setArmorName("none");
        monster.setShieldBonus(0);
        monster.setNaturalArmorBonus(7);
        monster.setOtherArmorDescription("14");
        // Hit Points
        monster.setHitDice(1);
        monster.setCustomHP(false);
        monster.setHPText("11 (2d8 + 2)");
        monster.setSpeed("10");
        monster.setBurrowSpeed("0");
        monster.setClimbSpeed("0");
        monster.setFlySpeed("30");
        monster.setHover(false);
        monster.setSwimSpeed("0");
        monster.setCustomSpeed(false);
        monster.setSpeedDescription("30 ft., swim 30 ft.");
        // Ability Scores
        monster.setStrengthScore(Integer.parseInt("2"));
        monster.setDexterityScore(Integer.parseInt("20"));
        monster.setConstitutionScore(Integer.parseInt("8"));
        monster.setIntelligenceScore(Integer.parseInt("10"));
        monster.setWisdomScore(Integer.parseInt("14"));
        monster.setCharismaScore(Integer.parseInt("15"));
        // Saving Throws
        monster.addSavingThrow(new SavingThrow("str", 0));
        monster.addSavingThrow(new SavingThrow("dex", 1));
        monster.addSavingThrow(new SavingThrow("con", 2));
        monster.addSavingThrow(new SavingThrow("int", 3));
        monster.addSavingThrow(new SavingThrow("wis", 4));
        monster.addSavingThrow(new SavingThrow("cha", 5));
        //Skills
        monster.addSkill(new Skill("perception", "wis"));
        monster.addSkill(new Skill("stealth", "dexterity"));
        // Damage Types
        monster.addDamageType(new DamageType("acid", " (Vulnerable)", "v"));
        monster.addDamageType(new DamageType("bludgeoning", " (Vulnerable)", "v"));
        monster.addDamageType(new DamageType("cold", " (Resistant)", "r"));
        monster.addDamageType(new DamageType("fire", " (Resistant)", "r"));
        monster.addDamageType(new DamageType("force", " (Immune)", "i"));
        monster.addDamageType(new DamageType("lightning", " (Immune)", "i"));
        monster.addDamageType(new DamageType("necrotic", " (Vulnerable)", "v"));
        monster.addDamageType(new DamageType("piercing", " (Resistant)", "r"));
        monster.addDamageType(new DamageType("poison", " (Immune)", "i"));
        // Condition Immunities
        monster.addConditionImmunity("blinded");
        // Senses
        monster.setBlindsight("10");
        monster.setIsBlind(true);
        monster.setDarkvision("20");
        monster.setTremorsense("30");
        monster.setTruesight("40");
        monster.setTelepathy(20);
        monster.setUnderstandsBut("doesn't care");
        // Languages
        monster.addLanguage(new Language("English", true));
        monster.addLanguage(new Language("Steve", false));
        monster.addLanguage(new Language("Spanish", true));
        monster.addLanguage(new Language("French", true));
        monster.addLanguage(new Language("Mermataur", false));
        monster.addLanguage(new Language("Goldfish", false));
        // Challenge Rating
        monster.setChallengeRating("*");
        monster.setCustomChallengeRating("Infinite (0XP)");
        monster.setCustomProficiencyBonus(4);
        // Abilities
        monster.addAbility(new Ability("Spellcasting", "The acolyte is a 1st-level spellcaster. Its spellcasting ability is Wisdom (spell save DC [WIS SAVE], [WIS ATK] to hit with spell attacks). The acolyte has following cleric spells prepared:\n\n\n> Cantrips (at will): _light, sacred flame, thaumaturgy_\n> 1st level (3 slots): _bless, cure wounds, sanctuary_"));
        monster.addAbility(new Ability("Amphibious", "The dragon can breathe air and water."));
        monster.addAbility(new Ability("Legendary Resistance (3/Day)", "If the dragon fails a saving throw, it can choose to succeed instead."));
        // END remove block
        monsterViewModel = new ViewModelProvider(this).get(MonsterViewModel.class);
        View root = inflater.inflate(R.layout.fragment_monster, container, false);
        monsterViewModel.setMonster(monster);

        final TextView monsterName = root.findViewById(R.id.name);
        monsterViewModel.getName().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String name) {
                monsterName.setText(name);
            }
        });

        final TextView monsterMeta = root.findViewById(R.id.meta);
        monsterViewModel.getMeta().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String metaText) {
                monsterMeta.setText(metaText);
            }
        });

        final TextView monsterArmorClass = root.findViewById(R.id.armor_class);
        monsterViewModel.getArmorClass().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String armorText) {
                monsterArmorClass.setText(Html.fromHtml("<b>Armor Class</b> " + armorText));
            }
        });

        final TextView monsterHitPoints = root.findViewById(R.id.hit_points);
        monsterViewModel.getHitPoints().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String hitPoints) {
                monsterHitPoints.setText(Html.fromHtml("<b>Hit Points</b> " + hitPoints));
            }
        });

        final TextView monsterSpeed = root.findViewById(R.id.speed);
        monsterViewModel.getSpeed().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String speed) {
                monsterSpeed.setText(Html.fromHtml("<b>Speed</b> " + speed));
            }
        });

        final TextView monsterStrength = root.findViewById(R.id.strength);
        monsterViewModel.getStrength().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String strength) {
                monsterStrength.setText(strength);
            }
        });

        final TextView monsterDexterity = root.findViewById(R.id.dexterity);
        monsterViewModel.getDexterity().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String dexterity) {
                monsterDexterity.setText(dexterity);
            }
        });

        final TextView monsterConstitution = root.findViewById(R.id.constitution);
        monsterViewModel.getConstitution().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String constitution) {
                monsterConstitution.setText(constitution);
            }
        });

        final TextView monsterIntelligence = root.findViewById(R.id.intelligence);
        monsterViewModel.getIntelligence().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String intelligence) {
                monsterIntelligence.setText(intelligence);
            }
        });

        final TextView monsterWisdom = root.findViewById(R.id.wisdom);
        monsterViewModel.getWisdom().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String wisdom) {
                monsterWisdom.setText(wisdom);
            }
        });

        final TextView monsterCharisma = root.findViewById(R.id.charisma);
        monsterViewModel.getCharisma().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String charisma) {
                monsterCharisma.setText(charisma);
            }
        });

        final TextView monsterSavingThrows = root.findViewById(R.id.saving_throws);
        monsterViewModel.getSavingThrows().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String savingThrows) {
                if (StringHelper.isNullOrEmpty(savingThrows)) {
                    monsterSavingThrows.setVisibility(View.GONE);
                } else {
                    monsterSavingThrows.setVisibility(View.VISIBLE);
                }
                monsterSavingThrows.setText(Html.fromHtml("<b>Saving Throws</b> " + savingThrows));
            }
        });

        final TextView monsterSkills = root.findViewById(R.id.skills);
        monsterViewModel.getSkills().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String skills) {
                if (StringHelper.isNullOrEmpty(skills)) {
                    monsterSkills.setVisibility(View.GONE);
                } else {
                    monsterSkills.setVisibility(View.VISIBLE);
                }
                monsterSkills.setText(Html.fromHtml("<b>Skills</b> " + skills));
            }
        });

        final TextView monsterDamageVulnerabilities = root.findViewById(R.id.damage_vulnerabilities);
        monsterViewModel.getDamageVulnerabilities().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String damageType) {
                if (StringHelper.isNullOrEmpty(damageType)) {
                    monsterDamageVulnerabilities.setVisibility(View.GONE);
                } else {
                    monsterDamageVulnerabilities.setVisibility(View.VISIBLE);
                }
                monsterDamageVulnerabilities.setText(Html.fromHtml("<b>Damage Vulnerabilities</b> " + damageType));
            }
        });

        final TextView monsterDamageResistances = root.findViewById(R.id.damage_resistances);
        monsterViewModel.getDamageResistances().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String damageType) {
                if (StringHelper.isNullOrEmpty(damageType)) {
                    monsterDamageResistances.setVisibility(View.GONE);
                } else {
                    monsterDamageResistances.setVisibility(View.VISIBLE);
                }
                monsterDamageResistances.setText(Html.fromHtml("<b>Damage Resistances</b> " + damageType));
            }
        });

        final TextView monsterDamageImmunities = root.findViewById(R.id.damage_immunities);
        monsterViewModel.getDamageImmunities().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String damageType) {
                if (StringHelper.isNullOrEmpty(damageType)) {
                    monsterDamageImmunities.setVisibility(View.GONE);
                } else {
                    monsterDamageImmunities.setVisibility(View.VISIBLE);
                }
                monsterDamageImmunities.setText(Html.fromHtml("<b>Damage Immunities</b> " + damageType));
            }
        });

        final TextView monsterConditionImmunities = root.findViewById(R.id.condition_immunities);
        monsterViewModel.getConditionImmunities().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String conditionImmunities) {
                if (StringHelper.isNullOrEmpty(conditionImmunities)) {
                    monsterConditionImmunities.setVisibility(View.GONE);
                } else {
                    monsterConditionImmunities.setVisibility(View.VISIBLE);
                }
                monsterConditionImmunities.setText(Html.fromHtml("<b>Condition Immunities</b> " + conditionImmunities));
            }
        });

        final TextView monsterSenses = root.findViewById(R.id.senses);
        monsterViewModel.getSenses().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String senses) {
                if (StringHelper.isNullOrEmpty(senses)) {
                    monsterSenses.setVisibility(View.GONE);
                } else {
                    monsterSenses.setVisibility(View.VISIBLE);
                }
                monsterSenses.setText(Html.fromHtml("<b>Senses</b> " + senses));
            }
        });

        final TextView monsterLanguages = root.findViewById(R.id.languages);
        monsterViewModel.getLanguages().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String languages) {
                if (StringHelper.isNullOrEmpty(languages)) {
                    monsterLanguages.setVisibility(View.GONE);
                } else {
                    monsterLanguages.setVisibility(View.VISIBLE);
                }
                monsterLanguages.setText(Html.fromHtml("<b>Languages</b> " + languages));
            }
        });

        final TextView monsterChallenge = root.findViewById(R.id.challenge);
        monsterViewModel.getChallenge().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String challengeRating) {
                monsterChallenge.setText(Html.fromHtml("<b>Challenge</b> " + challengeRating));
            }
        });

        final LinearLayout monsterAbilities = root.findViewById(R.id.abilities);
        monsterViewModel.getAbilities().observe(getViewLifecycleOwner(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> abilities) {
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
            }
        });

        return root;
    }
}
