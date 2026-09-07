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
import com.google.android.material.snackbar.Snackbar;
import com.majinnaibu.monstercards.R;
import com.majinnaibu.monstercards.data.MonsterRepository;
import com.majinnaibu.monstercards.models.Collection;
import com.majinnaibu.monstercards.models.Monster;
import com.majinnaibu.monstercards.ui.dashboard.DashboardRecyclerViewAdapter;
import com.majinnaibu.monstercards.ui.shared.MCFragment;
import com.majinnaibu.monstercards.utils.Logger;

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
            fab.setOnClickListener(v -> showAddMonsterDialog());
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
                mAdapter.submitList(monsters);
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

        mAdapter = new DashboardRecyclerViewAdapter(monster -> {
            if (monster != null) {
                navigateToMonsterDetail(monster.id);
            } else {
                Logger.logError("Can't navigate to MonsterDetailFragment with a null monster");
            }
        });
        recyclerView.setAdapter(mAdapter);
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
                            Snackbar.make(view, getString(R.string.snackbar_failed_to_create_monster), Snackbar.LENGTH_LONG).show();
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
                        Snackbar.make(
                                view,
                                getString(R.string.snackbar_monster_added_to_collection, monster.name, collectionName),
                                Snackbar.LENGTH_LONG)
                                .show();
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
        }
        return super.onOptionsItemSelected(item);
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
                        Snackbar.make(view, getString(R.string.snackbar_collection_added_to_dashboard, collectionName), Snackbar.LENGTH_LONG).show();
                    }
                }, Logger::logError));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mDisposables.clear();
    }
}
