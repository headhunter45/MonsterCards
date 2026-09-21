package com.majinnaibu.monstercards.models;

import androidx.room.Embedded;
import androidx.room.Ignore;

public class CollectionWithCount {
    @Embedded
    public Collection collection;

    public int monsterCount;

    public CollectionWithCount() {
        this.collection = new Collection();
        this.monsterCount = 0;
    }

    @Ignore
    public CollectionWithCount(Collection collection, int monsterCount) {
        this.collection = collection;
        this.monsterCount = monsterCount;
    }
}
