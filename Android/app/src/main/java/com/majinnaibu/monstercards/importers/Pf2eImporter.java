package com.majinnaibu.monstercards.importers;

import androidx.annotation.NonNull;

import com.majinnaibu.monstercards.models.Monster;

public class Pf2eImporter implements EntityImporter<Monster> {
    @Override
    public boolean canImport(@NonNull String input) {
        // TODO: Implement parsing check for PF2e JSON format
        return false;
    }

    @NonNull
    @Override
    public Monster parse(@NonNull String input) throws Exception {
        // TODO: Implement actual conversion to Monster cards model
        return new Monster();
    }
}
