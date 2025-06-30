package com.majinnaibu.monstercards.data.converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

public class SetOfStringConverter {

    @TypeConverter
    public static String fromSetOfString(Set<String> strings) {
        Gson gson = new Gson();
        return gson.toJson(strings);
    }

    @TypeConverter
    public static Set<String> setOfStringFromString(String string) {
        Gson gson = new Gson();
        Type setType = new TypeToken<HashSet<String>>() {
        }.getType();
        return gson.fromJson(string, setType);
    }
}
