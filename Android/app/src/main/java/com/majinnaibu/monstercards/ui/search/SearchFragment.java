package com.majinnaibu.monstercards.ui.search;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.majinnaibu.monstercards.databinding.FragmentSearchBinding;
import com.majinnaibu.monstercards.models.Monster;
import com.majinnaibu.monstercards.ui.shared.MCFragment;
import com.majinnaibu.monstercards.utils.Logger;

import java.util.List;

public class SearchFragment extends MCFragment {
    private SearchViewModel mViewModel;
    private ViewHolder mHolder;
    private SearchResultsRecyclerViewAdapter mAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        FragmentSearchBinding binding = FragmentSearchBinding.inflate(inflater, container, false);
        mHolder = new ViewHolder(binding);
        // TODO: set the title with setTitle(...)
        setupMonsterList(binding.monsterList);
        setupFilterBox(binding.searchQuery);
        return binding.getRoot();
    }

    private void setupFilterBox(@NonNull TextView textBox) {
        textBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mViewModel.setFilterText(textBox.getText().toString());
            }
        });
    }

    private void setupMonsterList(@NonNull RecyclerView recyclerView) {
        Context context = requireContext();
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        LiveData<List<Monster>> monsterData = mViewModel.getMatchedMonsters();
        mAdapter = new SearchResultsRecyclerViewAdapter(this::navigateToMonsterDetail);
        if (monsterData != null) {
            monsterData.observe(getViewLifecycleOwner(), monsters -> mAdapter.submitList(monsters));
        }
        recyclerView.setAdapter(mAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    public void navigateToMonsterDetail(Monster monster) {
        if (monster == null) {
            NavDirections action = SearchFragmentDirections.actionNavigationSearchToNavigationMonster(monster.id.toString());
            Navigation.findNavController(requireView()).navigate(action);
        } else {
            Logger.logError("Can't navigate to MonsterDetail without a monster.");
        }
    }

    private static class ViewHolder {
        final RecyclerView monsterList;
        final EditText filterQuery;

        public ViewHolder(FragmentSearchBinding binding) {
            monsterList = binding.monsterList;
            filterQuery = binding.searchQuery;
        }
    }
}
