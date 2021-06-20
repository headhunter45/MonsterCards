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
import com.majinnaibu.monstercards.ui.MCFragment;
import com.majinnaibu.monstercards.utils.Logger;
import com.majinnaibu.monstercards.utils.TextChangedListener;

public class EditSenseFragment extends MCFragment {
    private EditMonsterViewModel mEditMonsterViewModel;
    private EditSenseViewModel mViewModel;
    private ViewHolder mHolder;
    private String mOldSense;

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(EditSenseViewModel.class);
        if (getArguments() != null) {
            EditSenseFragmentArgs args = EditSenseFragmentArgs.fromBundle(getArguments());
            mOldSense = args.getSense();
            mViewModel.copyFromSense(mOldSense);
        } else {
            Logger.logWTF("This should never happen. EditSenseFragment needs arguments");
            mOldSense = null;
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.edit_monster_navigation);
        mEditMonsterViewModel = new ViewModelProvider(backStackEntry).get(EditMonsterViewModel.class);

        View root = inflater.inflate(R.layout.fragment_edit_sense, container, false);

        mHolder = new ViewHolder(root);

        mHolder.description.setText(mViewModel.getDescription().getValue());
        mHolder.description.addTextChangedListener(new TextChangedListener((TextChangedListener.OnTextChangedCallback) (s, start, before, count) -> mViewModel.setDescription(s.toString())));

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (mViewModel.hasChanges()) {
                    mEditMonsterViewModel.replaceSense(mOldSense, mViewModel.getDescription().getValue());
                }
                Navigation.findNavController(requireView()).navigateUp();
            }
        });

        return root;
    }

    private static class ViewHolder {
        EditText description;

        ViewHolder(View root) {
            description = root.findViewById(R.id.name);
        }
    }

}