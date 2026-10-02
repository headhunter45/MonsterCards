package com.majinnaibu.monstercards.data.converters;

import androidx.annotation.NonNull;
import androidx.room.TypeConverter;

import com.majinnaibu.monstercards.data.enums.MonsterOrigin;

public class MonsterOriginConverter {

    @TypeConverter
    public static String fromMonsterOrigin(@NonNull MonsterOrigin origin) {
        return origin.getValue();
    }

    @TypeConverter
    public static MonsterOrigin monsterOriginFromString(String value) {
        return MonsterOrigin.fromString(value);
    }
}
