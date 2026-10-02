package com.majinnaibu.monstercards.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.majinnaibu.monstercards.models.ReferenceMonster;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface ReferenceMonsterDAO {
    @Query("SELECT * FROM reference_monsters")
    Flowable<List<ReferenceMonster>> getAll();

    @Query("SELECT * FROM reference_monsters WHERE source_id = :sourceId")
    Flowable<List<ReferenceMonster>> getBySourceId(String sourceId);

    @Query("SELECT * FROM reference_monsters WHERE id = :id LIMIT 1")
    Flowable<ReferenceMonster> getById(String id);

    @Query("SELECT * FROM reference_monsters WHERE (:searchText = '' OR name LIKE '%' || :searchText || '%' OR type LIKE '%' || :searchText || '%' OR subtype LIKE '%' || :searchText || '%' OR source_label LIKE '%' || :searchText || '%' OR book_source LIKE '%' || :searchText || '%') ORDER BY name ASC LIMIT 200")
    Flowable<List<ReferenceMonster>> searchMonsters(String searchText);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAll(List<ReferenceMonster> monsters);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllSync(List<ReferenceMonster> monsters);

    @Query("DELETE FROM reference_monsters WHERE source_id = :sourceId")
    Completable deleteBySourceId(String sourceId);

    @Query("DELETE FROM reference_monsters WHERE source_id = :sourceId")
    void deleteBySourceIdSync(String sourceId);

    @Transaction
    default void replaceSourceMonstersSync(String sourceId, List<ReferenceMonster> monsters) {
        deleteBySourceIdSync(sourceId);
        if (monsters != null && !monsters.isEmpty()) {
            insertAllSync(monsters);
        }
    }

    @Query("SELECT COUNT(*) FROM reference_monsters WHERE source_id = :sourceId")
    int countBySourceId(String sourceId);

    @Query("SELECT COUNT(*) FROM reference_monsters")
    int countAll();

    @Query("DELETE FROM reference_monsters")
    Completable deleteAll();
}
