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

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.majinnaibu.monstercards.helpers.MonsterImportHelper;
import com.majinnaibu.monstercards.helpers.StringHelper;
import com.majinnaibu.monstercards.importers.BinderImporter;
import com.majinnaibu.monstercards.importers.DnDBeyondImporter;
import com.majinnaibu.monstercards.init.AppCenterInitializer;
import com.majinnaibu.monstercards.utils.Logger;
import com.majinnaibu.monstercards.utils.ToastHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

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

        com.google.android.material.appbar.MaterialToolbar toolbar = findViewById(R.id.toolbar);
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

        String json = readMonsterJSONFromIntent(intent);
        if (!StringHelper.isNullOrEmpty(json)) {
            importMonsterFromInputAndNavigate(json);
        }
    }

    public void importMonsterFromInputAndNavigate(@NonNull String input) {
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
                        Logger.logError("Failed to import binder from input", throwable);
                        ToastHelper.showLong(this, R.string.failed_to_import_url);
                    });
            return;
        }

        ToastHelper.showShort(this, R.string.toast_importing_url);
        Single.fromCallable(() -> MonsterImportHelper.fromJSON(input))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(monster -> {
                    String serializedJson = new Gson().toJson(monster);
                    NavHostFragment navHostFragment = Objects.requireNonNull((NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment));
                    NavController navController = navHostFragment.getNavController();
                    NavDirections navAction = MobileNavigationDirections.actionGlobalMonsterImportFragment(serializedJson);
                    navController.navigate(navAction);
                }, throwable -> {
                    Logger.logError("Failed to import monster from input", throwable);
                    ToastHelper.showLong(this, R.string.failed_to_import_url);
                });
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
            return null;
        }
        return builder.toString();
    }
}
