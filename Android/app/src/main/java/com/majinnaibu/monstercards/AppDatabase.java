package com.majinnaibu.monstercards;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.majinnaibu.monstercards.data.MonsterDAO;
import com.majinnaibu.monstercards.data.converters.ArmorTypeConverter;
import com.majinnaibu.monstercards.data.converters.ChallengeRatingConverter;
import com.majinnaibu.monstercards.data.converters.ListOfTraitsConverter;
import com.majinnaibu.monstercards.data.converters.SetOfLanguageConverter;
import com.majinnaibu.monstercards.data.converters.SetOfSkillConverter;
import com.majinnaibu.monstercards.data.converters.SetOfStringConverter;
import com.majinnaibu.monstercards.data.converters.UUIDConverter;
import com.majinnaibu.monstercards.models.Monster;
import com.majinnaibu.monstercards.models.MonsterFTS;

@Database(entities = {Monster.class, MonsterFTS.class}, version = 3)
@TypeConverters({
        ArmorTypeConverter.class,
        ChallengeRatingConverter.class,
        ListOfTraitsConverter.class,
        SetOfLanguageConverter.class,
        SetOfSkillConverter.class,
        SetOfStringConverter.class,
        UUIDConverter.class,
})
public abstract class AppDatabase extends RoomDatabase {
    public abstract MonsterDAO monsterDAO();
}
