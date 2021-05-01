package com.majinnaibu.monstercards.ui.library;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.majinnaibu.monstercards.R;
import com.majinnaibu.monstercards.data.MonsterRepository;
import com.majinnaibu.monstercards.ui.MCFragment;
import com.majinnaibu.monstercards.ui.MonsterListRecyclerViewAdapter;

public class LibraryFragment extends MCFragment {

    private LibraryViewModel libraryViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_library, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.monster_list);
        assert recyclerView != null;
        setupRecyclerView(recyclerView);

        return root;
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        MonsterRepository repository = this.getMonsterRepository();
        boolean mTwoPane = false;
        MonsterListRecyclerViewAdapter adapter = new MonsterListRecyclerViewAdapter(
                this,
                repository.getMonsters(),
                (monster) -> {
                },
                mTwoPane);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
    }
}
