package com.majinnaibu.monstercards.ui.library;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.majinnaibu.monstercards.AppDatabase;
import com.majinnaibu.monstercards.models.Monster;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.observers.DisposableCompletableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subscribers.DisposableSubscriber;

public class LibraryViewModel extends AndroidViewModel {
    private final AppDatabase mDB;
    private final MutableLiveData<List<Monster>> mMonsters;

    public LibraryViewModel(Application application) {
        super(application);
        mDB = AppDatabase.getInstance(application);
        mMonsters = new MutableLiveData<>(new ArrayList<>());
        mDB.monsterDAO()
                .getAll()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
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

    public void removeMonster(int position) {
        Monster monster = mMonsters.getValue().get(position);
        mDB.monsterDAO()
                .delete(monster)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }
}
