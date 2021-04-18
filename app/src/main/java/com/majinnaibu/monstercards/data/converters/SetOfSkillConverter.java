package com.majinnaibu.monstercards.data.converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.majinnaibu.monstercards.models.Skill;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

public class SetOfSkillConverter {

    @TypeConverter
    public static String fromSetOfSkill(Set<Skill> skills) {
        Gson gson = new Gson();
        return gson.toJson(skills);
    }

    @TypeConverter
    public static Set<Skill> setOfSkillFromString(String string) {
        Gson gson = new Gson();
        Type setType = new TypeToken<HashSet<Skill>>() {
        }.getType();
        return gson.fromJson(string, setType);
    }
}
