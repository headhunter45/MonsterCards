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
import com.majinnaibu.monstercards.ui.MCFragment;
import com.majinnaibu.monstercards.ui.shared.SwipeToDeleteCallback;
import com.majinnaibu.monstercards.utils.Logger;

/**
 * A fragment representing a list of Items.
 */
@SuppressWarnings("FieldCanBeLocal")
public class EditSensesFragment extends MCFragment {
    private EditMonsterViewModel mViewModel;
    private ViewHolder mHolder;

    private void navigateToEditSense(String sense) {
        NavDirections action = EditSensesFragmentDirections.actionEditSensesFragmentToEditSenseFragment(sense);
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

        View root = inflater.inflate(R.layout.fragment_edit_senses_list, container, false);

        mHolder = new ViewHolder(root);
        setupRecyclerView(mHolder.list);
        setupAddSenseButton(mHolder.addSense);

        return root;
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        Context context = requireContext();
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        mViewModel.getSenses().observe(getViewLifecycleOwner(), senses -> {
            EditSensesRecyclerViewAdapter adapter = new EditSensesRecyclerViewAdapter(mViewModel.getSensesArray(), sense -> {
                if (sense != null) {
                    navigateToEditSense(sense);
                } else {
                    Logger.logError("Can't navigate to EditSense with a null sense");
                }
            });
            recyclerView.setAdapter(adapter);
        });

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(context, mViewModel::removeSense));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void setupAddSenseButton(@NonNull FloatingActionButton fab) {
        fab.setOnClickListener(view -> {
            String newSense = mViewModel.addNewSense();
            navigateToEditSense(newSense);
        });
    }

    private static class ViewHolder {
        RecyclerView list;
        FloatingActionButton addSense;

        ViewHolder(View root) {
            this.list = root.findViewById(R.id.list);
            this.addSense = root.findViewById(R.id.add_sense);
        }
    }
}