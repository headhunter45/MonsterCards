package com.majinnaibu.monstercards;

import android.app.Application;
import android.content.res.Configuration;

import com.majinnaibu.monstercards.data.MonsterRepository;
import com.majinnaibu.monstercards.init.FlipperInitializer;

public class MonsterCardsApplication extends Application {


    private MonsterRepository m_monsterLibraryRepository;


    public MonsterCardsApplication() {
    }

    public MonsterRepository getMonsterRepository() {
        return m_monsterLibraryRepository;
    }

    // Called when the application is starting, before any other application objects have been created.
    // Overriding this method is totally optional!
    @Override
    public void onCreate() {
        super.onCreate();
        // Required initialization logic here!

        FlipperInitializer.init(this);
        AppDatabase mDB = AppDatabase.getInstance(this);
        m_monsterLibraryRepository = new MonsterRepository(mDB);
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
}
