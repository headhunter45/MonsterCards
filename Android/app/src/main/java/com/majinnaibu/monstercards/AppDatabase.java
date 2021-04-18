package com.majinnaibu.monstercards;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.majinnaibu.monstercards.data.MonsterDAO;
import com.majinnaibu.monstercards.data.converters.ArmorTypeConverter;
import com.majinnaibu.monstercards.data.converters.ChallengeRatingConverter;
import com.majinnaibu.monstercards.data.converters.SetOfLanguageConverter;
import com.majinnaibu.monstercards.data.converters.SetOfSavingThrowConverter;
import com.majinnaibu.monstercards.data.converters.SetOfSkillConverter;
import com.majinnaibu.monstercards.data.converters.SetOfStringConverter;
import com.majinnaibu.monstercards.data.converters.SetOfTraitConverter;
import com.majinnaibu.monstercards.data.converters.UUIDConverter;
import com.majinnaibu.monstercards.models.Monster;

@SuppressWarnings("unused")
@Database(entities = {Monster.class}, version = 1)
@TypeConverters({
        ArmorTypeConverter.class,
        ChallengeRatingConverter.class,
        SetOfLanguageConverter.class,
        SetOfSavingThrowConverter.class,
        SetOfSkillConverter.class,
        SetOfStringConverter.class,
        SetOfTraitConverter.class,
        UUIDConverter.class,
})
public abstract class AppDatabase extends RoomDatabase {
    public abstract MonsterDAO monsterDAO();
}
