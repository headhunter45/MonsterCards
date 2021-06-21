package com.majinnaibu.monstercards.ui.editmonster;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.majinnaibu.monstercards.R;
import com.majinnaibu.monstercards.ui.components.AdvantagePicker;
import com.majinnaibu.monstercards.ui.components.ProficiencyPicker;
import com.majinnaibu.monstercards.ui.shared.MCFragment;

public class EditSavingThrowsFragment extends MCFragment {
    private EditMonsterViewModel mViewModel;
    private ViewHolder mViewHolder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.edit_monster_navigation);
        mViewModel = new ViewModelProvider(backStackEntry).get(EditMonsterViewModel.class);

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_edit_saving_throws, container, false);

        mViewHolder = new ViewHolder(root);
        mViewHolder.strengthProficiency.setValue(mViewModel.getStrengthProficiency().getValue());
        mViewHolder.strengthProficiency.setOnValueChangedListener(value -> mViewModel.setStrengthProficiency(value));
        mViewHolder.strengthAdvantage.setValue(mViewModel.getStrengthAdvantage().getValue());
        mViewHolder.strengthAdvantage.setOnValueChangedListener(value -> mViewModel.setStrengthAdvantage(value));

        mViewHolder.dexterityProficiency.setValue(mViewModel.getDexterityProficiency().getValue());
        mViewHolder.dexterityProficiency.setOnValueChangedListener(value -> mViewModel.setDexterityProficiency(value));
        mViewHolder.dexterityAdvantage.setValue(mViewModel.getDexterityAdvantage().getValue());
        mViewHolder.dexterityAdvantage.setOnValueChangedListener(value -> mViewModel.setDexterityAdvantage(value));

        mViewHolder.constitutionProficiency.setValue(mViewModel.getConstitutionProficiency().getValue());
        mViewHolder.constitutionProficiency.setOnValueChangedListener(value -> mViewModel.setConstitutionProficiency(value));
        mViewHolder.constitutionAdvantage.setValue(mViewModel.getConstitutionAdvantage().getValue());
        mViewHolder.constitutionAdvantage.setOnValueChangedListener(value -> mViewModel.setConstitutionAdvantage(value));

        mViewHolder.intelligenceProficiency.setValue(mViewModel.getIntelligenceProficiency().getValue());
        mViewHolder.intelligenceProficiency.setOnValueChangedListener(value -> mViewModel.setIntelligenceProficiency(value));
        mViewHolder.intelligenceAdvantage.setValue(mViewModel.getIntelligenceAdvantage().getValue());
        mViewHolder.intelligenceAdvantage.setOnValueChangedListener(value -> mViewModel.setIntelligenceAdvantage(value));

        mViewHolder.wisdomProficiency.setValue(mViewModel.getWisdomProficiency().getValue());
        mViewHolder.wisdomProficiency.setOnValueChangedListener(value -> mViewModel.setWisdomProficiency(value));
        mViewHolder.wisdomAdvantage.setValue(mViewModel.getWisdomAdvantage().getValue());
        mViewHolder.wisdomAdvantage.setOnValueChangedListener(value -> mViewModel.setWisdomAdvantage(value));

        mViewHolder.charismaProficiency.setValue(mViewModel.getCharismaProficiency().getValue());
        mViewHolder.charismaProficiency.setOnValueChangedListener(value -> mViewModel.setCharismaProficiency(value));
        mViewHolder.charismaAdvantage.setValue(mViewModel.getCharismaAdvantage().getValue());
        mViewHolder.charismaAdvantage.setOnValueChangedListener(value -> mViewModel.setCharismaAdvantage(value));

        return root;
    }

    private static class ViewHolder {
        AdvantagePicker strengthAdvantage;
        ProficiencyPicker strengthProficiency;
        AdvantagePicker dexterityAdvantage;
        ProficiencyPicker dexterityProficiency;
        AdvantagePicker constitutionAdvantage;
        ProficiencyPicker constitutionProficiency;
        AdvantagePicker intelligenceAdvantage;
        ProficiencyPicker intelligenceProficiency;
        AdvantagePicker wisdomAdvantage;
        ProficiencyPicker wisdomProficiency;
        AdvantagePicker charismaAdvantage;
        ProficiencyPicker charismaProficiency;

        ViewHolder(View root) {
            strengthAdvantage = root.findViewById(R.id.strengthAdvantage);
            strengthProficiency = root.findViewById(R.id.strengthProficiency);
            dexterityAdvantage = root.findViewById(R.id.dexterityAdvantage);
            dexterityProficiency = root.findViewById(R.id.dexterityProficiency);
            constitutionAdvantage = root.findViewById(R.id.constitutionAdvantage);
            constitutionProficiency = root.findViewById(R.id.constitutionProficiency);
            intelligenceAdvantage = root.findViewById(R.id.intelligenceAdvantage);
            intelligenceProficiency = root.findViewById(R.id.intelligenceProficiency);
            wisdomAdvantage = root.findViewById(R.id.wisdomAdvantage);
            wisdomProficiency = root.findViewById(R.id.wisdomProficiency);
            charismaAdvantage = root.findViewById(R.id.charismaAdvantage);
            charismaProficiency = root.findViewById(R.id.charismaProficiency);
        }
    }
}