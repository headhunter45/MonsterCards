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
public class EditDamageVulnerabilitiesFragment extends MCFragment {
    private EditMonsterViewModel mViewModel;
    private ViewHolder mHolder;

    private void navigateToEditDamageVulnerability(String damageVulnerability) {
        NavDirections action = EditDamageVulnerabilitiesFragmentDirections.actionEditDamageVulnerabilitiesFragmentToEditDamageVulnerabilityFragment(damageVulnerability);
        Navigation.findNavController(requireView()).navigate(action);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.edit_monster_navigation);
        mViewModel = new ViewModelProvider(backStackEntry).get(EditMonsterViewModel.class);
        View root = inflater.inflate(R.layout.fragment_edit_damage_vulnerabilities_list, container, false);
        mHolder = new ViewHolder(root);
        setupRecyclerView(mHolder.list);
        setupAddDamageVulnerabilityButton(mHolder.addDamageVulnerability);
        return root;
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        Context context = requireContext();
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        mViewModel.getDamageVulnerabilities().observe(getViewLifecycleOwner(), damageVulnerabilities -> {
            EditDamageVulnerabilitiesRecyclerViewAdapter adapter = new EditDamageVulnerabilitiesRecyclerViewAdapter(mViewModel.getDamageVulnerabilitiesArray(), damageVulnerability -> {
                if (damageVulnerability != null) {
                    navigateToEditDamageVulnerability(damageVulnerability);
                } else {
                    Logger.logError("Can't navigate to EditDamageVulnerability with a null damageVulnerability");
                }
            });
            recyclerView.setAdapter(adapter);
        });

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(context, mViewModel::removeDamageVulnerability));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void setupAddDamageVulnerabilityButton(@NonNull FloatingActionButton fab) {
        fab.setOnClickListener(view -> {
            String newDamageVulnerability = mViewModel.addNewDamageVulnerability();
            navigateToEditDamageVulnerability(newDamageVulnerability);
        });
    }

    private static class ViewHolder {
        RecyclerView list;
        FloatingActionButton addDamageVulnerability;

        ViewHolder(View root) {
            list = root.findViewById(R.id.list);
            addDamageVulnerability = root.findViewById(R.id.add_damage_type);
        }
    }
}