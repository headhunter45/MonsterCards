package com.majinnaibu.monstercards.ui.editmonster;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.majinnaibu.monstercards.R;
import com.majinnaibu.monstercards.data.enums.ArmorType;
import com.majinnaibu.monstercards.helpers.ArrayHelper;
import com.majinnaibu.monstercards.ui.components.Stepper;
import com.majinnaibu.monstercards.ui.shared.MCFragment;
import com.majinnaibu.monstercards.utils.TextChangedListener;

public class EditArmorFragment extends MCFragment {
    private EditMonsterViewModel mViewModel;
    private ViewHolder mHolder;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.edit_monster_navigation);
        mViewModel = new ViewModelProvider(backStackEntry).get(EditMonsterViewModel.class);
        View root = inflater.inflate(R.layout.fragment_edit_armor, container, false);
        mHolder = new ViewHolder(root);

        mHolder.armorType.setAdapter(new ArrayAdapter<ArmorType>(requireContext(), R.layout.dropdown_list_item, ArmorType.values()) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                ArmorType item = getItem(position);
                TextView view = (TextView) super.getView(position, convertView, parent);
                view.setText(item.displayName);
                return view;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                ArmorType item = getItem(position);
                TextView view = (TextView) super.getDropDownView(position, convertView, parent);
                view.setText(item.displayName);
                return view;
            }
        });
        mHolder.armorType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArmorType selectedItem = (ArmorType) parent.getItemAtPosition(position);
                mViewModel.setArmorType(selectedItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mViewModel.setArmorType(ArmorType.NONE);
            }
        });
        mHolder.armorType.setSelection(ArrayHelper.indexOf(ArmorType.values(), mViewModel.getArmorType().getValue()));

        mHolder.naturalArmorBonus.setValue(mViewModel.getNaturalArmorBonusUnboxed());
        mHolder.naturalArmorBonus.setOnValueChangeListener((newValue, oldValue) -> mViewModel.setNaturalArmorBonus(newValue));

        mHolder.hasShield.setChecked(mViewModel.getHasShieldValueAsBoolean());
        mHolder.hasShield.setOnCheckedChangeListener((buttonView, isChecked) -> mViewModel.setHasShield(isChecked));

        mHolder.shieldBonus.setValue(mViewModel.getShieldBonusUnboxed());
        mHolder.shieldBonus.setOnValueChangeListener((newValue, oldValue) -> mViewModel.setShieldBonus(newValue));

        mHolder.customArmor.setText(mViewModel.getCustomArmor().getValue());
        mHolder.customArmor.addTextChangedListener((new TextChangedListener((TextChangedListener.OnTextChangedCallback) (s, start, before, count) -> mViewModel.setCustomArmor(s.toString()))));

        return root;
    }

    private static class ViewHolder {
        private final Spinner armorType;
        private final Stepper naturalArmorBonus;
        private final SwitchCompat hasShield;
        private final Stepper shieldBonus;
        private final EditText customArmor;

        ViewHolder(@NonNull View root) {
            armorType = root.findViewById(R.id.armorType);
            naturalArmorBonus = root.findViewById(R.id.naturalArmorBonus);
            hasShield = root.findViewById(R.id.hasShield);
            shieldBonus = root.findViewById(R.id.shieldBonus);
            customArmor = root.findViewById(R.id.customArmor);
        }
    }
}
