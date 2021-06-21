package com.majinnaibu.monstercards.ui.editmonster;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import com.majinnaibu.monstercards.ui.shared.SwipeToDeleteCallback;
import com.majinnaibu.monstercards.utils.Logger;

import org.jetbrains.annotations.NotNull;

/**
 * A fragment representing a list of Items.
 */
@SuppressWarnings("FieldCanBeLocal")
public class EditConditionImmunitiesFragment extends Fragment {
    private EditMonsterViewModel mViewModel;
    private ViewHolder mHolder;

    private void navigateToEditConditionImmunity(String condition) {
        NavDirections action = EditConditionImmunitiesFragmentDirections.actionEditConditionImmunitiesFragmentToEditConditionImmunity(condition);
        View view = getView();
        assert view != null;
        Navigation.findNavController(view).navigate(action);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.edit_monster_navigation);
        mViewModel = new ViewModelProvider(backStackEntry).get(EditMonsterViewModel.class);
        View root = inflater.inflate(R.layout.fragment_edit_condition_immunities_list, container, false);
        mHolder = new ViewHolder(root);
        setupRecyclerView(mHolder.list);
        setupAddConditionImmunityButton(mHolder.addConditionImmunity);
        return root;
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        Context context = requireContext();
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        mViewModel.getConditionImmunities().observe(getViewLifecycleOwner(), conditionImmunities -> {
            EditConditionImmunitiesRecyclerViewAdapter adapter = new EditConditionImmunitiesRecyclerViewAdapter(mViewModel.getConditionImmunitiesArray(), condition -> {
                if (condition != null) {
                    navigateToEditConditionImmunity(condition);
                } else {
                    Logger.logError("Can't navigate to EditConditionImmunityFragment with a null condition");
                }
            });
            recyclerView.setAdapter(adapter);
        });

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(context, mViewModel::removeConditionImmunity));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void setupAddConditionImmunityButton(@NonNull FloatingActionButton fab) {
        fab.setOnClickListener(view -> {
            String condition = mViewModel.addNewConditionImmunity();
            navigateToEditConditionImmunity(condition);
        });
    }

    private static class ViewHolder {
        RecyclerView list;
        FloatingActionButton addConditionImmunity;

        ViewHolder(View root) {
            list = root.findViewById(R.id.list);
            addConditionImmunity = root.findViewById(R.id.add_condition_immunity);
        }
    }
}