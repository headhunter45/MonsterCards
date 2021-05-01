package com.majinnaibu.monstercards.ui.library;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.majinnaibu.monstercards.R;
import com.majinnaibu.monstercards.data.MonsterRepository;
import com.majinnaibu.monstercards.models.Monster;
import com.majinnaibu.monstercards.ui.MCFragment;
import com.majinnaibu.monstercards.ui.MonsterListRecyclerViewAdapter;
import com.majinnaibu.monstercards.utils.Logger;

import java.util.UUID;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LibraryFragment extends MCFragment {

    private LibraryViewModel libraryViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_library, container, false);

        FloatingActionButton fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Monster monster = new Monster();
            monster.name = "Unnamed Monster";
            MonsterRepository repository = this.getMonsterRepository();
            repository.addMonster(monster)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                        Snackbar.make(
                                getView(),
                                String.format("%s created", monster.name),
                                Snackbar.LENGTH_LONG)
                                .setAction("Action", (_view) -> {
                                    navigateToMonsterDetail(monster.id);
                                })
                                .show();
                    }, throwable -> {
                        Logger.logError("Error creating monster", throwable);
                        Snackbar.make(getView(), "Failed to create monster", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    });
        });

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

    protected void navigateToMonsterDetail(UUID monsterId) {
        NavDirections action = LibraryFragmentDirections.actionNavigationLibraryToNavigationMonster(monsterId.toString());
        Navigation.findNavController(getView()).navigate(action);
    }

    }
}
