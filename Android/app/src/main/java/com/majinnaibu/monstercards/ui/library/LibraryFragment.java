package com.majinnaibu.monstercards.ui.library;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.majinnaibu.monstercards.R;
import com.majinnaibu.monstercards.data.MonsterRepository;
import com.majinnaibu.monstercards.databinding.FragmentLibraryBinding;
import com.majinnaibu.monstercards.models.Monster;
import com.majinnaibu.monstercards.ui.shared.MCFragment;
import com.majinnaibu.monstercards.ui.shared.SwipeToDeleteCallback;
import com.majinnaibu.monstercards.utils.Logger;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableCompletableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LibraryFragment extends MCFragment {
    private LibraryViewModel mViewModel;
    private ViewHolder mHolder;
    private LibraryRecyclerViewAdapter mAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(LibraryViewModel.class);
        FragmentLibraryBinding binding = FragmentLibraryBinding.inflate(inflater, container, false);
        mHolder = new ViewHolder(binding);
        // TODO: set the title with setTitle(...)
        setupAddMonsterButton(mHolder.addButton);
        setupMonsterList(mHolder.list);
        return binding.getRoot();
    }

    private void setupMonsterList(@NonNull RecyclerView recyclerView) {
        Context context = requireContext();
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        LiveData<List<Monster>> monsterData = mViewModel.getMonsters();
        mAdapter = new LibraryRecyclerViewAdapter(this::navigateToMonsterDetail);
        if (monsterData != null) {
            monsterData.observe(getViewLifecycleOwner(), monsters -> mAdapter.submitList(monsters));
        }
        recyclerView.setAdapter(mAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(
                requireContext(),
                (position, direction) -> mViewModel.removeMonster(position),
                null));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void setupAddMonsterButton(@NonNull FloatingActionButton fab) {
        fab.setOnClickListener(view -> {
            Monster monster = new Monster();
            monster.name = getString(R.string.default_monster_name);
            MonsterRepository repository = this.getMonsterRepository();
            repository.addMonster(monster)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            new DisposableCompletableObserver() {
                                @Override
                                public void onComplete() {
                                    View view = getView();
                                    assert view != null;
                                    Snackbar.make(
                                            view,
                                            getString(R.string.snackbar_monster_created, monster.name),
                                            Snackbar.LENGTH_LONG)
                                            .setAction("Action", (_view) -> navigateToMonsterDetail(monster))
                                            .show();
                                }

                                @Override
                                public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                                    Logger.logError("Error creating monster", e);
                                    View view = getView();
                                    assert view != null;
                                    Snackbar.make(view, getString(R.string.snackbar_failed_to_create_monster), Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                }
                            });
        });
    }

    protected void navigateToMonsterDetail(Monster monster) {
        if (monster != null) {
            NavDirections action = (NavDirections) LibraryFragmentDirections.actionNavigationLibraryToNavigationMonster(monster.id.toString());
            Navigation.findNavController(requireView()).navigate(action);
        } else {
            Logger.logError("Can't navigate to MonsterDetail without a monster.");
        }
    }

    private static class ViewHolder {
        final FloatingActionButton addButton;
        final RecyclerView list;

        public ViewHolder(FragmentLibraryBinding binding) {
            addButton = binding.fab;
            list = binding.monsterList;
        }
    }
}
