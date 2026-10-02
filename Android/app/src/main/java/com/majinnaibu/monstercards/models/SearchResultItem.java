package com.majinnaibu.monstercards.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SearchResultItem {
    public enum Type {
        MONSTER,
        COLLECTION,
        REFERENCE_MONSTER
    }

    @NonNull
    public final Type type;

    @Nullable
    public final Monster monster;

    @Nullable
    public final Collection collection;

    @Nullable
    public final ReferenceMonster referenceMonster;

    public SearchResultItem(@NonNull Monster monster) {
        this.type = Type.MONSTER;
        this.monster = monster;
        this.collection = null;
        this.referenceMonster = null;
    }

    public SearchResultItem(@NonNull Collection collection) {
        this.type = Type.COLLECTION;
        this.monster = null;
        this.collection = collection;
        this.referenceMonster = null;
    }

    public SearchResultItem(@NonNull ReferenceMonster referenceMonster) {
        this.type = Type.REFERENCE_MONSTER;
        this.monster = null;
        this.collection = null;
        this.referenceMonster = referenceMonster;
    }
}
