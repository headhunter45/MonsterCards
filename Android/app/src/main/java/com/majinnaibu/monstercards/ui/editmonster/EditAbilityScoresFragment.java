package com.majinnaibu.monstercards.ui.editmonster;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.majinnaibu.monstercards.R;
import com.majinnaibu.monstercards.ui.components.Stepper;
import com.majinnaibu.monstercards.ui.shared.MCFragment;

public class EditAbilityScoresFragment extends Fragment {
    private final String ABILITY_SCORE_FORMAT = "%d (%+d)";
    private EditMonsterViewModel mViewModel;
    private ViewHolder mHolder;

    private int getModifier(int value) {
        return value / 2 - 5;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.edit_monster_navigation);
        mViewModel = new ViewModelProvider(backStackEntry).get(EditMonsterViewModel.class);

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_edit_ability_scores, container, false);

        mHolder = new ViewHolder(root);

        mViewModel.getStrength().observe(getViewLifecycleOwner(), value -> mHolder.strength.setValue(value));
        mHolder.strength.setOnValueChangeListener((newValue, oldValue) -> mViewModel.setStrength(newValue));
        mHolder.strength.setOnFormatValueCallback(value -> String.format(ABILITY_SCORE_FORMAT, value, getModifier(value)));

        mViewModel.getDexterity().observe(getViewLifecycleOwner(), value -> mHolder.dexterity.setValue(value));
        mHolder.dexterity.setOnValueChangeListener((newValue, oldValue) -> mViewModel.setDexterity(newValue));
        mHolder.dexterity.setOnFormatValueCallback(value -> String.format(ABILITY_SCORE_FORMAT, value, getModifier(value)));

        mViewModel.getConstitution().observe(getViewLifecycleOwner(), value -> mHolder.constitution.setValue(value));
        mHolder.constitution.setOnValueChangeListener((newValue, oldValue) -> mViewModel.setConstitution(newValue));
        mHolder.constitution.setOnFormatValueCallback(value -> String.format(ABILITY_SCORE_FORMAT, value, getModifier(value)));

        mViewModel.getIntelligence().observe(getViewLifecycleOwner(), value -> mHolder.intelligence.setValue(value));
        mHolder.intelligence.setOnValueChangeListener((newValue, oldValue) -> mViewModel.setIntelligence(newValue));
        mHolder.intelligence.setOnFormatValueCallback(value -> String.format(ABILITY_SCORE_FORMAT, value, getModifier(value)));

        mViewModel.getWisdom().observe(getViewLifecycleOwner(), value -> mHolder.wisdom.setValue(value));
        mHolder.wisdom.setOnValueChangeListener((newValue, oldValue) -> mViewModel.setWisdom(newValue));
        mHolder.wisdom.setOnFormatValueCallback(value -> String.format(ABILITY_SCORE_FORMAT, value, getModifier(value)));

        mViewModel.getCharisma().observe(getViewLifecycleOwner(), value -> mHolder.charisma.setValue(value));
        mHolder.charisma.setOnValueChangeListener((newValue, oldValue) -> mViewModel.setCharisma(newValue));
        mHolder.charisma.setOnFormatValueCallback(value -> String.format(ABILITY_SCORE_FORMAT, value, getModifier(value)));

        return root;
    }

    private static class ViewHolder {
        final Stepper strength;
        final Stepper dexterity;
        final Stepper constitution;
        final Stepper intelligence;
        final Stepper wisdom;
        final Stepper charisma;

        ViewHolder(View root) {
            strength = root.findViewById(R.id.strength);
            dexterity = root.findViewById(R.id.dexterity);
            constitution = root.findViewById(R.id.constitution);
            intelligence = root.findViewById(R.id.intelligence);
            wisdom = root.findViewById(R.id.wisdom);
            charisma = root.findViewById(R.id.charisma);
        }
    }
}