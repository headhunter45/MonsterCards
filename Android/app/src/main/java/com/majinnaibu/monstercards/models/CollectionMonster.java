package com.majinnaibu.monstercards.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity(
        tableName = "collection_monsters",
        foreignKeys = {
                @ForeignKey(
                        entity = Collection.class,
                        parentColumns = "id",
                        childColumns = "collection_id",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = Monster.class,
                        parentColumns = "id",
                        childColumns = "monster_id",
                        onDelete = ForeignKey.CASCADE
                )
        },
        indices = {
                @Index(value = {"collection_id"}),
                @Index(value = {"monster_id"})
        }
)
public class CollectionMonster {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    @ColumnInfo(name = "collection_id")
    public UUID collectionId;

    @NonNull
    @ColumnInfo(name = "monster_id")
    public UUID monsterId;

    @ColumnInfo(name = "ordinal", defaultValue = "0")
    public int ordinal;

    public CollectionMonster() {
        this.collectionId = UUID.fromString("00000000-0000-0000-0000-000000000000");
        this.monsterId = UUID.fromString("00000000-0000-0000-0000-000000000000");
        this.ordinal = 0;
    }

    @Ignore
    public CollectionMonster(@NonNull UUID collectionId, @NonNull UUID monsterId, int ordinal) {
        this.collectionId = collectionId;
        this.monsterId = monsterId;
        this.ordinal = ordinal;
    }
}
