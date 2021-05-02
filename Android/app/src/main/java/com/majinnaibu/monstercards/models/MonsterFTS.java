package com.majinnaibu.monstercards.models;

import androidx.room.Entity;
import androidx.room.Fts4;

@Entity(tableName = "monsters_fts")
@Fts4(contentEntity = Monster.class)
public class MonsterFTS {
    public String name;
    public String size;
    public String type;
    public String subtype;
    public String alignment;
}
