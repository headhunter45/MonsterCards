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
public class EditDamageImmunitiesFragment extends MCFragment {
    private EditMonsterViewModel mViewModel;
    private ViewHolder mHolder;

    private void navigateToEditDamageImmunity(String damageImmunity) {
        NavDirections action = EditDamageImmunitiesFragmentDirections.actionEditDamageImmunitiesFragmentToEditDamageImmunityFragment(damageImmunity);
        View view = getView();
        assert view != null;
        Navigation.findNavController(view).navigate(action);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.edit_monster_navigation);
        mViewModel = new ViewModelProvider(backStackEntry).get(EditMonsterViewModel.class);
        View root = inflater.inflate(R.layout.fragment_edit_damage_immunities_list, container, false);
        mHolder = new ViewHolder(root);
        setupRecyclerView(mHolder.list);
        setupAddDamageImmunityButton(mHolder.addDamageImmunity);
        return root;
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        Context context = requireContext();
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        mViewModel.getDamageImmunities().observe(getViewLifecycleOwner(), damageImmunities -> {
            EditDamageImmunitiesRecyclerViewAdapter adapter = new EditDamageImmunitiesRecyclerViewAdapter(mViewModel.getDamageImmunitiesArray(), damageImmunity -> {
                if (damageImmunity != null) {
                    navigateToEditDamageImmunity(damageImmunity);
                } else {
                    Logger.logError("Can't navigate to EditDamageImmunity with a null damageImmunity");
                }
            });
            recyclerView.setAdapter(adapter);
        });

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(context, mViewModel::removeDamageImmunity));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void setupAddDamageImmunityButton(@NonNull FloatingActionButton fab) {
        fab.setOnClickListener(view -> {
            String newDamageImmunity = mViewModel.addNewDamageImmunity();
            navigateToEditDamageImmunity(newDamageImmunity);
        });
    }

    private static class ViewHolder {
        RecyclerView list;
        FloatingActionButton addDamageImmunity;

        ViewHolder(View root) {
            list = root.findViewById(R.id.list);
            addDamageImmunity = root.findViewById(R.id.add_damage_type);
        }
    }
}