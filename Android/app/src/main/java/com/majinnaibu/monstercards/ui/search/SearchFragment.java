package com.majinnaibu.monstercards.ui.search;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.chip.ChipGroup;
import com.google.gson.Gson;
import com.majinnaibu.monstercards.MobileNavigationDirections;
import com.majinnaibu.monstercards.R;
import com.majinnaibu.monstercards.data.MonsterRepository;
import com.majinnaibu.monstercards.models.Monster;
import com.majinnaibu.monstercards.models.SearchResultItem;
import com.majinnaibu.monstercards.ui.shared.MCFragment;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class SearchFragment extends MCFragment {

    private final CompositeDisposable mDisposables = new CompositeDisposable();
    private final PublishSubject<String> mSearchSubject = PublishSubject.create();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search, container, false);
        MonsterRepository repository = this.getMonsterRepository();
        SearchResultsRecyclerViewAdapter adapter = new SearchResultsRecyclerViewAdapter(repository, item -> {
            if (item != null) {
                if (item.type == SearchResultItem.Type.MONSTER && item.monster != null) {
                    NavDirections action = SearchFragmentDirections.actionNavigationSearchToNavigationMonster(item.monster.id.toString());
                    Navigation.findNavController(requireView()).navigate(action);
                } else if (item.type == SearchResultItem.Type.COLLECTION && item.collection != null) {
                    NavDirections action = SearchFragmentDirections.actionNavigationSearchToCollectionDetailFragment(item.collection.id.toString());
                    Navigation.findNavController(requireView()).navigate(action);
                } else if (item.type == SearchResultItem.Type.REFERENCE_MONSTER && item.referenceMonster != null) {
                    Monster monster = item.referenceMonster.toMonster();
                    String serializedJson = new Gson().toJson(monster);
                    NavDirections navAction = MobileNavigationDirections.actionGlobalMonsterImportFragment(serializedJson);
                    Navigation.findNavController(requireView()).navigate(navAction);
                }
            }
        });

        final RecyclerView recyclerView = root.findViewById(R.id.monster_list);
        assert recyclerView != null;
        setupRecyclerView(recyclerView, adapter);

        MaterialButtonToggleGroup scopeToggleGroup = root.findViewById(R.id.toggle_group_scope);
        if (scopeToggleGroup != null) {
            scopeToggleGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
                if (!isChecked) return;
                if (checkedId == R.id.button_scope_library) {
                    adapter.setFilterScope(SearchResultsRecyclerViewAdapter.ScopeMode.MY_LIBRARY);
                } else if (checkedId == R.id.button_scope_compendiums) {
                    adapter.setFilterScope(SearchResultsRecyclerViewAdapter.ScopeMode.COMPENDIUMS);
                } else if (checkedId == R.id.button_scope_all) {
                    adapter.setFilterScope(SearchResultsRecyclerViewAdapter.ScopeMode.ALL);
                } else if (checkedId == R.id.button_scope_collections) {
                    adapter.setFilterScope(SearchResultsRecyclerViewAdapter.ScopeMode.COLLECTIONS);
                }
            });
        }

        ChipGroup systemChipGroup = root.findViewById(R.id.chip_group_filters);
        if (systemChipGroup != null) {
            systemChipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
                if (checkedIds.isEmpty()) return;
                int checkedId = checkedIds.get(0);
                if (checkedId == R.id.chip_system_all) {
                    adapter.setSystemFilter(SearchResultsRecyclerViewAdapter.SystemFilter.ALL);
                } else if (checkedId == R.id.chip_dnd5e) {
                    adapter.setSystemFilter(SearchResultsRecyclerViewAdapter.SystemFilter.DND_5E);
                } else if (checkedId == R.id.chip_pf2e) {
                    adapter.setSystemFilter(SearchResultsRecyclerViewAdapter.SystemFilter.PF_2E);
                } else if (checkedId == R.id.chip_sf2e) {
                    adapter.setSystemFilter(SearchResultsRecyclerViewAdapter.SystemFilter.SF_2E);
                }
            });
        }

        final TextView textView = root.findViewById(R.id.search_query);
        textView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                mSearchSubject.onNext(editable != null ? editable.toString() : "");
            }
        });

        // 300ms debounce for responsive query execution
        mDisposables.add(mSearchSubject
                .debounce(300, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(adapter::doSearch));

        return root;
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView, @NonNull SearchResultsRecyclerViewAdapter adapter) {
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mDisposables.clear();
    }
}
