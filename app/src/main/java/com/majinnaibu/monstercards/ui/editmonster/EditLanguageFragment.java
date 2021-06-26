package com.majinnaibu.monstercards.ui.editmonster;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.majinnaibu.monstercards.R;
import com.majinnaibu.monstercards.models.Language;
import com.majinnaibu.monstercards.ui.shared.MCFragment;
import com.majinnaibu.monstercards.utils.Logger;
import com.majinnaibu.monstercards.utils.TextChangedListener;

public class EditLanguageFragment extends MCFragment {
    private EditMonsterViewModel mEditMonsterViewModel;
    private EditLanguageViewModel mViewModel;
    private ViewHolder mHolder;
    private Language mOldLanguage;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(EditLanguageViewModel.class);
        Bundle arguments = getArguments();
        if (arguments != null) {
            EditLanguageFragmentArgs args = EditLanguageFragmentArgs.fromBundle(arguments);
            mOldLanguage = new Language(args.getName(), args.getCanSpeak());
            mViewModel.copyFromLanguage(mOldLanguage);
        } else {
            Logger.logWTF("EditLanguageFragment needs arguments");
            mOldLanguage = null;
        }
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.edit_monster_navigation);
        mEditMonsterViewModel = new ViewModelProvider(backStackEntry).get(EditMonsterViewModel.class);
        View root = inflater.inflate(R.layout.fragment_edit_language, container, false);
        mHolder = new ViewHolder(root);
        setTitle(getString(R.string.title_edit_language));

        mHolder.name.setText(mViewModel.getName().getValue());
        mHolder.name.addTextChangedListener(new TextChangedListener((TextChangedListener.OnTextChangedCallback) (s, start, before, count) -> mViewModel.setName(s.toString())));

        mHolder.canSpeak.setChecked(mViewModel.getCanSpeakValue());
        mHolder.canSpeak.setOnCheckedChangeListener((buttonView, isChecked) -> mViewModel.setCanSpeak(isChecked));

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (mViewModel.hasChanges()) {
                    mEditMonsterViewModel.replaceLanguage(mOldLanguage, mViewModel.getLanguage().getValue());
                }
                Navigation.findNavController(requireView()).navigateUp();
            }
        });

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        mHolder.name.requestFocus();
    }

    private static class ViewHolder {
        EditText name;
        SwitchCompat canSpeak;

        ViewHolder(View root) {
            name = root.findViewById(R.id.name);
            canSpeak = root.findViewById(R.id.canSpeak);
        }
    }
}
