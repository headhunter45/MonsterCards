package com.majinnaibu.monstercards.data;

import androidx.annotation.NonNull;

import com.majinnaibu.monstercards.AppDatabase;
import com.majinnaibu.monstercards.helpers.StringHelper;
import com.majinnaibu.monstercards.models.BinderExport;
import com.majinnaibu.monstercards.models.Collection;
import com.majinnaibu.monstercards.models.CollectionMonster;
import com.majinnaibu.monstercards.models.CollectionWithCount;
import com.majinnaibu.monstercards.models.DashboardMonster;
import com.majinnaibu.monstercards.models.DashboardMonsterWithMonster;
import com.majinnaibu.monstercards.models.Monster;
import com.majinnaibu.monstercards.models.SearchResultItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
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

    public Flowable<List<com.majinnaibu.monstercards.models.DashboardMonsterWithMonster>> getDashboardMonstersWithMonster() {
        return m_db.dashboardDAO()
                .getDashboardMonstersWithMonster()
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

    public Completable clearAllData() {
        return Completable.fromAction(() -> {
            m_db.dashboardDAO().clearDashboard().blockingAwait();
            m_db.collectionDAO().deleteAllCollectionMonsters().blockingAwait();
            m_db.collectionDAO().deleteAllCollections().blockingAwait();
            m_db.monsterDAO().deleteAllMonsters().blockingAwait();
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Completable removeAllCollections() {
        return Completable.fromAction(() -> {
            m_db.collectionDAO().deleteAllCollectionMonsters().blockingAwait();
            m_db.collectionDAO().deleteAllCollections().blockingAwait();
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Completable removeAllMonstersFromCollection(@NonNull UUID collectionId) {
        Completable result = m_db.collectionDAO().removeAllMonstersFromCollection(collectionId.toString());
        result.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        return result;
    }

    public Single<String> exportCollections() {
        return m_db.collectionDAO().getAll().first(new ArrayList<>())
                .map(collections -> {
                    List<BinderExport.CollectionExport> colExports = new ArrayList<>();
                    for (Collection col : collections) {
                        List<Monster> colMonsters = m_db.collectionDAO().getMonstersForCollection(col.id.toString())
                                .first(new ArrayList<>()).blockingGet();
                        colExports.add(new BinderExport.CollectionExport(
                                col.id.toString(),
                                col.name,
                                col.description,
                                colMonsters
                        ));
                    }
                    return new com.majinnaibu.monstercards.exporters.BinderExporter().exportFullBackup(colExports, null);
                })
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Single<String> exportEverything() {
        return Single.zip(
                m_db.collectionDAO().getAll().first(new ArrayList<>()),
                m_db.monsterDAO().getAll().first(new ArrayList<>()),
                m_db.dashboardDAO().getDashboardMonsters().first(new ArrayList<>()),
                (collections, allMonsters, dashboardMonsters) -> {
                    List<BinderExport.CollectionExport> colExports = new ArrayList<>();
                    Set<UUID> monstersInCollections = new HashSet<>();

                    for (Collection col : collections) {
                        List<Monster> colMonsters = m_db.collectionDAO().getMonstersForCollection(col.id.toString())
                                .first(new ArrayList<>()).blockingGet();
                        for (Monster m : colMonsters) {
                            monstersInCollections.add(m.id);
                        }
                        colExports.add(new BinderExport.CollectionExport(
                                col.id.toString(),
                                col.name,
                                col.description,
                                colMonsters
                        ));
                    }

                    List<Monster> uncategorized = new ArrayList<>();
                    for (Monster m : allMonsters) {
                        if (!monstersInCollections.contains(m.id)) {
                            uncategorized.add(m);
                        }
                    }

                    if (!uncategorized.isEmpty() || colExports.isEmpty()) {
                        colExports.add(new BinderExport.CollectionExport(
                                UUID.randomUUID().toString(),
                                colExports.isEmpty() ? "All Monsters" : "Uncategorized",
                                "",
                                colExports.isEmpty() ? allMonsters : uncategorized
                        ));
                    }

                    return new com.majinnaibu.monstercards.exporters.BinderExporter().exportFullBackup(colExports, dashboardMonsters);
                }
        ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Completable updateDashboardMonsters(List<DashboardMonster> items) {
        Completable result = m_db.dashboardDAO().updateDashboardMonsters(items);
        result.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        return result;
    }

    public Completable importBinder(@NonNull BinderExport binder) {
        return Completable.fromAction(() -> {
            // 1. Process Collections & Cards in Binder
            if (binder.collections != null && !binder.collections.isEmpty()) {
                List<Collection> existingCols = m_db.collectionDAO().getAll().first(new ArrayList<>()).blockingGet();

                for (BinderExport.CollectionExport colExport : binder.collections) {
                    if (colExport == null) {
                        continue;
                    }

                    String colName = colExport.name != null ? colExport.name.trim() : "";
                    String colDesc = colExport.description != null ? colExport.description.trim() : "";

                    UUID colExportId = null;
                    if (colExport.id != null && !colExport.id.trim().isEmpty()) {
                        try {
                            colExportId = UUID.fromString(colExport.id.trim());
                        } catch (Exception ignored) {
                        }
                    }

                    Collection targetCollection = null;
                    if (colExportId != null) {
                        for (Collection existing : existingCols) {
                            if (existing.id.equals(colExportId)) {
                                targetCollection = existing;
                                break;
                            }
                        }
                    }
                    if (targetCollection == null && !colName.isEmpty()) {
                        for (Collection existing : existingCols) {
                            if (existing.name != null && existing.name.equalsIgnoreCase(colName)) {
                                targetCollection = existing;
                                break;
                            }
                        }
                    }

                    UUID targetCollectionId;
                    if (targetCollection != null) {
                        targetCollectionId = targetCollection.id;
                        if (!colDesc.isEmpty() && (targetCollection.description == null || targetCollection.description.isEmpty())) {
                            targetCollection.description = colDesc;
                            m_db.collectionDAO().save(targetCollection).blockingAwait();
                        }
                    } else {
                        Collection newCol = new Collection();
                        newCol.id = colExportId != null ? colExportId : UUID.randomUUID();
                        newCol.name = !colName.isEmpty() ? colName : "Imported Collection";
                        newCol.description = colDesc;
                        m_db.collectionDAO().save(newCol).blockingAwait();
                        targetCollectionId = newCol.id;
                        existingCols.add(newCol);
                    }

                    if (colExport.cards != null && !colExport.cards.isEmpty()) {
                        List<Monster> monstersToSave = new ArrayList<>();
                        Map<UUID, Integer> importedCardCounts = new HashMap<>();

                        for (Monster card : colExport.cards) {
                            if (card != null) {
                                if (card.id == null) {
                                    card.id = UUID.randomUUID();
                                }
                                monstersToSave.add(card);
                                importedCardCounts.put(card.id, getCount(importedCardCounts, card.id) + 1);
                            }
                        }

                        if (!monstersToSave.isEmpty()) {
                            m_db.monsterDAO().save(monstersToSave.toArray(new Monster[0])).blockingAwait();

                            List<Monster> existingColMonsters = m_db.collectionDAO().getMonstersForCollection(targetCollectionId.toString())
                                    .first(new ArrayList<>()).blockingGet();
                            Map<UUID, Integer> existingCardCounts = new HashMap<>();
                            for (Monster m : existingColMonsters) {
                                existingCardCounts.put(m.id, getCount(existingCardCounts, m.id) + 1);
                            }

                            int ordinal = existingColMonsters.size();
                            for (Map.Entry<UUID, Integer> entry : importedCardCounts.entrySet()) {
                                UUID monsterId = entry.getKey();
                                int importedCount = entry.getValue();
                                int existingCount = getCount(existingCardCounts, monsterId);

                                if (importedCount > existingCount) {
                                    int extraCopiesNeeded = importedCount - existingCount;
                                    for (int k = 0; k < extraCopiesNeeded; k++) {
                                        m_db.collectionDAO().addMonsterToCollection(new CollectionMonster(targetCollectionId, monsterId, ordinal++)).blockingAwait();
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 2. Process Dashboard entries in Binder if present
            if (binder.dashboard != null && !binder.dashboard.isEmpty()) {
                List<Monster> dashboardMonstersToSave = new ArrayList<>();
                Map<UUID, Integer> importedDashboardCounts = new HashMap<>();

                for (Monster card : binder.dashboard) {
                    if (card != null) {
                        if (card.id == null) {
                            card.id = UUID.randomUUID();
                        }
                        dashboardMonstersToSave.add(card);
                        importedDashboardCounts.put(card.id, getCount(importedDashboardCounts, card.id) + 1);
                    }
                }

                if (!dashboardMonstersToSave.isEmpty()) {
                    m_db.monsterDAO().save(dashboardMonstersToSave.toArray(new Monster[0])).blockingAwait();

                    List<DashboardMonsterWithMonster> existingDashboard = m_db.dashboardDAO().getDashboardMonstersWithMonster()
                            .first(new ArrayList<>()).blockingGet();
                    Map<UUID, Integer> existingDashboardCounts = new HashMap<>();
                    int currentMaxOrdinal = 0;
                    for (DashboardMonsterWithMonster entry : existingDashboard) {
                        if (entry.monster != null) {
                            existingDashboardCounts.put(entry.monster.id, getCount(existingDashboardCounts, entry.monster.id) + 1);
                        }
                        if (entry.dashboardEntry != null && entry.dashboardEntry.ordinal > currentMaxOrdinal) {
                            currentMaxOrdinal = entry.dashboardEntry.ordinal;
                        }
                    }

                    int ordinal = currentMaxOrdinal + 1;
                    List<DashboardMonster> extraEntriesToAdd = new ArrayList<>();

                    for (Map.Entry<UUID, Integer> entry : importedDashboardCounts.entrySet()) {
                        UUID monsterId = entry.getKey();
                        int importedCount = entry.getValue();
                        int existingCount = getCount(existingDashboardCounts, monsterId);

                        if (importedCount > existingCount) {
                            int extraNeeded = importedCount - existingCount;
                            for (int k = 0; k < extraNeeded; k++) {
                                extraEntriesToAdd.add(new DashboardMonster(monsterId, ordinal++));
                            }
                        }
                    }

                    if (!extraEntriesToAdd.isEmpty()) {
                        m_db.dashboardDAO().addMonsterToDashboard(extraEntriesToAdd.toArray(new DashboardMonster[0])).blockingAwait();
                    }
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    private static int getCount(Map<UUID, Integer> map, UUID key) {
        if (map == null || key == null) return 0;
        Integer val = map.get(key);
        return val != null ? val : 0;
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
