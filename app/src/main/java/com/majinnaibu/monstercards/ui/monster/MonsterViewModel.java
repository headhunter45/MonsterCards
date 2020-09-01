package com.majinnaibu.monstercards.ui.monster;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.majinnaibu.monstercards.models.Monster;

public class MonsterViewModel extends ViewModel {

    public MonsterViewModel() {
        mMonster = null;
        mName = new MutableLiveData<>();
        mName.setValue("");
        mMeta = new MutableLiveData<>();
        mMeta.setValue("");
        mArmorClass = new MutableLiveData<>();
        mArmorClass.setValue("");

    }

    private MutableLiveData<String> mName;
    public LiveData<String> getName() {
        return mName;
    }
    private MutableLiveData<String> mMeta;
    public LiveData<String> getMeta() {
        return mMeta;
    }
    private MutableLiveData<String> mArmorClass;
    public LiveData<String> getArmorClass() {
        return mArmorClass;
    }

    private Monster mMonster;
    public void setMonster(Monster monster) {
        mMonster = monster;
        mName.setValue(mMonster.getName());
        mMeta.setValue(mMonster.getMeta());
        mArmorClass.setValue(mMonster.getArmorClass());
    }
}