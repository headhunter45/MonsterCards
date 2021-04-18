package com.majinnaibu.monstercards.data.converters;


import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.majinnaibu.monstercards.models.SavingThrow;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

public class SetOfSavingThrowConverter {

    @TypeConverter
    public static String fromSetOfSavingThrow(Set<SavingThrow> savingThrows) {
        Gson gson = new Gson();
        SavingThrow[] saves = new SavingThrow[savingThrows.size()];
        savingThrows.toArray(saves);
        return gson.toJson(saves);
    }

    @TypeConverter
    public static Set<SavingThrow> setOfSavingThrowFromString(String string) {
        Gson gson = new Gson();
        Type setType = new TypeToken<HashSet<SavingThrow>>() {
        }.getType();
        return gson.fromJson(string, setType);
    }
}
