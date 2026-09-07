package com.majinnaibu.monstercards.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.majinnaibu.monstercards.models.Collection;
import com.majinnaibu.monstercards.models.CollectionMonster;
import com.majinnaibu.monstercards.models.CollectionWithCount;
import com.majinnaibu.monstercards.models.Monster;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface CollectionDAO {

    @Query("SELECT * FROM collections")
    Flowable<List<Collection>> getAll();

    @Query("SELECT collections.*, COUNT(collection_monsters.id) as monsterCount FROM collections LEFT JOIN collection_monsters ON collections.id = collection_monsters.collection_id GROUP BY collections.id")
    Flowable<List<CollectionWithCount>> getCollectionsWithCount();

    @Query("SELECT * FROM collections WHERE id = :id LIMIT 1")
    Flowable<Collection> getById(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable save(Collection... collections);

    @Delete
    Completable delete(Collection collection);

    @Query("SELECT monsters.* FROM monsters INNER JOIN collection_monsters ON monsters.id = collection_monsters.monster_id WHERE collection_monsters.collection_id = :collectionId ORDER BY collection_monsters.ordinal ASC, collection_monsters.id ASC")
    Flowable<List<Monster>> getMonstersForCollection(String collectionId);

    @Query("SELECT * FROM collection_monsters WHERE collection_id = :collectionId ORDER BY ordinal ASC, id ASC")
    Flowable<List<CollectionMonster>> getCollectionMonstersForCollection(String collectionId);

    @Insert
    Completable addMonsterToCollection(CollectionMonster... collectionMonsters);

    @Query("DELETE FROM collection_monsters WHERE collection_id = :collectionId AND monster_id = :monsterId")
    Completable removeMonsterFromCollection(String collectionId, String monsterId);

    @Query("DELETE FROM collection_monsters WHERE id = :id")
    Completable removeCollectionMonsterById(long id);

    @Update
    Completable updateCollectionMonsters(List<CollectionMonster> collectionMonsters);
}
