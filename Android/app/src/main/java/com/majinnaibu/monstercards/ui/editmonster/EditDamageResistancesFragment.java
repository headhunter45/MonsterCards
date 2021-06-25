package com.majinnaibu.monstercards.ui.editmonster;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.majinnaibu.monstercards.R;
import com.majinnaibu.monstercards.ui.shared.MCFragment;
import com.majinnaibu.monstercards.ui.shared.SwipeToDeleteCallback;
import com.majinnaibu.monstercards.utils.Logger;

/**
 * A fragment representing a list of Items.
 */
public class EditDamageResistancesFragment extends MCFragment {
    private EditMonsterViewModel mViewModel;
    private ViewHolder mHolder;

    private void navigateToEditDamageResistance(String damageResistance) {
        NavDirections action = EditDamageResistancesFragmentDirections.actionEditDamageResistancesFragmentToEditDamageResistanceFragment(damageResistance);
        Navigation.findNavController(requireView()).navigate(action);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.edit_monster_navigation);
        mViewModel = new ViewModelProvider(backStackEntry).get(EditMonsterViewModel.class);
        View root = inflater.inflate(R.layout.fragment_edit_damage_resistances_list, container, false);
        mHolder = new ViewHolder(root);
        setupRecyclerView(mHolder.list);
        setupAddDamageResistanceButton(mHolder.addDamageResistance);
        return root;
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        Context context = requireContext();
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        mViewModel.getDamageResistances().observe(getViewLifecycleOwner(), damageResistances -> {
            EditDamageResistancesRecyclerViewAdapter adapter = new EditDamageResistancesRecyclerViewAdapter(mViewModel.getDamageResistancesArray(), damageResistance -> {
                if (damageResistance != null) {
                    navigateToEditDamageResistance(damageResistance);
                } else {
                    Logger.logError("Can't navigate to EditDamageResistance with a null damageResistance");
                }
            });
            recyclerView.setAdapter(adapter);
        });

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(context, mViewModel::removeDamageResistance));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void setupAddDamageResistanceButton(@NonNull FloatingActionButton fab) {
        fab.setOnClickListener(view -> {
            String newDamageResistance = mViewModel.addNewDamageResistance();
            navigateToEditDamageResistance(newDamageResistance);
        });
    }

    private static class ViewHolder {
        RecyclerView list;
        FloatingActionButton addDamageResistance;

        ViewHolder(View root) {
            list = root.findViewById(R.id.list);
            addDamageResistance = root.findViewById(R.id.add_damage_type);
        }
    }
}