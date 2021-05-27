package com.majinnaibu.monstercards.ui.editmonster;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.majinnaibu.monstercards.R;
import com.majinnaibu.monstercards.ui.MCFragment;
import com.majinnaibu.monstercards.utils.Logger;
import com.majinnaibu.monstercards.utils.TextChangedListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditBasicInfoFragment extends MCFragment {
    private EditMonsterViewModel mViewModel;
    private ViewHolder mHolder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.edit_monster_navigation);
        mViewModel = new ViewModelProvider(backStackEntry).get(EditMonsterViewModel.class);

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_edit_basic_info, container, false);

        mHolder = new ViewHolder(root);
        mHolder.name.setText(mViewModel.getName().getValue());
        mHolder.name.addTextChangedListener(new TextChangedListener((TextChangedListener.OnTextChangedCallback) (s, start, before, count) -> {
            mViewModel.setName(s.toString());
            Logger.logDebug(String.format("Monster Name changed to %s", mViewModel.getName().getValue()));
        }));

        mHolder.size.setText(mViewModel.getSize().getValue());
        mHolder.size.addTextChangedListener(new TextChangedListener((TextChangedListener.OnTextChangedCallback) (s, start, before, count) -> {
            mViewModel.setSize(s.toString());
            Logger.logDebug(String.format("Monster Size changed to %s", mViewModel.getSize().getValue()));
        }));

        mHolder.type.setText(mViewModel.getType().getValue());
        mHolder.type.addTextChangedListener(new TextChangedListener((TextChangedListener.OnTextChangedCallback) (s, start, before, count) -> {
            mViewModel.setType(s.toString());
            Logger.logDebug(String.format("Monster Type changed to %s", mViewModel.getType().getValue()));
        }));

        mHolder.subtype.setText(mViewModel.getSubtype().getValue());
        mHolder.subtype.addTextChangedListener(new TextChangedListener((TextChangedListener.OnTextChangedCallback) (s, start, before, count) -> {
            mViewModel.setSubtype(s.toString());
            Logger.logDebug(String.format("Monster Subtype changed to %s", mViewModel.getSubtype().getValue()));
        }));

        mHolder.alignment.setText(mViewModel.getAlignment().getValue());
        mHolder.alignment.addTextChangedListener(new TextChangedListener((TextChangedListener.OnTextChangedCallback) (s, start, before, count) -> {
            mViewModel.setAlignment(s.toString());
            Logger.logDebug(String.format("Monster Alignment changed to %s", mViewModel.getAlignment().getValue()));
        }));

        mHolder.customHitPoints.setText(mViewModel.getCustomHitPoints().getValue());
        mHolder.customHitPoints.addTextChangedListener((new TextChangedListener((TextChangedListener.OnTextChangedCallback) (s, start, before, count) -> {
            mViewModel.setCustomHitPoints(s.toString());
            Logger.logDebug(String.format("Monster Custom Hit Points changed to %s", mViewModel.getCustomHitPoints().getValue()));
        })));

        return root;
    }

    private static class ViewHolder {
        private final EditText name;
        private final EditText size;
        private final EditText type;
        private final EditText subtype;
        private final EditText alignment;
        private final EditText customHitPoints;

        ViewHolder(View root) {
            name = root.findViewById(R.id.name);
            size = root.findViewById(R.id.size);
            type = root.findViewById(R.id.type);
            subtype = root.findViewById(R.id.subtype);
            alignment = root.findViewById(R.id.alignment);
            customHitPoints = root.findViewById(R.id.customHitPoints);
            // TODO: add hitDice, hasCustomHitPoints, and customHitPoints
        }
    }
}