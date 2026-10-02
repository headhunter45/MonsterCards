package com.majinnaibu.monstercards.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
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

    @Query("SELECT * FROM monsters WHERE (:searchText = '' OR name LIKE '%' || :searchText || '%' OR type LIKE '%' || :searchText || '%' OR subtype LIKE '%' || :searchText || '%' OR source_label LIKE '%' || :searchText || '%' OR book_source LIKE '%' || :searchText || '%') ORDER BY name ASC LIMIT 200")
    Flowable<List<Monster>> searchMonsters(String searchText);

    @Insert
    Completable insertAll(Monster... monsters);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable save(Monster... monsters);

    @Delete
    Completable delete(Monster monster);

    @Query("DELETE FROM monsters")
    Completable deleteAllMonsters();
}
