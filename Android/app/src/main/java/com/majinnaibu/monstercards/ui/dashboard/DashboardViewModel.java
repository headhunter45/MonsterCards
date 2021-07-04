package com.majinnaibu.monstercards.ui.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.majinnaibu.monstercards.models.Monster;

import java.util.ArrayList;
import java.util.List;

public class DashboardViewModel extends ViewModel {
    private final MutableLiveData<List<Monster>> mMonsters;

    public DashboardViewModel() {
        mMonsters = new MutableLiveData<>(new ArrayList<>());
    }

    public LiveData<List<Monster>> getMonsters() {
        return mMonsters;
    }

    public void setMonsters(List<Monster> monsters) {
        mMonsters.setValue(monsters);
    }
}
