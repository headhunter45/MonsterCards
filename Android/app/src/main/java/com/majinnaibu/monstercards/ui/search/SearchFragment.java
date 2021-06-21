package com.majinnaibu.monstercards.ui.search;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.majinnaibu.monstercards.R;
import com.majinnaibu.monstercards.data.MonsterRepository;
import com.majinnaibu.monstercards.ui.shared.MCFragment;

public class SearchFragment extends MCFragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search, container, false);
        MonsterRepository repository = this.getMonsterRepository();
        SearchResultsRecyclerViewAdapter adapter = new SearchResultsRecyclerViewAdapter(repository, null);
        final RecyclerView recyclerView = root.findViewById(R.id.monster_list);
        assert recyclerView != null;
        setupRecyclerView(recyclerView, adapter);

        final TextView textView = root.findViewById(R.id.search_query);
        textView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                adapter.doSearch(textView.getText().toString());
            }
        });

        return root;
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView, @NonNull SearchResultsRecyclerViewAdapter adapter) {
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}
