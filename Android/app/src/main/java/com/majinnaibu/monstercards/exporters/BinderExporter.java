package com.majinnaibu.monstercards.exporters;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.majinnaibu.monstercards.models.BinderExport;
import com.majinnaibu.monstercards.models.Monster;

import java.util.Collections;
import java.util.List;

public class BinderExporter {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    @NonNull
    public String exportBinder(String collectionName, List<Monster> monsters) {
        BinderExport export = new BinderExport();
        export.schemaVersion = 1;

        if (monsters != null) {
            for (Monster monster : monsters) {
                monster.schemaVersion = 1;
            }
        }

        BinderExport.CollectionExport col = new BinderExport.CollectionExport(
                collectionName != null ? collectionName : "",
                monsters
        );
        export.collections = Collections.singletonList(col);
        return GSON.toJson(export);
    }
}
