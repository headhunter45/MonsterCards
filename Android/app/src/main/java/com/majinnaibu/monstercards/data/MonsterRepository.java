package com.majinnaibu.monstercards.data;

import androidx.annotation.NonNull;

import com.majinnaibu.monstercards.AppDatabase;
import com.majinnaibu.monstercards.helpers.StringHelper;
import com.majinnaibu.monstercards.models.BinderExport;
import com.majinnaibu.monstercards.models.Collection;
import com.majinnaibu.monstercards.models.CollectionMonster;
import com.majinnaibu.monstercards.models.CollectionWithCount;
import com.majinnaibu.monstercards.models.DashboardMonster;
import com.majinnaibu.monstercards.models.Monster;
import com.majinnaibu.monstercards.models.SearchResultItem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class MonsterRepository {

    private final AppDatabase m_db;

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
                .getAll()
                .map(monsters -> {
                    ArrayList<Monster> filteredMonsters = new ArrayList<>();
                    for (Monster monster : monsters) {
                        if (Helpers.monsterMatchesSearch(monster, searchText)) {
                            filteredMonsters.add(monster);
                        }
                    }
                    return (List<Monster>) filteredMonsters;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Flowable<List<SearchResultItem>> searchAll(String searchText) {
        Flowable<List<Monster>> monstersFlowable = m_db.monsterDAO()
                .getAll()
                .map(monsters -> {
                    ArrayList<Monster> filteredMonsters = new ArrayList<>();
                    for (Monster monster : monsters) {
                        if (Helpers.monsterMatchesSearch(monster, searchText)) {
                            filteredMonsters.add(monster);
                        }
                    }
                    return (List<Monster>) filteredMonsters;
                });

        Flowable<List<Collection>> collectionsFlowable = m_db.collectionDAO()
                .getAll()
                .map(collections -> {
                    ArrayList<Collection> filteredCollections = new ArrayList<>();
                    for (Collection collection : collections) {
                        if (StringHelper.isNullOrEmpty(searchText) || StringHelper.containsCaseInsensitive(collection.name, searchText)) {
                            filteredCollections.add(collection);
                        }
                    }
                    return (List<Collection>) filteredCollections;
                });

        return Flowable.combineLatest(monstersFlowable, collectionsFlowable, (monsters, collections) -> {
            ArrayList<SearchResultItem> results = new ArrayList<>();
            for (Monster monster : monsters) {
                results.add(new SearchResultItem(monster));
            }
            for (Collection collection : collections) {
                results.add(new SearchResultItem(collection));
            }
            return (List<SearchResultItem>) results;
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Flowable<Monster> getMonster(@NonNull UUID monsterId) {
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

    public Completable saveMonster(Monster monster) {
        Completable result = m_db.monsterDAO().save(monster);
        result.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        return result;
    }

    public Flowable<List<Collection>> getCollections() {
        return m_db.collectionDAO()
                .getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Flowable<List<CollectionWithCount>> getCollectionsWithCount() {
        return m_db.collectionDAO()
                .getCollectionsWithCount()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Flowable<Collection> getCollection(@NonNull UUID collectionId) {
        return m_db.collectionDAO()
                .getById(collectionId.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable saveCollection(Collection collection) {
        Completable result = m_db.collectionDAO().save(collection);
        result.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        return result;
    }

    public Completable deleteCollection(Collection collection) {
        Completable result = m_db.collectionDAO().delete(collection);
        result.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        return result;
    }

    public Flowable<List<Monster>> getMonstersForCollection(@NonNull UUID collectionId) {
        return m_db.collectionDAO()
                .getMonstersForCollection(collectionId.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Flowable<List<CollectionMonster>> getCollectionMonstersForCollection(@NonNull UUID collectionId) {
        return m_db.collectionDAO()
                .getCollectionMonstersForCollection(collectionId.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable addMonsterToCollection(@NonNull UUID collectionId, @NonNull UUID monsterId) {
        return addMonsterToCollection(collectionId, monsterId, 0);
    }

    public Completable addMonsterToCollection(@NonNull UUID collectionId, @NonNull UUID monsterId, int ordinal) {
        CollectionMonster collectionMonster = new CollectionMonster(collectionId, monsterId, ordinal);
        Completable result = m_db.collectionDAO().addMonsterToCollection(collectionMonster);
        result.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        return result;
    }

    public Completable removeMonsterFromCollection(@NonNull UUID collectionId, @NonNull UUID monsterId) {
        Completable result = m_db.collectionDAO().removeMonsterFromCollection(collectionId.toString(), monsterId.toString());
        result.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        return result;
    }

    public Completable removeCollectionMonsterById(long junctionId) {
        Completable result = m_db.collectionDAO().removeCollectionMonsterById(junctionId);
        result.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        return result;
    }

    public Completable updateCollectionMonsters(List<CollectionMonster> collectionMonsters) {
        Completable result = m_db.collectionDAO().updateCollectionMonsters(collectionMonsters);
        result.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        return result;
    }

    public Flowable<List<Monster>> getDashboardMonsters() {
        return m_db.dashboardDAO()
                .getDashboardMonsters()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Flowable<List<DashboardMonster>> getDashboardMonsterEntries() {
        return m_db.dashboardDAO()
                .getDashboardMonsterEntries()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable addMonsterToDashboard(@NonNull UUID monsterId) {
        DashboardMonster entry = new DashboardMonster(monsterId, 0);
        Completable result = m_db.dashboardDAO().addMonsterToDashboard(entry);
        result.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        return result;
    }

    public Completable addCollectionToDashboard(@NonNull UUID collectionId) {
        return m_db.collectionDAO().getMonstersForCollection(collectionId.toString())
                .firstOrError()
                .flatMapCompletable(monsters -> {
                    if (monsters.isEmpty()) {
                        return Completable.complete();
                    }
                    DashboardMonster[] entries = new DashboardMonster[monsters.size()];
                    for (int i = 0; i < monsters.size(); i++) {
                        entries[i] = new DashboardMonster(monsters.get(i).id, i);
                    }
                    return m_db.dashboardDAO().addMonsterToDashboard(entries);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable removeMonsterFromDashboard(@NonNull UUID monsterId) {
        Completable result = m_db.dashboardDAO().removeMonsterFromDashboard(monsterId.toString());
        result.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        return result;
    }

    public Completable removeDashboardMonsterById(long id) {
        Completable result = m_db.dashboardDAO().removeDashboardMonsterById(id);
        result.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        return result;
    }

    public Completable clearDashboard() {
        Completable result = m_db.dashboardDAO().clearDashboard();
        result.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        return result;
    }

    public Completable updateDashboardMonsters(List<DashboardMonster> items) {
        Completable result = m_db.dashboardDAO().updateDashboardMonsters(items);
        result.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        return result;
    }

    public Completable importBinder(@NonNull BinderExport binder) {
        return Completable.fromAction(() -> {
            if (binder.collections == null || binder.collections.isEmpty()) {
                return;
            }

            for (BinderExport.CollectionExport colExport : binder.collections) {
                if (colExport.cards == null || colExport.cards.isEmpty()) {
                    continue;
                }

                List<Monster> monstersToSave = new ArrayList<>();
                for (Monster card : colExport.cards) {
                    if (card.id == null) {
                        card.id = UUID.randomUUID();
                    }
                    monstersToSave.add(card);
                }
                m_db.monsterDAO().save(monstersToSave.toArray(new Monster[0])).blockingAwait();

                String colName = colExport.name != null ? colExport.name.trim() : "";
                if (!colName.isEmpty()) {
                    List<Collection> existingCols = m_db.collectionDAO().getAll().first(new ArrayList<>()).blockingGet();
                    Collection targetCollection = null;
                    for (Collection existing : existingCols) {
                        if (existing.name != null && existing.name.equalsIgnoreCase(colName)) {
                            targetCollection = existing;
                            break;
                        }
                    }

                    UUID targetCollectionId;
                    if (targetCollection != null) {
                        targetCollectionId = targetCollection.id;
                    } else {
                        Collection newCol = new Collection();
                        newCol.id = UUID.randomUUID();
                        newCol.name = colName;
                        m_db.collectionDAO().save(newCol).blockingAwait();
                        targetCollectionId = newCol.id;
                    }

                    List<Monster> colMonsters = m_db.collectionDAO().getMonstersForCollection(targetCollectionId.toString())
                            .first(new ArrayList<>()).blockingGet();
                    Set<UUID> existingMonsterIds = new HashSet<>();
                    for (Monster m : colMonsters) {
                        existingMonsterIds.add(m.id);
                    }

                    int ordinal = colMonsters.size();
                    for (Monster monster : monstersToSave) {
                        if (!existingMonsterIds.contains(monster.id)) {
                            m_db.collectionDAO().addMonsterToCollection(new CollectionMonster(targetCollectionId, monster.id, ordinal++)).blockingAwait();
                            existingMonsterIds.add(monster.id);
                        }
                    }
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    private static class Helpers {
        static boolean monsterMatchesSearch(Monster monster, String searchText) {
            if (StringHelper.isNullOrEmpty(searchText)) {
                return true;
            }

            if (StringHelper.containsCaseInsensitive(monster.name, searchText)) {
                return true;
            }

            if (StringHelper.containsCaseInsensitive(monster.size, searchText)) {
                return true;
            }

            if (StringHelper.containsCaseInsensitive(monster.type, searchText)) {
                return true;
            }

            if (StringHelper.containsCaseInsensitive(monster.subtype, searchText)) {
                return true;
            }

            if (StringHelper.containsCaseInsensitive(monster.alignment, searchText)) {
                return true;
            }

            return false;
        }
    }
}
