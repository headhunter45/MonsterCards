package com.majinnaibu.monstercards.ui.editmonster;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.majinnaibu.monstercards.R;
import com.majinnaibu.monstercards.utils.Logger;
import com.majinnaibu.monstercards.utils.TextChangedListener;

@SuppressWarnings("FieldCanBeLocal")
public class EditConditionImmunityFragment extends Fragment {
    private EditMonsterViewModel mEditMonsterViewModel;
    private EditConditionImmunityViewModel mViewModel;
    private ViewHolder mHolder;
    private String mOldValue;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(EditConditionImmunityViewModel.class);
        if (getArguments() != null) {
            EditConditionImmunityFragmentArgs args = EditConditionImmunityFragmentArgs.fromBundle(getArguments());
            mOldValue = args.getCondition();
            mViewModel.reset(mOldValue);
        } else {
            Logger.logWTF("This should never happen. EditConditionImmunityFragment needs arguments");
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
        mHolder.description.setText(mViewModel.getDescription().getValue());
        mHolder.description.addTextChangedListener(new TextChangedListener((TextChangedListener.OnTextChangedCallback) (s, start, before, count) -> mViewModel.setDescription(s.toString())));

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (mViewModel.hasChanges()) {
                    mEditMonsterViewModel.replaceConditionImmunity(mOldValue, mViewModel.getDescription().getValue());
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