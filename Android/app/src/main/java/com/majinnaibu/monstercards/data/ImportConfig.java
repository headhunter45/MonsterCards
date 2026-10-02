package com.majinnaibu.monstercards.data;

import com.majinnaibu.monstercards.data.enums.GameSystem;
import com.majinnaibu.monstercards.models.ImportSource;

import java.util.ArrayList;
import java.util.List;

public class ImportConfig {
    public static final List<ImportSource> SOURCES = new ArrayList<>();

    static {
        SOURCES.add(new ImportSource(
            "open5e",
            "Open5e.com SRD 5.1 Monsters",
            "Open5e Community",
            "https://open5e.com",
            ImportSource.ImportType.OPEN5E_API,
            GameSystem.DND_5E,
            "SRD 5.1",
            "Open5e Bestiary",
            "Full System Reference Document 5.1 monster collection from Open5e API.",
            "~8 MB",
            null,
            null,
            null,
            null
        ));

        SOURCES.add(new ImportSource(
            "pf2e_foundry",
            "Pathfinder 2e (Foundry VTT)",
            "Foundry VTT PF2e Developers",
            "https://github.com/foundryvtt/pf2e",
            ImportSource.ImportType.GIT_ARCHIVE,
            GameSystem.PF_2E,
            "Bestiary",
            "Foundry VTT PF2e",
            "Complete Pathfinder 2nd Edition community bestiary data extract.",
            "~25 MB",
            "https://github.com/foundryvtt/pf2e/archive/refs/heads/v14-dev.zip",
            "com.majinnaibu.monstercards.importers.Pf2eImporter",
            ".json",
            "pf2e-14-dev/packs/pf2e"
        ));

        SOURCES.add(new ImportSource(
            "sf2e_foundry",
            "Starfinder 2e (Foundry VTT)",
            "Foundry VTT PF2e Developers",
            "https://github.com/foundryvtt/pf2e",
            ImportSource.ImportType.GIT_ARCHIVE,
            GameSystem.SF_2E,
            "Alien Archive",
            "Foundry VTT SF2e",
            "Starfinder 2nd Edition playtest creature and NPC compendium.",
            "~12 MB",
            "https://github.com/foundryvtt/pf2e/archive/refs/heads/v14-dev.zip",
            "com.majinnaibu.monstercards.importers.Pf2eImporter",
            ".json",
            "pf2e-14-dev/packs/sf2e"
        ));
    }
}
