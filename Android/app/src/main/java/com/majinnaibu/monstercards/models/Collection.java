package com.majinnaibu.monstercards.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Objects;
import java.util.UUID;

@Entity(tableName = "collections")
public class Collection {

    @PrimaryKey
    @NonNull
    public UUID id;

    @NonNull
    @ColumnInfo(defaultValue = "")
    public String name;

    @NonNull
    @ColumnInfo(defaultValue = "")
    public String description;

    public Collection() {
        this.id = UUID.randomUUID();
        this.name = "";
        this.description = "";
    }

    @Ignore
    public Collection(@NonNull String name, @NonNull String description) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.description = description;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Collection that = (Collection) obj;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description);
    }
}
