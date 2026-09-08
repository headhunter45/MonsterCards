package com.majinnaibu.monstercards.importers;

import androidx.annotation.NonNull;

public interface EntityImporter<T> {
    /**
     * Determines whether the given input string or payload can be handled by this importer.
     */
    boolean canImport(@NonNull String input);

    /**
     * Parses the raw input string into internal domain memory objects (e.g., Monster).
     */
    @NonNull
    T parse(@NonNull String input) throws Exception;
}
