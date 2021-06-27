package com.majinnaibu.monstercards.data.converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.majinnaibu.monstercards.models.Trait;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ListOfTraitsConverter {
    @TypeConverter
    public static String fromListOfTraits(List<Trait> traits) {
        Gson gson = new Gson();
        return gson.toJson(traits);
    }

    @TypeConverter
    public static List<Trait> listOfTraitsFromString(String string) {
        Gson gson = new Gson();
        Type setType = new TypeToken<ArrayList<Trait>>() {
        }.getType();
        return gson.fromJson(string, setType);
    }
}
