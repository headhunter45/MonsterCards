package com.majinnaibu.monstercards;

import android.app.Application;
import android.content.res.Configuration;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.majinnaibu.monstercards.data.MonsterRepository;
import com.majinnaibu.monstercards.init.FlipperInitializer;

public class MonsterCardsApplication extends Application {

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
    private static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // Add the senses column
            database.execSQL("ALTER TABLE monsters ADD COLUMN 'senses' TEXT DEFAULT '[]'");
            database.execSQL("CREATE TABLE new_monsters (`id` TEXT NOT NULL, `name` TEXT NOT NULL DEFAULT '', `size` TEXT NOT NULL DEFAULT '', `type` TEXT NOT NULL DEFAULT '', `subtype` TEXT NOT NULL DEFAULT '', `alignment` TEXT NOT NULL DEFAULT '', `strength_score` INTEGER NOT NULL DEFAULT 10, `strength_saving_throw_advantage` TEXT DEFAULT 'none', `strength_saving_throw_proficiency` TEXT DEFAULT 'none', `dexterity_score` INTEGER NOT NULL DEFAULT 10, `dexterity_saving_throw_advantage` TEXT DEFAULT 'none', `dexterity_saving_throw_proficiency` TEXT DEFAULT 'none', `constitution_score` INTEGER NOT NULL DEFAULT 10, `constitution_saving_throw_advantage` TEXT DEFAULT 'none', `constitution_saving_throw_proficiency` TEXT DEFAULT 'none', `intelligence_score` INTEGER NOT NULL DEFAULT 10, `intelligence_saving_throw_advantage` TEXT DEFAULT 'none', `intelligence_saving_throw_proficiency` TEXT DEFAULT 'none', `wisdom_score` INTEGER NOT NULL DEFAULT 10, `wisdom_saving_throw_advantage` TEXT DEFAULT 'none', `wisdom_saving_throw_proficiency` TEXT DEFAULT 'none', `charisma_score` INTEGER NOT NULL DEFAULT 10, `charisma_saving_throw_advantage` TEXT DEFAULT 'none', `charisma_saving_throw_proficiency` TEXT DEFAULT 'none', `armor_type` TEXT DEFAULT 'none', `shield_bonus` INTEGER NOT NULL DEFAULT 0, `natural_armor_bonus` INTEGER NOT NULL DEFAULT 0, `other_armor_description` TEXT DEFAULT '', `hit_dice` INTEGER NOT NULL DEFAULT 1, `has_custom_hit_points` INTEGER NOT NULL, `custom_hit_points_description` TEXT DEFAULT '', `walk_speed` INTEGER NOT NULL DEFAULT 0, `burrow_speed` INTEGER NOT NULL DEFAULT 0, `climb_speed` INTEGER NOT NULL DEFAULT 0, `fly_speed` INTEGER NOT NULL DEFAULT 0, `can_hover` INTEGER NOT NULL DEFAULT false, `swim_speed` INTEGER NOT NULL DEFAULT 0, `has_custom_speed` INTEGER NOT NULL DEFAULT false, `custom_speed_description` TEXT, `challenge_rating` TEXT DEFAULT '1', `custom_challenge_rating_description` TEXT DEFAULT '', `custom_proficiency_bonus` INTEGER NOT NULL DEFAULT 0, `telepathy_range` INTEGER NOT NULL DEFAULT 0, `understands_but_description` TEXT DEFAULT '', `senses` TEXT DEFAULT '[]', `skills` TEXT DEFAULT '[]', `damage_immunities` TEXT DEFAULT '[]', `damage_resistances` TEXT DEFAULT '[]', `damage_vulnerabilities` TEXT DEFAULT '[]', `condition_immunities` TEXT DEFAULT '[]', `languages` TEXT DEFAULT '[]', `abilities` TEXT DEFAULT '[]', `actions` TEXT DEFAULT '[]', `reactions` TEXT DEFAULT '[]', `lair_actions` TEXT DEFAULT '[]', `legendary_actions` TEXT DEFAULT '[]', `regional_actions` TEXT DEFAULT '[]', PRIMARY KEY(`id`))");
            database.execSQL("INSERT INTO new_monsters(id, name, size, type, subtype, alignment, strength_score, strength_saving_throw_advantage, strength_saving_throw_proficiency, dexterity_score, dexterity_saving_throw_advantage, dexterity_saving_throw_proficiency, constitution_score, constitution_saving_throw_advantage, constitution_saving_throw_proficiency, intelligence_score, intelligence_saving_throw_advantage, intelligence_saving_throw_proficiency, wisdom_score, wisdom_saving_throw_advantage, wisdom_saving_throw_proficiency, charisma_score, charisma_saving_throw_advantage, charisma_saving_throw_proficiency, armor_type, shield_bonus, natural_armor_bonus, other_armor_description, hit_dice, has_custom_hit_points, custom_hit_points_description, walk_speed, burrow_speed, climb_speed, fly_speed, can_hover, swim_speed, has_custom_speed, custom_speed_description, challenge_rating, custom_challenge_rating_description, custom_proficiency_bonus, telepathy_range, understands_but_description, senses, skills, damage_immunities, damage_resistances, damage_vulnerabilities, condition_immunities, languages, abilities, actions, reactions, lair_actions, legendary_actions, regional_actions) SELECT id, name, size, type, subtype, alignment, strength_score, strength_saving_throw_advantage, strength_saving_throw_proficiency, dexterity_score, dexterity_saving_throw_advantage, dexterity_saving_throw_proficiency, constitution_score, constitution_saving_throw_advantage, constitution_saving_throw_proficiency, intelligence_score, intelligence_saving_throw_advantage, intelligence_saving_throw_proficiency, wisdom_score, wisdom_saving_throw_advantage, wisdom_saving_throw_proficiency, charisma_score, charisma_saving_throw_advantage, charisma_saving_throw_proficiency, armor_type, shield_bonus, natural_armor_bonus, other_armor_description, hit_dice, has_custom_hit_points, custom_hit_points_description, walk_speed, burrow_speed, climb_speed, fly_speed, can_hover, swim_speed, has_custom_speed, custom_speed_description, challenge_rating, custom_challenge_rating_description, custom_proficiency_bonus, telepathy_range, understands_but_description, senses, skills, damage_immunities, damage_resistances, damage_vulnerabilities, condition_immunities, languages, abilities, actions, reactions, lair_actions, legendary_actions, regional_actions FROM monsters");
            database.execSQL("DROP TABLE monsters");
            database.execSQL("ALTER TABLE new_monsters RENAME TO monsters");
        }
    };
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

        //                .fallbackToDestructiveMigration()
        AppDatabase m_db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "monsters")
                .addMigrations(MIGRATION_1_2)
                .addMigrations(MIGRATION_2_3)
                .fallbackToDestructiveMigrationOnDowngrade()
//                .fallbackToDestructiveMigration()
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
}
