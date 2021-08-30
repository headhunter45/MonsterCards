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

import java.util.List;

public class EditTraitsFragment extends MCFragment {
    private EditMonsterViewModel mViewModel;
    private ViewHolder mHolder;
    private TraitType mTraitType;
    private EditTraitsRecyclerViewAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            EditTraitsFragmentArgs args = EditTraitsFragmentArgs.fromBundle(getArguments());
            mTraitType = args.getTraitType();
        } else {
            Logger.logWTF("EditTraitFragment needs arguments");
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.edit_monster_navigation);
        mViewModel = new ViewModelProvider(backStackEntry).get(EditMonsterViewModel.class);
        View root = inflater.inflate(R.layout.fragment_edit_traits_list, container, false);
        mHolder = new ViewHolder(root);
        setTitle(getTitleForTraitType(mTraitType));
        setupRecyclerView(mHolder.list);
        setupAddButton(mHolder.addTrait);
        return root;
    }

    @NonNull
    private String getTitleForTraitType(TraitType type) {
        switch (type) {
            case ABILITY:
                return getString(R.string.title_editAbilities);
            case ACTION:
                return getString(R.string.title_editActions);
            case LAIR_ACTION:
                return getString(R.string.title_editLairActions);
            case LEGENDARY_ACTION:
                return getString(R.string.title_editLegendaryActions);
            case REACTIONS:
                return getString(R.string.title_editReactions);
            case REGIONAL_ACTION:
                return getString(R.string.title_editRegionalActions);
            default:
                return getString(R.string.title_editTraits);
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        Context context = requireContext();
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        LiveData<List<Trait>> traitData = mViewModel.getTraits(mTraitType);
        mAdapter = new EditTraitsRecyclerViewAdapter(trait -> {
            if (trait != null) {
                navigateToEditTrait(trait);
            } else {
                Logger.logError("Can't navigate to EditTraitFragment with a null trait");
            }
        });
        if (traitData != null) {
            traitData.observe(getViewLifecycleOwner(), traits -> mAdapter.submitList(traits));
        }
        recyclerView.setAdapter(mAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(context, (position, direction) -> mViewModel.removeTrait(mTraitType, position), (from, to) -> mViewModel.moveTrait(mTraitType, from, to)));
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

    protected void navigateToEditTrait(@NonNull Trait trait) {
        NavDirections action = EditTraitsFragmentDirections.actionEditTraitListFragmentToEditTraitFragment(trait.description, trait.name, mTraitType);
        Navigation.findNavController(requireView()).navigate(action);
    }

    private static class ViewHolder {
        RecyclerView list;
        FloatingActionButton addTrait;

        ViewHolder(@NonNull View root) {
            list = root.findViewById(R.id.list);
            addTrait = root.findViewById(R.id.add_trait);
        }
    }
}
