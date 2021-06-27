package com.majinnaibu.monstercards.ui.monster;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.majinnaibu.monstercards.R;
import com.majinnaibu.monstercards.data.MonsterRepository;
import com.majinnaibu.monstercards.helpers.CommonMarkHelper;
import com.majinnaibu.monstercards.helpers.StringHelper;
import com.majinnaibu.monstercards.models.Monster;
import com.majinnaibu.monstercards.ui.shared.MCFragment;
import com.majinnaibu.monstercards.utils.Logger;

import java.util.UUID;

import io.reactivex.rxjava3.observers.DisposableSingleObserver;

public class MonsterDetailFragment extends MCFragment {
    private ViewHolder mHolder;

    private MonsterDetailViewModel monsterDetailViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MonsterRepository repository = getMonsterRepository();
        Bundle arguments = getArguments();
        assert arguments != null;
        UUID monsterId = UUID.fromString(MonsterDetailFragmentArgs.fromBundle(arguments).getMonsterId());
        setHasOptionsMenu(true);

        monsterDetailViewModel = new ViewModelProvider(this).get(MonsterDetailViewModel.class);
        View root = inflater.inflate(R.layout.fragment_monster, container, false);

        mHolder = new ViewHolder(root);

        repository.getMonster(monsterId).toObservable()
                .firstOrError()
                .subscribe(new DisposableSingleObserver<Monster>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull Monster monster) {
                        monsterDetailViewModel.setMonster(monster);
                        dispose();
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Logger.logError(e);
                        dispose();
                    }
                });

        monsterDetailViewModel.getName().observe(getViewLifecycleOwner(), mHolder.name::setText);
        monsterDetailViewModel.getMeta().observe(getViewLifecycleOwner(), mHolder.meta::setText);
        monsterDetailViewModel.getArmorClass().observe(getViewLifecycleOwner(), armorText -> mHolder.armorClass.setText(Html.fromHtml("<b>Armor Class</b> " + armorText)));
        monsterDetailViewModel.getHitPoints().observe(getViewLifecycleOwner(), hitPoints -> mHolder.hitPoints.setText(Html.fromHtml("<b>Hit Points</b> " + hitPoints)));
        monsterDetailViewModel.getSpeed().observe(getViewLifecycleOwner(), speed -> mHolder.speed.setText(Html.fromHtml("<b>Speed</b> " + speed)));
        monsterDetailViewModel.getStrength().observe(getViewLifecycleOwner(), mHolder.strength::setText);
        monsterDetailViewModel.getDexterity().observe(getViewLifecycleOwner(), mHolder.dexterity::setText);
        monsterDetailViewModel.getConstitution().observe(getViewLifecycleOwner(), mHolder.constitution::setText);
        monsterDetailViewModel.getIntelligence().observe(getViewLifecycleOwner(), mHolder.intelligence::setText);
        monsterDetailViewModel.getWisdom().observe(getViewLifecycleOwner(), mHolder.wisdom::setText);
        monsterDetailViewModel.getCharisma().observe(getViewLifecycleOwner(), mHolder.charisma::setText);
        monsterDetailViewModel.getSavingThrows().observe(getViewLifecycleOwner(), savingThrows -> {
            if (StringHelper.isNullOrEmpty(savingThrows)) {
                mHolder.savingThrows.setVisibility(View.GONE);
            } else {
                mHolder.savingThrows.setVisibility(View.VISIBLE);
            }
            mHolder.savingThrows.setText(Html.fromHtml("<b>Saving Throws</b> " + savingThrows));
        });
        monsterDetailViewModel.getSkills().observe(getViewLifecycleOwner(), skills -> {
            if (StringHelper.isNullOrEmpty(skills)) {
                mHolder.skills.setVisibility(View.GONE);
            } else {
                mHolder.skills.setVisibility(View.VISIBLE);
            }
            mHolder.skills.setText(Html.fromHtml("<b>Skills</b> " + skills));
        });
        monsterDetailViewModel.getDamageVulnerabilities().observe(getViewLifecycleOwner(), damageType -> {
            if (StringHelper.isNullOrEmpty(damageType)) {
                mHolder.damageVulnerabilities.setVisibility(View.GONE);
            } else {
                mHolder.damageVulnerabilities.setVisibility(View.VISIBLE);
            }
            mHolder.damageVulnerabilities.setText(Html.fromHtml("<b>Damage Vulnerabilities</b> " + damageType));
        });
        monsterDetailViewModel.getDamageResistances().observe(getViewLifecycleOwner(), damageType -> {
            if (StringHelper.isNullOrEmpty(damageType)) {
                mHolder.damageResistances.setVisibility(View.GONE);
            } else {
                mHolder.damageResistances.setVisibility(View.VISIBLE);
            }
            mHolder.damageResistances.setText(Html.fromHtml("<b>Damage Resistances</b> " + damageType));
        });
        monsterDetailViewModel.getDamageImmunities().observe(getViewLifecycleOwner(), damageType -> {
            if (StringHelper.isNullOrEmpty(damageType)) {
                mHolder.damageImmunities.setVisibility(View.GONE);
            } else {
                mHolder.damageImmunities.setVisibility(View.VISIBLE);
            }
            mHolder.damageImmunities.setText(Html.fromHtml("<b>Damage Immunities</b> " + damageType));
        });
        monsterDetailViewModel.getConditionImmunities().observe(getViewLifecycleOwner(), conditionImmunities -> {
            if (StringHelper.isNullOrEmpty(conditionImmunities)) {
                mHolder.conditionImmunities.setVisibility(View.GONE);
            } else {
                mHolder.conditionImmunities.setVisibility(View.VISIBLE);
            }
            mHolder.conditionImmunities.setText(Html.fromHtml("<b>Condition Immunities</b> " + conditionImmunities));
        });
        monsterDetailViewModel.getSenses().observe(getViewLifecycleOwner(), senses -> {
            if (StringHelper.isNullOrEmpty(senses)) {
                mHolder.senses.setVisibility(View.GONE);
            } else {
                mHolder.senses.setVisibility(View.VISIBLE);
            }
            mHolder.senses.setText(Html.fromHtml("<b>Senses</b> " + senses));
        });
        monsterDetailViewModel.getLanguages().observe(getViewLifecycleOwner(), languages -> {
            if (StringHelper.isNullOrEmpty(languages)) {
                mHolder.languages.setVisibility(View.GONE);
            } else {
                mHolder.languages.setVisibility(View.VISIBLE);
            }
            mHolder.languages.setText(Html.fromHtml("<b>Languages</b> " + languages));
        });
        monsterDetailViewModel.getChallenge().observe(getViewLifecycleOwner(), challengeRating -> mHolder.challenge.setText(Html.fromHtml("<b>Challenge</b> " + challengeRating)));
        monsterDetailViewModel.getAbilities().observe(getViewLifecycleOwner(), abilities -> {
            Context context = getContext();
            DisplayMetrics displayMetrics = null;
            if (context != null) {
                Resources resources = context.getResources();
                if (resources != null) {
                    displayMetrics = resources.getDisplayMetrics();
                }
            }
            mHolder.abilities.removeAllViews();
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
                    mHolder.abilities.addView(tvAbility);
                }
            }
        });
        monsterDetailViewModel.getActions().observe(getViewLifecycleOwner(), actions -> {
            Context context = getContext();
            DisplayMetrics displayMetrics = null;
            if (context != null) {
                Resources resources = context.getResources();
                if (resources != null) {
                    displayMetrics = resources.getDisplayMetrics();
                }
            }
            mHolder.actions.removeAllViews();
            if (actions != null) {
                for (String action : actions) {
                    TextView tvAction = new TextView(getContext());
                    tvAction.setText(Html.fromHtml(CommonMarkHelper.toHtml(action)));
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams.topMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, displayMetrics);
                    tvAction.setLayoutParams(layoutParams);
                    mHolder.actions.addView(tvAction);
                }
            }
        });

        // TODO: add lair actions, legendary actions, reactions, and regional actions

        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.monster_detail_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_action_edit_monster) {
            UUID monsterId = monsterDetailViewModel.getId().getValue();
            if (monsterId != null) {
                NavDirections action = MonsterDetailFragmentDirections.actionNavigationMonsterToEditMonsterFragment(monsterId.toString());
                Navigation.findNavController(requireView()).navigate(action);
            } else {
                Logger.logWTF("monsterId cannot be null.");
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private static class ViewHolder {
        final TextView name;
        final TextView meta;
        final TextView armorClass;
        final TextView hitPoints;
        final TextView speed;
        final TextView strength;
        final TextView dexterity;
        final TextView constitution;
        final TextView intelligence;
        final TextView wisdom;
        final TextView charisma;
        final TextView savingThrows;
        final TextView skills;
        final TextView damageVulnerabilities;
        final TextView damageResistances;
        final TextView damageImmunities;
        final TextView conditionImmunities;
        final TextView senses;
        final TextView languages;
        final TextView challenge;
        final LinearLayout abilities;
        final LinearLayout actions;
        final LinearLayout reactions;
        final LinearLayout legendaryActions;
        final LinearLayout lairActions;
        final LinearLayout regionalEffects;

        ViewHolder(View root) {
            name = root.findViewById(R.id.name);
            meta = root.findViewById(R.id.meta);
            armorClass = root.findViewById(R.id.armorClass);
            hitPoints = root.findViewById(R.id.hitPoints);
            speed = root.findViewById(R.id.speed);
            strength = root.findViewById(R.id.strength);
            dexterity = root.findViewById(R.id.dexterity);
            constitution = root.findViewById(R.id.constitution);
            intelligence = root.findViewById(R.id.intelligence);
            wisdom = root.findViewById(R.id.wisdom);
            charisma = root.findViewById(R.id.charisma);
            savingThrows = root.findViewById(R.id.savingThrows);
            skills = root.findViewById(R.id.skills);
            damageVulnerabilities = root.findViewById(R.id.damageVulnerabilities);
            damageResistances = root.findViewById(R.id.damageResistances);
            damageImmunities = root.findViewById(R.id.damageImmunities);
            conditionImmunities = root.findViewById(R.id.conditionImmunities);
            senses = root.findViewById(R.id.senses);
            languages = root.findViewById(R.id.languages);
            challenge = root.findViewById(R.id.challenge);
            abilities = root.findViewById(R.id.abilities);
            actions = root.findViewById(R.id.actions);
            reactions = root.findViewById(R.id.reactions);
            legendaryActions = root.findViewById(R.id.legendaryActions);
            lairActions = root.findViewById(R.id.lairActions);
            regionalEffects = root.findViewById(R.id.regionalEffects);
        }
    }
}
