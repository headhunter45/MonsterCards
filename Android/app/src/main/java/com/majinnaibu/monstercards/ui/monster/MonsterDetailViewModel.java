package com.majinnaibu.monstercards.ui.monster;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.majinnaibu.monstercards.models.Monster;

import java.util.ArrayList;
import java.util.List;

public class MonsterDetailViewModel extends ViewModel {

    public MonsterDetailViewModel() {
        mMonster = null;
        mAbilities = new MutableLiveData<>();
        mAbilities.setValue(new ArrayList<>());
        mActions = new MutableLiveData<>();
        mActions.setValue(new ArrayList<>());
        mArmorClass = new MutableLiveData<>();
        mArmorClass.setValue("");
        mChallenge = new MutableLiveData<>();
        mChallenge.setValue("");
        mCharisma = new MutableLiveData<>();
        mCharisma.setValue("");
        mConditionImmunities = new MutableLiveData<>();
        mConditionImmunities.setValue("");
        mConstitution = new MutableLiveData<>();
        mConstitution.setValue("");
        mDamageImmunities = new MutableLiveData<>();
        mDamageImmunities.setValue("");
        mDamageResistances = new MutableLiveData<>();
        mDamageResistances.setValue("");
        mDamageVulnerabilities = new MutableLiveData<>();
        mDamageVulnerabilities.setValue("");
        mDexterity = new MutableLiveData<>();
        mDexterity.setValue("");
        mHitPoints = new MutableLiveData<>();
        mHitPoints.setValue("");
        mIntelligence = new MutableLiveData<>();
        mIntelligence.setValue("");
        mLanguages = new MutableLiveData<>();
        mLanguages.setValue("");
        mMeta = new MutableLiveData<>();
        mMeta.setValue("");
        mName = new MutableLiveData<>();
        mName.setValue("");
        mSavingThrows = new MutableLiveData<>();
        mSavingThrows.setValue("");
        mSenses = new MutableLiveData<>();
        mSenses.setValue("");
        mSkills = new MutableLiveData<>();
        mSkills.setValue("");
        mSpeed = new MutableLiveData<>();
        mSpeed.setValue("");
        mStrength = new MutableLiveData<>();
        mStrength.setValue("");
        mWisdom = new MutableLiveData<>();
        mWisdom.setValue("");
    }

    private final MutableLiveData<List<String>> mAbilities;

    public LiveData<List<String>> getAbilities() {
        return mAbilities;
    }

    private final MutableLiveData<List<String>> mActions;

    public LiveData<List<String>> getActions() {
        return mActions;
    }

    private final MutableLiveData<String> mArmorClass;

    public LiveData<String> getArmorClass() {
        return mArmorClass;
    }

    private final MutableLiveData<String> mChallenge;

    public LiveData<String> getChallenge() {
        return mChallenge;
    }

    private final MutableLiveData<String> mCharisma;

    public LiveData<String> getCharisma() {
        return mCharisma;
    }

    private final MutableLiveData<String> mConditionImmunities;

    public LiveData<String> getConditionImmunities() {
        return mConditionImmunities;
    }

    private final MutableLiveData<String> mConstitution;

    public LiveData<String> getConstitution() {
        return mConstitution;
    }

    private final MutableLiveData<String> mDamageResistances;

    public LiveData<String> getDamageResistances() {
        return mDamageResistances;
    }

    private final MutableLiveData<String> mDamageImmunities;

    public LiveData<String> getDamageImmunities() {
        return mDamageImmunities;
    }

    private final MutableLiveData<String> mDamageVulnerabilities;

    public LiveData<String> getDamageVulnerabilities() {
        return mDamageVulnerabilities;
    }

    private final MutableLiveData<String> mDexterity;

    public LiveData<String> getDexterity() {
        return mDexterity;
    }

    private final MutableLiveData<String> mHitPoints;

    public LiveData<String> getHitPoints() {
        return mHitPoints;
    }

    private final MutableLiveData<String> mIntelligence;

    public LiveData<String> getIntelligence() {
        return mIntelligence;
    }

    private final MutableLiveData<String> mLanguages;

    public LiveData<String> getLanguages() {
        return mLanguages;
    }

    private final MutableLiveData<String> mMeta;

    public LiveData<String> getMeta() {
        return mMeta;
    }

    private final MutableLiveData<String> mName;

    public LiveData<String> getName() {
        return mName;
    }

    private final MutableLiveData<String> mSavingThrows;

    public LiveData<String> getSavingThrows() {
        return mSavingThrows;
    }

    private final MutableLiveData<String> mSenses;

    public LiveData<String> getSenses() {
        return mSenses;
    }

    private final MutableLiveData<String> mSkills;

    public LiveData<String> getSkills() {
        return mSkills;
    }

    private final MutableLiveData<String> mSpeed;

    public LiveData<String> getSpeed() {
        return mSpeed;
    }

    private final MutableLiveData<String> mStrength;

    public LiveData<String> getStrength() {
        return mStrength;
    }

    private final MutableLiveData<String> mWisdom;

    public LiveData<String> getWisdom() {
        return mWisdom;
    }

    private Monster mMonster;

    public void setMonster(Monster monster) {
        mMonster = monster;

        mAbilities.setValue(mMonster.getAbilityDescriptions());
        mActions.setValue(mMonster.getActionDescriptions());
        mArmorClass.setValue(mMonster.getArmorClass());
        mChallenge.setValue(mMonster.getChallengeRatingDescription());
        mCharisma.setValue(monster.getCharismaDescription());
        mConditionImmunities.setValue(mMonster.getConditionImmunitiesDescription());
        mConstitution.setValue(monster.getConstitutionDescription());
        mDamageImmunities.setValue(mMonster.getDamageImmunitiesDescription());
        mDamageResistances.setValue(mMonster.getDamageResistancesDescription());
        mDamageVulnerabilities.setValue(mMonster.getDamageVulnerabilitiesDescription());
        mDexterity.setValue(monster.getDexterityDescription());
        mHitPoints.setValue(mMonster.getHitPoints());
        mIntelligence.setValue(monster.getIntelligenceDescription());
        mLanguages.setValue(mMonster.getLanguagesDescription());
        mMeta.setValue(mMonster.getMeta());
        mName.setValue(mMonster.name);
        mSavingThrows.setValue(monster.getSavingThrowsDescription());
        mSenses.setValue(monster.getSensesDescription());
        mSkills.setValue(monster.getSkillsDescription());
        mSpeed.setValue(mMonster.getSpeedText());
        mStrength.setValue(monster.getStrengthDescription());
        mWisdom.setValue(monster.getWisdomDescription());
    }
}