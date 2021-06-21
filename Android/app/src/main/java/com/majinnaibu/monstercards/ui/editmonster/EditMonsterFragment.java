package com.majinnaibu.monstercards.ui.editmonster;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.google.android.material.snackbar.Snackbar;
import com.majinnaibu.monstercards.R;
import com.majinnaibu.monstercards.data.MonsterRepository;
import com.majinnaibu.monstercards.models.Monster;
import com.majinnaibu.monstercards.ui.monster.MonsterDetailFragmentArgs;
import com.majinnaibu.monstercards.ui.shared.MCFragment;
import com.majinnaibu.monstercards.utils.Logger;

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

        MonsterRepository repository = getMonsterRepository();
        Bundle arguments = getArguments();
        assert arguments != null;
        UUID monsterId = UUID.fromString(MonsterDetailFragmentArgs.fromBundle(arguments).getMonsterId());

        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.edit_monster_navigation);
        mViewModel = new ViewModelProvider(backStackEntry).get(EditMonsterViewModel.class);

        View root = inflater.inflate(R.layout.fragment_edit_monster, container, false);
        mHolder = new ViewHolder(root);

        setTitle(getString(R.string.title_edit_monster, getString(R.string.default_monster_name)));

        // TODO: Show a loading spinner until we have the monster loaded.
        if (mViewModel.hasError() || !mViewModel.hasLoaded() || !Objects.equals(mViewModel.getMonsterId().getValue(), monsterId)) {
            repository.getMonster(monsterId).toObservable()
                    .firstOrError()
                    .subscribe(new DisposableSingleObserver<Monster>() {
                        @Override
                        public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull Monster monster) {
                            mViewModel.setHasLoaded(true);
                            mViewModel.setHasError(false);
                            mViewModel.copyFromMonster(monster);
                            setTitle(getString(R.string.title_edit_monster, monster.name));
                            dispose();
                        }

                        @Override
                        public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                            // TODO: Show an error state.
                            Logger.logError(e);
                            mViewModel.setHasError(true);
                            mViewModel.setErrorMessage(e.toString());
                            dispose();
                        }
                    });
        }

        mHolder.basicInfoButton.setOnClickListener(v -> {
            NavDirections action = EditMonsterFragmentDirections.actionEditMonsterFragmentToEditBasicInfoFragment();
            Navigation.findNavController(requireView()).navigate(action);
        });

        mHolder.armorButton.setOnClickListener(v -> {
            NavDirections action = EditMonsterFragmentDirections.actionEditMonsterFragmentToEditArmorFragment();
            Navigation.findNavController(requireView()).navigate(action);
        });

        mHolder.speedButton.setOnClickListener(v -> {
            NavDirections action = EditMonsterFragmentDirections.actionEditMonsterFragmentToEditSpeedFragment();
            Navigation.findNavController(requireView()).navigate(action);
        });

        mHolder.abilityScoresButton.setOnClickListener(v -> {
            NavDirections action = EditMonsterFragmentDirections.actionEditMonsterFragmentToEditAbilityScoresFragment();
            Navigation.findNavController(requireView()).navigate(action);
        });

        mHolder.savingThrows.setOnClickListener(v -> {
            NavDirections action = EditMonsterFragmentDirections.actionEditMonsterFragmentToEditSavingThrowsFragment();
            Navigation.findNavController(requireView()).navigate(action);
        });

        mHolder.challengeRating.setOnClickListener(v -> {
            NavDirections action = EditMonsterFragmentDirections.actionEditMonsterFragmentToEditChallengeRatingFragment();
            Navigation.findNavController(requireView()).navigate(action);
        });

        mHolder.skills.setOnClickListener(v -> {
            NavDirections action = EditMonsterFragmentDirections.actionEditMonsterFragmentToEditSkillsFragment();
            Navigation.findNavController(requireView()).navigate(action);
        });

        mHolder.senses.setOnClickListener(v -> {
            NavDirections action = EditMonsterFragmentDirections.actionEditMonsterFragmentToEditSensesFragment();
            Navigation.findNavController(requireView()).navigate(action);
        });

        mHolder.conditionImmunities.setOnClickListener(v -> {
            NavDirections action = EditMonsterFragmentDirections.actionEditMonsterFragmentToEditConditionImmunitiesFragment();
            Navigation.findNavController(requireView()).navigate(action);
        });

        mHolder.damageResistances.setOnClickListener(v -> {
            NavDirections action = EditMonsterFragmentDirections.actionEditMonsterFragmentToEditDamageResistancesFragment();
            Navigation.findNavController(requireView()).navigate(action);
        });

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (mViewModel.hasChanges()) {
                    View view = getView();
                    AlertDialog alertDialog = new AlertDialog.Builder(requireContext()).create();
                    alertDialog.setTitle("Unsaved Changes");
                    alertDialog.setMessage("Do you want to save your changes?");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Save", (dialog, id) -> {
                        // Save the monster. Navigate up if the save is successful. Show a SnackBar if there was an error.
                        getMonsterRepository().saveMonster(mViewModel.buildMonster())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        new DisposableCompletableObserver() {
                                            @Override
                                            public void onComplete() {
                                                Navigation.findNavController(requireView()).navigateUp();
                                            }

                                            @Override
                                            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                                                Logger.logError("Error creating monster", e);
                                                assert view != null;
                                                Snackbar.make(view, getString(R.string.snackbar_failed_to_create_monster), Snackbar.LENGTH_LONG)
                                                        .setAction("Action", null).show();
                                            }
                                        });
                    });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Discard", (dialog, id) -> {
                        // Navigate up ignoring unsaved changes.
                        Navigation.findNavController(requireView()).navigateUp();
                    });
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Cancel", (dialog, id) -> {
                        // Do nothing.
                    });
                    alertDialog.show();
                } else {
                    // No changes so we can safely leave.
                    Navigation.findNavController(requireView()).navigateUp();
                }
            }
        });

        return root;
    }

    private static class ViewHolder {

        TextView basicInfoButton;
        TextView armorButton;
        TextView speedButton;
        TextView abilityScoresButton;
        TextView savingThrows;
        TextView skills;
        TextView conditionImmunities;
        TextView damageImmunities;
        TextView damageResistances;
        TextView damageVulnerabilities;
        TextView senses;
        TextView languages;
        TextView challengeRating;
        TextView abilities;
        TextView actions;
        TextView reactions;
        TextView legendaryActions;
        TextView lairActions;
        TextView regionalActions;

        ViewHolder(View root) {
            basicInfoButton = root.findViewById(R.id.basicInfo);
            armorButton = root.findViewById(R.id.armor);
            speedButton = root.findViewById(R.id.speed);
            abilityScoresButton = root.findViewById(R.id.abilityScores);
            savingThrows = root.findViewById(R.id.savingThrows);
            skills = root.findViewById(R.id.skills);
            conditionImmunities = root.findViewById(R.id.conditionImmunities);
            damageImmunities = root.findViewById(R.id.damageImmunities);
            damageResistances = root.findViewById(R.id.damageResistances);
            damageVulnerabilities = root.findViewById(R.id.damageVulnerabilities);
            senses = root.findViewById(R.id.senses);
            languages = root.findViewById(R.id.languages);
            challengeRating = root.findViewById(R.id.challengeRating);
            abilities = root.findViewById(R.id.abilities);
            actions = root.findViewById(R.id.actions);
            reactions = root.findViewById(R.id.reactions);
            legendaryActions = root.findViewById(R.id.legendaryActions);
            lairActions = root.findViewById(R.id.lairActions);
            regionalActions = root.findViewById(R.id.regionalActions);
        }
    }
}