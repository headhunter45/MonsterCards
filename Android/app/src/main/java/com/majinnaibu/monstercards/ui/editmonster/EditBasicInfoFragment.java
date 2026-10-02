package com.majinnaibu.monstercards.ui.editmonster;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.majinnaibu.monstercards.R;
import com.majinnaibu.monstercards.data.enums.GameSystem;
import com.majinnaibu.monstercards.ui.components.Stepper;
import com.majinnaibu.monstercards.ui.shared.MCFragment;
import com.majinnaibu.monstercards.utils.TextChangedListener;

public class EditBasicInfoFragment extends MCFragment {
    private EditMonsterViewModel mViewModel;
    private ViewHolder mHolder;

    @Override
    public void onStart() {
        super.onStart();
        mHolder.name.requestFocus();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.edit_monster_navigation);
        mViewModel = new ViewModelProvider(backStackEntry).get(EditMonsterViewModel.class);
        View root = inflater.inflate(R.layout.fragment_edit_basic_info, container, false);
        mHolder = new ViewHolder(root);

        mHolder.name.setText(mViewModel.getName().getValue());
        mHolder.name.addTextChangedListener(new TextChangedListener((TextChangedListener.OnTextChangedCallback) (s, start, before, count) -> mViewModel.setName(s.toString())));

        mHolder.size.setText(mViewModel.getSize().getValue());
        mHolder.size.addTextChangedListener(new TextChangedListener((TextChangedListener.OnTextChangedCallback) (s, start, before, count) -> mViewModel.setSize(s.toString())));

        mHolder.type.setText(mViewModel.getType().getValue());
        mHolder.type.addTextChangedListener(new TextChangedListener((TextChangedListener.OnTextChangedCallback) (s, start, before, count) -> mViewModel.setType(s.toString())));

        mHolder.subtype.setText(mViewModel.getSubtype().getValue());
        mHolder.subtype.addTextChangedListener(new TextChangedListener((TextChangedListener.OnTextChangedCallback) (s, start, before, count) -> mViewModel.setSubtype(s.toString())));

        mHolder.alignment.setText(mViewModel.getAlignment().getValue());
        mHolder.alignment.addTextChangedListener(new TextChangedListener((TextChangedListener.OnTextChangedCallback) (s, start, before, count) -> mViewModel.setAlignment(s.toString())));

        ArrayAdapter<GameSystem> gameSystemAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, GameSystem.values());
        gameSystemAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mHolder.gameSystem.setAdapter(gameSystemAdapter);
        GameSystem currentSystem = mViewModel.getGameSystem().getValue();
        if (currentSystem != null) {
            mHolder.gameSystem.setSelection(currentSystem.ordinal());
        }
        mHolder.gameSystem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mViewModel.setGameSystem(GameSystem.values()[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mHolder.sourceLabel.setText(mViewModel.getSourceLabel().getValue());
        mHolder.sourceLabel.addTextChangedListener(new TextChangedListener((TextChangedListener.OnTextChangedCallback) (s, start, before, count) -> mViewModel.setSourceLabel(s.toString())));

        mHolder.customHitPoints.setText(mViewModel.getCustomHitPoints().getValue());
        mHolder.customHitPoints.addTextChangedListener((new TextChangedListener((TextChangedListener.OnTextChangedCallback) (s, start, before, count) -> mViewModel.setCustomHitPoints(s.toString()))));

        mHolder.hitDice.setValue(mViewModel.getHitDiceUnboxed());
        mHolder.hitDice.setOnValueChangeListener((newValue, oldValue) -> mViewModel.setHitDice(newValue));

        mHolder.hasCustomHitPoints.setChecked(mViewModel.getHasCustomHitPointsValueAsBoolean());
        mHolder.hasCustomHitPoints.setOnCheckedChangeListener((button, isChecked) -> mViewModel.setHasCustomHitPoints(isChecked));

        return root;
    }

    private static class ViewHolder {
        private final EditText name;
        private final EditText size;
        private final EditText type;
        private final EditText subtype;
        private final EditText alignment;
        private final Spinner gameSystem;
        private final EditText sourceLabel;
        private final EditText customHitPoints;
        private final Stepper hitDice;
        private final SwitchMaterial hasCustomHitPoints;

        ViewHolder(@NonNull View root) {
            name = root.findViewById(R.id.name);
            size = root.findViewById(R.id.size);
            type = root.findViewById(R.id.type);
            subtype = root.findViewById(R.id.subtype);
            alignment = root.findViewById(R.id.alignment);
            gameSystem = root.findViewById(R.id.gameSystem);
            sourceLabel = root.findViewById(R.id.sourceLabel);
            customHitPoints = root.findViewById(R.id.customHitPoints);
            hitDice = root.findViewById(R.id.hitDice);
            hasCustomHitPoints = root.findViewById(R.id.hasCustomHitPoints);
        }
    }
}
