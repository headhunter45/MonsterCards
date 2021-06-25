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

public class EditConditionImmunityFragment extends MCFragment {
    private EditMonsterViewModel mEditMonsterViewModel;
    private EditStringViewModel mViewModel;
    private ViewHolder mHolder;
    private String mOldValue;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(EditStringViewModel.class);
        if (getArguments() != null) {
            EditConditionImmunityFragmentArgs args = EditConditionImmunityFragmentArgs.fromBundle(getArguments());
            mOldValue = args.getCondition();
            mViewModel.resetValue(mOldValue);
        } else {
            Logger.logWTF("EditConditionImmunityFragment needs arguments");
            mOldValue = null;
        }

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.edit_monster_navigation);
        mEditMonsterViewModel = new ViewModelProvider(backStackEntry).get(EditMonsterViewModel.class);
        View root = inflater.inflate(R.layout.fragment_edit_condition_immunity, container, false);
        mHolder = new ViewHolder(root);
        mHolder.description.setText(mViewModel.getValueAsString());
        mHolder.description.addTextChangedListener(new TextChangedListener((TextChangedListener.OnTextChangedCallback) (s, start, before, count) -> mViewModel.setValue(s.toString())));

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (mViewModel.hasChanges()) {
                    mEditMonsterViewModel.replaceConditionImmunity(mOldValue, mViewModel.getValueAsString());
                }
                Navigation.findNavController(requireView()).navigateUp();
            }
        });

        return root;
    }

    private static class ViewHolder {
        EditText description;

        ViewHolder(View root) {
            description = root.findViewById(R.id.description);
        }
    }
}
