package com.majinnaibu.monstercards.data;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.majinnaibu.monstercards.models.Monster;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface MonsterDAO {
    @Query("SELECT * FROM monster")
    Flowable<List<Monster>> getAll();

    @Query("SELECT * FROM monster WHERE id IN (:monsterIds)")
    Flowable<List<Monster>> loadAllByIds(String[] monsterIds);

    @Query("SELECT * FROM monster WHERE name LIKE :name LIMIT 1")
    Flowable<Monster> findByName(String name);

    @Insert
    Completable insertAll(Monster... monsters);

    @Delete
    Completable delete(Monster monster);
}
