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
import com.majinnaibu.monstercards.ui.shared.MCFragment;
import com.majinnaibu.monstercards.utils.Logger;
import com.majinnaibu.monstercards.utils.TextChangedListener;

public class EditDamageImmunityFragment extends MCFragment {
    private EditMonsterViewModel mEditMonsterViewModel;
    private EditStringViewModel mViewModel;
    private EditDamageImmunityFragment.ViewHolder mHolder;
    private String mOldDamageImmunity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(EditStringViewModel.class);
        if (getArguments() != null) {
            EditDamageImmunityFragmentArgs args = EditDamageImmunityFragmentArgs.fromBundle(getArguments());
            mOldDamageImmunity = args.getDamageType();
            mViewModel.resetValue(mOldDamageImmunity);
        } else {
            Logger.logWTF("EditDamageImmunityFragment needs arguments");
            mOldDamageImmunity = null;
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.edit_monster_navigation);
        mEditMonsterViewModel = new ViewModelProvider(backStackEntry).get(EditMonsterViewModel.class);

        View root = inflater.inflate(R.layout.fragment_edit_damage_immunity, container, false);

        mHolder = new EditDamageImmunityFragment.ViewHolder(root);

        mHolder.value.setText(mViewModel.getValueAsString());
        mHolder.value.addTextChangedListener(new TextChangedListener((TextChangedListener.OnTextChangedCallback) (s, start, before, count) -> mViewModel.setValue(s.toString())));

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (mViewModel.hasChanges()) {
                    mEditMonsterViewModel.replaceDamageImmunity(mOldDamageImmunity, mViewModel.getValueAsString());
                }
                Navigation.findNavController(requireView()).navigateUp();
            }
        });

        return root;
    }

    private static class ViewHolder {
        EditText value;

        ViewHolder(View root) {
            value = root.findViewById(R.id.value);
        }
    }
}
