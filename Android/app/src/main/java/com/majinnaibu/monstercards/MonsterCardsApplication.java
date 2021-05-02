package com.majinnaibu.monstercards;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.facebook.flipper.android.AndroidFlipperClient;
import com.facebook.flipper.android.utils.FlipperUtils;
import com.facebook.flipper.core.FlipperClient;
import com.facebook.flipper.plugins.databases.DatabasesFlipperPlugin;
import com.facebook.flipper.plugins.inspector.DescriptorMapping;
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin;
import com.facebook.flipper.plugins.navigation.NavigationFlipperPlugin;
import com.facebook.soloader.SoLoader;
import com.majinnaibu.monstercards.data.MonsterRepository;

public class MonsterCardsApplication extends Application {

    private AppDatabase m_db;
    private MonsterRepository m_monsterLibraryRepository;

    public MonsterRepository getMonsterRepository() {
        return m_monsterLibraryRepository;
    }

    public static MonsterCardsApplication getInstance(Context context) {
        return (MonsterCardsApplication) context.getApplicationContext();
    }

    public MonsterCardsApplication() {
    }


    // Called when the application is starting, before any other application objects have been created.
    // Overriding this method is totally optional!
    @Override
    public void onCreate() {
        super.onCreate();
        // Required initialization logic here!
        SoLoader.init(this, false);

        if (BuildConfig.DEBUG && FlipperUtils.shouldEnableFlipper(this)) {
            final FlipperClient client = AndroidFlipperClient.getInstance(this);
            client.addPlugin(new InspectorFlipperPlugin(this, DescriptorMapping.withDefaults()));
            client.addPlugin(new DatabasesFlipperPlugin(this));
            client.addPlugin(NavigationFlipperPlugin.getInstance());
            client.start();
        }

        m_db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "monsters")
                .addMigrations(MIGRATION_1_2)
                .fallbackToDestructiveMigrationOnDowngrade()
                .build();
        m_monsterLibraryRepository = new MonsterRepository(m_db);
    }

    // Called by the system when the device configuration changes while your component is running.
    // Overriding this method is totally optional!
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    // This is called when the overall system is running low on memory,
    // and would like actively running processes to tighten their belts.
    // Overriding this method is totally optional!
    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // rename table monster to monsters
            database.execSQL("ALTER TABLE monster RENAME TO monsters");
            // create the fts view
            database.execSQL("CREATE VIRTUAL TABLE IF NOT EXISTS `monsters_fts` USING FTS4(`name` TEXT, `size` TEXT, `type` TEXT, `subtype` TEXT, `alignment` TEXT, content=`monsters`)");
            // build the initial full text search index
            database.execSQL("INSERT INTO monsters_fts(monsters_fts) VALUES('rebuild')");

        }
    };
}
