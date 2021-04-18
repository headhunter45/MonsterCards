package com.majinnaibu.monstercards.data.converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.majinnaibu.monstercards.models.Trait;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

public class SetOfTraitConverter {
    @TypeConverter
    public static String fromSetOfTrait(Set<Trait> traits) {
        Gson gson = new Gson();
        return gson.toJson(traits);
    }

    @TypeConverter
    public static Set<Trait> setOfTraitFromString(String string) {
        Gson gson = new Gson();
        Type setType = new TypeToken<HashSet<Trait>>() {
        }.getType();
        return gson.fromJson(string, setType);
    }
}
