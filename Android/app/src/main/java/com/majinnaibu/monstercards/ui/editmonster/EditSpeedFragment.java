package com.majinnaibu.monstercards.ui.editmonster;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.majinnaibu.monstercards.R;
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

        mViewModel.getWalkSpeed().observe(getViewLifecycleOwner(), value -> {
            mHolder.baseSpeed.setText(String.format(getString(R.string.format_distance_in_feet), value));
        });
        mHolder.incrementBaseSpeed.setOnClickListener(v -> mViewModel.incrementWalkSpeed());
        mHolder.decrementBaseSpeed.setOnClickListener(v -> mViewModel.decrementWalkSpeed());

        mViewModel.getBurrowSpeed().observe(getViewLifecycleOwner(), value -> {
            mHolder.burrowSpeed.setText(String.format(getString(R.string.format_distance_in_feet), value));
        });
        mHolder.incrementBurrowSpeed.setOnClickListener(v -> mViewModel.incrementBurrowSpeed());
        mHolder.decrementBurrowSpeed.setOnClickListener(v -> mViewModel.decrementBurrowSpeed());

        mViewModel.getClimbSpeed().observe(getViewLifecycleOwner(), value -> mHolder.climbSpeed.setText(String.format(getString(R.string.format_distance_in_feet), value)));
        mHolder.incrementClimbSpeed.setOnClickListener(v -> mViewModel.incrementClimbSpeed());
        mHolder.decrementBurrowSpeed.setOnClickListener(v -> mViewModel.decrementClimbSpeed());

        mViewModel.getFlySpeed().observe(getViewLifecycleOwner(), value -> mHolder.flySpeed.setText(String.format(getString(R.string.format_distance_in_feet), value)));
        mHolder.incrementFlySpeed.setOnClickListener(v -> mViewModel.incrementFlySpeed());
        mHolder.decrementBurrowSpeed.setOnClickListener(v -> mViewModel.decrementFlySpeed());

        mViewModel.getSwimSpeed().observe(getViewLifecycleOwner(), value -> mHolder.swimSpeed.setText(String.format(getString(R.string.format_distance_in_feet), value)));
        mHolder.incrementSwimSpeed.setOnClickListener(v -> mViewModel.incrementSwimSpeed());
        mHolder.decrementBurrowSpeed.setOnClickListener(v -> mViewModel.decrementSwimSpeed());

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

        final TextView baseSpeed;
        final Button incrementBaseSpeed;
        final Button decrementBaseSpeed;
        final TextView burrowSpeed;
        final Button incrementBurrowSpeed;
        final Button decrementBurrowSpeed;
        final TextView climbSpeed;
        final Button incrementClimbSpeed;
        final Button decrementClimbSpeed;
        final TextView flySpeed;
        final Button incrementFlySpeed;
        final Button decrementFlySpeed;
        final TextView swimSpeed;
        final Button incrementSwimSpeed;
        final Button decrementSwimSpeed;
        final SwitchCompat canHover;
        final SwitchCompat hasCustomSpeed;
        final EditText customSpeed;

        ViewHolder(View root) {
            baseSpeed = root.findViewById(R.id.baseSpeed);
            incrementBaseSpeed = root.findViewById(R.id.baseSpeed_increment);
            decrementBaseSpeed = root.findViewById(R.id.baseSpeed_decrement);
            burrowSpeed = root.findViewById(R.id.burrowSpeed);
            incrementBurrowSpeed = root.findViewById(R.id.burrowSpeed_increment);
            decrementBurrowSpeed = root.findViewById(R.id.burrowSpeed_decrement);
            climbSpeed = root.findViewById(R.id.climbSpeed);
            incrementClimbSpeed = root.findViewById(R.id.climbSpeed_increment);
            decrementClimbSpeed = root.findViewById(R.id.climbSpeed_decrement);
            flySpeed = root.findViewById(R.id.flySpeed);
            incrementFlySpeed = root.findViewById(R.id.flySpeed_increment);
            decrementFlySpeed = root.findViewById(R.id.flySpeed_decrement);
            swimSpeed = root.findViewById(R.id.swimSpeed);
            incrementSwimSpeed = root.findViewById(R.id.swimSpeed_increment);
            decrementSwimSpeed = root.findViewById(R.id.swimSpeed_decrement);
            canHover = root.findViewById(R.id.canHover);
            hasCustomSpeed = root.findViewById(R.id.hasCustomSpeed);
            customSpeed = root.findViewById(R.id.customSpeed);
        }
    }

}