package com.majinnaibu.monstercards;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.majinnaibu.monstercards.helpers.StringHelper;
import com.majinnaibu.monstercards.init.AppCenterInitializer;
import com.majinnaibu.monstercards.init.FlipperInitializer;
import com.majinnaibu.monstercards.utils.Logger;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

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
        super.onCreate(savedInstanceState);
        AppCenterInitializer.init(getApplication());
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
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
        navController.addOnDestinationChangedListener(FlipperInitializer::sendNavigationEvent);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        onNewIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        String json = readMonsterJSONFromIntent(intent);
        if (!StringHelper.isNullOrEmpty(json)) {
            NavHostFragment navHostFragment = Objects.requireNonNull((NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment));
            NavController navController = navHostFragment.getNavController();
            NavDirections action = MobileNavigationDirections.actionGlobalMonsterImportFragment(json);
            navController.navigate(action);
        }
    }

    @Nullable
    private String readMonsterJSONFromIntent(@NonNull Intent intent) {
        String action = intent.getAction();
        Bundle extras = intent.getExtras();
        String type = intent.getType();
        String json;
        Uri uri = null;
        if ("android.intent.action.MAIN".equals(action)) {
            return null;
        } else if ("android.intent.action.SEND".equals(action) && "text/plain".equals(type)) {
            uri = extras.getParcelable("android.intent.extra.STREAM");
        } else if ("android.intent.action.VIEW".equals(action) && ("text/plain".equals(type) || "application/octet-stream".equals(type))) {
            uri = intent.getData();
        } else {
            Logger.logError(String.format("unexpected launch configuration action: %s, type: %s", action, type));
        }
        if (uri == null) {
            return null;
        }
        json = readContentsOfUri(uri);
        if (StringHelper.isNullOrEmpty(json)) {
            return null;
        }
        return json;
    }

    @Nullable
    private String readContentsOfUri(Uri uri) {
        StringBuilder builder = new StringBuilder();
        try (
                InputStream inputStream = openInputStream(uri);
                BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inputStream)))
        ) {
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

    private boolean isVirtualFile(Uri uri) {
        if (!DocumentsContract.isDocumentUri(this, uri)) {
            return false;
        }

        Cursor cursor = getContentResolver().query(
                uri,
                new String[]{DocumentsContract.Document.COLUMN_FLAGS},
                null, null, null);

        int flags = 0;
        if (cursor.moveToFirst()) {
            flags = cursor.getInt(0);
        }
        cursor.close();

        return (flags & DocumentsContract.Document.FLAG_VIRTUAL_DOCUMENT) != 0;
    }

    private InputStream openInputStream(Uri uri) throws IOException {
        ContentResolver resolver = getContentResolver();
        if (isVirtualFile(uri)) {
            String[] openableMimeTypes = resolver.getStreamTypes(uri, "*/*");
            if (openableMimeTypes == null || openableMimeTypes.length <= 0) {
                throw new FileNotFoundException();
            }
            return resolver.openTypedAssetFileDescriptor(uri, openableMimeTypes[0], null).createInputStream();
        } else {
            return resolver.openInputStream(uri);
        }
    }
}
