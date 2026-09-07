package com.majinnaibu.monstercards.models;

import androidx.room.Embedded;

public class CollectionWithCount {
    @Embedded
    public Collection collection;

    public int monsterCount;

    public CollectionWithCount() {
        this.collection = new Collection();
        this.monsterCount = 0;
    }

    public CollectionWithCount(Collection collection, int monsterCount) {
        this.collection = collection;
        this.monsterCount = monsterCount;
    }
}
