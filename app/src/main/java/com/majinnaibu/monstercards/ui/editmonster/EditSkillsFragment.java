package com.majinnaibu.monstercards.ui.editmonster;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
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
import com.majinnaibu.monstercards.models.Skill;
import com.majinnaibu.monstercards.ui.shared.SwipeToDeleteCallback;
import com.majinnaibu.monstercards.utils.Logger;

/**
 * A fragment representing a list of Items.
 */
@SuppressWarnings("FieldCanBeLocal")
public class EditSkillsFragment extends Fragment {
    private EditMonsterViewModel mViewModel;
    private ViewHolder mHolder;

    private void navigateToEditSkill(Skill skill) {
        NavDirections action = EditSkillsFragmentDirections.actionEditSkillsFragmentToEditSkillFragment(skill.name, skill.abilityScore, skill.proficiencyType, skill.advantageType);
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
            }, null);
            recyclerView.setAdapter(adapter);
        });

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(context, mViewModel::removeSkill));
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

        ViewHolder(View root) {
            this.list = root.findViewById(R.id.list);
            this.addSkill = root.findViewById(R.id.add_skill);
        }
    }
}
