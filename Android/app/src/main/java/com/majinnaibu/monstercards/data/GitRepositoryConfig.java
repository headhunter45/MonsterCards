package com.majinnaibu.monstercards.data;

import com.majinnaibu.monstercards.models.GitRepositorySource;

import java.util.ArrayList;
import java.util.List;

public class GitRepositoryConfig {
    public static final List<GitRepositorySource> SOURCES = new ArrayList<>();

    static {
        SOURCES.add(new GitRepositorySource(
            "pf2e_foundry",
            "Pathfinder 2e Foundry VTT",
            "foundryvtt",
            "https://github.com/foundryvtt/pf2e",
            "https://github.com/foundryvtt/pf2e/archive/refs/heads/master.zip",
            "com.majinnaibu.monstercards.importers.Pf2eImporter",
            ".json"
        ));
    }
}
