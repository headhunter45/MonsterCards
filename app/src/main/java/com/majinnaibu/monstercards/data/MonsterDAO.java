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
    @Query("SELECT * FROM monsters")
    Flowable<List<Monster>> getAll();

    @Query("SELECT * FROM monsters WHERE id IN (:monsterIds)")
    Flowable<List<Monster>> loadAllByIds(String[] monsterIds);

    @Query("SELECT * FROM monsters WHERE name LIKE :name LIMIT 1")
    Flowable<Monster> findByName(String name);

    @Query("SELECT monsters.* FROM monsters JOIN monsters_fts ON monsters.oid = monsters_fts.docid WHERE monsters_fts MATCH :searchText")
    Flowable<List<Monster>> search(String searchText);

    @Insert
    Completable insertAll(Monster... monsters);

    @Delete
    Completable delete(Monster monster);
}
