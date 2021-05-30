package com.majinnaibu.monstercards.ui.editmonster;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.majinnaibu.monstercards.R;
import com.majinnaibu.monstercards.ui.components.Stepper;
import com.majinnaibu.monstercards.utils.TextChangedListener;

public class EditSpeedFragment extends Fragment {
    private EditMonsterViewModel mViewModel;
    private ViewHolder mHolder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.edit_monster_navigation);
        mViewModel = new ViewModelProvider(backStackEntry).get(EditMonsterViewModel.class);

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_edit_speed, container, false);

        mHolder = new ViewHolder(root);

        mHolder.baseSpeed.setOnValueChangeListener((newValue, oldValue) -> mViewModel.setWalkSpeed(newValue));
        mHolder.baseSpeed.setOnFormatValueCallback(value -> String.format(getString(R.string.format_distance_in_feet), value));
        mViewModel.getWalkSpeed().observe(getViewLifecycleOwner(), value -> mHolder.baseSpeed.setValue(value));

        mHolder.burrowSpeed.setOnValueChangeListener((newValue, oldValue) -> mViewModel.setBurrowSpeed(newValue));
        mHolder.burrowSpeed.setOnFormatValueCallback(value -> String.format(getString(R.string.format_distance_in_feet), value));
        mViewModel.getBurrowSpeed().observe(getViewLifecycleOwner(), value -> mHolder.burrowSpeed.setValue(value));

        mHolder.climbSpeed.setOnValueChangeListener((newValue, oldValue) -> mViewModel.setClimbSpeed(newValue));
        mHolder.climbSpeed.setOnFormatValueCallback(value -> String.format(getString(R.string.format_distance_in_feet), value));
        mViewModel.getClimbSpeed().observe(getViewLifecycleOwner(), value -> mHolder.climbSpeed.setValue(value));

        mHolder.flySpeed.setOnValueChangeListener((newValue, oldValue) -> mViewModel.setFlySpeed(newValue));
        mHolder.flySpeed.setOnFormatValueCallback(value -> String.format(getString(R.string.format_distance_in_feet), value));
        mViewModel.getFlySpeed().observe(getViewLifecycleOwner(), value -> mHolder.flySpeed.setValue(value));

        mHolder.swimSpeed.setOnValueChangeListener((newValue, oldValue) -> mViewModel.setSwimSpeed(newValue));
        mHolder.swimSpeed.setOnFormatValueCallback(value -> String.format(getString(R.string.format_distance_in_feet), value));
        mViewModel.getSwimSpeed().observe(getViewLifecycleOwner(), value -> mHolder.swimSpeed.setValue(value));

        mViewModel.getCanHover().observe(getViewLifecycleOwner(), value -> mHolder.canHover.setChecked(value));
        mHolder.canHover.setOnCheckedChangeListener((buttonView, isChecked) -> mViewModel.setCanHover(isChecked));

        mViewModel.getHasCustomSpeed().observe(getViewLifecycleOwner(), value -> mHolder.hasCustomSpeed.setChecked(value));
        mHolder.hasCustomSpeed.setOnCheckedChangeListener((buttonView, isChecked) -> mViewModel.setHasCustomSpeed(isChecked));

        //mViewModel.getCustomSpeed().observe(getViewLifecycleOwner(), value -> mHolder.customSpeed.setText(value));
        mHolder.customSpeed.setText(mViewModel.getCustomSpeed().getValue());
        mHolder.customSpeed.addTextChangedListener(new TextChangedListener((TextChangedListener.OnTextChangedCallback) (s, start, before, count) -> mViewModel.setCustomSpeed(s.toString())));

        return root;
    }

    private static class ViewHolder {

        final Stepper baseSpeed;
        final Stepper burrowSpeed;
        final Stepper climbSpeed;
        final Stepper flySpeed;
        final Stepper swimSpeed;
        final SwitchCompat canHover;
        final SwitchCompat hasCustomSpeed;
        final EditText customSpeed;

        ViewHolder(View root) {
            baseSpeed = root.findViewById(R.id.baseSpeed);
            burrowSpeed = root.findViewById(R.id.burrowSpeed);
            climbSpeed = root.findViewById(R.id.climbSpeed);
            flySpeed = root.findViewById(R.id.flySpeed);
            swimSpeed = root.findViewById(R.id.swimSpeed);
            canHover = root.findViewById(R.id.canHover);
            hasCustomSpeed = root.findViewById(R.id.hasCustomSpeed);
            customSpeed = root.findViewById(R.id.customSpeed);
        }
    }

}