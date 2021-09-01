package com.majinnaibu.monstercards.ui.search;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.majinnaibu.monstercards.AppDatabase;
import com.majinnaibu.monstercards.helpers.StringHelper;
import com.majinnaibu.monstercards.models.Monster;
import com.majinnaibu.monstercards.utils.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subscribers.DisposableSubscriber;

public class SearchViewModel extends AndroidViewModel {
    private final MutableLiveData<List<Monster>> mAllMonsters;
    private final MediatorLiveData<List<Monster>> mFilteredMonsters;
    private final MutableLiveData<String> mFilterText;
    private final AppDatabase mDB;

    public SearchViewModel(Application application) {
        super(application);
        mDB = AppDatabase.getInstance(application);
        mAllMonsters = new MutableLiveData<>(new ArrayList<>());
        mFilterText = new MutableLiveData<>("");
        mFilteredMonsters = new MediatorLiveData<>();
        mFilteredMonsters.addSource(
                mAllMonsters,
                allMonsters -> mFilteredMonsters.setValue(
                        filterMonsters(allMonsters, mFilterText.getValue())));
        mFilteredMonsters.addSource(
                mFilterText,
                filterText -> mFilteredMonsters.setValue(
                        filterMonsters(mAllMonsters.getValue(), filterText)));
        mDB.monsterDAO()
                .getAll()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DisposableSubscriber<List<Monster>>() {
                    @Override
                    public void onNext(List<Monster> monsters) {
                        mAllMonsters.setValue(monsters);
                    }

                    @Override
                    public void onError(Throwable t) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    private boolean monsterMatchesFilter(Monster monster, String filterText) {
        if (StringHelper.isNullOrEmpty(filterText)) {
            return true;
        }

        if (StringHelper.containsCaseInsensitive(monster.name, filterText)) {
            return true;
        }

        if (StringHelper.containsCaseInsensitive(monster.size, filterText)) {
            return true;
        }

        if (StringHelper.containsCaseInsensitive(monster.type, filterText)) {
            return true;
        }

        if (StringHelper.containsCaseInsensitive(monster.subtype, filterText)) {
            return true;
        }

        if (StringHelper.containsCaseInsensitive(monster.alignment, filterText)) {
            return true;
        }

        return false;
    }

    private List<Monster> filterMonsters(List<Monster> allMonsters, String filterText) {
        ArrayList<Monster> filteredMonsters = new ArrayList<>();
        filterText = filterText.toLowerCase(Locale.ROOT);
        if (allMonsters != null) {
            for (Monster monster : allMonsters) {
                // TODO: do the filtering like the iOS app does.
                Logger.logUnimplementedFeature("do the filtering like the iOS app does");
                // TODO: consider splitting search text into words and if each word appears in any of these fields return true e.g, "large demon" would match large in size and demon in type.
                // TODO: add tags and search by tags
                // TODO: add a display of what fields matched on each item in the results
                // TODO: make the criteria configurable from this screen
                // TODO: find a way to add challenge rating as a search criteria
                if (monsterMatchesFilter(monster, filterText)) {
                    filteredMonsters.add(monster);
                }
            }
        }
        return filteredMonsters;
    }

    public LiveData<List<Monster>> getMatchedMonsters() {
        return mFilteredMonsters;
    }

    public void setFilterText(String filterText) {
        mFilterText.setValue(filterText);
    }
}
