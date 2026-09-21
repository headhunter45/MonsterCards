package com.majinnaibu.monstercards.models;

import androidx.room.Embedded;
import androidx.room.Ignore;
import androidx.room.Relation;

public class DashboardMonsterWithMonster {
    @Embedded
    public DashboardMonster dashboardEntry;

    @Relation(
            parentColumn = "monster_id",
            entityColumn = "id"
    )
    public Monster monster;

    public DashboardMonsterWithMonster() {
    }

    @Ignore
    public DashboardMonsterWithMonster(DashboardMonster dashboardEntry, Monster monster) {
        this.dashboardEntry = dashboardEntry;
        this.monster = monster;
    }
}
