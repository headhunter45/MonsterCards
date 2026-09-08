package com.majinnaibu.monstercards.helpers;

import androidx.annotation.NonNull;

import com.majinnaibu.monstercards.importers.DnDBeyondImporter;
import com.majinnaibu.monstercards.importers.EntityImporter;
import com.majinnaibu.monstercards.importers.MonsterJsonImporter;
import com.majinnaibu.monstercards.importers.Open5eImporter;
import com.majinnaibu.monstercards.importers.TetraCubeMonsterImporter;
import com.majinnaibu.monstercards.models.Monster;

import java.util.ArrayList;
import java.util.List;

public class MonsterImportHelper {
    private static final List<EntityImporter<Monster>> IMPORTERS = new ArrayList<>();

    static {
        IMPORTERS.add(new MonsterJsonImporter());
        IMPORTERS.add(new Open5eImporter());
        IMPORTERS.add(new TetraCubeMonsterImporter());
        IMPORTERS.add(new DnDBeyondImporter());
    }

    @NonNull
    public static Monster fromJSON(String json) {
        if (json != null) {
            for (EntityImporter<Monster> importer : IMPORTERS) {
                if (importer.canImport(json)) {
                    try {
                        return importer.parse(json);
                    } catch (Exception e) {
                        throw new IllegalArgumentException("Failed to parse monster JSON", e);
                    }
                }
            }
        }
        throw new IllegalArgumentException("No importer found capable of parsing the given JSON payload.");
    }
}
