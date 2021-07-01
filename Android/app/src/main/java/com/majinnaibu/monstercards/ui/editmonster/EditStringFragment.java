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
import com.majinnaibu.monstercards.data.enums.StringType;
import com.majinnaibu.monstercards.ui.shared.MCFragment;
import com.majinnaibu.monstercards.utils.Logger;
import com.majinnaibu.monstercards.utils.TextChangedListener;

public class EditStringFragment extends MCFragment {
    private EditMonsterViewModel mEditMonsterViewModel;
    private EditStringViewModel mViewModel;
    private ViewHolder mHolder;
    private String mOldValue;
    private StringType mStringType;

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(EditStringViewModel.class);
        if (getArguments() != null) {
            EditStringFragmentArgs args = EditStringFragmentArgs.fromBundle(getArguments());
            mOldValue = args.getValue();
            mViewModel.setValue(mOldValue);
            mStringType = args.getStringType();
        } else {
            Logger.logWTF("EditStringFragment needs arguments");
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
        View root = inflater.inflate(R.layout.fragment_edit_string, container, false);
        mHolder = new ViewHolder(root);
        setTitle(getTitleForStringType(mStringType));

        mHolder.description.setText(mViewModel.getValueAsString());
        mHolder.description.addTextChangedListener(new TextChangedListener((TextChangedListener.OnTextChangedCallback) (s, start, before, count) -> mViewModel.setValue(s.toString())));

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (mViewModel.hasChanges()) {
                    mEditMonsterViewModel.replaceString(mStringType, mOldValue, mViewModel.getValueAsString());
                }
                Navigation.findNavController(requireView()).navigateUp();
            }
        });

        return root;
    }

    private String getTitleForStringType(StringType type) {
        switch (type) {
            case CONDITION_IMMUNITY:
                return getString(R.string.title_editConditionImmunity);
            case DAMAGE_IMMUNITY:
                return getString(R.string.title_editDamageImmunity);
            case DAMAGE_RESISTANCE:
                return getString(R.string.title_editDamageResistance);
            case DAMAGE_VULNERABILITY:
                return getString(R.string.title_editDamageVulnerability);
            case SENSE:
                return getString(R.string.title_editSense);
            default:
                return getString(R.string.title_editString);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mHolder.description.requestFocus();
    }

    private static class ViewHolder {
        EditText description;

        ViewHolder(View root) {
            description = root.findViewById(R.id.description);
        }
    }
}
