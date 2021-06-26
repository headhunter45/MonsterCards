package com.majinnaibu.monstercards.ui.editmonster;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.majinnaibu.monstercards.R;
import com.majinnaibu.monstercards.data.enums.TraitType;
import com.majinnaibu.monstercards.models.Trait;
import com.majinnaibu.monstercards.ui.shared.MCFragment;
import com.majinnaibu.monstercards.utils.Logger;
import com.majinnaibu.monstercards.utils.TextChangedListener;

public class EditTraitFragment extends MCFragment {
    private EditMonsterViewModel mEditMonsterViewModel;
    private EditTraitViewModel mViewModel;
    private EditTraitFragment.ViewHolder mHolder;
    private Trait mOldValue;
    private TraitType mTraitType;

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(EditTraitViewModel.class);
        if (getArguments() != null) {
            EditTraitFragmentArgs args = EditTraitFragmentArgs.fromBundle(getArguments());
            mOldValue = new Trait(args.getName(), args.getDescription());
            mViewModel.copyFromTrait(mOldValue);
            mTraitType = args.getTraitType();
        } else {
            Logger.logWTF("EditTraitFragment needs arguments");
            mOldValue = null;
        }

        super.onCreate(savedInstanceState);
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.edit_monster_navigation);
        mEditMonsterViewModel = new ViewModelProvider(backStackEntry).get(EditMonsterViewModel.class);
        View root = inflater.inflate(R.layout.fragment_edit_trait, container, false);
        mHolder = new EditTraitFragment.ViewHolder(root);

        mHolder.name.setText(mViewModel.getNameAsString());
        mHolder.name.addTextChangedListener(new TextChangedListener((TextChangedListener.OnTextChangedCallback) (s, start, before, count) -> mViewModel.setName(s.toString())));

        mHolder.description.setText(mViewModel.getDescriptionAsString());
        mHolder.description.addTextChangedListener(new TextChangedListener((TextChangedListener.OnTextChangedCallback) (s, start, before, count) -> mViewModel.setDescription(s.toString())));

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (mViewModel.hasChanges()) {
                    mEditMonsterViewModel.replaceTrait(mTraitType, mOldValue, mViewModel.getAbilityValue());
                }
                Navigation.findNavController(requireView()).navigateUp();
            }
        });

        return root;
    }

    private static class ViewHolder {
        EditText description;
        EditText name;

        ViewHolder(View root) {
            description = root.findViewById(R.id.description);
            name = root.findViewById(R.id.name);
        }
    }
}
