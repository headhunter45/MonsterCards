package com.majinnaibu.monstercards.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity(
        tableName = "dashboard_monsters",
        foreignKeys = @ForeignKey(
                entity = Monster.class,
                parentColumns = "id",
                childColumns = "monster_id",
                onDelete = ForeignKey.CASCADE
        ),
        indices = @Index(value = {"monster_id"})
)
public class DashboardMonster {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    @ColumnInfo(name = "monster_id")
    public UUID monsterId;

    @ColumnInfo(name = "ordinal", defaultValue = "0")
    public int ordinal;

    public DashboardMonster() {
        this.monsterId = UUID.fromString("00000000-0000-0000-0000-000000000000");
        this.ordinal = 0;
    }

    public DashboardMonster(@NonNull UUID monsterId, int ordinal) {
        this.monsterId = monsterId;
        this.ordinal = ordinal;
    }
}
