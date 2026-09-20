package com.majinnaibu.monstercards.ui.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.majinnaibu.monstercards.models.DashboardMonsterWithMonster;

import java.util.ArrayList;
import java.util.List;

public class DashboardViewModel extends ViewModel {
    private final MutableLiveData<List<DashboardMonsterWithMonster>> mMonsters;

    public DashboardViewModel() {
        mMonsters = new MutableLiveData<>(new ArrayList<>());
    }

    public LiveData<List<DashboardMonsterWithMonster>> getMonsters() {
        return mMonsters;
    }

    public void setMonsters(List<DashboardMonsterWithMonster> monsters) {
        mMonsters.setValue(monsters);
    }
}
