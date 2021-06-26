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
import com.majinnaibu.monstercards.models.Skill;
import com.majinnaibu.monstercards.ui.components.AbilityScorePicker;
import com.majinnaibu.monstercards.ui.components.AdvantagePicker;
import com.majinnaibu.monstercards.ui.components.ProficiencyPicker;
import com.majinnaibu.monstercards.ui.shared.MCFragment;
import com.majinnaibu.monstercards.utils.Logger;
import com.majinnaibu.monstercards.utils.TextChangedListener;

public class EditSkillFragment extends MCFragment {
    private EditMonsterViewModel mEditMonsterViewModel;
    private EditSkillViewModel mViewModel;
    private ViewHolder mHolder;
    private Skill mOldSkill;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(EditSkillViewModel.class);
        if (getArguments() != null) {
            EditSkillFragmentArgs args = EditSkillFragmentArgs.fromBundle(getArguments());
            mOldSkill = new Skill(args.getName(), args.getAbilityScore(), args.getAdvantage(), args.getProficiency());
            mViewModel.copyFromSkill(mOldSkill);
        } else {
            Logger.logWTF("EditSkillFragment needs arguments.");
            mOldSkill = null;
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.edit_monster_navigation);
        mEditMonsterViewModel = new ViewModelProvider(backStackEntry).get(EditMonsterViewModel.class);
        View root = inflater.inflate(R.layout.fragment_edit_skill, container, false);
        mHolder = new ViewHolder(root);
        setTitle(getString(R.string.title_edit_skill));

        mHolder.abilityScore.setValue(mViewModel.getAbilityScore().getValue());
        mHolder.abilityScore.setOnValueChangedListener(value -> mViewModel.setAbilityScore(value));

        mHolder.advantage.setValue(mViewModel.getAdvantage().getValue());
        mHolder.advantage.setOnValueChangedListener(value -> mViewModel.setAdvantage(value));

        mHolder.proficiency.setValue(mViewModel.getProficiency().getValue());
        mHolder.proficiency.setOnValueChangedListener(value -> mViewModel.setProficiency(value));

        mHolder.name.setText(mViewModel.getName().getValue());
        mHolder.name.addTextChangedListener(new TextChangedListener((TextChangedListener.OnTextChangedCallback) (s, start, before, count) -> mViewModel.setName(s.toString())));

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (mViewModel.hasChanges()) {
                    mEditMonsterViewModel.replaceSkill(mViewModel.getSkill().getValue(), mOldSkill);
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
        AbilityScorePicker abilityScore;
        AdvantagePicker advantage;
        ProficiencyPicker proficiency;
        EditText name;

        ViewHolder(View root) {
            abilityScore = root.findViewById(R.id.abilityScore);
            advantage = root.findViewById(R.id.advantage);
            proficiency = root.findViewById(R.id.proficiency);
            name = root.findViewById(R.id.name);
        }
    }
}
