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
import com.majinnaibu.monstercards.models.Language;
import com.majinnaibu.monstercards.ui.shared.MCFragment;
import com.majinnaibu.monstercards.ui.shared.SwipeToDeleteCallback;
import com.majinnaibu.monstercards.utils.Logger;
import com.majinnaibu.monstercards.utils.TextChangedListener;

public class EditLanguagesFragment extends MCFragment {
    // TODO: Make the swipe to delete not happen for the header
    private EditMonsterViewModel mViewModel;
    private ViewHolder mHolder;

    private void navigateToEditLanguage(Language language) {
        NavDirections action = EditLanguagesFragmentDirections.actionEditLanguagesFragmentToEditLanguageFragment(language.getName(), language.getSpeaks());
        Navigation.findNavController(requireView()).navigate(action);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.edit_monster_navigation);
        mViewModel = new ViewModelProvider(backStackEntry).get(EditMonsterViewModel.class);

        View root = inflater.inflate(R.layout.fragment_edit_languages_list, container, false);

        mHolder = new ViewHolder(root);
        setupRecyclerView(mHolder.list);
        setupAddLanguageButton(mHolder.addLanguage);

        return root;
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        Context context = requireContext();
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        mViewModel.getLanguages().observe(getViewLifecycleOwner(), languages -> {
            EditLanguagesRecyclerViewAdapter adapter = new EditLanguagesRecyclerViewAdapter(
                    mViewModel.getLanguagesArray(),
                    language -> {
                        if (language != null) {
                            navigateToEditLanguage(language);
                        } else {
                            Logger.logError("Can't navigate to EditSkill with a null skill");
                        }
                    },
                    mViewModel.getTelepathyRangeUnboxed(),
                    (value, previousValue) -> mViewModel.setTelepathyRange(value),
                    mViewModel.getUnderstandsButDescription().getValue(),
                    new TextChangedListener((TextChangedListener.OnTextChangedCallback) (s, start, before, count) -> mViewModel.setUnderstandsButDescription(s.toString())));
            recyclerView.setAdapter(adapter);
        });

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(context, position -> {
            if (position > 0) {
                mViewModel.removeLanguage(position - 1);
            }
        }));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void setupAddLanguageButton(@NonNull FloatingActionButton fab) {
        fab.setOnClickListener(view -> {
            Language newLanguage = mViewModel.addNewLanguage();
            navigateToEditLanguage(newLanguage);
        });
    }

    private static class ViewHolder {
        RecyclerView list;
        FloatingActionButton addLanguage;

        ViewHolder(View root) {
            this.list = root.findViewById(R.id.list);
            this.addLanguage = root.findViewById(R.id.add_language);
        }
    }
}
