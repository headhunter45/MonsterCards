package com.majinnaibu.monstercards.data.converters;

import androidx.annotation.NonNull;
import androidx.room.TypeConverter;

import java.util.UUID;

public class UUIDConverter {

    @NonNull
    @TypeConverter
    public static String fromUUID(@NonNull UUID uuid) {
        return uuid.toString();
    }

    @TypeConverter
    public static UUID uuidFromString(String string) {
        return UUID.fromString(string);
    }
}
