package com.majinnaibu.monstercards.ui.shared;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.OpenableColumns;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.majinnaibu.monstercards.MonsterCardsApplication;
import com.majinnaibu.monstercards.R;
import com.majinnaibu.monstercards.data.MonsterRepository;
import com.majinnaibu.monstercards.utils.Logger;
import com.majinnaibu.monstercards.utils.SnackbarHelper;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class MCFragment extends Fragment {
    private String mPendingExportContent;
    private ActivityResultLauncher<Intent> mCreateDocumentLauncher;

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
