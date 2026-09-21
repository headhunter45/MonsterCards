package com.majinnaibu.monstercards.exporters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.majinnaibu.monstercards.models.BinderExport;
import com.majinnaibu.monstercards.models.Collection;
import com.majinnaibu.monstercards.models.Monster;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class BinderExporter {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    @NonNull
    public String exportBinder(String collectionName, List<Monster> monsters) {
        return exportBinder(new Collection(collectionName != null ? collectionName : "", ""), monsters, null);
    }

    @NonNull
    public String exportBinder(@Nullable Collection collection, List<Monster> monsters) {
        return exportBinder(collection, monsters, null);
    }

    @NonNull
    public String exportBinder(@Nullable Collection collection, @Nullable List<Monster> monsters, @Nullable List<Monster> dashboardMonsters) {
        BinderExport export = new BinderExport();
        export.schema = "https://majinnaibu.com/schemas/binder.schema.json";
        export.schemaVersion = 1;

        if (monsters != null) {
            for (Monster monster : monsters) {
                monster.schema = "https://majinnaibu.com/schemas/monster-card.schema.json";
                monster.schemaVersion = 1;
            }
        }

        if (dashboardMonsters != null && !dashboardMonsters.isEmpty()) {
            for (Monster monster : dashboardMonsters) {
                monster.schema = "https://majinnaibu.com/schemas/monster-card.schema.json";
                monster.schemaVersion = 1;
            }
            export.dashboard = dashboardMonsters;
        }

        if (collection != null) {
            String colId = collection.id.toString();
            String colName = collection.name;
            String colDesc = collection.description;

            BinderExport.CollectionExport col = new BinderExport.CollectionExport(
                    colId,
                    colName,
                    colDesc,
                    monsters
            );
            export.collections = Collections.singletonList(col);
        }
        return GSON.toJson(export);
    }

    @NonNull
    public String exportFullBackup(@Nullable List<BinderExport.CollectionExport> collections, @Nullable List<Monster> dashboardMonsters) {
        BinderExport export = new BinderExport();
        export.schema = "https://majinnaibu.com/schemas/binder.schema.json";
        export.schemaVersion = 1;

        if (collections != null) {
            for (BinderExport.CollectionExport col : collections) {
                if (col.cards != null) {
                    for (Monster m : col.cards) {
                        m.schema = "https://majinnaibu.com/schemas/monster-card.schema.json";
                        m.schemaVersion = 1;
                    }
                }
            }
            export.collections = collections;
        }

        if (dashboardMonsters != null && !dashboardMonsters.isEmpty()) {
            for (Monster m : dashboardMonsters) {
                m.schema = "https://majinnaibu.com/schemas/monster-card.schema.json";
                m.schemaVersion = 1;
            }
            export.dashboard = dashboardMonsters;
        }

        return GSON.toJson(export);
    }
}
