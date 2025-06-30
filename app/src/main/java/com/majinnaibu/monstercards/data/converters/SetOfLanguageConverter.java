package com.majinnaibu.monstercards.data.converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.majinnaibu.monstercards.models.Language;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

public class SetOfLanguageConverter {

    @TypeConverter
    public static String fromSetOfLanguage(Set<Language> languages) {
        Gson gson = new Gson();
        return gson.toJson(languages);
    }

    @TypeConverter
    public static Set<Language> setOfLanguageFromString(String string) {
        Gson gson = new Gson();
        Type setType = new TypeToken<HashSet<Language>>() {
        }.getType();
        return gson.fromJson(string, setType);
    }
}
