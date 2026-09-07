package com.majinnaibu.monstercards.ui.collections;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import com.majinnaibu.monstercards.models.Collection;
import com.majinnaibu.monstercards.ui.shared.MCFragment;
import com.majinnaibu.monstercards.ui.shared.SwipeToDeleteCallback;
import com.majinnaibu.monstercards.utils.Logger;

import java.util.UUID;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableCompletableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CollectionsFragment extends MCFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_collections, container, false);

        FloatingActionButton fab = root.findViewById(R.id.fab_add_collection);
        if (fab != null) {
            fab.setOnClickListener(v -> showCreateCollectionDialog());
        }

        RecyclerView recyclerView = root.findViewById(R.id.collection_list);
        if (recyclerView != null) {
            setupRecyclerView(recyclerView);
        }

        return root;
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        Context context = requireContext();
        MonsterRepository repository = getMonsterRepository();

        CollectionsRecyclerViewAdapter adapter = new CollectionsRecyclerViewAdapter(
                context,
                repository.getCollectionsWithCount(),
                collection -> navigateToCollectionDetail(collection.id),
                collection -> repository.deleteCollection(collection)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new DisposableCompletableObserver() {
                            @Override
                            public void onComplete() {
                            }

                            @Override
                            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                                Logger.logError(e);
                            }
                        })
        );

        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(context, (position, direction) -> adapter.deleteItem(position), null));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void showCreateCollectionDialog() {
        Context context = requireContext();
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        int padding = (int) (16 * getResources().getDisplayMetrics().density);
        layout.setPadding(padding, padding, padding, padding);

        final EditText nameInput = new EditText(context);
        nameInput.setHint(R.string.label_collection_name);
        layout.addView(nameInput);

        final EditText descInput = new EditText(context);
        descInput.setHint(R.string.label_collection_description);
        layout.addView(descInput);

        new AlertDialog.Builder(context)
                .setTitle(R.string.title_new_collection)
                .setView(layout)
                .setPositiveButton(R.string.dialog_create, (dialog, which) -> {
                    String name = nameInput.getText().toString().trim();
                    String description = descInput.getText().toString().trim();
                    if (TextUtils.isEmpty(name)) {
                        name = getString(R.string.title_new_collection);
                    }
                    createCollection(name, description);
                })
                .setNegativeButton(R.string.dialog_cancel, null)
                .show();
    }

    private void createCollection(@NonNull String name, @NonNull String description) {
        Collection collection = new Collection(name, description);
        MonsterRepository repository = getMonsterRepository();
        repository.saveCollection(collection)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        View view = getView();
                        if (view != null) {
                            Snackbar.make(
                                    view,
                                    getString(R.string.snackbar_collection_created, collection.name),
                                    Snackbar.LENGTH_LONG)
                                    .setAction("View", v -> navigateToCollectionDetail(collection.id))
                                    .show();
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Logger.logError("Error creating collection", e);
                        View view = getView();
                        if (view != null) {
                            Snackbar.make(view, getString(R.string.snackbar_failed_to_create_collection), Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void navigateToCollectionDetail(@NonNull UUID collectionId) {
        NavDirections action = CollectionsFragmentDirections.actionNavigationCollectionsToCollectionDetailFragment(collectionId.toString());
        Navigation.findNavController(requireView()).navigate(action);
    }
}
