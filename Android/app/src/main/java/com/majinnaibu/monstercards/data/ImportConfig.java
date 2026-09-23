package com.majinnaibu.monstercards.data;

import com.majinnaibu.monstercards.models.ImportSource;

import java.util.ArrayList;
import java.util.List;

public class ImportConfig {
    public static final List<ImportSource> SOURCES = new ArrayList<>();

    static {
        SOURCES.add(new ImportSource(
            "open5e",
            "Open5e.com System Reference Document",
            "Open5e",
            "https://open5e.com",
            ImportSource.ImportType.OPEN5E_API,
            null,
            null,
            null,
            null
        ));
        
        SOURCES.add(new ImportSource(
            "pf2e_foundry",
            "Pathfinder 2e Foundry VTT",
            "foundryvtt",
            "https://github.com/foundryvtt/pf2e",
            ImportSource.ImportType.GIT_ARCHIVE,
            "https://github.com/foundryvtt/pf2e/archive/refs/heads/master.zip",
            "com.majinnaibu.monstercards.importers.Pf2eImporter",
            ".json",
            "packs/pf2e"
        ));
        
        SOURCES.add(new ImportSource(
            "sf2e_foundry",
            "Starfinder 2e Foundry VTT",
            "foundryvtt",
            "https://github.com/foundryvtt/pf2e",
            ImportSource.ImportType.GIT_ARCHIVE,
            "https://github.com/foundryvtt/pf2e/archive/refs/heads/master.zip",
            "com.majinnaibu.monstercards.importers.Pf2eImporter",
            ".json",
            "packs/sf2e"
        ));
    }
}
