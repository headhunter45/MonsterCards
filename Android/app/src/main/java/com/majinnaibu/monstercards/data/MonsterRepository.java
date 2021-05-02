package com.majinnaibu.monstercards.data;

import androidx.annotation.NonNull;

import com.majinnaibu.monstercards.AppDatabase;
import com.majinnaibu.monstercards.models.Monster;

import java.util.List;
import java.util.UUID;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MonsterRepository {

    private AppDatabase m_db;

    public MonsterRepository(@NonNull AppDatabase db) {
        m_db = db;
    }

    public Flowable<List<Monster>> getMonsters() {

        return m_db.monsterDAO()
                .getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Flowable<List<Monster>> searchMonsters(String searchText) {
        return m_db.monsterDAO()
                .search(searchText)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Flowable<Monster> getMonster(UUID monsterId) {
        return m_db.monsterDAO()
                .loadAllByIds(new String[]{monsterId.toString()})
                .map(
                        monsters -> {
                            if (monsters.size() > 0) {
                                return monsters.get(0);
                            } else {
                                return null;
                            }
                        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable addMonster(Monster monster) {
        Completable result = m_db.monsterDAO().insertAll(monster);
        result.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        return result;
    }

    public Completable deleteMonster(Monster monster) {
        Completable result = m_db.monsterDAO().delete(monster);
        result.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        return result;
    }

}
