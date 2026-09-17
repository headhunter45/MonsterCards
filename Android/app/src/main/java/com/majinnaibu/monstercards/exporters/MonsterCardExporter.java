package com.majinnaibu.monstercards.exporters;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.majinnaibu.monstercards.models.Monster;

public class MonsterCardExporter {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    @NonNull
    public String exportCard(@NonNull Monster monster) {
        monster.schemaVersion = 1;
        return GSON.toJson(monster);
    }
}
