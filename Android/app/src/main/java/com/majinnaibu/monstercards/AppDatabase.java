package com.majinnaibu.monstercards;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.majinnaibu.monstercards.converters.UUIDConverter;
import com.majinnaibu.monstercards.data.MonsterDAO;
import com.majinnaibu.monstercards.models.Monster;

@Database(entities = {Monster.class}, version=1)
@TypeConverters({UUIDConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract MonsterDAO monsterDAO();
}
