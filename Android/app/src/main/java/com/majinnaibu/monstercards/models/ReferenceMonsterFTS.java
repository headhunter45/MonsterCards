package com.majinnaibu.monstercards.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Fts4;

@Entity(tableName = "reference_monsters_fts")
@Fts4(contentEntity = ReferenceMonster.class)
public class ReferenceMonsterFTS {
    public String name;
    public String size;
    public String type;
    public String subtype;
    public String alignment;
    @ColumnInfo(name = "source_label")
    public String sourceLabel;
    @ColumnInfo(name = "book_source")
    public String bookSource;
}
