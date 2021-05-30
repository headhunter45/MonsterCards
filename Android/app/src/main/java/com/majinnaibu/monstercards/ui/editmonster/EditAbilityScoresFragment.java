package com.majinnaibu.monstercards.ui.editmonster;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.majinnaibu.monstercards.R;

public class EditAbilityScoresFragment extends Fragment {
    private EditMonsterViewModel mViewModel;
    private ViewHolder mHolder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.edit_monster_navigation);
        mViewModel = new ViewModelProvider(backStackEntry).get(EditMonsterViewModel.class);

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_edit_ability_scores, container, false);

        mHolder = new ViewHolder(root);

        mViewModel.getStrength().observe(getViewLifecycleOwner(), value -> mHolder.strength.setText(String.valueOf(value)));
        mHolder.increaseStrength.setOnClickListener(v -> mViewModel.incrementStrength());
        mHolder.decreaseStrength.setOnClickListener(v -> mViewModel.decrementStrength());

        mViewModel.getDexterity().observe(getViewLifecycleOwner(), value -> mHolder.dexterity.setText(String.valueOf(value)));
        mHolder.increaseDexterity.setOnClickListener(v -> mViewModel.incrementDexterity());
        mHolder.decreaseDexterity.setOnClickListener(v -> mViewModel.decrementDexterity());

        mViewModel.getConstitution().observe(getViewLifecycleOwner(), value -> mHolder.constitution.setText(String.valueOf(value)));
        mHolder.increaseConstitution.setOnClickListener(v -> mViewModel.incrementConstitution());
        mHolder.decreaseConstitution.setOnClickListener(v -> mViewModel.decrementConstitution());

        mViewModel.getIntelligence().observe(getViewLifecycleOwner(), value -> mHolder.intelligence.setText(String.valueOf(value)));
        mHolder.increaseIntelligence.setOnClickListener(v -> mViewModel.incrementIntelligence());
        mHolder.decreaseIntelligence.setOnClickListener(v -> mViewModel.decrementIntelligence());

        mViewModel.getWisdom().observe(getViewLifecycleOwner(), value -> mHolder.wisdom.setText(String.valueOf(value)));
        mHolder.increaseWisdom.setOnClickListener(v -> mViewModel.incrementWisdom());
        mHolder.decreaseWisdom.setOnClickListener(v -> mViewModel.decrementWisdom());

        mViewModel.getCharisma().observe(getViewLifecycleOwner(), value -> mHolder.charisma.setText(String.valueOf(value)));
        mHolder.increaseCharisma.setOnClickListener(v -> mViewModel.incrementCharisma());
        mHolder.decreaseCharisma.setOnClickListener(v -> mViewModel.decrementCharisma());

        return root;
    }

    private static class ViewHolder {
        TextView strength;
        Button decreaseStrength;
        Button increaseStrength;
        TextView dexterity;
        Button increaseDexterity;
        Button decreaseDexterity;
        TextView constitution;
        Button increaseConstitution;
        Button decreaseConstitution;
        TextView intelligence;
        Button increaseIntelligence;
        Button decreaseIntelligence;
        TextView wisdom;
        Button increaseWisdom;
        Button decreaseWisdom;
        TextView charisma;
        Button increaseCharisma;
        Button decreaseCharisma;

        ViewHolder(View root) {
            strength = root.findViewById(R.id.strength);
            increaseStrength = root.findViewById(R.id.strength_increment);
            decreaseStrength = root.findViewById(R.id.strength_decrement);
            dexterity = root.findViewById(R.id.dexterity);
            increaseDexterity = root.findViewById(R.id.dexterity_increment);
            decreaseDexterity = root.findViewById(R.id.dexterity_decrement);
            constitution = root.findViewById(R.id.constitution);
            increaseConstitution = root.findViewById(R.id.constitution_increment);
            decreaseConstitution = root.findViewById(R.id.constitution_decrement);
            intelligence = root.findViewById(R.id.intelligence);
            increaseIntelligence = root.findViewById(R.id.intelligence_increment);
            decreaseIntelligence = root.findViewById(R.id.intelligence_decrement);
            wisdom = root.findViewById(R.id.wisdom);
            increaseWisdom = root.findViewById(R.id.wisdom_increment);
            decreaseWisdom = root.findViewById(R.id.wisdom_decrement);
            charisma = root.findViewById(R.id.charisma);
            increaseCharisma = root.findViewById(R.id.charisma_increment);
            decreaseCharisma = root.findViewById(R.id.charisma_decrement);
        }
    }
}