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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.google.android.material.snackbar.Snackbar;
import com.majinnaibu.monstercards.MonsterCardsApplication;
import com.majinnaibu.monstercards.R;
import com.majinnaibu.monstercards.data.MonsterRepository;
import com.majinnaibu.monstercards.helpers.CommonMarkHelper;
import com.majinnaibu.monstercards.helpers.MonsterImportHelper;
import com.majinnaibu.monstercards.helpers.StringHelper;
import com.majinnaibu.monstercards.models.Monster;
import com.majinnaibu.monstercards.ui.library.LibraryFragmentDirections;
import com.majinnaibu.monstercards.ui.shared.MCFragment;
import com.majinnaibu.monstercards.utils.Logger;

import java.util.List;
import java.util.UUID;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableCompletableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MonsterImportFragment extends MCFragment {
    private ViewHolder mHolder;
    private MonsterImportViewModel mViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Logger.logDebug("MonsterCards: loading monster for import");
        Bundle arguments = getArguments();
        assert arguments != null;
        String json = MonsterImportFragmentArgs.fromBundle(arguments).getJson();
        setHasOptionsMenu(true);
        Monster monster = MonsterImportHelper.fromJSON(json);
        mViewModel = new ViewModelProvider(this).get(MonsterImportViewModel.class);
        mViewModel.setMonster(monster);
        View root = inflater.inflate(R.layout.fragment_monster, container, false);
        mHolder = new ViewHolder(root);

        mViewModel.getName().observe(getViewLifecycleOwner(), mHolder.name::setText);
        mViewModel.getMeta().observe(getViewLifecycleOwner(), mHolder.meta::setText);
        mViewModel.getArmorClass().observe(getViewLifecycleOwner(), armorText -> setupLabeledTextView(mHolder.armorClass, armorText, R.string.label_armor_class));
        mViewModel.getHitPoints().observe(getViewLifecycleOwner(), hitPoints -> setupLabeledTextView(mHolder.hitPoints, hitPoints, R.string.label_hit_points));
        mViewModel.getSpeed().observe(getViewLifecycleOwner(), speed -> setupLabeledTextView(mHolder.speed, speed, R.string.label_speed));
        mViewModel.getStrength().observe(getViewLifecycleOwner(), mHolder.strength::setText);
        mViewModel.getDexterity().observe(getViewLifecycleOwner(), mHolder.dexterity::setText);
        mViewModel.getConstitution().observe(getViewLifecycleOwner(), mHolder.constitution::setText);
        mViewModel.getIntelligence().observe(getViewLifecycleOwner(), mHolder.intelligence::setText);
        mViewModel.getWisdom().observe(getViewLifecycleOwner(), mHolder.wisdom::setText);
        mViewModel.getCharisma().observe(getViewLifecycleOwner(), mHolder.charisma::setText);
        mViewModel.getSavingThrows().observe(getViewLifecycleOwner(), savingThrows -> setupOptionalTextView(mHolder.savingThrows, savingThrows, R.string.label_saving_throws));
        mViewModel.getSkills().observe(getViewLifecycleOwner(), skills -> setupOptionalTextView(mHolder.skills, skills, R.string.label_skills));
        mViewModel.getDamageVulnerabilities().observe(getViewLifecycleOwner(), damageTypes -> setupOptionalTextView(mHolder.damageVulnerabilities, damageTypes, R.string.label_damage_vulnerabilities));
        mViewModel.getDamageResistances().observe(getViewLifecycleOwner(), damageTypes -> setupOptionalTextView(mHolder.damageResistances, damageTypes, R.string.label_damage_resistances));
        mViewModel.getDamageImmunities().observe(getViewLifecycleOwner(), damageTypes -> setupOptionalTextView(mHolder.damageImmunities, damageTypes, R.string.label_damage_immunities));
        mViewModel.getConditionImmunities().observe(getViewLifecycleOwner(), conditionImmunities -> setupOptionalTextView(mHolder.conditionImmunities, conditionImmunities, R.string.label_condition_immunities));
        mViewModel.getSenses().observe(getViewLifecycleOwner(), senses -> setupOptionalTextView(mHolder.senses, senses, R.string.label_senses));
        mViewModel.getLanguages().observe(getViewLifecycleOwner(), languages -> setupOptionalTextView(mHolder.languages, languages, R.string.label_languages));
        mViewModel.getChallenge().observe(getViewLifecycleOwner(), challengeRating -> setupLabeledTextView(mHolder.challenge, challengeRating, R.string.label_challenge_rating));
        mViewModel.getAbilities().observe(getViewLifecycleOwner(), abilities -> setupTraitList(mHolder.abilities, abilities));
        mViewModel.getActions().observe(getViewLifecycleOwner(), actions -> setupTraitList(mHolder.actions, actions, mHolder.actions_label, mHolder.actions_divider));
        mViewModel.getReactions().observe(getViewLifecycleOwner(), reactions -> setupTraitList(mHolder.reactions, reactions, mHolder.reactions_label, mHolder.reactions_divider));
        mViewModel.getRegionalEffects().observe(getViewLifecycleOwner(), regionalEffects -> setupTraitList(mHolder.regionalEffects, regionalEffects, mHolder.regionalEffects_label, mHolder.regionalEffects_divider));
        mViewModel.getLairActions().observe(getViewLifecycleOwner(), lairActions -> setupTraitList(mHolder.lairActions, lairActions, mHolder.lairActions_label, mHolder.lairActions_divider));
        mViewModel.getLegendaryActions().observe(getViewLifecycleOwner(), legendaryActions -> setupTraitList(mHolder.legendaryActions, legendaryActions, mHolder.legendaryActions_label, mHolder.legendaryActions_divider));

        return root;
    }

    private void setupLabeledTextView(@NonNull TextView view, String text, int titleId) {
        String title = getString(titleId);
        String fullText = String.format("<b>%s</b> %s", title, text);
        view.setText(Html.fromHtml(fullText));
    }

    private void setupOptionalTextView(TextView root, String text, int titleId) {
        String title = getString(titleId);
        if (StringHelper.isNullOrEmpty(text)) {
            root.setVisibility(View.GONE);
        } else {
            root.setVisibility(View.VISIBLE);
        }
        Spanned formatted;
        if (StringHelper.isNullOrEmpty(title)) {
            formatted = Html.fromHtml(text);
        } else {
            formatted = Html.fromHtml(String.format("<b>%s</b> %s", title, text));
        }
        root.setText(formatted);
    }

    private void setupTraitList(@NonNull LinearLayout root, @NonNull List<String> traits) {
        setupTraitList(root, traits, null, null);
    }

    private void setupTraitList(@NonNull LinearLayout root, @NonNull List<String> traits, View label, View divider) {
        int visibility = traits.size() > 0 ? View.VISIBLE : View.GONE;
        Context context = getContext();
        DisplayMetrics displayMetrics = null;
        if (context != null) {
            Resources resources = context.getResources();
            if (resources != null) {
                displayMetrics = resources.getDisplayMetrics();
            }
        }
        root.removeAllViews();
        for (String action : traits) {
            TextView tvAction = new TextView(getContext());
            // TODO: Handle multiline block quotes specially so they stay multiline.
            // TODO: Replace QuoteSpans in the result of fromHtml with something like this https://stackoverflow.com/questions/7717567/how-to-style-blockquotes-in-android-textviews to make them indent as expected
            tvAction.setText(Html.fromHtml(CommonMarkHelper.toHtml(action)));
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.topMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, displayMetrics);
            tvAction.setLayoutParams(layoutParams);
            root.addView(tvAction);
        }
        root.setVisibility(visibility);
        if (label != null) {
            label.setVisibility(visibility);
        }
        if (divider != null) {
            divider.setVisibility(visibility);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.import_monster, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_action_import_monster) {
            Logger.logDebug("Menu Item Selected");
            Monster monster = mViewModel.getMonster();
            if (monster != null) {
                monster.id = UUID.randomUUID();
                MonsterCardsApplication application = (MonsterCardsApplication) getApplication();
                MonsterRepository repository = application.getMonsterRepository();
                repository.addMonster(monster).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        Snackbar.make(
                                mHolder.root,
                                getString(R.string.snackbar_monster_created, monster.name),
                                Snackbar.LENGTH_LONG)
                                .setAction("Action", (_view) -> navigateToEditMonster(monster.id))
                                .show();
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Logger.logError("Error creating monster", e);
                        Snackbar.make(mHolder.root, getString(R.string.snackbar_failed_to_create_monster), Snackbar.LENGTH_LONG).show();
                    }
                });
            } else {
                Logger.logWTF("monsterId cannot be null.");
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void navigateToEditMonster(@NonNull UUID monsterId) {
        NavController navController = Navigation.findNavController(requireView());
        NavDirections action;
        action = MonsterImportFragmentDirections.actionMonsterImportFragmentToNavigationLibrary();
        navController.navigate(action);
        action = LibraryFragmentDirections.actionNavigationLibraryToNavigationMonster(monsterId.toString());
        navController.navigate(action);
        action = MonsterDetailFragmentDirections.actionNavigationMonsterToEditMonsterFragment(monsterId.toString());
        navController.navigate(action);
    }

    private static class ViewHolder {
        final View root;
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
        final TextView actions_label;
        final ImageView actions_divider;
        final LinearLayout reactions;
        final TextView reactions_label;
        final ImageView reactions_divider;
        final LinearLayout legendaryActions;
        final TextView legendaryActions_label;
        final ImageView legendaryActions_divider;
        final LinearLayout lairActions;
        final TextView lairActions_label;
        final ImageView lairActions_divider;
        final LinearLayout regionalEffects;
        final TextView regionalEffects_label;
        final ImageView regionalEffects_divider;

        ViewHolder(@NonNull View root) {
            this.root = root;
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
            actions_divider = root.findViewById(R.id.actions_divider);
            actions_label = root.findViewById(R.id.actions_label);
            reactions = root.findViewById(R.id.reactions);
            reactions_divider = root.findViewById(R.id.reactions_divider);
            reactions_label = root.findViewById(R.id.reactions_label);
            legendaryActions = root.findViewById(R.id.legendaryActions);
            legendaryActions_divider = root.findViewById(R.id.legendaryActions_divider);
            legendaryActions_label = root.findViewById(R.id.legendaryActions_label);
            lairActions = root.findViewById(R.id.lairActions);
            lairActions_divider = root.findViewById(R.id.lairActions_divider);
            lairActions_label = root.findViewById(R.id.lairActions_label);
            regionalEffects = root.findViewById(R.id.regionalEffects);
            regionalEffects_divider = root.findViewById(R.id.regionalEffects_divider);
            regionalEffects_label = root.findViewById(R.id.regionalEffects_label);
        }
    }

}
