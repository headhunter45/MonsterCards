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
import com.majinnaibu.monstercards.models.Skill;
import com.majinnaibu.monstercards.ui.shared.MCFragment;
import com.majinnaibu.monstercards.ui.shared.SwipeToDeleteCallback;
import com.majinnaibu.monstercards.utils.Logger;

/**
 * A fragment representing a list of Items.
 */
public class EditSkillsFragment extends MCFragment {
    private EditMonsterViewModel mViewModel;
    private ViewHolder mHolder;

    private void navigateToEditSkill(@NonNull Skill skill) {
        NavDirections action = EditSkillsFragmentDirections.actionEditSkillsFragmentToEditSkillFragment(skill.name, skill.abilityScore, skill.proficiencyType, skill.advantageType);
        Navigation.findNavController(requireView()).navigate(action);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.edit_monster_navigation);
        mViewModel = new ViewModelProvider(backStackEntry).get(EditMonsterViewModel.class);
        View root = inflater.inflate(R.layout.fragment_edit_skills_list, container, false);
        mHolder = new ViewHolder(root);
        setupRecyclerView(mHolder.list);
        setupAddSkillButton(mHolder.addSkill);

        return root;
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        Context context = requireContext();
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        mViewModel.getSkills().observe(getViewLifecycleOwner(), skills -> {
            EditSkillsRecyclerViewAdapter adapter = new EditSkillsRecyclerViewAdapter(mViewModel.getSkillsArray(), skill -> {
                if (skill != null) {
                    navigateToEditSkill(skill);
                } else {
                    Logger.logError("Can't navigate to EditSkill with a null skill");
                }
            });
            recyclerView.setAdapter(adapter);
        });

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(context, (position, direction) -> mViewModel.removeSkill(position), null));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void setupAddSkillButton(@NonNull FloatingActionButton fab) {
        fab.setOnClickListener(view -> {
            Skill newSkill = mViewModel.addNewSkill();
            navigateToEditSkill(newSkill);
        });
    }

    private static class ViewHolder {
        RecyclerView list;
        FloatingActionButton addSkill;

        ViewHolder(@NonNull View root) {
            this.list = root.findViewById(R.id.list);
            this.addSkill = root.findViewById(R.id.add_skill);
        }
    }
}
