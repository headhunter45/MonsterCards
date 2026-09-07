package com.majinnaibu.monstercards.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SearchResultItem {
    public enum Type {
        MONSTER,
        COLLECTION
    }

    @NonNull
    public final Type type;

    @Nullable
    public final Monster monster;

    @Nullable
    public final Collection collection;

    public SearchResultItem(@NonNull Monster monster) {
        this.type = Type.MONSTER;
        this.monster = monster;
        this.collection = null;
    }

    public SearchResultItem(@NonNull Collection collection) {
        this.type = Type.COLLECTION;
        this.monster = null;
        this.collection = collection;
    }
}
