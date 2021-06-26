package com.majinnaibu.monstercards.ui.editmonster;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
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
import com.majinnaibu.monstercards.data.enums.TraitType;
import com.majinnaibu.monstercards.models.Trait;
import com.majinnaibu.monstercards.ui.shared.MCFragment;
import com.majinnaibu.monstercards.ui.shared.SwipeToDeleteCallback;
import com.majinnaibu.monstercards.utils.Logger;

import org.jetbrains.annotations.NotNull;

import java.util.List;

// TODO: rename to EditTraitsFragment
public class EditTraitListFragment extends MCFragment {
    private EditMonsterViewModel mViewModel;
    private ViewHolder mHolder;
    private TraitType mTraitType;


    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            EditTraitListFragmentArgs args = EditTraitListFragmentArgs.fromBundle(getArguments());
            mTraitType = args.getTraitType();
        } else {
            Logger.logWTF("EditTraitFragment needs arguments");
        }
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.edit_monster_navigation);
        mViewModel = new ViewModelProvider(backStackEntry).get(EditMonsterViewModel.class);
        View root = inflater.inflate(R.layout.fragment_edit_traits_list, container, false);
        mHolder = new ViewHolder(root);
        setupRecyclerView(mHolder.list);
        setupAddButton(mHolder.addTrait);
        return root;
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        Context context = requireContext();
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        LiveData<List<Trait>> traitData = mViewModel.getTraits(mTraitType);
        if (traitData != null) {
            traitData.observe(getViewLifecycleOwner(), traits -> {
                EditTraitsRecyclerViewAdapter adapter = new EditTraitsRecyclerViewAdapter(traits, trait -> {
                    if (trait != null) {
                        navigateToEditTrait(trait);
                    } else {
                        Logger.logError("Can't navigate to EditTraitFragment with a null trait");
                    }
                });
                recyclerView.setAdapter(adapter);
            });
        }
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(context, position -> mViewModel.removeTrait(mTraitType, position)));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void setupAddButton(@NonNull FloatingActionButton fab) {
        fab.setOnClickListener(view -> {
            Trait newTrait = mViewModel.addNewTrait(mTraitType);
            if (newTrait != null) {
                navigateToEditTrait(newTrait);
            }
        });
    }

    protected void navigateToEditTrait(Trait trait) {
        NavDirections action = EditTraitListFragmentDirections.actionEditTraitListFragmentToEditTraitFragment(trait.description, trait.name, mTraitType);
        Navigation.findNavController(requireView()).navigate(action);
    }

    private static class ViewHolder {
        RecyclerView list;
        FloatingActionButton addTrait;

        ViewHolder(View root) {
            list = root.findViewById(R.id.list);
            addTrait = root.findViewById(R.id.add_trait);
        }
    }
}
