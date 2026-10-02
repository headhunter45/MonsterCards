package com.majinnaibu.monstercards.ui.compendium;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.majinnaibu.monstercards.AppDatabase;
import com.majinnaibu.monstercards.MonsterCardsApplication;
import com.majinnaibu.monstercards.R;
import com.majinnaibu.monstercards.data.CompendiumSourceManager;
import com.majinnaibu.monstercards.data.ImportConfig;
import com.majinnaibu.monstercards.models.ImportSource;
import com.majinnaibu.monstercards.ui.shared.MCFragment;
import com.majinnaibu.monstercards.utils.Logger;
import com.majinnaibu.monstercards.utils.SnackbarHelper;

import java.lang.reflect.Field;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CompendiumSourcesFragment extends MCFragment implements CompendiumSourcesAdapter.SourceActionListener {

    private final CompositeDisposable mDisposables = new CompositeDisposable();
    private CompendiumSourcesAdapter mAdapter;
    private Disposable mActiveDownloadDisposable;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setTitle(getString(R.string.title_compendium_sources));
        View root;
        try {
            root = inflater.inflate(R.layout.fragment_compendium_sources, container, false);
        } catch (Exception e) {
            Logger.logError("Failed to inflate fragment_compendium_sources layout", e);
            return new View(requireContext());
        }

        try {
            RecyclerView recyclerView = root.findViewById(R.id.recycler_compendium_sources);
            if (recyclerView != null) {
                recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

                List<ImportSource> sources = ImportConfig.SOURCES;
                mAdapter = new CompendiumSourcesAdapter(requireContext(), sources, this);
                recyclerView.setAdapter(mAdapter);

                checkForUpdates(sources);
            }
        } catch (Exception e) {
            Logger.logError("Error initializing compendium sources view", e);
        }

        return root;
    }

    private void checkForUpdates(@NonNull List<ImportSource> sources) {
        Context context = requireContext().getApplicationContext();
        for (ImportSource source : sources) {
            if (CompendiumSourceManager.isSourceDownloaded(context, source.id)) {
                mDisposables.add(Single.fromCallable(() -> CompendiumSourceManager.checkForUpdate(context, source))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(result -> {
                            if (result.hasUpdate && mAdapter != null) {
                                mAdapter.setSourceHasUpdate(source.id, true);
                            }
                        }, throwable -> Logger.logError("Failed to check for updates on " + source.id, throwable)));
            }
        }
    }

    @Override
    public void onDownloadClicked(@NonNull ImportSource source) {
        showLegalDisclaimerDialog(source);
    }

    private void showLegalDisclaimerDialog(@NonNull ImportSource source) {
        Context context = requireContext();
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_compendium_disclaimer, null);

        TextView textMessage = view.findViewById(R.id.text_disclaimer_message);
        MaterialCheckBox checkBox = view.findViewById(R.id.checkbox_acknowledge);

        String url = source.downloadUrl != null ? source.downloadUrl : (source.creatorPageLink != null ? source.creatorPageLink : "https://open5e.com");
        textMessage.setText(getString(R.string.dialog_disclaimer_message, url));

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(R.string.dialog_disclaimer_title)
                .setView(view)
                .setPositiveButton(R.string.action_agree_and_download, (d, which) -> {
                    startCompendiumDownload(source);
                })
                .setNegativeButton(R.string.dialog_cancel, null)
                .create();

        dialog.show();

        // Initially disable positive button until acknowledgment checkbox is checked
        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        if (positiveButton != null) {
            positiveButton.setEnabled(false);
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> positiveButton.setEnabled(isChecked));
        }
    }

    private void startCompendiumDownload(@NonNull ImportSource source) {
        if (mActiveDownloadDisposable != null && !mActiveDownloadDisposable.isDisposed()) {
            View view = getView();
            if (view != null) {
                SnackbarHelper.showLong(view, "A download is already in progress.");
            }
            return;
        }

        Context context = requireContext().getApplicationContext();
        AppDatabase db = getAppDatabase();
        if (db == null) {
            Logger.logError("Cannot start download: AppDatabase is null", null);
            View view = getView();
            if (view != null) {
                SnackbarHelper.showLong(view, getString(R.string.snackbar_compendium_download_failed, source.projectName));
            }
            return;
        }

        mAdapter.updateProgress(source.id, true, 0, 0, getString(R.string.status_downloading));

        mActiveDownloadDisposable = Single.fromCallable(() -> {
                    return CompendiumSourceManager.downloadAndIngest(
                            context,
                            db,
                            source,
                            () -> mActiveDownloadDisposable != null && mActiveDownloadDisposable.isDisposed(),
                            (current, total, statusMessage) -> {
                                if (getActivity() != null) {
                                    requireActivity().runOnUiThread(() -> {
                                        if (mAdapter != null) {
                                            mAdapter.updateProgress(source.id, true, current, total, statusMessage);
                                        }
                                    });
                                }
                            }
                    );
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(count -> {
                    mAdapter.updateProgress(source.id, false, 0, 0, null);
                    View view = getView();
                    if (view != null) {
                        SnackbarHelper.showLong(view, getString(R.string.snackbar_compendium_downloaded, count, source.projectName));
                    }
                }, throwable -> {
                    mAdapter.updateProgress(source.id, false, 0, 0, null);
                    Logger.logError("Compendium download error for " + source.projectName, throwable);
                    View view = getView();
                    if (view != null) {
                        SnackbarHelper.showLong(view, getString(R.string.snackbar_compendium_download_failed, source.projectName));
                    }
                });

        mDisposables.add(mActiveDownloadDisposable);
    }

    @Override
    public void onRemoveClicked(@NonNull ImportSource source) {
        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.dialog_remove_compendium_title)
                .setMessage(getString(R.string.dialog_remove_compendium_message, source.projectName))
                .setPositiveButton(R.string.action_remove, (dialog, which) -> {
                    Context context = requireContext().getApplicationContext();
                    AppDatabase db = getAppDatabase();
                    if (db == null) {
                        Logger.logError("Cannot clear source: AppDatabase is null", null);
                        return;
                    }
                    mDisposables.add(Single.fromCallable(() -> {
                                CompendiumSourceManager.clearSource(context, db, source.id);
                                return true;
                            })
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(result -> {
                                mAdapter.notifyDataSetChanged();
                                View view = getView();
                                if (view != null) {
                                    SnackbarHelper.showLong(view, getString(R.string.snackbar_compendium_cleared, source.projectName));
                                }
                            }, throwable -> Logger.logError("Failed to clear compendium source", throwable)));
                })
                .setNegativeButton(R.string.dialog_cancel, null)
                .show();
    }

    private AppDatabase getAppDatabase() {
        try {
            MonsterCardsApplication app = getApplication();
            if (app != null && app.getDatabase() != null) {
                return app.getDatabase();
            }
            Field dbField = app.getMonsterRepository().getClass().getDeclaredField("m_db");
            dbField.setAccessible(true);
            return (AppDatabase) dbField.get(app.getMonsterRepository());
        } catch (Exception e) {
            Logger.logError("Failed to access AppDatabase directly", e);
            return null;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mDisposables.clear();
    }
}
