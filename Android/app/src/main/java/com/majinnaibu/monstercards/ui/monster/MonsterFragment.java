package com.majinnaibu.monstercards.ui.monster;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.majinnaibu.monstercards.R;
import com.majinnaibu.monstercards.models.Monster;

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

        return root;
    }
}
