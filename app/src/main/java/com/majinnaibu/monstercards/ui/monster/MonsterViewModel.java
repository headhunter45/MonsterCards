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
        mHitPoints = new MutableLiveData<>();
        mHitPoints.setValue("");
        mSpeed = new MutableLiveData<>();
        mSpeed.setValue("");
        mStrength = new MutableLiveData<>();
        mStrength.setValue("");
        mDexterity = new MutableLiveData<>();
        mDexterity.setValue("");
        mConstitution = new MutableLiveData<>();
        mConstitution.setValue("");
        mIntelligence = new MutableLiveData<>();
        mIntelligence.setValue("");
        mWisdom = new MutableLiveData<>();
        mWisdom.setValue("");
        mCharisma = new MutableLiveData<>();
        mCharisma.setValue("");
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
    private MutableLiveData<String> mHitPoints;
    public LiveData<String> getHitPoints() {
        return mHitPoints;
    }
    private MutableLiveData<String> mSpeed;
    public LiveData<String> getSpeed() {
        return mSpeed;
    }
    private MutableLiveData<String> mStrength;
    public LiveData<String> getStrength() {
        return mStrength;
    }
    private MutableLiveData<String> mDexterity;
    public LiveData<String> getDexterity() {
        return mDexterity;
    }
    private MutableLiveData<String> mConstitution;
    public LiveData<String> getConstitution() {
        return mConstitution;
    }
    private MutableLiveData<String> mIntelligence;
    public LiveData<String> getIntelligence() {
        return mIntelligence;
    }
    private MutableLiveData<String> mWisdom;
    public LiveData<String> getWisdom() {
        return mWisdom;
    }
    private MutableLiveData<String> mCharisma;
    public LiveData<String> getCharisma() {
        return mCharisma;
    }

    private Monster mMonster;
    public void setMonster(Monster monster) {
        mMonster = monster;
        mName.setValue(mMonster.getName());
        mMeta.setValue(mMonster.getMeta());
        mArmorClass.setValue(mMonster.getArmorClass());
        mHitPoints.setValue(mMonster.getHitPoints());
        mSpeed.setValue(mMonster.getSpeedText());
        mStrength.setValue(monster.getStrengthDescription());
        mDexterity.setValue(monster.getDexterityDescription());
        mConstitution.setValue(monster.getConstitutionDescription());
        mIntelligence.setValue(monster.getIntelligenceDescription());
        mWisdom.setValue(monster.getWisdomDescription());
        mCharisma.setValue(monster.getCharismaDescription());
    }
}