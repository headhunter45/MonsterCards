package com.majinnaibu.monstercards.ui.editmonster;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.snackbar.Snackbar;
import com.majinnaibu.monstercards.R;
import com.majinnaibu.monstercards.data.MonsterRepository;
import com.majinnaibu.monstercards.data.enums.StringType;
import com.majinnaibu.monstercards.data.enums.TraitType;
import com.majinnaibu.monstercards.models.Monster;
import com.majinnaibu.monstercards.ui.shared.MCFragment;
import com.majinnaibu.monstercards.utils.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableCompletableObserver;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class EditMonsterFragment extends MCFragment {

    private EditMonsterViewModel mViewModel;
    private ViewHolder mHolder;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_edit_monster, container, false);
        mHolder = new ViewHolder(root);

        MonsterRepository repository = getMonsterRepository();
        Bundle arguments = getArguments();
        if (arguments == null) {
            Logger.logError("Arguments bundle is null in EditMonsterFragment");
            return root;
        }

        String monsterIdStr = EditMonsterFragmentArgs.fromBundle(arguments).getMonsterId();
        if (monsterIdStr == null || monsterIdStr.isEmpty()) {
            Logger.logError("monsterId is null or empty in EditMonsterFragment");
            return root;
        }

        UUID monsterId = UUID.fromString(monsterIdStr);

        try {
            NavController navController = NavHostFragment.findNavController(this);
            NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.edit_monster_navigation);
            mViewModel = new ViewModelProvider(backStackEntry).get(EditMonsterViewModel.class);
        } catch (Exception e) {
            mViewModel = new ViewModelProvider(this).get(EditMonsterViewModel.class);
        }

        setTitle(getString(R.string.title_editMonster_fmt, getString(R.string.default_monster_name)));

        if (mViewModel.hasError() || !mViewModel.hasLoaded() || !Objects.equals(mViewModel.getMonsterId().getValue(), monsterId)) {
            repository.getMonster(monsterId).toObservable()
                    .firstOrError()
                    .subscribe(new DisposableSingleObserver<Monster>() {
                        @Override
                        public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull Monster monster) {
                            mViewModel.setHasLoaded(true);
                            mViewModel.setHasError(false);
                            mViewModel.copyFromMonster(monster);
                            setTitle(getString(R.string.title_editMonster_fmt, monster.name));
                            dispose();
                        }

                        @Override
                        public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                            Logger.logError(e);
                            mViewModel.setHasError(true);
                            mViewModel.setErrorMessage(e.toString());
                            dispose();
                        }
                    });
        }

        setupAccordions();
        setupSummaries();
        setupSubClickListeners();

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (mViewModel.hasChanges()) {
                    View view = getView();
                    AlertDialog alertDialog = new AlertDialog.Builder(requireContext()).create();
                    alertDialog.setTitle("Unsaved Changes");
                    alertDialog.setMessage("Do you want to save your changes?");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Save", (dialog, id) -> {
                        getMonsterRepository().saveMonster(mViewModel.buildMonster())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        new DisposableCompletableObserver() {
                                            @Override
                                            public void onComplete() {
                                                NavHostFragment.findNavController(EditMonsterFragment.this).navigateUp();
                                            }

                                            @Override
                                            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                                                Logger.logError("Error saving monster", e);
                                                assert view != null;
                                                Snackbar.make(view, getString(R.string.snackbar_failed_to_create_monster), Snackbar.LENGTH_LONG)
                                                        .setAction("Action", null).show();
                                            }
                                        });
                    });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Discard", (dialog, id) -> NavHostFragment.findNavController(EditMonsterFragment.this).navigateUp());
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Cancel", (dialog, id) -> {});
                    alertDialog.show();
                } else {
                    NavHostFragment.findNavController(EditMonsterFragment.this).navigateUp();
                }
            }
        });

        return root;
    }

    private void setupAccordions() {
        if (mHolder.headerBasicInfo != null && mHolder.bodyBasicInfo != null && mHolder.chevronBasicInfo != null) {
            setupAccordion(mHolder.headerBasicInfo, mHolder.bodyBasicInfo, mHolder.chevronBasicInfo);
        }
        if (mHolder.headerCombatAbilities != null && mHolder.bodyCombatAbilities != null && mHolder.chevronCombatAbilities != null) {
            setupAccordion(mHolder.headerCombatAbilities, mHolder.bodyCombatAbilities, mHolder.chevronCombatAbilities);
        }
        if (mHolder.headerSavesSkillsLanguages != null && mHolder.bodySavesSkillsLanguages != null && mHolder.chevronSavesSkillsLanguages != null) {
            setupAccordion(mHolder.headerSavesSkillsLanguages, mHolder.bodySavesSkillsLanguages, mHolder.chevronSavesSkillsLanguages);
        }
        if (mHolder.headerDefenses != null && mHolder.bodyDefenses != null && mHolder.chevronDefenses != null) {
            setupAccordion(mHolder.headerDefenses, mHolder.bodyDefenses, mHolder.chevronDefenses);
        }
        if (mHolder.headerTraitsActions != null && mHolder.bodyTraitsActions != null && mHolder.chevronTraitsActions != null) {
            setupAccordion(mHolder.headerTraitsActions, mHolder.bodyTraitsActions, mHolder.chevronTraitsActions);
        }
    }

    private void setupAccordion(View header, View body, ImageView chevron) {
        header.setOnClickListener(v -> {
            boolean isExpanded = body.getVisibility() == View.VISIBLE;
            body.setVisibility(isExpanded ? View.GONE : View.VISIBLE);
            chevron.setImageResource(isExpanded ? R.drawable.ic_expand_more_24 : R.drawable.ic_expand_less_24);
        });
    }

    private void setupSummaries() {
        mViewModel.getName().observe(getViewLifecycleOwner(), v -> updateBasicInfoSummary());
        mViewModel.getSize().observe(getViewLifecycleOwner(), v -> updateBasicInfoSummary());
        mViewModel.getType().observe(getViewLifecycleOwner(), v -> updateBasicInfoSummary());
        mViewModel.getChallengeRating().observe(getViewLifecycleOwner(), v -> updateBasicInfoSummary());

        mViewModel.getStrength().observe(getViewLifecycleOwner(), v -> updateCombatAbilitiesSummary());
        mViewModel.getDexterity().observe(getViewLifecycleOwner(), v -> updateCombatAbilitiesSummary());
        mViewModel.getConstitution().observe(getViewLifecycleOwner(), v -> updateCombatAbilitiesSummary());
        mViewModel.getIntelligence().observe(getViewLifecycleOwner(), v -> updateCombatAbilitiesSummary());
        mViewModel.getWisdom().observe(getViewLifecycleOwner(), v -> updateCombatAbilitiesSummary());
        mViewModel.getCharisma().observe(getViewLifecycleOwner(), v -> updateCombatAbilitiesSummary());

        mViewModel.getSkills().observe(getViewLifecycleOwner(), v -> updateSavesSkillsSummary());
        mViewModel.getLanguages().observe(getViewLifecycleOwner(), v -> updateSavesSkillsSummary());

        mViewModel.getConditionImmunities().observe(getViewLifecycleOwner(), v -> updateDefensesSummary());
        mViewModel.getDamageImmunities().observe(getViewLifecycleOwner(), v -> updateDefensesSummary());
        mViewModel.getDamageResistances().observe(getViewLifecycleOwner(), v -> updateDefensesSummary());
        mViewModel.getDamageVulnerabilities().observe(getViewLifecycleOwner(), v -> updateDefensesSummary());

        mViewModel.getAbilities().observe(getViewLifecycleOwner(), v -> updateTraitsActionsSummary());
        mViewModel.getActions().observe(getViewLifecycleOwner(), v -> updateTraitsActionsSummary());
        mViewModel.getReactions().observe(getViewLifecycleOwner(), v -> updateTraitsActionsSummary());
    }

    private void updateBasicInfoSummary() {
        if (mHolder.summaryBasicInfo == null) return;
        String name = mViewModel.getName().getValue();
        String size = mViewModel.getSize().getValue();
        String type = mViewModel.getType().getValue();

        List<String> parts = new ArrayList<>();
        if (!TextUtils.isEmpty(name)) parts.add(name);
        if (!TextUtils.isEmpty(size)) parts.add(size);
        if (!TextUtils.isEmpty(type)) parts.add(type);

        mHolder.summaryBasicInfo.setText(parts.isEmpty() ? "Tap to edit basic info" : TextUtils.join(", ", parts));
    }

    private void updateCombatAbilitiesSummary() {
        if (mHolder.summaryCombatAbilities == null) return;
        Integer str = mViewModel.getStrength().getValue();
        Integer dex = mViewModel.getDexterity().getValue();
        Integer con = mViewModel.getConstitution().getValue();
        Integer intel = mViewModel.getIntelligence().getValue();
        Integer wis = mViewModel.getWisdom().getValue();
        Integer cha = mViewModel.getCharisma().getValue();

        String summary = String.format("STR %d | DEX %d | CON %d | INT %d | WIS %d | CHA %d",
                str != null ? str : 10,
                dex != null ? dex : 10,
                con != null ? con : 10,
                intel != null ? intel : 10,
                wis != null ? wis : 10,
                cha != null ? cha : 10);
        mHolder.summaryCombatAbilities.setText(summary);
    }

    private void updateSavesSkillsSummary() {
        if (mHolder.summarySavesSkillsLanguages == null) return;
        int skillsCount = mViewModel.getSkills().getValue() != null ? mViewModel.getSkills().getValue().size() : 0;
        int languagesCount = mViewModel.getLanguages().getValue() != null ? mViewModel.getLanguages().getValue().size() : 0;

        mHolder.summarySavesSkillsLanguages.setText(String.format("%d Skills, %d Languages", skillsCount, languagesCount));
    }

    private void updateDefensesSummary() {
        if (mHolder.summaryDefenses == null) return;
        List<String> defenses = new ArrayList<>();
        if (mViewModel.getConditionImmunities().getValue() != null && !mViewModel.getConditionImmunities().getValue().isEmpty()) {
            defenses.add(mViewModel.getConditionImmunities().getValue().size() + " Condition Immunities");
        }
        if (mViewModel.getDamageImmunities().getValue() != null && !mViewModel.getDamageImmunities().getValue().isEmpty()) {
            defenses.add(mViewModel.getDamageImmunities().getValue().size() + " Damage Immunities");
        }
        if (mViewModel.getDamageResistances().getValue() != null && !mViewModel.getDamageResistances().getValue().isEmpty()) {
            defenses.add(mViewModel.getDamageResistances().getValue().size() + " Resistances");
        }

        mHolder.summaryDefenses.setText(defenses.isEmpty() ? "None" : TextUtils.join(", ", defenses));
    }

    private void updateTraitsActionsSummary() {
        if (mHolder.summaryTraitsActions == null) return;
        int abilitiesCount = mViewModel.getAbilities().getValue() != null ? mViewModel.getAbilities().getValue().size() : 0;
        int actionsCount = mViewModel.getActions().getValue() != null ? mViewModel.getActions().getValue().size() : 0;
        int reactionsCount = mViewModel.getReactions().getValue() != null ? mViewModel.getReactions().getValue().size() : 0;

        mHolder.summaryTraitsActions.setText(String.format("%d Abilities, %d Actions, %d Reactions", abilitiesCount, actionsCount, reactionsCount));
    }

    private void setupSubClickListeners() {
        if (mHolder.basicInfoButton != null) {
            mHolder.basicInfoButton.setOnClickListener(v -> {
                NavDirections action = EditMonsterFragmentDirections.actionEditMonsterFragmentToEditBasicInfoFragment();
                NavHostFragment.findNavController(this).navigate(action);
            });
        }

        if (mHolder.challengeRating != null) {
            mHolder.challengeRating.setOnClickListener(v -> {
                NavDirections action = EditMonsterFragmentDirections.actionEditMonsterFragmentToEditChallengeRatingFragment();
                NavHostFragment.findNavController(this).navigate(action);
            });
        }

        if (mHolder.armorButton != null) {
            mHolder.armorButton.setOnClickListener(v -> {
                NavDirections action = EditMonsterFragmentDirections.actionEditMonsterFragmentToEditArmorFragment();
                NavHostFragment.findNavController(this).navigate(action);
            });
        }

        if (mHolder.speedButton != null) {
            mHolder.speedButton.setOnClickListener(v -> {
                NavDirections action = EditMonsterFragmentDirections.actionEditMonsterFragmentToEditSpeedFragment();
                NavHostFragment.findNavController(this).navigate(action);
            });
        }

        if (mHolder.abilityScoresButton != null) {
            mHolder.abilityScoresButton.setOnClickListener(v -> {
                NavDirections action = EditMonsterFragmentDirections.actionEditMonsterFragmentToEditAbilityScoresFragment();
                NavHostFragment.findNavController(this).navigate(action);
            });
        }

        if (mHolder.savingThrows != null) {
            mHolder.savingThrows.setOnClickListener(v -> {
                NavDirections action = EditMonsterFragmentDirections.actionEditMonsterFragmentToEditSavingThrowsFragment();
                NavHostFragment.findNavController(this).navigate(action);
            });
        }

        if (mHolder.skills != null) {
            mHolder.skills.setOnClickListener(v -> {
                NavDirections action = EditMonsterFragmentDirections.actionEditMonsterFragmentToEditSkillsFragment();
                NavHostFragment.findNavController(this).navigate(action);
            });
        }

        if (mHolder.senses != null) {
            mHolder.senses.setOnClickListener(v -> {
                NavDirections action = EditMonsterFragmentDirections.actionEditMonsterFragmentToEditStringsFragment(StringType.SENSE);
                NavHostFragment.findNavController(this).navigate(action);
            });
        }

        if (mHolder.languages != null) {
            mHolder.languages.setOnClickListener(v -> {
                NavDirections action = EditMonsterFragmentDirections.actionEditMonsterFragmentToEditLanguagesFragment();
                NavHostFragment.findNavController(this).navigate(action);
            });
        }

        if (mHolder.conditionImmunities != null) {
            mHolder.conditionImmunities.setOnClickListener(v -> {
                NavDirections action = EditMonsterFragmentDirections.actionEditMonsterFragmentToEditStringsFragment(StringType.CONDITION_IMMUNITY);
                NavHostFragment.findNavController(this).navigate(action);
            });
        }

        if (mHolder.damageImmunities != null) {
            mHolder.damageImmunities.setOnClickListener(v -> {
                NavDirections action = EditMonsterFragmentDirections.actionEditMonsterFragmentToEditStringsFragment(StringType.DAMAGE_IMMUNITY);
                NavHostFragment.findNavController(this).navigate(action);
            });
        }

        if (mHolder.damageResistances != null) {
            mHolder.damageResistances.setOnClickListener(v -> {
                NavDirections action = EditMonsterFragmentDirections.actionEditMonsterFragmentToEditStringsFragment(StringType.DAMAGE_RESISTANCE);
                NavHostFragment.findNavController(this).navigate(action);
            });
        }

        if (mHolder.damageVulnerabilities != null) {
            mHolder.damageVulnerabilities.setOnClickListener(v -> {
                NavDirections action = EditMonsterFragmentDirections.actionEditMonsterFragmentToEditStringsFragment(StringType.DAMAGE_VULNERABILITY);
                NavHostFragment.findNavController(this).navigate(action);
            });
        }

        if (mHolder.abilities != null) {
            mHolder.abilities.setOnClickListener(v -> {
                NavDirections action = EditMonsterFragmentDirections.actionEditMonsterFragmentToEditTraitListFragment(TraitType.ABILITY);
                NavHostFragment.findNavController(this).navigate(action);
            });
        }

        if (mHolder.actions != null) {
            mHolder.actions.setOnClickListener(v -> {
                NavDirections action = EditMonsterFragmentDirections.actionEditMonsterFragmentToEditTraitListFragment(TraitType.ACTION);
                NavHostFragment.findNavController(this).navigate(action);
            });
        }

        if (mHolder.reactions != null) {
            mHolder.reactions.setOnClickListener(v -> {
                NavDirections action = EditMonsterFragmentDirections.actionEditMonsterFragmentToEditTraitListFragment(TraitType.REACTIONS);
                NavHostFragment.findNavController(this).navigate(action);
            });
        }

        if (mHolder.legendaryActions != null) {
            mHolder.legendaryActions.setOnClickListener(v -> {
                NavDirections action = EditMonsterFragmentDirections.actionEditMonsterFragmentToEditTraitListFragment(TraitType.LEGENDARY_ACTION);
                NavHostFragment.findNavController(this).navigate(action);
            });
        }

        if (mHolder.lairActions != null) {
            mHolder.lairActions.setOnClickListener(v -> {
                NavDirections action = EditMonsterFragmentDirections.actionEditMonsterFragmentToEditTraitListFragment(TraitType.LAIR_ACTION);
                NavHostFragment.findNavController(this).navigate(action);
            });
        }

        if (mHolder.regionalActions != null) {
            mHolder.regionalActions.setOnClickListener(v -> {
                NavDirections action = EditMonsterFragmentDirections.actionEditMonsterFragmentToEditTraitListFragment(TraitType.REGIONAL_ACTION);
                NavHostFragment.findNavController(this).navigate(action);
            });
        }
    }

    private static class ViewHolder {
        final View headerBasicInfo;
        final View bodyBasicInfo;
        final ImageView chevronBasicInfo;
        final TextView summaryBasicInfo;

        final View headerCombatAbilities;
        final View bodyCombatAbilities;
        final ImageView chevronCombatAbilities;
        final TextView summaryCombatAbilities;

        final View headerSavesSkillsLanguages;
        final View bodySavesSkillsLanguages;
        final ImageView chevronSavesSkillsLanguages;
        final TextView summarySavesSkillsLanguages;

        final View headerDefenses;
        final View bodyDefenses;
        final ImageView chevronDefenses;
        final TextView summaryDefenses;

        final View headerTraitsActions;
        final View bodyTraitsActions;
        final ImageView chevronTraitsActions;
        final TextView summaryTraitsActions;

        final TextView basicInfoButton;
        final TextView challengeRating;
        final TextView armorButton;
        final TextView speedButton;
        final TextView abilityScoresButton;
        final TextView savingThrows;
        final TextView skills;
        final TextView senses;
        final TextView languages;
        final TextView conditionImmunities;
        final TextView damageImmunities;
        final TextView damageResistances;
        final TextView damageVulnerabilities;
        final TextView abilities;
        final TextView actions;
        final TextView reactions;
        final TextView legendaryActions;
        final TextView lairActions;
        final TextView regionalActions;

        ViewHolder(@NonNull View root) {
            headerBasicInfo = root.findViewById(R.id.headerBasicInfo);
            bodyBasicInfo = root.findViewById(R.id.bodyBasicInfo);
            chevronBasicInfo = root.findViewById(R.id.chevronBasicInfo);
            summaryBasicInfo = root.findViewById(R.id.summaryBasicInfo);

            headerCombatAbilities = root.findViewById(R.id.headerCombatAbilities);
            bodyCombatAbilities = root.findViewById(R.id.bodyCombatAbilities);
            chevronCombatAbilities = root.findViewById(R.id.chevronCombatAbilities);
            summaryCombatAbilities = root.findViewById(R.id.summaryCombatAbilities);

            headerSavesSkillsLanguages = root.findViewById(R.id.headerSavesSkillsLanguages);
            bodySavesSkillsLanguages = root.findViewById(R.id.bodySavesSkillsLanguages);
            chevronSavesSkillsLanguages = root.findViewById(R.id.chevronSavesSkillsLanguages);
            summarySavesSkillsLanguages = root.findViewById(R.id.summarySavesSkillsLanguages);

            headerDefenses = root.findViewById(R.id.headerDefenses);
            bodyDefenses = root.findViewById(R.id.bodyDefenses);
            chevronDefenses = root.findViewById(R.id.chevronDefenses);
            summaryDefenses = root.findViewById(R.id.summaryDefenses);

            headerTraitsActions = root.findViewById(R.id.headerTraitsActions);
            bodyTraitsActions = root.findViewById(R.id.bodyTraitsActions);
            chevronTraitsActions = root.findViewById(R.id.chevronTraitsActions);
            summaryTraitsActions = root.findViewById(R.id.summaryTraitsActions);

            basicInfoButton = root.findViewById(R.id.basicInfo);
            challengeRating = root.findViewById(R.id.challengeRating);
            armorButton = root.findViewById(R.id.armor);
            speedButton = root.findViewById(R.id.speed);
            abilityScoresButton = root.findViewById(R.id.abilityScores);
            savingThrows = root.findViewById(R.id.savingThrows);
            skills = root.findViewById(R.id.skills);
            senses = root.findViewById(R.id.senses);
            languages = root.findViewById(R.id.languages);
            conditionImmunities = root.findViewById(R.id.conditionImmunities);
            damageImmunities = root.findViewById(R.id.damageImmunities);
            damageResistances = root.findViewById(R.id.damageResistances);
            damageVulnerabilities = root.findViewById(R.id.damageVulnerabilities);
            abilities = root.findViewById(R.id.abilities);
            actions = root.findViewById(R.id.actions);
            reactions = root.findViewById(R.id.reactions);
            legendaryActions = root.findViewById(R.id.legendaryActions);
            lairActions = root.findViewById(R.id.lairActions);
            regionalActions = root.findViewById(R.id.regionalActions);
        }
    }
}
