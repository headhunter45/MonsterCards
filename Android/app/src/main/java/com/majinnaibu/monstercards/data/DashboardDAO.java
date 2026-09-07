package com.majinnaibu.monstercards.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.majinnaibu.monstercards.models.DashboardMonster;
import com.majinnaibu.monstercards.models.Monster;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface DashboardDAO {

    @Query("SELECT monsters.* FROM monsters INNER JOIN dashboard_monsters ON monsters.id = dashboard_monsters.monster_id ORDER BY dashboard_monsters.ordinal ASC, dashboard_monsters.id ASC")
    Flowable<List<Monster>> getDashboardMonsters();

    @Query("SELECT * FROM dashboard_monsters ORDER BY ordinal ASC, id ASC")
    Flowable<List<DashboardMonster>> getDashboardMonsterEntries();

    @Insert
    Completable addMonsterToDashboard(DashboardMonster... dashboardMonsters);

    @Query("DELETE FROM dashboard_monsters WHERE id = :id")
    Completable removeDashboardMonsterById(long id);

    @Query("DELETE FROM dashboard_monsters WHERE monster_id = :monsterId")
    Completable removeMonsterFromDashboard(String monsterId);

    @Query("DELETE FROM dashboard_monsters")
    Completable clearDashboard();

    @Update
    Completable updateDashboardMonsters(List<DashboardMonster> dashboardMonsters);
}
