package com.majinnaibu.monstercards.importers;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.majinnaibu.monstercards.models.BinderExport;

public class BinderImporter implements EntityImporter<BinderExport> {

    private static final Gson GSON = new Gson();

    @Override
    public boolean canImport(@NonNull String input) {
        try {
            JsonElement el = JsonParser.parseString(input);
            if (el.isJsonObject()) {
                JsonObject obj = el.getAsJsonObject();
                if (obj.has("$schema")) {
                    String schemaVal = obj.get("$schema").getAsString();
                    if (schemaVal.contains("binder.schema.json")) {
                        return true;
                    }
                }
                return obj.has("collections") && obj.get("collections").isJsonArray();
            }
        } catch (Exception ignored) {
        }
        return false;
    }

    @NonNull
    @Override
    public BinderExport parse(@NonNull String input) throws Exception {
        BinderExport binder = GSON.fromJson(input, BinderExport.class);
        if (binder == null || binder.collections == null) {
            throw new IllegalArgumentException("Failed to deserialize Binder JSON");
        }
        return binder;
    }
}
