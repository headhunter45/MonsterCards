package com.majinnaibu.monstercards.ui.library;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.majinnaibu.monstercards.MainActivity;
import com.majinnaibu.monstercards.R;
import com.majinnaibu.monstercards.data.MonsterRepository;
import com.majinnaibu.monstercards.importers.DnDBeyondImporter;
import com.majinnaibu.monstercards.importers.Open5eImporter;
import com.majinnaibu.monstercards.models.Monster;
import com.majinnaibu.monstercards.ui.monster.MonsterDetailFragmentDirections;
import com.majinnaibu.monstercards.ui.shared.MCFragment;
import com.majinnaibu.monstercards.ui.shared.SwipeToDeleteCallback;
import com.majinnaibu.monstercards.utils.Logger;
import com.majinnaibu.monstercards.utils.SnackbarHelper;

import java.util.UUID;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableCompletableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LibraryFragment extends MCFragment {
    private final CompositeDisposable mDisposables = new CompositeDisposable();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View root = inflater.inflate(R.layout.fragment_library, container, false);

        FloatingActionButton fab = root.findViewById(R.id.fab);
        assert fab != null;
        setupAddMonsterButton(fab);

        View emptyState = root.findViewById(R.id.empty_state);
        View emptyStateButton = root.findViewById(R.id.empty_state_button);
        if (emptyStateButton != null) {
            emptyStateButton.setOnClickListener(v -> createNewMonster());
        }

        final RecyclerView recyclerView = root.findViewById(R.id.monster_list);
        assert recyclerView != null;
        setupRecyclerView(recyclerView, emptyState);

        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.library_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_action_import_from_url) {
            showImportUrlDialog();
            return true;
        } else if (item.getItemId() == R.id.menu_action_import_from_file) {
            importMonsterFromFile();
            return true;
        } else if (item.getItemId() == R.id.menu_action_export_library) {
            exportLibrary();
            return true;
        } else if (item.getItemId() == R.id.menu_action_export_everything) {
            exportEverything();
            return true;
        } else if (item.getItemId() == R.id.menu_action_clear_all_data) {
            showClearAllDataConfirmationDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showClearAllDataConfirmationDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.dialog_clear_all_data_title)
                .setMessage(R.string.dialog_clear_all_data_message)
                .setPositiveButton(R.string.action_clear, (dialog, which) -> {
                    mDisposables.add(getMonsterRepository().clearAllData()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(() -> {
                                View view = getView();
                                if (view != null) {
                                    SnackbarHelper.showLong(view, R.string.snackbar_all_data_cleared);
                                }
                            }, throwable -> Logger.logError("Failed to clear all data", throwable)));
                })
                .setNegativeButton(R.string.dialog_cancel, null)
                .show();
    }

    private void exportLibrary() {
        getMonsterRepository().getMonsters()
                .firstOrError()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(monsters -> {
                    String json = new com.majinnaibu.monstercards.exporters.BinderExporter().exportBinder("", monsters);
                    String fileName = getString(R.string.default_filename_library) + ".binder";
                    exportToFile(fileName, json);
                }, Logger::logError);
    }

    private void exportEverything() {
        mDisposables.add(getMonsterRepository().exportEverything()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(json -> {
                    String fileName = getString(R.string.default_filename_export_everything) + ".binder";
                    exportToFile(fileName, json);
                }, throwable -> Logger.logError("Failed to export everything", throwable)));
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView, @Nullable View emptyState) {
        Context context = requireContext();
        MonsterRepository repository = this.getMonsterRepository();

        mDisposables.add(repository.getMonsters()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(monsters -> {
                    boolean isEmpty = (monsters == null || monsters.isEmpty());
                    if (emptyState != null) {
                        emptyState.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
                    }
                    recyclerView.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
                }, Logger::logError));

        LibraryRecyclerViewAdapter adapter = new LibraryRecyclerViewAdapter(
                context,
                repository.getMonsters(),
                (monster) -> navigateToMonsterDetail(monster.id),
                (monster) -> repository
                        .deleteMonster(monster)
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
                        }));
        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(requireContext(), (position, direction) -> adapter.deleteItem(position), null));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void setupAddMonsterButton(@NonNull FloatingActionButton fab) {
        fab.setOnClickListener(view -> showLibraryFabOptionsDialog());
    }

    private void showLibraryFabOptionsDialog() {
        String[] options = new String[]{
                getString(R.string.action_create_monster),
                getString(R.string.action_import_monster_from_url),
                getString(R.string.action_import_monster_from_file)
        };
        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.title_library_actions)
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        createNewMonster();
                    } else if (which == 1) {
                        showImportUrlDialog();
                    } else if (which == 2) {
                        importMonsterFromFile();
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, null)
                .show();
    }

    protected void navigateToMonsterDetail(@NonNull UUID monsterId) {
        NavDirections action = LibraryFragmentDirections.actionNavigationLibraryToNavigationMonster(monsterId.toString());
        Navigation.findNavController(requireView()).navigate(action);
    }

    protected void navigateToEditMonster(@NonNull UUID monsterId) {
        NavController navController = Navigation.findNavController(requireView());
        NavDirections action = LibraryFragmentDirections.actionNavigationLibraryToNavigationMonster(monsterId.toString());
        navController.navigate(action);
        action = MonsterDetailFragmentDirections.actionNavigationMonsterToEditMonsterFragment(monsterId.toString());
        navController.navigate(action);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mDisposables.clear();
    }
}
