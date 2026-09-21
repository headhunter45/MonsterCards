package com.majinnaibu.monstercards.ui.shared;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.EditText;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.majinnaibu.monstercards.MainActivity;
import com.majinnaibu.monstercards.MonsterCardsApplication;
import com.majinnaibu.monstercards.R;
import com.majinnaibu.monstercards.data.MonsterRepository;
import com.majinnaibu.monstercards.importers.DnDBeyondImporter;
import com.majinnaibu.monstercards.importers.Open5eImporter;
import com.majinnaibu.monstercards.models.Monster;
import com.majinnaibu.monstercards.utils.Logger;
import com.majinnaibu.monstercards.utils.SnackbarHelper;
import com.majinnaibu.monstercards.utils.ToastHelper;

import com.majinnaibu.monstercards.data.GitRepositoryConfig;
import com.majinnaibu.monstercards.models.GitRepositorySource;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableCompletableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MCFragment extends Fragment {
    private String mPendingExportContent;
    private ActivityResultLauncher<Intent> mCreateDocumentLauncher;
    private ActivityResultLauncher<Intent> mOpenDocumentLauncher;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCreateDocumentLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri uri = result.getData().getData();
                        if (uri != null && mPendingExportContent != null) {
                            writeContentToUri(uri, mPendingExportContent);
                        }
                    }
                    mPendingExportContent = null;
                });

        mOpenDocumentLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();
                        List<Uri> uris = new ArrayList<>();
                        if (data.getClipData() != null) {
                            ClipData clipData = data.getClipData();
                            for (int i = 0; i < clipData.getItemCount(); i++) {
                                Uri uri = clipData.getItemAt(i).getUri();
                                if (uri != null) {
                                    uris.add(uri);
                                }
                            }
                        } else if (data.getData() != null) {
                            uris.add(data.getData());
                        }

                        if (!uris.isEmpty() && getActivity() instanceof MainActivity) {
                            ((MainActivity) getActivity()).importMultipleFilesFromUris(uris);
                        }
                    }
                });
    }

    public void importMonsterFromFile() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        try {
            mOpenDocumentLauncher.launch(intent);
        } catch (Exception e) {
            Logger.logError("Failed to launch file picker", e);
            View view = getView();
            if (view != null) {
                SnackbarHelper.showLong(view, R.string.failed_to_import_url);
            }
        }
    }

    public void importCollectionFromFile() {
        importMonsterFromFile();
    }

    public void showImportDialog() {
        Context context = requireContext();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.dialog_import_title);

        View view = getLayoutInflater().inflate(R.layout.dialog_import, null);
        builder.setView(view);

        AlertDialog dialog = builder.create();

        final EditText input = view.findViewById(R.id.edit_text_url);
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard != null && clipboard.hasPrimaryClip()) {
            ClipData clip = clipboard.getPrimaryClip();
            if (clip != null && clip.getItemCount() > 0) {
                CharSequence clipText = clip.getItemAt(0).getText();
                if (clipText != null) {
                    String clipStr = clipText.toString().trim();
                    if (new DnDBeyondImporter().canImport(clipStr) || new Open5eImporter().canImport(clipStr)) {
                        input.setText(clipStr);
                        input.selectAll();
                    }
                }
            }
        }

        view.findViewById(R.id.button_import_url).setOnClickListener(v -> {
            String urlOrId = input.getText().toString().trim();
            if (!urlOrId.isEmpty()) {
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).importMonsterFromInputAndNavigate(urlOrId);
                }
            }
            dialog.dismiss();
        });

        view.findViewById(R.id.button_import_file).setOnClickListener(v -> {
            importMonsterFromFile();
            dialog.dismiss();
        });

        view.findViewById(R.id.button_import_open5e).setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).importAllFromOpen5e();
            }
            dialog.dismiss();
        });

        view.findViewById(R.id.button_import_github).setOnClickListener(v -> {
            dialog.dismiss();
            showGithubRepoPicker();
        });

        dialog.show();
    }

    private void showGithubRepoPicker() {
        Context context = requireContext();
        List<GitRepositorySource> sources = GitRepositoryConfig.SOURCES;
        if (sources.isEmpty()) {
            return;
        }

        String[] repoNames = new String[sources.size()];
        for (int i = 0; i < sources.size(); i++) {
            repoNames[i] = sources.get(i).projectName;
        }

        new AlertDialog.Builder(context)
                .setTitle(R.string.dialog_select_github_repo)
                .setItems(repoNames, (d, which) -> {
                    GitRepositorySource selected = sources.get(which);
                    if (getActivity() instanceof MainActivity) {
                        ((MainActivity) getActivity()).importFromGitRepository(selected);
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, null)
                .show();
    }

    public void createNewMonster() {
        Monster monster = new Monster();
        monster.name = getString(R.string.default_monster_name);
        MonsterRepository repository = getMonsterRepository();
        repository.addMonster(monster)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        navigateToEditMonster(monster.id);
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Logger.logError("Error creating monster", e);
                        View view = getView();
                        if (view != null) {
                            SnackbarHelper.showLong(view, getString(R.string.snackbar_failed_to_create_monster));
                        }
                    }
                });
    }

    protected void navigateToEditMonster(@NonNull UUID monsterId) {
        try {
            NavController navController = Navigation.findNavController(requireView());
            Bundle args = new Bundle();
            args.putString("monster_id", monsterId.toString());
            navController.navigate(R.id.edit_monster_navigation, args);
        } catch (Exception e) {
            Logger.logError("Error navigating to edit monster", e);
        }
    }

    private String readContentsFromUri(@NonNull Uri uri) {
        Context context = getContext();
        if (context == null) return null;
        StringBuilder builder = new StringBuilder();
        try (InputStream inputStream = context.getContentResolver().openInputStream(uri);
             BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inputStream), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }
        } catch (Exception e) {
            Logger.logError("Error reading URI contents for import", e);
            Context ctx = getContext();
            if (ctx != null) {
                ToastHelper.showLong(ctx, "An error occurred while reading the file.");
            }
            return null;
        }
        return builder.toString();
    }

    public MonsterCardsApplication getApplication() {
        return (MonsterCardsApplication) requireActivity().getApplication();
    }

    protected MonsterRepository getMonsterRepository() {
        return this.getApplication().getMonsterRepository();
    }

    public AppCompatActivity requireAppCompatActivity() {
        return (AppCompatActivity) requireActivity();
    }

    public void setTitle(CharSequence title) {
        Activity activity = requireActivity();
        if (activity instanceof AppCompatActivity) {
            AppCompatActivity appCompatActivity = (AppCompatActivity) activity;
            ActionBar supportActionBar = appCompatActivity.getSupportActionBar();
            if (supportActionBar != null) {
                supportActionBar.setTitle(title);
            }
        }
    }

    protected void exportToFile(@NonNull String defaultFileName, @NonNull String content) {
        mPendingExportContent = content;
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_TITLE, defaultFileName);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Uri downloadsUri = Uri.parse("content://com.android.externalstorage.documents/document/primary%3ADownload");
            intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, downloadsUri);
        }
        try {
            mCreateDocumentLauncher.launch(intent);
        } catch (Exception e) {
            Logger.logError("Failed to launch document creation picker", e);
            View view = getView();
            if (view != null) {
                SnackbarHelper.showLong(view, R.string.snackbar_export_failed);
            }
        }
    }

    private void writeContentToUri(@NonNull Uri uri, @NonNull String content) {
        Context context = getContext();
        if (context == null) return;
        try (OutputStream out = context.getContentResolver().openOutputStream(uri)) {
            if (out != null) {
                out.write(content.getBytes(StandardCharsets.UTF_8));
                out.flush();
                View view = getView();
                if (view != null) {
                    String fileName = getFileNameFromUri(context, uri);
                    String message = getString(R.string.snackbar_export_success, fileName);
                    SnackbarHelper.showLong(view, message);
                }
            }
        } catch (Exception e) {
            Logger.logError("Failed to write export content to URI", e);
            View view = getView();
            if (view != null) {
                SnackbarHelper.showLong(view, R.string.snackbar_export_failed);
            }
        }
    }

    private String getFileNameFromUri(@NonNull Context context, @NonNull Uri uri) {
        String displayName = null;
        if ("content".equals(uri.getScheme())) {
            try (Cursor cursor = context.getContentResolver().query(uri, new String[]{OpenableColumns.DISPLAY_NAME}, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (nameIndex != -1) {
                        displayName = cursor.getString(nameIndex);
                    }
                }
            } catch (Exception e) {
                Logger.logError("Error querying display name from content URI", e);
            }
        }
        return displayName != null ? displayName : defaultFileNameFromUri(uri);
    }

    private String defaultFileNameFromUri(@NonNull Uri uri) {
        String path = uri.getPath();
        if (path != null) {
            int lastSlash = path.lastIndexOf('/');
            if (lastSlash >= 0 && lastSlash < path.length() - 1) {
                return path.substring(lastSlash + 1);
            }
        }
        return uri.toString();
    }
}
