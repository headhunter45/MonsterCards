package com.majinnaibu.monstercards;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.majinnaibu.monstercards.helpers.MonsterImportHelper;
import com.majinnaibu.monstercards.helpers.StringHelper;
import com.majinnaibu.monstercards.importers.BinderImporter;
import com.majinnaibu.monstercards.importers.DnDBeyondImporter;
import com.majinnaibu.monstercards.importers.Open5eApiWrapper;
import com.majinnaibu.monstercards.init.AppCenterInitializer;
import com.majinnaibu.monstercards.models.BinderExport;
import com.majinnaibu.monstercards.models.Monster;
import com.majinnaibu.monstercards.utils.Logger;
import com.majinnaibu.monstercards.utils.SnackbarHelper;
import com.majinnaibu.monstercards.utils.ToastHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private Disposable mOpen5eImportDisposable;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getOnBackPressedDispatcher().onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        super.onCreate(savedInstanceState);
        AppCenterInitializer.init(getApplication());
        setContentView(R.layout.activity_main);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        View appBarLayout = findViewById(R.id.app_bar_layout);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        View container = findViewById(R.id.container);
        if (container != null) {
            ViewCompat.setOnApplyWindowInsetsListener(container, (v, windowInsets) -> {
                Insets systemBars = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
                if (appBarLayout != null) {
                    appBarLayout.setPadding(0, systemBars.top, 0, 0);
                }
                if (navView != null) {
                    navView.setPadding(0, 0, 0, systemBars.bottom);
                }
                return windowInsets;
            });
        }
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_search,
                R.id.navigation_dashboard,
                R.id.navigation_collections,
                R.id.navigation_library)
                .build();
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        onNewIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (intent == null) {
            return;
        }
        String action = intent.getAction();
        Bundle extras = intent.getExtras();

        if ("android.intent.action.SEND".equals(action) && extras != null) {
            CharSequence sharedText = extras.getCharSequence(Intent.EXTRA_TEXT);
            if (sharedText != null) {
                String textStr = sharedText.toString().trim();
                DnDBeyondImporter dndImporter = new DnDBeyondImporter();
                if (dndImporter.canImport(textStr)) {
                    importMonsterFromInputAndNavigate(textStr);
                    return;
                }
            }
        } else if ("android.intent.action.VIEW".equals(action)) {
            Uri dataUri = intent.getData();
            if (dataUri != null) {
                String dataStr = dataUri.toString().trim();
                DnDBeyondImporter dndImporter = new DnDBeyondImporter();
                if (dndImporter.canImport(dataStr)) {
                    importMonsterFromInputAndNavigate(dataStr);
                    return;
                }
            }
        }

        Uri intentUri = getUriFromIntent(intent);
        String fileName = intentUri != null ? getFileNameFromUri(intentUri) : null;
        String json = readMonsterJSONFromIntent(intent);
        if (!StringHelper.isNullOrEmpty(json)) {
            importMonsterFromInputAndNavigate(json, fileName);
        }
    }

    @Nullable
    private Uri getUriFromIntent(@NonNull Intent intent) {
        String action = intent.getAction();
        Bundle extras = intent.getExtras();
        if ("android.intent.action.SEND".equals(action)) {
            if (extras != null) {
                return extras.getParcelable(Intent.EXTRA_STREAM);
            }
        } else if ("android.intent.action.VIEW".equals(action) || "android.intent.action.EDIT".equals(action)) {
            return intent.getData();
        }
        return null;
    }

    public void importMonsterFromInputAndNavigate(@NonNull String input) {
        importMonsterFromInputAndNavigate(input, null);
    }

    public void importMonsterFromInputAndNavigate(@NonNull String input, @Nullable String fileName) {
        try {
            BinderImporter binderImporter = new BinderImporter();
            if (binderImporter.canImport(input)) {
                ToastHelper.showShort(this, R.string.toast_importing_url);
                Single.fromCallable(() -> binderImporter.parse(input))
                        .flatMapCompletable(binder -> ((MonsterCardsApplication) getApplication()).getMonsterRepository().importBinder(binder))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            ToastHelper.showLong(this, R.string.snackbar_import_binder_success);
                            NavHostFragment navHostFragment = Objects.requireNonNull((NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment));
                            NavController navController = navHostFragment.getNavController();
                            navController.navigate(R.id.navigation_library);
                        }, throwable -> {
                            Logger.logError("Failed to import binder from input: " + fileName, throwable);
                            ToastHelper.showLong(this, R.string.failed_to_import_url);
                        });
                return;
            }

            ToastHelper.showShort(this, R.string.toast_importing_url);
            Single.fromCallable(() -> MonsterImportHelper.fromJSON(input, fileName))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(monster -> {
                        String serializedJson = new Gson().toJson(monster);
                        NavHostFragment navHostFragment = Objects.requireNonNull((NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment));
                        NavController navController = navHostFragment.getNavController();
                        NavDirections navAction = MobileNavigationDirections.actionGlobalMonsterImportFragment(serializedJson);
                        navController.navigate(navAction);
                    }, throwable -> {
                        Logger.logError("Failed to import monster from input: " + fileName, throwable);
                        ToastHelper.showLong(this, R.string.failed_to_import_url);
                    });
        } catch (Exception e) {
            Logger.logError("Failed to process import input: " + fileName, e);
            ToastHelper.showLong(this, R.string.failed_to_import_url);
        }
    }

    public void importAllFromOpen5e() {
        if (mOpen5eImportDisposable != null && !mOpen5eImportDisposable.isDisposed()) {
            ToastHelper.showShort(this, "Open5e import already running");
            return;
        }

        View rootView = findViewById(android.R.id.content);
        Snackbar snackbar = SnackbarHelper.makeIndefinite(rootView, R.string.snackbar_importing_open5e);
        snackbar.setAction(R.string.action_cancel, v -> {
            if (mOpen5eImportDisposable != null && !mOpen5eImportDisposable.isDisposed()) {
                mOpen5eImportDisposable.dispose();
                SnackbarHelper.showLong(rootView, R.string.snackbar_open5e_import_cancelled);
            }
        });
        snackbar.show();

        mOpen5eImportDisposable = Single.fromCallable(() -> {
            int totalImported = 0;
            String nextUrl = null;
            do {
                if (mOpen5eImportDisposable != null && mOpen5eImportDisposable.isDisposed()) {
                    break;
                }
                Open5eApiWrapper.Open5ePageResult page = Open5eApiWrapper.fetchPage(nextUrl);
                for (Monster monster : page.monsters) {
                    if (mOpen5eImportDisposable != null && mOpen5eImportDisposable.isDisposed()) {
                        break;
                    }
                    try {
                        ((MonsterCardsApplication) getApplication()).getMonsterRepository()
                                .saveMonster(monster)
                                .blockingAwait();
                        totalImported++;
                    } catch (Exception e) {
                        Logger.logError("Failed to save monster to database", e);
                        runOnUiThread(() -> ToastHelper.showShort(MainActivity.this, "An error occurred while saving a monster."));
                    }
                }
                nextUrl = page.nextUrl;
            } while (nextUrl != null && !nextUrl.isEmpty());
            return totalImported;
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(result -> {
            snackbar.dismiss();
            SnackbarHelper.showLong(rootView, R.string.snackbar_open5e_import_complete);
        }, throwable -> {
            snackbar.dismiss();
            Logger.logError("Failed to import all Open5e monsters", throwable);
            SnackbarHelper.showLong(rootView, R.string.snackbar_open5e_import_failed);
        });
    }

    public void importMultipleFilesFromUris(@NonNull List<Uri> uris) {
        if (uris.isEmpty()) return;
        if (uris.size() == 1) {
            Uri singleUri = uris.get(0);
            String fileName = getFileNameFromUri(singleUri);
            String content = readContentsOfUri(singleUri);
            if (content != null && !content.trim().isEmpty()) {
                importMonsterFromInputAndNavigate(content, fileName);
            } else {
                ToastHelper.showLong(this, R.string.failed_to_import_url);
            }
            return;
        }

        ToastHelper.showShort(this, R.string.toast_importing_url);
        Single.fromCallable(() -> {
            int monstersImported = 0;
            int bindersImported = 0;

            for (Uri uri : uris) {
                try {
                    String fileName = getFileNameFromUri(uri);
                    String content = readContentsOfUri(uri);
                    if (content == null || content.trim().isEmpty()) {
                        continue;
                    }

                    BinderImporter binderImporter = new BinderImporter();
                    if (binderImporter.canImport(content)) {
                        BinderExport binder = binderImporter.parse(content);
                        ((MonsterCardsApplication) getApplication()).getMonsterRepository()
                                .importBinder(binder)
                                .blockingAwait();
                        bindersImported++;
                    } else {
                        Monster monster = MonsterImportHelper.fromJSON(content, fileName);
                        ((MonsterCardsApplication) getApplication()).getMonsterRepository()
                                .saveMonster(monster)
                                .blockingAwait();
                        monstersImported++;
                    }
                } catch (Exception e) {
                    Logger.logError("Failed to import file URI: " + uri, e);
                }
            }

            return new BatchImportResult(monstersImported, bindersImported);
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(result -> {
            if (result.bindersImported > 0 || result.monstersImported > 0) {
                String message = buildImportSummaryMessage(result.monstersImported, result.bindersImported);
                ToastHelper.showLong(this, message);
                NavHostFragment navHostFragment = Objects.requireNonNull((NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment));
                NavController navController = navHostFragment.getNavController();
                navController.navigate(R.id.navigation_library);
            } else {
                ToastHelper.showLong(this, R.string.failed_to_import_url);
            }
        }, throwable -> {
            Logger.logError("Failed to execute batch file import", throwable);
            ToastHelper.showLong(this, R.string.failed_to_import_url);
        });
    }

    private String buildImportSummaryMessage(int monstersCount, int bindersCount) {
        StringBuilder sb = new StringBuilder("Successfully imported ");
        if (bindersCount > 0 && monstersCount > 0) {
            sb.append(bindersCount).append(bindersCount == 1 ? " collection" : " collections")
              .append(" and ")
              .append(monstersCount).append(monstersCount == 1 ? " monster" : " monsters");
        } else if (bindersCount > 0) {
            sb.append(bindersCount).append(bindersCount == 1 ? " collection" : " collections");
        } else {
            sb.append(monstersCount).append(monstersCount == 1 ? " monster" : " monsters");
        }
        return sb.toString();
    }

    private static class BatchImportResult {
        final int monstersImported;
        final int bindersImported;

        BatchImportResult(int monstersImported, int bindersImported) {
            this.monstersImported = monstersImported;
            this.bindersImported = bindersImported;
        }
    }

    @Nullable
    private String readMonsterJSONFromIntent(@NonNull Intent intent) {
        String action = intent.getAction();
        Bundle extras = intent.getExtras();
        Uri uri = null;
        if ("android.intent.action.SEND".equals(action)) {
            if (extras != null) {
                uri = extras.getParcelable(Intent.EXTRA_STREAM);
            }
        } else if ("android.intent.action.VIEW".equals(action) || "android.intent.action.EDIT".equals(action)) {
            uri = intent.getData();
        } else {
            Logger.logError(String.format("unexpected launch configuration action: %s", action));
        }

        if (uri == null || !isMonsterFile(uri)) {
            if (uri != null) {
                Logger.logError("Ignored file because extension is not supported (.monster, .card, .binder): " + uri);
            }
            return null;
        }

        String json = readContentsOfUri(uri);
        if (StringHelper.isNullOrEmpty(json)) {
            return null;
        }
        return json;
    }

    private boolean isMonsterFile(@NonNull Uri uri) {
        String fileName = getFileNameFromUri(uri);
        if (fileName == null) {
            return false;
        }
        String lowerName = fileName.toLowerCase(Locale.ROOT);
        return lowerName.endsWith(".monster") || lowerName.endsWith(".monster.txt")
                || lowerName.endsWith(".card") || lowerName.endsWith(".card.txt")
                || lowerName.endsWith(".binder") || lowerName.endsWith(".binder.txt");
    }

    @Nullable
    private String getFileNameFromUri(@NonNull Uri uri) {
        String displayName = null;
        if ("content".equals(uri.getScheme())) {
            try (Cursor cursor = getContentResolver().query(uri, new String[]{OpenableColumns.DISPLAY_NAME}, null, null, null)) {
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
        if (displayName == null) {
            displayName = uri.getPath();
        }
        return displayName;
    }

    @Nullable
    private String readContentsOfUri(Uri uri) {
        StringBuilder builder = new StringBuilder();
        try (InputStream inputStream =
                     getContentResolver().openInputStream(uri);
             BufferedReader reader = new BufferedReader(
                     new InputStreamReader(Objects.requireNonNull(inputStream)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {
            Logger.logError("error reading file", e);
            ToastHelper.showLong(this, "An error occurred while reading the file.");
            return null;
        }
        return builder.toString();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mOpen5eImportDisposable != null && !mOpen5eImportDisposable.isDisposed()) {
            mOpen5eImportDisposable.dispose();
        }
    }
}
