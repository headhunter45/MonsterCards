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

import com.google.android.material.chip.ChipGroup;
import com.majinnaibu.monstercards.R;
import com.majinnaibu.monstercards.data.MonsterRepository;
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
                    showReferenceMonsterDialog(item.referenceMonster);
                }
            }
        });

        final RecyclerView recyclerView = root.findViewById(R.id.monster_list);
        assert recyclerView != null;
        setupRecyclerView(recyclerView, adapter);

        ChipGroup chipGroup = root.findViewById(R.id.chip_group_filters);
        if (chipGroup != null) {
            chipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
                if (checkedIds.isEmpty()) return;
                int checkedId = checkedIds.get(0);
                if (checkedId == R.id.chip_all) {
                    adapter.setFilterMode(SearchResultsRecyclerViewAdapter.FilterMode.ALL);
                } else if (checkedId == R.id.chip_my_library) {
                    adapter.setFilterMode(SearchResultsRecyclerViewAdapter.FilterMode.MY_LIBRARY);
                } else if (checkedId == R.id.chip_dnd5e) {
                    adapter.setFilterMode(SearchResultsRecyclerViewAdapter.FilterMode.DND_5E);
                } else if (checkedId == R.id.chip_pf2e) {
                    adapter.setFilterMode(SearchResultsRecyclerViewAdapter.FilterMode.PF_2E);
                } else if (checkedId == R.id.chip_sf2e) {
                    adapter.setFilterMode(SearchResultsRecyclerViewAdapter.FilterMode.SF_2E);
                } else if (checkedId == R.id.chip_collections) {
                    adapter.setFilterMode(SearchResultsRecyclerViewAdapter.FilterMode.COLLECTIONS);
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

    private void showReferenceMonsterDialog(@NonNull com.majinnaibu.monstercards.models.ReferenceMonster referenceMonster) {
        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle(referenceMonster.name + " (" + referenceMonster.getSourceTag() + ")")
                .setMessage(referenceMonster.size + " " + referenceMonster.type + "\n"
                        + "Alignment: " + referenceMonster.alignment + "\n"
                        + "Challenge Rating: " + referenceMonster.getChallengeRatingDescription() + "\n\n"
                        + "Ingested from: " + referenceMonster.bookSource)
                .setPositiveButton("Import to Library", (dialog, which) -> {
                    mDisposables.add(getMonsterRepository().cloneReferenceMonsterToLibrary(referenceMonster)
                            .subscribeOn(io.reactivex.rxjava3.schedulers.Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(() -> {
                                View view = getView();
                                if (view != null) {
                                    com.majinnaibu.monstercards.utils.SnackbarHelper.showLong(view, referenceMonster.name + " imported into your library!");
                                }
                            }, throwable -> com.majinnaibu.monstercards.utils.Logger.logError("Failed to clone reference monster", throwable)));
                })
                .setNegativeButton(R.string.dialog_cancel, null)
                .show();
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
