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
import com.majinnaibu.monstercards.ui.MCFragment;
import com.majinnaibu.monstercards.ui.monster.MonsterDetailFragmentArgs;
import com.majinnaibu.monstercards.utils.Logger;

import java.util.Objects;
import java.util.UUID;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableCompletableObserver;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

@SuppressWarnings("FieldCanBeLocal")
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
            View view = getView();
            assert view != null;
            Navigation.findNavController(view).navigate(action);
        });

        mHolder.armorButton.setOnClickListener(v -> {
            NavDirections action = EditMonsterFragmentDirections.actionEditMonsterFragmentToEditArmorFragment();
            View view = getView();
            assert view != null;
            Navigation.findNavController(view).navigate(action);
        });

        mHolder.speedButton.setOnClickListener(v -> {
            NavDirections action = EditMonsterFragmentDirections.actionEditMonsterFragmentToEditSpeedFragment();
            View view = getView();
            assert view != null;
            Navigation.findNavController(view).navigate(action);
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

        ViewHolder(View root) {
            basicInfoButton = root.findViewById(R.id.basicInfo);
            armorButton = root.findViewById(R.id.armor);
            speedButton = root.findViewById(R.id.speed);
        }
    }
}