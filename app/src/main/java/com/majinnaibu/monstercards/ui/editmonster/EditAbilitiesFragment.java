package com.majinnaibu.monstercards.ui.editmonster;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.majinnaibu.monstercards.models.Trait;
import com.majinnaibu.monstercards.ui.shared.MCFragment;
import com.majinnaibu.monstercards.ui.shared.SwipeToDeleteCallback;
import com.majinnaibu.monstercards.utils.Logger;

public class EditAbilitiesFragment extends MCFragment {
    private EditMonsterViewModel mViewModel;
    private ViewHolder mHolder;

    private void navigateToEditAbility(Trait trait) {
        NavDirections action = EditAbilitiesFragmentDirections.actionEditAbilitiesFragmentToEditAbilityFragment(trait.description, trait.name);
        Navigation.findNavController(requireView()).navigate(action);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.edit_monster_navigation);
        mViewModel = new ViewModelProvider(backStackEntry).get(EditMonsterViewModel.class);
        View root = inflater.inflate(R.layout.fragment_edit_abilities_list, container, false);
        mHolder = new ViewHolder(root);
        setupRecyclerView(mHolder.list);
        setupAddAbilityButton(mHolder.addAbility);
        return root;
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        Context context = requireContext();
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        mViewModel.getAbilities().observe(getViewLifecycleOwner(), abilities -> {
            EditAbilitiesRecyclerViewAdapter adapter = new EditAbilitiesRecyclerViewAdapter(abilities, ability -> {
                if (ability != null) {
                    navigateToEditAbility(ability);
                } else {
                    Logger.logError("Can't navigate to EditAbilityFragment with a null ability");
                }
            });
            recyclerView.setAdapter(adapter);
        });

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(context, mViewModel::removeAbility));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void setupAddAbilityButton(@NonNull FloatingActionButton fab) {
        fab.setOnClickListener(view -> {
            Trait ability = mViewModel.addNewAbility();
            navigateToEditAbility(ability);
        });
    }

    private static class ViewHolder {
        RecyclerView list;
        FloatingActionButton addAbility;

        ViewHolder(View root) {
            list = root.findViewById(R.id.list);
            addAbility = root.findViewById(R.id.add_ability);
        }
    }
}
