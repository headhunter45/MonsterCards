package com.majinnaibu.monstercards.ui.dashboard;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.majinnaibu.monstercards.AppDatabase;
import com.majinnaibu.monstercards.models.Monster;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subscribers.DisposableSubscriber;

public class DashboardViewModel extends AndroidViewModel {
    private final AppDatabase mDB;
    private final MutableLiveData<List<Monster>> mMonsters;

    public DashboardViewModel(Application application) {
        super(application);
        mDB = AppDatabase.getInstance(application);
        mMonsters = new MutableLiveData<>(new ArrayList<>());
        mDB.monsterDAO()
                .getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSubscriber<List<Monster>>() {
                    @Override
                    public void onNext(List<Monster> monsters) {
                        mMonsters.setValue(monsters);
                    }

                    @Override
                    public void onError(Throwable t) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public LiveData<List<Monster>> getMonsters() {
        return mMonsters;
    }
}
