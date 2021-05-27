package com.majinnaibu.monstercards.ui.monster;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.majinnaibu.monstercards.models.Monster;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MonsterDetailViewModel extends ViewModel {

    private final MutableLiveData<List<String>> mAbilities;
    private final MutableLiveData<List<String>> mActions;
    private final MutableLiveData<String> mArmorClass;
    private final MutableLiveData<String> mChallenge;
    private final MutableLiveData<String> mCharisma;
    private final MutableLiveData<String> mConditionImmunities;
    private final MutableLiveData<String> mConstitution;
    private final MutableLiveData<String> mDamageResistances;
    private final MutableLiveData<String> mDamageImmunities;
    private final MutableLiveData<String> mDamageVulnerabilities;
    private final MutableLiveData<String> mDexterity;
    private final MutableLiveData<String> mHitPoints;
    private final MutableLiveData<String> mIntelligence;
    private final MutableLiveData<String> mLanguages;
    private final MutableLiveData<String> mMeta;
    private final MutableLiveData<String> mName;
    private final MutableLiveData<String> mSavingThrows;
    private final MutableLiveData<String> mSenses;
    private final MutableLiveData<String> mSkills;
    private final MutableLiveData<String> mSpeed;
    private final MutableLiveData<String> mStrength;
    private final MutableLiveData<String> mWisdom;
    private final MutableLiveData<UUID> mMonsterId;
    private Monster mMonster;

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
        mMonsterId = new MutableLiveData<>();
        mMonsterId.setValue(UUID.fromString("00000000-0000-0000-0000-000000000000"));
    }

    public LiveData<List<String>> getAbilities() {
        return mAbilities;
    }

    public LiveData<List<String>> getActions() {
        return mActions;
    }

    public LiveData<String> getArmorClass() {
        return mArmorClass;
    }

    public LiveData<String> getChallenge() {
        return mChallenge;
    }

    public LiveData<String> getCharisma() {
        return mCharisma;
    }

    public LiveData<String> getConditionImmunities() {
        return mConditionImmunities;
    }

    public LiveData<String> getConstitution() {
        return mConstitution;
    }

    public LiveData<String> getDamageResistances() {
        return mDamageResistances;
    }

    public LiveData<String> getDamageImmunities() {
        return mDamageImmunities;
    }

    public LiveData<String> getDamageVulnerabilities() {
        return mDamageVulnerabilities;
    }

    public LiveData<String> getDexterity() {
        return mDexterity;
    }

    public LiveData<String> getHitPoints() {
        return mHitPoints;
    }

    public LiveData<String> getIntelligence() {
        return mIntelligence;
    }

    public LiveData<String> getLanguages() {
        return mLanguages;
    }

    public LiveData<String> getMeta() {
        return mMeta;
    }

    public LiveData<String> getName() {
        return mName;
    }

    public LiveData<String> getSavingThrows() {
        return mSavingThrows;
    }

    public LiveData<String> getSenses() {
        return mSenses;
    }

    public LiveData<String> getSkills() {
        return mSkills;
    }

    public LiveData<String> getSpeed() {
        return mSpeed;
    }

    public LiveData<String> getStrength() {
        return mStrength;
    }

    public LiveData<String> getWisdom() {
        return mWisdom;
    }

    public LiveData<UUID> getId() {
        return mMonsterId;
    }

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
        mMonsterId.setValue(mMonster.id);
        mName.setValue(mMonster.name);
        mSavingThrows.setValue(monster.getSavingThrowsDescription());
        mSenses.setValue(monster.getSensesDescription());
        mSkills.setValue(monster.getSkillsDescription());
        mSpeed.setValue(mMonster.getSpeedText());
        mStrength.setValue(monster.getStrengthDescription());
        mWisdom.setValue(monster.getWisdomDescription());
    }
}