package com.majinnaibu.monstercards.ui.monster;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.majinnaibu.monstercards.models.Monster;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MonsterImportViewModel extends ViewModel {
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
    private final MutableLiveData<List<String>> mLairActions;
    private final MutableLiveData<String> mLanguages;
    private final MutableLiveData<List<String>> mLegendaryActions;
    private final MutableLiveData<String> mMeta;
    private final MutableLiveData<String> mName;
    private final MutableLiveData<List<String>> mReactions;
    private final MutableLiveData<List<String>> mRegionalEffects;
    private final MutableLiveData<String> mSavingThrows;
    private final MutableLiveData<String> mSenses;
    private final MutableLiveData<String> mSkills;
    private final MutableLiveData<String> mSpeed;
    private final MutableLiveData<String> mStrength;
    private final MutableLiveData<String> mWisdom;
    private final MutableLiveData<UUID> mMonsterId;
    private Monster mMonster;

    public MonsterImportViewModel() {
        mMonster = null;
        mAbilities = new MutableLiveData<>(new ArrayList<>());
        mActions = new MutableLiveData<>(new ArrayList<>());
        mArmorClass = new MutableLiveData<>("");
        mChallenge = new MutableLiveData<>("");
        mCharisma = new MutableLiveData<>("");
        mConditionImmunities = new MutableLiveData<>("");
        mConstitution = new MutableLiveData<>("");
        mDamageImmunities = new MutableLiveData<>("");
        mDamageResistances = new MutableLiveData<>("");
        mDamageVulnerabilities = new MutableLiveData<>("");
        mDexterity = new MutableLiveData<>("");
        mHitPoints = new MutableLiveData<>("");
        mIntelligence = new MutableLiveData<>("");
        mLairActions = new MutableLiveData<>(new ArrayList<>());
        mLanguages = new MutableLiveData<>("");
        mLegendaryActions = new MutableLiveData<>(new ArrayList<>());
        mMeta = new MutableLiveData<>("");
        mName = new MutableLiveData<>("");
        mReactions = new MutableLiveData<>(new ArrayList<>());
        mRegionalEffects = new MutableLiveData<>(new ArrayList<>());
        mSavingThrows = new MutableLiveData<>("");
        mSenses = new MutableLiveData<>("");
        mSkills = new MutableLiveData<>("");
        mSpeed = new MutableLiveData<>("");
        mStrength = new MutableLiveData<>("");
        mWisdom = new MutableLiveData<>("");
        mMonsterId = new MutableLiveData<>(UUID.fromString("00000000-0000-0000-0000-000000000000"));
    }

    public LiveData<List<String>> getAbilities() {
        return mAbilities;
    }

    public LiveData<List<String>> getActions() {
        return mActions;
    }

    public LiveData<List<String>> getReactions() {
        return mReactions;
    }

    public LiveData<List<String>> getLegendaryActions() {
        return mLegendaryActions;
    }

    public LiveData<List<String>> getLairActions() {
        return mLairActions;
    }

    public LiveData<List<String>> getRegionalEffects() {
        return mRegionalEffects;
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

    public Monster getMonster() {
        return mMonster;
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
        mLairActions.setValue(mMonster.getLairActionDescriptions());
        mLanguages.setValue(mMonster.getLanguagesDescription());
        mLegendaryActions.setValue(mMonster.getLegendaryActionDescriptions());
        mMeta.setValue(mMonster.getMeta());
        mMonsterId.setValue(mMonster.id);
        mName.setValue(mMonster.name);
        mReactions.setValue(monster.getReactionDescriptions());
        mRegionalEffects.setValue(monster.getRegionalActionDescriptions());
        mSavingThrows.setValue(monster.getSavingThrowsDescription());
        mSenses.setValue(monster.getSensesDescription());
        mSkills.setValue(monster.getSkillsDescription());
        mSpeed.setValue(mMonster.getSpeedText());
        mStrength.setValue(monster.getStrengthDescription());
        mWisdom.setValue(monster.getWisdomDescription());
    }
}
