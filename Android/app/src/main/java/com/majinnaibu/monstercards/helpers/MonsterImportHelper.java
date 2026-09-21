package com.majinnaibu.monstercards.helpers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.majinnaibu.monstercards.importers.DnDBeyondImporter;
import com.majinnaibu.monstercards.importers.EntityImporter;
import com.majinnaibu.monstercards.importers.MonsterJsonImporter;
import com.majinnaibu.monstercards.importers.Open5eImporter;
import com.majinnaibu.monstercards.importers.TetraCubeMonsterImporter;
import com.majinnaibu.monstercards.models.Monster;
import com.majinnaibu.monstercards.utils.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MonsterImportHelper {
    private static final List<EntityImporter<Monster>> IMPORTERS = new ArrayList<>();

    static {
        IMPORTERS.add(new TetraCubeMonsterImporter());
        IMPORTERS.add(new MonsterJsonImporter());
        IMPORTERS.add(new Open5eImporter());
        IMPORTERS.add(new DnDBeyondImporter());
    }

    @NonNull
    public static Monster fromJSON(String json) {
        return fromJSON(json, null);
    }

    @NonNull
    public static Monster fromJSON(String json, @Nullable String fileName) {
        if (json == null || json.trim().isEmpty()) {
            throw new IllegalArgumentException("JSON payload is null or empty");
        }

        // 1. Check for explicit $schema reference matching monster-card or tetracube schema
        try {
            JsonElement el = JsonParser.parseString(json);
            if (el.isJsonObject()) {
                JsonObject obj = el.getAsJsonObject();
                if (obj.has("$schema")) {
                    String schemaVal = obj.get("$schema").getAsString();
                    if (schemaVal.contains("monster-card.schema.json")) {
                        return new MonsterJsonImporter().parse(json);
                    } else if (schemaVal.contains("tetracube-monster.schema.json")) {
                        return new TetraCubeMonsterImporter().parse(json);
                    }
                }
            }
        } catch (Exception e) {
            Logger.logError("Error checking $schema reference tag", e);
        }

        // 2. If filename ends in .monster, try TetraCube importer first
        String lowerFileName = fileName != null ? fileName.toLowerCase(Locale.ROOT) : "";
        boolean isMonsterFile = lowerFileName.endsWith(".monster") || lowerFileName.endsWith(".monster.txt");
        if (isMonsterFile) {
            TetraCubeMonsterImporter tetraImporter = new TetraCubeMonsterImporter();
            if (tetraImporter.canImport(json)) {
                try {
                    return tetraImporter.parse(json);
                } catch (Exception e) {
                    Logger.logError("TetraCube importer failed for .monster file: " + fileName, e);
                }
            }
        }

        // 3. Fallback to current importer detection chain
        for (EntityImporter<Monster> importer : IMPORTERS) {
            if (importer.canImport(json)) {
                try {
                    return importer.parse(json);
                } catch (Exception e) {
                    Logger.logError("Importer " + importer.getClass().getSimpleName() + " failed", e);
                }
            }
        }
        throw new IllegalArgumentException("No importer found capable of parsing the given JSON payload.");
    }
}
