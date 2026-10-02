package com.majinnaibu.monstercards.data.converters;

import androidx.annotation.NonNull;
import androidx.room.TypeConverter;

import com.majinnaibu.monstercards.data.enums.GameSystem;

public class GameSystemConverter {

    @TypeConverter
    public static String fromGameSystem(@NonNull GameSystem gameSystem) {
        return gameSystem.name();
    }

    @TypeConverter
    public static GameSystem gameSystemFromString(String value) {
        return GameSystem.fromString(value);
    }
}
