package com.majinnaibu.monstercards.ui.dashboard;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.majinnaibu.monstercards.R;
import com.majinnaibu.monstercards.data.MonsterRepository;
import com.majinnaibu.monstercards.models.Collection;
import com.majinnaibu.monstercards.models.Monster;
import com.majinnaibu.monstercards.ui.shared.MCFragment;
import com.majinnaibu.monstercards.utils.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DashboardFragment extends MCFragment {
    private DashboardViewModel mViewModel;
    private ViewHolder mHolder;
    private DashboardRecyclerViewAdapter mAdapter;
    private final CompositeDisposable mDisposables = new CompositeDisposable();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        mViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        mHolder = new ViewHolder(root);

        FloatingActionButton fab = root.findViewById(R.id.fab_add_to_dashboard);
        if (fab != null) {
            fab.setOnClickListener(v -> showAddToDashboardOptionsDialog());
        }

        setupRecyclerView(mHolder.list);
        loadDashboardMonsters();

        return root;
    }

    private void loadDashboardMonsters() {
        MonsterRepository repository = getMonsterRepository();
        mDisposables.add(repository.getDashboardMonsters()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(monsters -> mViewModel.setMonsters(monsters), Logger::logError));
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        int columnCount = Math.max(1, getResources().getConfiguration().screenWidthDp / 360);
        Logger.logWTF(String.format(Locale.US, "Setting column count to %d", columnCount));
        Context context = requireContext();
        GridLayoutManager layoutManager = new GridLayoutManager(context, columnCount);
        recyclerView.setLayoutManager(layoutManager);

        LiveData<List<Monster>> monsterData = mViewModel.getMonsters();
        mAdapter = new DashboardRecyclerViewAdapter(monster -> {
            if (monster != null) {
                navigateToMonsterDetail(monster);
            } else {
                Logger.logError("Can't navigate to MonsterDetailFragment with a null monster");
            }
        });
        if (monsterData != null) {
            monsterData.observe(getViewLifecycleOwner(), monsters -> mAdapter.submitList(monsters));
        }
        recyclerView.setAdapter(mAdapter);

        ItemTouchHelper.SimpleCallback touchHelperCallback = new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT
        ) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                int fromPos = viewHolder.getAdapterPosition();
                int toPos = target.getAdapterPosition();
                if (fromPos != RecyclerView.NO_POSITION && toPos != RecyclerView.NO_POSITION) {
                    List<Monster> currentList = new ArrayList<>(mAdapter.getCurrentList());
                    if (fromPos < currentList.size() && toPos < currentList.size()) {
                        Monster moved = currentList.remove(fromPos);
                        currentList.add(toPos, moved);
                        mAdapter.submitList(currentList);
                    }
                }
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    List<Monster> currentList = mAdapter.getCurrentList();
                    if (position < currentList.size()) {
                        Monster monster = currentList.get(position);
                        removeMonsterFromDashboard(monster);
                    }
                }
            }
        };
        new ItemTouchHelper(touchHelperCallback).attachToRecyclerView(recyclerView);
    }

    private void showAddToDashboardOptionsDialog() {
        String[] options = new String[]{
                getString(R.string.action_add_single_monster),
                getString(R.string.action_add_collection_option),
                getString(R.string.action_clear_dashboard)
        };
        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.dialog_add_to_dashboard)
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        showAddMonsterPicker();
                    } else if (which == 1) {
                        showAddCollectionPicker();
                    } else if (which == 2) {
                        clearDashboard();
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, null)
                .show();
    }

    private void showAddMonsterPicker() {
        MonsterRepository repository = getMonsterRepository();
        mDisposables.add(repository.getMonsters()
                .firstOrError()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(monsters -> {
                    if (monsters.isEmpty()) {
                        View view = getView();
                        if (view != null) {
                            Snackbar.make(view, getString(R.string.no_monsters_available), Snackbar.LENGTH_LONG).show();
                        }
                        return;
                    }
                    String[] names = new String[monsters.size()];
                    for (int i = 0; i < monsters.size(); i++) {
                        names[i] = monsters.get(i).name;
                    }
                    new AlertDialog.Builder(requireContext())
                            .setTitle(R.string.action_add_single_monster)
                            .setItems(names, (dialog, which) -> {
                                Monster selected = monsters.get(which);
                                addMonsterToDashboard(selected);
                            })
                            .setNegativeButton(R.string.dialog_cancel, null)
                            .show();
                }, Logger::logError));
    }

    private void showAddCollectionPicker() {
        MonsterRepository repository = getMonsterRepository();
        mDisposables.add(repository.getCollections()
                .firstOrError()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(collections -> {
                    if (collections.isEmpty()) {
                        View view = getView();
                        if (view != null) {
                            Snackbar.make(view, getString(R.string.no_collections_available), Snackbar.LENGTH_LONG).show();
                        }
                        return;
                    }
                    String[] names = new String[collections.size()];
                    for (int i = 0; i < collections.size(); i++) {
                        names[i] = collections.get(i).name;
                    }
                    new AlertDialog.Builder(requireContext())
                            .setTitle(R.string.action_add_collection_option)
                            .setItems(names, (dialog, which) -> {
                                Collection selected = collections.get(which);
                                addCollectionToDashboard(selected);
                            })
                            .setNegativeButton(R.string.dialog_cancel, null)
                            .show();
                }, Logger::logError));
    }

    private void addMonsterToDashboard(@NonNull Monster monster) {
        MonsterRepository repository = getMonsterRepository();
        mDisposables.add(repository.addMonsterToDashboard(monster.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    View view = getView();
                    if (view != null) {
                        Snackbar.make(view, getString(R.string.snackbar_added_to_dashboard, monster.name), Snackbar.LENGTH_LONG).show();
                    }
                }, Logger::logError));
    }

    private void addCollectionToDashboard(@NonNull Collection collection) {
        MonsterRepository repository = getMonsterRepository();
        mDisposables.add(repository.addCollectionToDashboard(collection.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    View view = getView();
                    if (view != null) {
                        Snackbar.make(view, getString(R.string.snackbar_collection_added_to_dashboard, collection.name), Snackbar.LENGTH_LONG).show();
                    }
                }, Logger::logError));
    }

    private void removeMonsterFromDashboard(@NonNull Monster monster) {
        MonsterRepository repository = getMonsterRepository();
        mDisposables.add(repository.removeMonsterFromDashboard(monster.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {}, Logger::logError));
    }

    private void clearDashboard() {
        MonsterRepository repository = getMonsterRepository();
        mDisposables.add(repository.clearDashboard()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    View view = getView();
                    if (view != null) {
                        Snackbar.make(view, getString(R.string.snackbar_dashboard_cleared), Snackbar.LENGTH_LONG).show();
                    }
                }, Logger::logError));
    }

    private void navigateToMonsterDetail(Monster monster) {
        NavDirections action = DashboardFragmentDirections.actionNavigationDashboardToNavigationMonster(monster.id.toString());
        Navigation.findNavController(requireView()).navigate(action);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.dashboard_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_action_add_single_monster) {
            showAddMonsterPicker();
            return true;
        } else if (item.getItemId() == R.id.menu_action_add_collection_option) {
            showAddCollectionPicker();
            return true;
        } else if (item.getItemId() == R.id.menu_action_clear_dashboard) {
            clearDashboard();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mDisposables.clear();
    }

    private static class ViewHolder {
        final RecyclerView list;

        ViewHolder(View root) {
            list = root.findViewById(R.id.list);
        }
    }
}
