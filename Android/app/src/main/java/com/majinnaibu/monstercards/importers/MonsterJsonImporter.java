package com.majinnaibu.monstercards.importers;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.majinnaibu.monstercards.models.Monster;

public class MonsterJsonImporter implements EntityImporter<Monster> {

    private static final Gson GSON = new Gson();

    @Override
    public boolean canImport(@NonNull String input) {
        try {
            JsonElement el = JsonParser.parseString(input);
            if (el.isJsonObject()) {
                JsonObject obj = el.getAsJsonObject();
                return obj.has("name") && (obj.has("strengthScore") || obj.has("hitDice") || obj.has("size"));
            }
        } catch (Exception ignored) {
        }
        return false;
    }

    @NonNull
    @Override
    public Monster parse(@NonNull String input) throws Exception {
        Monster monster = GSON.fromJson(input, Monster.class);
        if (monster == null) {
            throw new IllegalArgumentException("Failed to deserialize Monster JSON");
        }
        return monster;
    }
}
