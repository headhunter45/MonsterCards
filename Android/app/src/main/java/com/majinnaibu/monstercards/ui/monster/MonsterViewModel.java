package com.majinnaibu.monstercards.ui.monster;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.majinnaibu.monstercards.models.Monster;

import java.util.ArrayList;
import java.util.List;

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
        mSavingThrows = new MutableLiveData<>();
        mSavingThrows.setValue("");
        mSkills = new MutableLiveData<>();
        mSkills.setValue("");
        mDamageVulnerabilities = new MutableLiveData<>();
        mDamageVulnerabilities.setValue("");
        mDamageResistances = new MutableLiveData<>();
        mDamageResistances.setValue("");
        mDamageImmunities = new MutableLiveData<>();
        mDamageImmunities.setValue("");
        mConditionImmunities = new MutableLiveData<>();
        mConditionImmunities.setValue("");
        mSenses = new MutableLiveData<>();
        mSenses.setValue("");
        mLanguages = new MutableLiveData<>();
        mLanguages.setValue("");
        mChallenge = new MutableLiveData<>();
        mChallenge.setValue("");
        mAbilities = new MutableLiveData<>();
        mAbilities.setValue(new ArrayList<String>());
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
    private MutableLiveData<String> mSavingThrows;
    public LiveData<String> getSavingThrows() {
        return mSavingThrows;
    }
    private MutableLiveData<String> mSkills;
    public LiveData<String> getSkills() {
        return mSkills;
    }
    private MutableLiveData<String> mDamageVulnerabilities;
    public LiveData<String> getDamageVulnerabilities() {
        return mDamageVulnerabilities;
    }
    private MutableLiveData<String> mDamageResistances;
    public LiveData<String> getDamageResistances() {
        return mDamageResistances;
    }
    private MutableLiveData<String> mDamageImmunities;
    public LiveData<String> getDamageImmunities() {
        return mDamageImmunities;
    }
    private MutableLiveData<String> mConditionImmunities;
    public LiveData<String> getConditionImmunities() {
        return mConditionImmunities;
    }
    private MutableLiveData<String> mSenses;
    public LiveData<String> getSenses() {
        return mSenses;
    }
    private MutableLiveData<String> mLanguages;
    public LiveData<String> getLanguages() {
        return mLanguages;
    }
    private MutableLiveData<String> mChallenge;
    public LiveData<String> getChallenge() {
        return mChallenge;
    }
    private MutableLiveData<List<String>> mAbilities;
    public LiveData<List<String>> getAbilities() {
        return mAbilities;
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
        mSavingThrows.setValue(monster.getSavingThrowsDescription());
        mSkills.setValue(monster.getSkillsDescription());
        mDamageVulnerabilities.setValue(mMonster.getDamageVulnerabilitiesDescription());
        mDamageResistances.setValue(mMonster.getDamageResistancesDescription());
        mDamageImmunities.setValue(mMonster.getDamageImmunitiesDescription());
        mConditionImmunities.setValue(mMonster.getConditionImmunitiesDescription());
        mSenses.setValue(monster.getSensesDescription());
        mLanguages.setValue(mMonster.getLanguagesDescription());
        mChallenge.setValue(mMonster.getChallengeRatingDescription());
        mAbilities.setValue(mMonster.getAbilityDescriptions());
    }
}