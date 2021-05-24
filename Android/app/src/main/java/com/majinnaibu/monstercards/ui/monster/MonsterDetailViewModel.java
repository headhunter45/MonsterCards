package com.majinnaibu.monstercards.ui.monster;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.majinnaibu.monstercards.models.Monster;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MonsterDetailViewModel extends ViewModel {

    public MonsterDetailViewModel() {
        mMonster = null;
        mAbilities = new MutableLiveData<>();
        mAbilities.setValue(new ArrayList<String>());
        mActions = new MutableLiveData<>();
        mActions.setValue(new ArrayList<String>());
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
        mMonsterId = new MutableLiveData<>();
        mMonsterId.setValue(UUID.fromString("00000000-0000-0000-0000-000000000000"));
    }

    private MutableLiveData<List<String>> mAbilities;
    public LiveData<List<String>> getAbilities() {
        return mAbilities;
    }
    private MutableLiveData<List<String>> mActions;
    public LiveData<List<String>> getActions() {
        return mActions;
    }
    private MutableLiveData<String> mArmorClass;
    public LiveData<String> getArmorClass() {
        return mArmorClass;
    }
    private MutableLiveData<String> mChallenge;
    public LiveData<String> getChallenge() {
        return mChallenge;
    }
    private MutableLiveData<String> mCharisma;
    public LiveData<String> getCharisma() {
        return mCharisma;
    }
    private MutableLiveData<String> mConditionImmunities;
    public LiveData<String> getConditionImmunities() {
        return mConditionImmunities;
    }
    private MutableLiveData<String> mConstitution;
    public LiveData<String> getConstitution() {
        return mConstitution;
    }
    private MutableLiveData<String> mDamageResistances;
    public LiveData<String> getDamageResistances() {
        return mDamageResistances;
    }
    private MutableLiveData<String> mDamageImmunities;
    public LiveData<String> getDamageImmunities() {
        return mDamageImmunities;
    }
    private MutableLiveData<String> mDamageVulnerabilities;
    public LiveData<String> getDamageVulnerabilities() {
        return mDamageVulnerabilities;
    }
    private MutableLiveData<String> mDexterity;
    public LiveData<String> getDexterity() {
        return mDexterity;
    }
    private MutableLiveData<String> mHitPoints;
    public LiveData<String> getHitPoints() {
        return mHitPoints;
    }
    private MutableLiveData<String> mIntelligence;
    public LiveData<String> getIntelligence() {
        return mIntelligence;
    }
    private MutableLiveData<String> mLanguages;
    public LiveData<String> getLanguages() {
        return mLanguages;
    }
    private MutableLiveData<String> mMeta;
    public LiveData<String> getMeta() {
        return mMeta;
    }
    private MutableLiveData<String> mName;
    public LiveData<String> getName() {
        return mName;
    }
    private MutableLiveData<String> mSavingThrows;
    public LiveData<String> getSavingThrows() {
        return mSavingThrows;
    }
    private MutableLiveData<String> mSenses;
    public LiveData<String> getSenses() {
        return mSenses;
    }
    private MutableLiveData<String> mSkills;
    public LiveData<String> getSkills() {
        return mSkills;
    }
    private MutableLiveData<String> mSpeed;
    public LiveData<String> getSpeed() {
        return mSpeed;
    }
    private MutableLiveData<String> mStrength;
    public LiveData<String> getStrength() {
        return mStrength;
    }
    private MutableLiveData<String> mWisdom;
    public LiveData<String> getWisdom() {
        return mWisdom;
    }

    private final MutableLiveData<UUID> mMonsterId;

    public LiveData<UUID> getId() {
        return mMonsterId;
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