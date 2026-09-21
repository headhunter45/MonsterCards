package com.majinnaibu.monstercards.ui.collections;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.majinnaibu.monstercards.R;
import com.majinnaibu.monstercards.data.MonsterRepository;
import com.majinnaibu.monstercards.models.Collection;
import com.majinnaibu.monstercards.models.DashboardMonster;
import com.majinnaibu.monstercards.models.DashboardMonsterWithMonster;
import com.majinnaibu.monstercards.models.Monster;
import com.majinnaibu.monstercards.ui.dashboard.DashboardRecyclerViewAdapter;
import com.majinnaibu.monstercards.ui.shared.MCFragment;
import com.majinnaibu.monstercards.utils.Logger;
import com.majinnaibu.monstercards.utils.SnackbarHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CollectionDetailFragment extends MCFragment {

    private CollectionDetailViewModel mViewModel;
    private UUID mCollectionId;
    private DashboardRecyclerViewAdapter mAdapter;
    private final CompositeDisposable mDisposables = new CompositeDisposable();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View root = inflater.inflate(R.layout.fragment_collection_detail, container, false);

        Bundle arguments = getArguments();
        assert arguments != null;
        mCollectionId = UUID.fromString(CollectionDetailFragmentArgs.fromBundle(arguments).getCollectionId());

        mViewModel = new ViewModelProvider(this).get(CollectionDetailViewModel.class);

        TextView nameView = root.findViewById(R.id.detail_collection_name);
        TextView descriptionView = root.findViewById(R.id.detail_collection_description);
        RecyclerView recyclerView = root.findViewById(R.id.collection_monster_list);
        FloatingActionButton fab = root.findViewById(R.id.fab_add_monster_to_collection);

        if (fab != null) {
            fab.setOnClickListener(v -> showCollectionDetailFabOptionsDialog());
        }

        mViewModel.getCollection().observe(getViewLifecycleOwner(), collection -> {
            if (collection != null) {
                nameView.setText(collection.name);
                setTitle(collection.name);
                if (!TextUtils.isEmpty(collection.description)) {
                    descriptionView.setVisibility(View.VISIBLE);
                    descriptionView.setText(collection.description);
                } else {
                    descriptionView.setVisibility(View.GONE);
                }
            }
        });

        mViewModel.getMonsters().observe(getViewLifecycleOwner(), monsters -> {
            if (mAdapter != null) {
                List<DashboardMonsterWithMonster> items = new ArrayList<>();
                if (monsters != null) {
                    for (int i = 0; i < monsters.size(); i++) {
                        DashboardMonster dm = new DashboardMonster(monsters.get(i).id, i);
                        dm.id = i + 1;
                        items.add(new DashboardMonsterWithMonster(dm, monsters.get(i)));
                    }
                }
                mAdapter.submitList(items);
            }
        });

        setupRecyclerView(recyclerView);
        loadCollectionData();

        return root;
    }

    private void loadCollectionData() {
        MonsterRepository repository = getMonsterRepository();
        mDisposables.add(repository.getCollection(mCollectionId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(collection -> {
                    if (collection != null) {
                        mViewModel.setCollection(collection);
                    }
                }, Logger::logError));

        mDisposables.add(repository.getMonstersForCollection(mCollectionId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(monsters -> mViewModel.setMonsters(monsters), Logger::logError));
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        int columnCount = Math.max(1, getResources().getConfiguration().screenWidthDp / 396);
        Context context = requireContext();
        GridLayoutManager layoutManager = new GridLayoutManager(context, columnCount);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new DashboardRecyclerViewAdapter(item -> {
            if (item != null && item.monster != null) {
                navigateToMonsterDetail(item.monster.id);
            } else {
                Logger.logError("Can't navigate to MonsterDetailFragment with a null monster");
            }
        });
        recyclerView.setAdapter(mAdapter);
    }

    private void showCollectionDetailFabOptionsDialog() {
        String[] options = new String[]{
                getString(R.string.action_add_monster),
                getString(R.string.action_create_monster),
                getString(R.string.action_import_monster_from_url),
                getString(R.string.action_import_monster_from_file)
        };
        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.title_collection_actions)
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        showAddMonsterDialog();
                    } else if (which == 1) {
                        createNewMonster();
                    } else if (which == 2) {
                        showImportUrlDialog();
                    } else if (which == 3) {
                        importMonsterFromFile();
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, null)
                .show();
    }

    private void showAddMonsterDialog() {
        MonsterRepository repository = getMonsterRepository();
        mDisposables.add(repository.getMonsters()
                .firstOrError()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(monsters -> {
                    if (monsters.isEmpty()) {
                        View view = getView();
                        if (view != null) {
                            SnackbarHelper.showLong(view, getString(R.string.snackbar_failed_to_create_monster));
                        }
                        return;
                    }
                    String[] monsterNames = new String[monsters.size()];
                    for (int i = 0; i < monsters.size(); i++) {
                        monsterNames[i] = monsters.get(i).name;
                    }
                    new AlertDialog.Builder(requireContext())
                            .setTitle(R.string.action_add_monster)
                            .setItems(monsterNames, (dialog, which) -> {
                                Monster selectedMonster = monsters.get(which);
                                addMonsterToCollection(selectedMonster);
                            })
                            .setNegativeButton(R.string.dialog_cancel, null)
                            .show();
                }, Logger::logError));
    }

    private void addMonsterToCollection(Monster monster) {
        MonsterRepository repository = getMonsterRepository();
        mDisposables.add(repository.addMonsterToCollection(mCollectionId, monster.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    View view = getView();
                    if (view != null) {
                        Collection collection = mViewModel.getCollection().getValue();
                        String collectionName = collection != null ? collection.name : "";
                        SnackbarHelper.showLong(view, getString(R.string.snackbar_monster_added_to_collection, monster.name, collectionName));
                    }
                }, Logger::logError));
    }

    private void navigateToMonsterDetail(@NonNull UUID monsterId) {
        NavDirections action = CollectionDetailFragmentDirections.actionCollectionDetailFragmentToNavigationMonster(monsterId.toString());
        Navigation.findNavController(requireView()).navigate(action);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.collection_detail_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_action_add_collection_to_dashboard) {
            addCollectionToDashboard();
            return true;
        } else if (item.getItemId() == R.id.menu_action_export_collection) {
            exportCollection();
            return true;
        } else if (item.getItemId() == R.id.menu_action_remove_all_monsters_from_collection) {
            showRemoveAllMonstersConfirmationDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showRemoveAllMonstersConfirmationDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.dialog_remove_all_from_collection_title)
                .setMessage(R.string.dialog_remove_all_from_collection_message)
                .setPositiveButton(R.string.action_remove_all, (dialog, which) -> {
                    mDisposables.add(getMonsterRepository().removeAllMonstersFromCollection(mCollectionId)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(() -> {
                                View view = getView();
                                if (view != null) {
                                    SnackbarHelper.showLong(view, R.string.snackbar_collection_cleared);
                                }
                            }, throwable -> Logger.logError("Failed to remove all monsters from collection", throwable)));
                })
                .setNegativeButton(R.string.dialog_cancel, null)
                .show();
    }

    private void exportCollection() {
        Collection collection = mViewModel.getCollection().getValue();
        List<Monster> monsters = mViewModel.getMonsters().getValue();
        if (collection != null) {
            String json = new com.majinnaibu.monstercards.exporters.BinderExporter().exportBinder(
                    collection.name,
                    monsters != null ? monsters : new java.util.ArrayList<>()
            );
            String collectionName = (collection.name != null && !collection.name.trim().isEmpty())
                    ? collection.name.trim()
                    : "collection";
            exportToFile(collectionName + ".binder", json);
        }
    }

    private void addCollectionToDashboard() {
        MonsterRepository repository = getMonsterRepository();
        mDisposables.add(repository.addCollectionToDashboard(mCollectionId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    View view = getView();
                    if (view != null) {
                        Collection collection = mViewModel.getCollection().getValue();
                        String collectionName = collection != null ? collection.name : "";
                        SnackbarHelper.showLong(view, getString(R.string.snackbar_collection_added_to_dashboard, collectionName));
                    }
                }, Logger::logError));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mDisposables.clear();
    }
}
