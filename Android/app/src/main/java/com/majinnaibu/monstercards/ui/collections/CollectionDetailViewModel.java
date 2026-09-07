package com.majinnaibu.monstercards.ui.collections;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.majinnaibu.monstercards.models.Collection;
import com.majinnaibu.monstercards.models.Monster;

import java.util.ArrayList;
import java.util.List;

public class CollectionDetailViewModel extends ViewModel {
    private final MutableLiveData<Collection> mCollection = new MutableLiveData<>();
    private final MutableLiveData<List<Monster>> mMonsters = new MutableLiveData<>(new ArrayList<>());

    public CollectionDetailViewModel() {
    }

    public LiveData<Collection> getCollection() {
        return mCollection;
    }

    public void setCollection(Collection collection) {
        mCollection.setValue(collection);
    }

    public LiveData<List<Monster>> getMonsters() {
        return mMonsters;
    }

    public void setMonsters(List<Monster> monsters) {
        mMonsters.setValue(monsters);
    }
}
