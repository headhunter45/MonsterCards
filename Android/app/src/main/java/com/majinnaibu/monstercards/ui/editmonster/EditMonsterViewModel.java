package com.majinnaibu.monstercards.ui.editmonster;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.majinnaibu.monstercards.data.enums.AbilityScore;
import com.majinnaibu.monstercards.data.enums.AdvantageType;
import com.majinnaibu.monstercards.data.enums.ArmorType;
import com.majinnaibu.monstercards.data.enums.ChallengeRating;
import com.majinnaibu.monstercards.data.enums.ProficiencyType;
import com.majinnaibu.monstercards.data.enums.StringType;
import com.majinnaibu.monstercards.data.enums.TraitType;
import com.majinnaibu.monstercards.helpers.StringHelper;
import com.majinnaibu.monstercards.models.Language;
import com.majinnaibu.monstercards.models.Monster;
import com.majinnaibu.monstercards.models.Skill;
import com.majinnaibu.monstercards.models.Trait;
import com.majinnaibu.monstercards.ui.shared.ChangeTrackedViewModel;
import com.majinnaibu.monstercards.utils.ChangeTrackedLiveData;
import com.majinnaibu.monstercards.utils.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@SuppressWarnings({"ConstantConditions"})
public class EditMonsterViewModel extends ChangeTrackedViewModel {
    private final ChangeTrackedLiveData<UUID> mMonsterId;
    private final MutableLiveData<Boolean> mHasError;
    private final MutableLiveData<Boolean> mHasLoaded;
    private final ChangeTrackedLiveData<Boolean> mHasCustomHitPoints;
    private final ChangeTrackedLiveData<Boolean> mHasShield;
    private final ChangeTrackedLiveData<Boolean> mCanHover;
    private final ChangeTrackedLiveData<Boolean> mHasCustomSpeed;
    private final ChangeTrackedLiveData<ArmorType> mArmorType;
    private final ChangeTrackedLiveData<ProficiencyType> mStrengthProficiency;
    private final ChangeTrackedLiveData<AdvantageType> mStrengthAdvantage;
    private final ChangeTrackedLiveData<ProficiencyType> mDexterityProficiency;
    private final ChangeTrackedLiveData<AdvantageType> mDexterityAdvantage;
    private final ChangeTrackedLiveData<ProficiencyType> mConstitutionProficiency;
    private final ChangeTrackedLiveData<AdvantageType> mConstitutionAdvantage;
    private final ChangeTrackedLiveData<ProficiencyType> mIntelligenceProficiency;
    private final ChangeTrackedLiveData<AdvantageType> mIntelligenceAdvantage;
    private final ChangeTrackedLiveData<ProficiencyType> mWisdomProficiency;
    private final ChangeTrackedLiveData<AdvantageType> mWisdomAdvantage;
    private final ChangeTrackedLiveData<ProficiencyType> mCharismaProficiency;
    private final ChangeTrackedLiveData<AdvantageType> mCharismaAdvantage;
    private final ChangeTrackedLiveData<Integer> mHitDice;
    private final ChangeTrackedLiveData<Integer> mNaturalArmorBonus;
    private final ChangeTrackedLiveData<Integer> mShieldBonus;
    private final ChangeTrackedLiveData<Integer> mWalkSpeed;
    private final ChangeTrackedLiveData<Integer> mBurrowSpeed;
    private final ChangeTrackedLiveData<Integer> mClimbSpeed;
    private final ChangeTrackedLiveData<Integer> mFlySpeed;
    private final ChangeTrackedLiveData<Integer> mSwimSpeed;
    private final ChangeTrackedLiveData<Integer> mStrength;
    private final ChangeTrackedLiveData<Integer> mDexterity;
    private final ChangeTrackedLiveData<Integer> mConstitution;
    private final ChangeTrackedLiveData<Integer> mIntelligence;
    private final ChangeTrackedLiveData<Integer> mWisdom;
    private final ChangeTrackedLiveData<Integer> mCharisma;
    private final ChangeTrackedLiveData<String> mName;
    private final MutableLiveData<String> mErrorMessage;
    private final ChangeTrackedLiveData<String> mSize;
    private final ChangeTrackedLiveData<String> mType;
    private final ChangeTrackedLiveData<String> mSubtype;
    private final ChangeTrackedLiveData<String> mAlignment;
    private final ChangeTrackedLiveData<String> mCustomHitPoints;
    private final ChangeTrackedLiveData<String> mCustomArmor;
    private final ChangeTrackedLiveData<String> mCustomSpeed;
    private final ChangeTrackedLiveData<ChallengeRating> mChallengeRating;
    private final ChangeTrackedLiveData<String> mCustomChallengeRatingDescription;
    private final ChangeTrackedLiveData<Integer> mCustomProficiencyBonus;
    private final ChangeTrackedLiveData<Integer> mTelepathyRange;
    private final ChangeTrackedLiveData<String> mUnderstandsButDescription;
    private final ChangeTrackedLiveData<List<Skill>> mSkills;
    private final ChangeTrackedLiveData<List<String>> mSenses;
    private final ChangeTrackedLiveData<List<String>> mDamageImmunities;
    private final ChangeTrackedLiveData<List<String>> mDamageResistances;
    private final ChangeTrackedLiveData<List<String>> mDamageVulnerabilities;
    private final ChangeTrackedLiveData<List<String>> mConditionImmunities;
    private final ChangeTrackedLiveData<List<Language>> mLanguages;
    private final ChangeTrackedLiveData<List<Trait>> mAbilities;
    private final ChangeTrackedLiveData<List<Trait>> mActions;
    private final ChangeTrackedLiveData<List<Trait>> mReactions;
    private final ChangeTrackedLiveData<List<Trait>> mLairActions;
    private final ChangeTrackedLiveData<List<Trait>> mLegendaryActions;
    private final ChangeTrackedLiveData<List<Trait>> mRegionalActions;

    public EditMonsterViewModel() {
        super();
        mErrorMessage = new MutableLiveData<>("");
        mHasError = new MutableLiveData<>(false);
        mHasLoaded = new MutableLiveData<>(false);

        mName = new ChangeTrackedLiveData<>("", this::makeDirty);
        mMonsterId = new ChangeTrackedLiveData<>(UUID.randomUUID(), this::makeDirty);
        mSize = new ChangeTrackedLiveData<>("", this::makeDirty);
        mType = new ChangeTrackedLiveData<>("", this::makeDirty);
        mSubtype = new ChangeTrackedLiveData<>("", this::makeDirty);
        mAlignment = new ChangeTrackedLiveData<>("", this::makeDirty);
        mCustomHitPoints = new ChangeTrackedLiveData<>("", this::makeDirty);
        mHitDice = new ChangeTrackedLiveData<>(0, this::makeDirty);
        mNaturalArmorBonus = new ChangeTrackedLiveData<>(0, this::makeDirty);
        mHasCustomHitPoints = new ChangeTrackedLiveData<>(false, this::makeDirty);
        mArmorType = new ChangeTrackedLiveData<>(ArmorType.NONE, this::makeDirty);
        mHasShield = new ChangeTrackedLiveData<>(false, this::makeDirty);
        mShieldBonus = new ChangeTrackedLiveData<>(0, this::makeDirty);
        mCustomArmor = new ChangeTrackedLiveData<>("", this::makeDirty);
        mWalkSpeed = new ChangeTrackedLiveData<>(0, this::makeDirty);
        mBurrowSpeed = new ChangeTrackedLiveData<>(0, this::makeDirty);
        mClimbSpeed = new ChangeTrackedLiveData<>(0, this::makeDirty);
        mFlySpeed = new ChangeTrackedLiveData<>(0, this::makeDirty);
        mSwimSpeed = new ChangeTrackedLiveData<>(0, this::makeDirty);
        mCanHover = new ChangeTrackedLiveData<>(false, this::makeDirty);
        mHasCustomSpeed = new ChangeTrackedLiveData<>(false, this::makeDirty);
        mCustomSpeed = new ChangeTrackedLiveData<>("", this::makeDirty);
        mStrength = new ChangeTrackedLiveData<>(10, this::makeDirty);
        mDexterity = new ChangeTrackedLiveData<>(10, this::makeDirty);
        mConstitution = new ChangeTrackedLiveData<>(10, this::makeDirty);
        mIntelligence = new ChangeTrackedLiveData<>(10, this::makeDirty);
        mWisdom = new ChangeTrackedLiveData<>(10, this::makeDirty);
        mCharisma = new ChangeTrackedLiveData<>(10, this::makeDirty);
        mStrengthProficiency = new ChangeTrackedLiveData<>(ProficiencyType.NONE, this::makeDirty);
        mStrengthAdvantage = new ChangeTrackedLiveData<>(AdvantageType.NONE, this::makeDirty);
        mDexterityProficiency = new ChangeTrackedLiveData<>(ProficiencyType.NONE, this::makeDirty);
        mDexterityAdvantage = new ChangeTrackedLiveData<>(AdvantageType.NONE, this::makeDirty);
        mConstitutionProficiency = new ChangeTrackedLiveData<>(ProficiencyType.NONE, this::makeDirty);
        mConstitutionAdvantage = new ChangeTrackedLiveData<>(AdvantageType.NONE, this::makeDirty);
        mIntelligenceProficiency = new ChangeTrackedLiveData<>(ProficiencyType.NONE, this::makeDirty);
        mIntelligenceAdvantage = new ChangeTrackedLiveData<>(AdvantageType.NONE, this::makeDirty);
        mWisdomProficiency = new ChangeTrackedLiveData<>(ProficiencyType.NONE, this::makeDirty);
        mWisdomAdvantage = new ChangeTrackedLiveData<>(AdvantageType.NONE, this::makeDirty);
        mCharismaProficiency = new ChangeTrackedLiveData<>(ProficiencyType.NONE, this::makeDirty);
        mCharismaAdvantage = new ChangeTrackedLiveData<>(AdvantageType.NONE, this::makeDirty);
        mChallengeRating = new ChangeTrackedLiveData<>(ChallengeRating.ONE_EIGHTH, this::makeDirty);
        mCustomChallengeRatingDescription = new ChangeTrackedLiveData<>("", this::makeDirty);
        mCustomProficiencyBonus = new ChangeTrackedLiveData<>(0, this::makeDirty);
        mTelepathyRange = new ChangeTrackedLiveData<>(0, this::makeDirty);
        mUnderstandsButDescription = new ChangeTrackedLiveData<>("", this::makeDirty);
        mSkills = new ChangeTrackedLiveData<>(new ArrayList<>(), this::makeDirty);
        mSenses = new ChangeTrackedLiveData<>(new ArrayList<>(), this::makeDirty);
        mDamageImmunities = new ChangeTrackedLiveData<>(new ArrayList<>(), this::makeDirty);
        mDamageResistances = new ChangeTrackedLiveData<>(new ArrayList<>(), this::makeDirty);
        mDamageVulnerabilities = new ChangeTrackedLiveData<>(new ArrayList<>(), this::makeDirty);
        mConditionImmunities = new ChangeTrackedLiveData<>(new ArrayList<>(), this::makeDirty);
        mLanguages = new ChangeTrackedLiveData<>(new ArrayList<>(), this::makeDirty);
        mAbilities = new ChangeTrackedLiveData<>(new ArrayList<>(), this::makeDirty);
        mActions = new ChangeTrackedLiveData<>(new ArrayList<>(), this::makeDirty);
        mReactions = new ChangeTrackedLiveData<>(new ArrayList<>(), this::makeDirty);
        mLairActions = new ChangeTrackedLiveData<>(new ArrayList<>(), this::makeDirty);
        mLegendaryActions = new ChangeTrackedLiveData<>(new ArrayList<>(), this::makeDirty);
        mRegionalActions = new ChangeTrackedLiveData<>(new ArrayList<>(), this::makeDirty);
    }

    public void copyFromMonster(@NonNull Monster monster) {
        mMonsterId.resetValue(monster.id);
        mName.resetValue(monster.name);
        mSize.resetValue(monster.size);
        mType.resetValue(monster.type);
        mSubtype.resetValue(monster.subtype);
        mAlignment.resetValue(monster.alignment);
        mCustomHitPoints.resetValue(monster.customHPDescription);
        mHitDice.resetValue(monster.hitDice);
        mNaturalArmorBonus.resetValue(monster.naturalArmorBonus);
        mHasCustomHitPoints.resetValue(monster.hasCustomHP);
        mArmorType.resetValue(monster.armorType);
        mHasShield.resetValue(monster.shieldBonus != 0);
        mShieldBonus.resetValue(monster.shieldBonus);
        mCustomArmor.resetValue(monster.otherArmorDescription);
        mWalkSpeed.resetValue(monster.walkSpeed);
        mBurrowSpeed.resetValue(monster.burrowSpeed);
        mClimbSpeed.resetValue(monster.climbSpeed);
        mFlySpeed.resetValue(monster.flySpeed);
        mSwimSpeed.resetValue(monster.swimSpeed);
        mCanHover.resetValue(monster.canHover);
        mHasCustomSpeed.resetValue(monster.hasCustomSpeed);
        mCustomSpeed.resetValue(monster.customSpeedDescription);
        mStrength.resetValue(monster.strengthScore);
        mDexterity.resetValue(monster.dexterityScore);
        mConstitution.resetValue(monster.constitutionScore);
        mIntelligence.resetValue(monster.intelligenceScore);
        mWisdom.resetValue(monster.wisdomScore);
        mCharisma.resetValue(monster.charismaScore);
        mStrengthProficiency.resetValue(monster.strengthSavingThrowProficiency);
        mStrengthAdvantage.resetValue(monster.strengthSavingThrowAdvantage);
        mDexterityProficiency.resetValue(monster.dexteritySavingThrowProficiency);
        mDexterityAdvantage.resetValue(monster.dexteritySavingThrowAdvantage);
        mConstitutionProficiency.resetValue(monster.constitutionSavingThrowProficiency);
        mConstitutionAdvantage.resetValue(monster.constitutionSavingThrowAdvantage);
        mIntelligenceProficiency.resetValue(monster.intelligenceSavingThrowProficiency);
        mIntelligenceAdvantage.resetValue(monster.intelligenceSavingThrowAdvantage);
        mWisdomProficiency.resetValue(monster.wisdomSavingThrowProficiency);
        mWisdomAdvantage.resetValue(monster.wisdomSavingThrowAdvantage);
        mCharismaProficiency.resetValue(monster.charismaSavingThrowProficiency);
        mCharismaAdvantage.resetValue(monster.charismaSavingThrowAdvantage);
        mChallengeRating.resetValue(monster.challengeRating);
        mCustomChallengeRatingDescription.resetValue(monster.customChallengeRatingDescription);
        mCustomProficiencyBonus.resetValue(monster.customProficiencyBonus);
        mTelepathyRange.resetValue(monster.telepathyRange);
        mUnderstandsButDescription.resetValue(monster.understandsButDescription);

        ArrayList<Skill> skills = new ArrayList<>(monster.skills);
        Collections.sort(skills, Skill::compareTo);
        mSkills.resetValue(skills);
        ArrayList<String> senses = new ArrayList<>(monster.senses);
        Collections.sort(senses, String::compareToIgnoreCase);
        mSenses.resetValue(senses);
        ArrayList<String> damageImmunities = new ArrayList<>(monster.damageImmunities);
        Collections.sort(damageImmunities, String::compareToIgnoreCase);
        mDamageImmunities.resetValue(damageImmunities);
        ArrayList<String> damageResistances = new ArrayList<>(monster.damageResistances);
        Collections.sort(damageResistances, String::compareToIgnoreCase);
        mDamageResistances.resetValue(damageResistances);
        ArrayList<String> damageVulnerabilities = new ArrayList<>(monster.damageVulnerabilities);
        Collections.sort(damageVulnerabilities, String::compareToIgnoreCase);
        mDamageVulnerabilities.resetValue(damageVulnerabilities);
        ArrayList<String> conditionImmunities = new ArrayList<>(monster.conditionImmunities);
        Collections.sort(conditionImmunities, String::compareToIgnoreCase);
        mConditionImmunities.resetValue(conditionImmunities);
        ArrayList<Language> languages = new ArrayList<>(monster.languages);
        Collections.sort(languages, Language::compareTo);
        mLanguages.resetValue(languages);
        mAbilities.resetValue(new ArrayList<>(monster.abilities));
        mActions.resetValue(new ArrayList<>(monster.actions));
        mReactions.resetValue(new ArrayList<>(monster.reactions));
        mLairActions.resetValue(new ArrayList<>(monster.lairActions));
        mLegendaryActions.resetValue(new ArrayList<>(monster.legendaryActions));
        mRegionalActions.resetValue(new ArrayList<>(monster.regionalActions));
        makeClean();
    }

    public LiveData<String> getName() {
        return mName;
    }

    public void setName(@NonNull String name) {
        mName.setValue(name);
    }

    public LiveData<UUID> getMonsterId() {
        return mMonsterId;
    }

    public void setErrorMessage(@NonNull String errorMessage) {
        mErrorMessage.setValue(errorMessage);
    }

    public LiveData<Boolean> getHasError() {
        return mHasError;
    }

    public void setHasError(@NonNull Boolean hasError) {
        mHasError.setValue(hasError);
    }

    public boolean hasError() {
        return getHasError().getValue();
    }

    public LiveData<Boolean> getHasLoaded() {
        return mHasLoaded;
    }

    public void setHasLoaded(@NonNull Boolean hasLoaded) {
        mHasLoaded.setValue(hasLoaded);
    }

    public boolean hasLoaded() {
        return getHasLoaded().getValue();
    }

    public LiveData<String> getSize() {
        return mSize;
    }

    public void setSize(@NonNull String size) {
        mSize.setValue(size);
    }

    public LiveData<String> getType() {
        return mType;
    }

    public void setType(@NonNull String type) {
        mType.setValue(type);
    }

    public LiveData<String> getSubtype() {
        return mSubtype;
    }

    public void setSubtype(@NonNull String subtype) {
        mSubtype.setValue(subtype);
    }

    public LiveData<String> getAlignment() {
        return mAlignment;
    }

    public void setAlignment(@NonNull String alignment) {
        mAlignment.setValue(alignment);
    }

    public LiveData<String> getCustomHitPoints() {
        return mCustomHitPoints;
    }

    public void setCustomHitPoints(String customHitPoints) {
        mCustomHitPoints.setValue(customHitPoints);
    }

    public void setHitDice(int hitDice) {
        mHitDice.setValue(hitDice);
    }

    public int getHitDiceUnboxed() {
        return Helpers.unboxInteger(mHitDice.getValue(), 1);
    }

    public void setNaturalArmorBonus(int naturalArmorBonus) {
        mNaturalArmorBonus.setValue(naturalArmorBonus);
    }

    public int getNaturalArmorBonusUnboxed() {
        return Helpers.unboxInteger(mNaturalArmorBonus.getValue(), 0);
    }

    public LiveData<Boolean> getHasCustomHitPoints() {
        return mHasCustomHitPoints;
    }

    public void setHasCustomHitPoints(boolean hasCustomHitPoints) {
        mHasCustomHitPoints.setValue(hasCustomHitPoints);
    }

    public boolean getHasCustomHitPointsValueAsBoolean() {
        return mHasCustomHitPoints.getValue();
    }

    public LiveData<ArmorType> getArmorType() {
        return mArmorType;
    }

    public void setArmorType(ArmorType armorType) {
        mArmorType.setValue(armorType);
    }

    public void setHasShield(boolean hasShield) {
        mHasShield.setValue(hasShield);
    }

    public boolean getHasShieldValueAsBoolean() {
        return mHasShield.getValue();
    }

    public void setShieldBonus(int shieldBonus) {
        mShieldBonus.setValue(shieldBonus);
    }

    public int getShieldBonusUnboxed() {
        return Helpers.unboxInteger(mShieldBonus.getValue(), 0);
    }

    public LiveData<String> getCustomArmor() {
        return mCustomArmor;
    }

    public void setCustomArmor(String customArmor) {
        mCustomArmor.setValue(customArmor);
    }

    public String getShieldBonusValueAsString() {
        return mShieldBonus.getValue().toString();
    }

    public LiveData<Integer> getWalkSpeed() {
        return mWalkSpeed;
    }

    public void setWalkSpeed(int walkSpeed) {
        mWalkSpeed.setValue(walkSpeed);
    }

    public LiveData<Integer> getBurrowSpeed() {
        return mBurrowSpeed;
    }

    public void setBurrowSpeed(int burrowSpeed) {
        mBurrowSpeed.setValue(burrowSpeed);
    }

    public LiveData<Integer> getClimbSpeed() {
        return mClimbSpeed;
    }

    public void setClimbSpeed(int climbSpeed) {
        mClimbSpeed.setValue(climbSpeed);
    }

    public LiveData<Integer> getFlySpeed() {
        return mFlySpeed;
    }

    public void setFlySpeed(int flySpeed) {
        mFlySpeed.setValue(flySpeed);
    }

    public LiveData<Integer> getSwimSpeed() {
        return mSwimSpeed;
    }

    public void setSwimSpeed(int swimSpeed) {
        mSwimSpeed.setValue(swimSpeed);
    }

    public LiveData<Boolean> getCanHover() {
        return mCanHover;
    }

    public void setCanHover(boolean canHover) {
        mCanHover.setValue(canHover);
    }

    public LiveData<Boolean> getHasCustomSpeed() {
        return mHasCustomSpeed;
    }

    public void setHasCustomSpeed(boolean hasCustomSpeed) {
        mHasCustomSpeed.setValue(hasCustomSpeed);
    }

    public LiveData<String> getCustomSpeed() {
        return mCustomSpeed;
    }

    public void setCustomSpeed(String customSpeed) {
        mCustomSpeed.setValue(customSpeed);
    }

    public LiveData<Integer> getStrength() {
        return mStrength;
    }

    public void setStrength(int strength) {
        mStrength.setValue(strength);
    }

    public LiveData<Integer> getDexterity() {
        return mDexterity;
    }

    public void setDexterity(int dexterity) {
        mDexterity.setValue(dexterity);
    }

    public LiveData<Integer> getConstitution() {
        return mConstitution;
    }

    public void setConstitution(int constitution) {
        mConstitution.setValue(constitution);
    }

    public LiveData<Integer> getIntelligence() {
        return mIntelligence;
    }

    public void setIntelligence(int intelligence) {
        mIntelligence.setValue(intelligence);
    }

    public LiveData<Integer> getWisdom() {
        return mWisdom;
    }

    public void setWisdom(int wisdom) {
        mWisdom.setValue(wisdom);
    }

    public LiveData<Integer> getCharisma() {
        return mCharisma;
    }

    public void setCharisma(int charisma) {
        mCharisma.setValue(charisma);
    }

    public LiveData<ProficiencyType> getStrengthProficiency() {
        return mStrengthProficiency;
    }

    public void setStrengthProficiency(ProficiencyType proficiency) {
        mStrengthProficiency.setValue(proficiency);
    }

    public LiveData<AdvantageType> getStrengthAdvantage() {
        return mStrengthAdvantage;
    }

    public void setStrengthAdvantage(AdvantageType advantage) {
        mStrengthAdvantage.setValue(advantage);
    }

    public LiveData<ProficiencyType> getDexterityProficiency() {
        return mDexterityProficiency;
    }

    public void setDexterityProficiency(ProficiencyType proficiency) {
        mDexterityProficiency.setValue(proficiency);
    }

    public LiveData<AdvantageType> getDexterityAdvantage() {
        return mDexterityAdvantage;
    }

    public void setDexterityAdvantage(AdvantageType advantage) {
        mDexterityAdvantage.setValue(advantage);
    }

    public LiveData<ProficiencyType> getConstitutionProficiency() {
        return mConstitutionProficiency;
    }

    public void setConstitutionProficiency(ProficiencyType proficiency) {
        mConstitutionProficiency.setValue(proficiency);
    }

    public LiveData<AdvantageType> getConstitutionAdvantage() {
        return mConstitutionAdvantage;
    }

    public void setConstitutionAdvantage(AdvantageType advantage) {
        mConstitutionAdvantage.setValue(advantage);
    }

    public LiveData<ProficiencyType> getIntelligenceProficiency() {
        return mIntelligenceProficiency;
    }

    public void setIntelligenceProficiency(ProficiencyType proficiency) {
        mIntelligenceProficiency.setValue(proficiency);
    }

    public LiveData<AdvantageType> getIntelligenceAdvantage() {
        return mIntelligenceAdvantage;
    }

    public void setIntelligenceAdvantage(AdvantageType advantage) {
        mIntelligenceAdvantage.setValue(advantage);
    }

    public LiveData<ProficiencyType> getWisdomProficiency() {
        return mWisdomProficiency;
    }

    public void setWisdomProficiency(ProficiencyType proficiency) {
        mWisdomProficiency.setValue(proficiency);
    }

    public LiveData<AdvantageType> getWisdomAdvantage() {
        return mWisdomAdvantage;
    }

    public void setWisdomAdvantage(AdvantageType advantage) {
        mWisdomAdvantage.setValue(advantage);
    }

    public LiveData<ProficiencyType> getCharismaProficiency() {
        return mCharismaProficiency;
    }

    public void setCharismaProficiency(ProficiencyType proficiency) {
        mCharismaProficiency.setValue(proficiency);
    }

    public LiveData<AdvantageType> getCharismaAdvantage() {
        return mCharismaAdvantage;
    }

    public void setCharismaAdvantage(AdvantageType advantage) {
        mCharismaAdvantage.setValue(advantage);
    }

    public LiveData<ChallengeRating> getChallengeRating() {
        return mChallengeRating;
    }

    public void setChallengeRating(ChallengeRating challengeRating) {
        mChallengeRating.setValue(challengeRating);
    }

    public LiveData<String> getCustomChallengeRatingDescription() {
        return mCustomChallengeRatingDescription;
    }

    public void setCustomChallengeRatingDescription(String customChallengeRatingDescription) {
        mCustomChallengeRatingDescription.setValue(customChallengeRatingDescription);
    }

    public LiveData<Integer> getCustomProficiencyBonus() {
        return mCustomProficiencyBonus;
    }

    public void setCustomProficiencyBonus(int proficiencyBonus) {
        mCustomProficiencyBonus.setValue(proficiencyBonus);
    }

    public void setCustomProficiencyBonus(String proficiencyBonus) {
        Integer parsedValue = StringHelper.parseInt(proficiencyBonus);
        this.setCustomProficiencyBonus(parsedValue != null ? parsedValue : 0);
    }

    public String getCustomProficiencyBonusValueAsString() {
        return mCustomProficiencyBonus.getValue().toString();
    }

    public LiveData<Integer> getTelepathyRange() {
        return mTelepathyRange;
    }

    public void setTelepathyRange(int telepathyRange) {
        mTelepathyRange.setValue(telepathyRange);
    }

    public int getTelepathyRangeUnboxed() {
        return Helpers.unboxInteger(mTelepathyRange.getValue(), 0);
    }

    public LiveData<String> getUnderstandsButDescription() {
        return mUnderstandsButDescription;
    }

    public void setUnderstandsButDescription(String understandsButDescription) {
        mUnderstandsButDescription.setValue(understandsButDescription);
    }

    public LiveData<List<Skill>> getSkills() {
        return mSkills;
    }

    public List<Skill> getSkillsArray() {
        return mSkills.getValue();
    }

    public Skill addNewSkill() {
        Skill newSkill = new Skill("Unnamed Skill", AbilityScore.DEXTERITY);
        ArrayList<Skill> newSkills = new ArrayList<>(mSkills.getValue());
        newSkills.add(newSkill);
        Collections.sort(newSkills, (skill1, skill2) -> skill1.name.compareToIgnoreCase(skill2.name));
        mSkills.setValue(newSkills);
        return newSkill;
    }

    public void removeSkill(int position) {
        List<Skill> skills = mSkills.getValue();
        ArrayList<Skill> newSkills = new ArrayList<>(skills);
        newSkills.remove(position);
        mSkills.setValue(newSkills);
    }

    public void replaceSkill(Skill newSkill, Skill oldSkill) {
        List<Skill> oldSkills = mSkills.getValue();
        if (oldSkills == null) {
            oldSkills = new ArrayList<>();
        }
        boolean hasReplaced = false;
        ArrayList<Skill> newSkills = new ArrayList<>(oldSkills.size());
        for (Skill skill : oldSkills) {
            if (Objects.equals(skill, oldSkill)) {
                newSkills.add(newSkill);
                hasReplaced = true;
            } else {
                newSkills.add(skill);
            }
        }
        if (!hasReplaced) {
            newSkills.add(newSkill);
        }
        Collections.sort(newSkills, (skill1, skill2) -> skill1.name.compareToIgnoreCase(skill2.name));
        mSkills.setValue(newSkills);
    }

    public LiveData<List<String>> getSenses() {
        return mSenses;
    }

    public LiveData<List<String>> getDamageImmunities() {
        return mDamageImmunities;
    }

    public List<String> getDamageImmunitiesArray() {
        return mDamageImmunities.getValue();
    }

    public LiveData<List<String>> getDamageResistances() {
        return mDamageResistances;
    }

    public LiveData<List<Language>> getLanguages() {
        return mLanguages;
    }

    public List<Language> getLanguagesArray() {
        return mLanguages.getValue();
    }

    public Language addNewLanguage() {
        Language newLanguage = new Language("", true);
        return Helpers.addItemToList(mLanguages, newLanguage, Language::compareTo);
    }

    public void removeLanguage(int position) {
        Helpers.removeFromList(mLanguages, position);
    }

    public void replaceLanguage(Language oldLanguage, Language newLanguage) {
        Helpers.replaceItemInList(mLanguages, oldLanguage, newLanguage, Language::compareTo);
    }

    public Monster buildMonster() {
        Monster monster = new Monster();

        monster.id = mMonsterId.getValue();
        monster.name = mName.getValue();
        monster.size = mSize.getValue();
        monster.type = mType.getValue();
        monster.subtype = mSubtype.getValue();
        monster.alignment = mAlignment.getValue();
        monster.customHPDescription = mCustomHitPoints.getValue();
        monster.hitDice = mHitDice.getValue();
        monster.hasCustomHP = mHasCustomHitPoints.getValue();
        monster.armorType = mArmorType.getValue();
        monster.naturalArmorBonus = mNaturalArmorBonus.getValue();
        monster.shieldBonus = mShieldBonus.getValue();
        monster.otherArmorDescription = mCustomArmor.getValue();
        monster.walkSpeed = mWalkSpeed.getValue();
        monster.burrowSpeed = mBurrowSpeed.getValue();
        monster.climbSpeed = mClimbSpeed.getValue();
        monster.flySpeed = mFlySpeed.getValue();
        monster.swimSpeed = mSwimSpeed.getValue();
        monster.canHover = mCanHover.getValue();
        monster.hasCustomSpeed = mHasCustomSpeed.getValue();
        monster.customSpeedDescription = mCustomSpeed.getValue();
        monster.strengthScore = mStrength.getValue();
        monster.dexterityScore = mDexterity.getValue();
        monster.constitutionScore = mConstitution.getValue();
        monster.intelligenceScore = mIntelligence.getValue();
        monster.wisdomScore = mWisdom.getValue();
        monster.charismaScore = mCharisma.getValue();
        monster.strengthSavingThrowAdvantage = mStrengthAdvantage.getValue();
        monster.strengthSavingThrowProficiency = mStrengthProficiency.getValue();
        monster.dexteritySavingThrowAdvantage = mDexterityAdvantage.getValue();
        monster.dexteritySavingThrowProficiency = mDexterityProficiency.getValue();
        monster.constitutionSavingThrowAdvantage = mConstitutionAdvantage.getValue();
        monster.constitutionSavingThrowProficiency = mConstitutionProficiency.getValue();
        monster.intelligenceSavingThrowAdvantage = mIntelligenceAdvantage.getValue();
        monster.intelligenceSavingThrowProficiency = mIntelligenceProficiency.getValue();
        monster.wisdomSavingThrowAdvantage = mWisdomAdvantage.getValue();
        monster.wisdomSavingThrowProficiency = mWisdomProficiency.getValue();
        monster.charismaSavingThrowAdvantage = mCharismaAdvantage.getValue();
        monster.charismaSavingThrowProficiency = mCharismaProficiency.getValue();
        monster.challengeRating = mChallengeRating.getValue();
        monster.customChallengeRatingDescription = mCustomChallengeRatingDescription.getValue();
        monster.customProficiencyBonus = mCustomProficiencyBonus.getValue();
        monster.telepathyRange = mTelepathyRange.getValue();
        monster.understandsButDescription = mUnderstandsButDescription.getValue();
        monster.skills = new HashSet<>(mSkills.getValue());
        monster.senses = new HashSet<>(mSenses.getValue());
        monster.damageImmunities = new HashSet<>(mDamageImmunities.getValue());
        monster.damageResistances = new HashSet<>(mDamageResistances.getValue());
        monster.damageVulnerabilities = new HashSet<>(mDamageVulnerabilities.getValue());
        monster.conditionImmunities = new HashSet<>(mConditionImmunities.getValue());
        monster.languages = new HashSet<>(mLanguages.getValue());
        monster.abilities = new ArrayList<>(mAbilities.getValue());
        monster.actions = new ArrayList<>(mActions.getValue());
        monster.reactions = new ArrayList<>(mReactions.getValue());
        monster.lairActions = new ArrayList<>(mLairActions.getValue());
        monster.legendaryActions = new ArrayList<>(mLegendaryActions.getValue());
        monster.regionalActions = new ArrayList<>(mRegionalActions.getValue());

        return monster;
    }

    public LiveData<List<Trait>> getTraits(@NonNull TraitType type) {
        switch (type) {
            case ABILITY:
                return mAbilities;
            case ACTION:
                return mActions;
            case LAIR_ACTION:
                return mLairActions;
            case LEGENDARY_ACTION:
                return mLegendaryActions;
            case REACTIONS:
                return mReactions;
            case REGIONAL_ACTION:
                return mRegionalActions;
            default:
                Logger.logUnimplementedFeature(String.format("Unrecognized TraitType: %s", type));
                return null;
        }
    }

    public void removeTrait(@NonNull TraitType type, int position) {
        switch (type) {
            case ABILITY:
                Helpers.removeFromList(mAbilities, position);
                break;
            case ACTION:
                Helpers.removeFromList(mActions, position);
                break;
            case LAIR_ACTION:
                Helpers.removeFromList(mLairActions, position);
                break;
            case LEGENDARY_ACTION:
                Helpers.removeFromList(mLegendaryActions, position);
                break;
            case REACTIONS:
                Helpers.removeFromList(mReactions, position);
                break;
            case REGIONAL_ACTION:
                Helpers.removeFromList(mRegionalActions, position);
                break;
            default:
                Logger.logUnimplementedFeature(String.format("Unrecognized TraitType: %s", type));
                break;
        }
    }

    public void replaceTrait(@NonNull TraitType type, Trait oldTrait, Trait newTrait) {
        switch (type) {
            case ABILITY:
                Helpers.replaceItemInList(mAbilities, oldTrait, newTrait);
                break;
            case ACTION:
                Helpers.replaceItemInList(mActions, oldTrait, newTrait);
                break;
            case LAIR_ACTION:
                Helpers.replaceItemInList(mLairActions, oldTrait, newTrait);
                break;
            case LEGENDARY_ACTION:
                Helpers.replaceItemInList(mLegendaryActions, oldTrait, newTrait);
                break;
            case REACTIONS:
                Helpers.replaceItemInList(mReactions, oldTrait, newTrait);
                break;
            case REGIONAL_ACTION:
                Helpers.replaceItemInList(mRegionalActions, oldTrait, newTrait);
                break;
            default:
                Logger.logUnimplementedFeature(String.format("Unrecognized TraitType: %s", type));
        }
    }

    public Trait addNewTrait(@NonNull TraitType type) {
        Trait newAction = new Trait("", "");
        switch (type) {
            case ABILITY:
                return Helpers.addItemToList(mAbilities, newAction);
            case ACTION:
                return Helpers.addItemToList(mActions, newAction);
            case LAIR_ACTION:
                return Helpers.addItemToList(mLairActions, newAction);
            case LEGENDARY_ACTION:
                return Helpers.addItemToList(mLegendaryActions, newAction);
            case REACTIONS:
                return Helpers.addItemToList(mReactions, newAction);
            case REGIONAL_ACTION:
                return Helpers.addItemToList(mRegionalActions, newAction);
            default:
                Logger.logUnimplementedFeature(String.format("Unrecognized TraitType: %s", type));
                return null;
        }
    }

    public LiveData<List<String>> getStrings(@NonNull StringType type) {
        switch (type) {
            case CONDITION_IMMUNITY:
                return mConditionImmunities;
            case DAMAGE_IMMUNITY:
                return mDamageImmunities;
            case DAMAGE_RESISTANCE:
                return mDamageResistances;
            case DAMAGE_VULNERABILITY:
                return mDamageVulnerabilities;
            case SENSE:
                return mSenses;
            default:
                Logger.logUnimplementedFeature(String.format("Unrecognized StringType: %s", type));
                return null;
        }
    }

    public void removeString(@NonNull StringType type, int position) {
        switch (type) {
            case CONDITION_IMMUNITY:
                Helpers.removeFromList(mConditionImmunities, position);
                break;
            case DAMAGE_IMMUNITY:
                Helpers.removeFromList(mDamageImmunities, position);
                break;
            case DAMAGE_RESISTANCE:
                Helpers.removeFromList(mDamageResistances, position);
                break;
            case DAMAGE_VULNERABILITY:
                Helpers.removeFromList(mDamageVulnerabilities, position);
                break;
            case SENSE:
                Helpers.removeFromList(mSenses, position);
                break;
            default:
                Logger.logUnimplementedFeature(String.format("Unrecognized StringType: %s", type));
                break;
        }
    }

    public String addNewString(@NonNull StringType type) {
        String newString = "";
        switch (type) {
            case CONDITION_IMMUNITY:
                return Helpers.addItemToList(mConditionImmunities, newString);
            case DAMAGE_IMMUNITY:
                return Helpers.addItemToList(mDamageImmunities, newString);
            case DAMAGE_RESISTANCE:
                return Helpers.addItemToList(mDamageResistances, newString);
            case DAMAGE_VULNERABILITY:
                return Helpers.addItemToList(mDamageVulnerabilities, newString);
            case SENSE:
                return Helpers.addItemToList(mSenses, newString);
            default:
                Logger.logUnimplementedFeature(String.format("Unrecognized StringType: %s", type));
                return null;
        }
    }

    public void replaceString(@NonNull StringType type, String oldValue, String newValue) {
        switch (type) {
            case CONDITION_IMMUNITY:
                Helpers.replaceItemInList(mConditionImmunities, oldValue, newValue);
                break;
            case DAMAGE_IMMUNITY:
                Helpers.replaceItemInList(mDamageImmunities, oldValue, newValue);
                break;
            case DAMAGE_RESISTANCE:
                Helpers.replaceItemInList(mDamageResistances, oldValue, newValue);
                break;
            case DAMAGE_VULNERABILITY:
                Helpers.replaceItemInList(mDamageVulnerabilities, oldValue, newValue);
                break;
            case SENSE:
                Helpers.replaceItemInList(mSenses, oldValue, newValue);
                break;
            default:
                Logger.logUnimplementedFeature(String.format("Unrecognized StringType: %s", type));
        }
    }

    public boolean moveTrait(@NonNull TraitType type, int from, int to) {
        switch (type) {
            case ABILITY:
                return Helpers.moveItemInList(mAbilities, from, to);
            case ACTION:
                return Helpers.moveItemInList(mActions, from, to);
            case LAIR_ACTION:
                return Helpers.moveItemInList(mLairActions, from, to);
            case LEGENDARY_ACTION:
                return Helpers.moveItemInList(mLegendaryActions, from, to);
            case REACTIONS:
                return Helpers.moveItemInList(mReactions, from, to);
            case REGIONAL_ACTION:
                return Helpers.moveItemInList(mRegionalActions, from, to);
            default:
                Logger.logUnimplementedFeature(String.format("Unrecognized TraitType: %s", type));
                return false;
        }


    }

    @SuppressWarnings("SameParameterValue")
    private static class Helpers {
        static <T> T addItemToList(MutableLiveData<List<T>> listData, T newItem) {
            return addItemToList(listData, newItem, null);
        }

        static <T> T addItemToList(@NonNull MutableLiveData<List<T>> listData, T newItem, Comparator<? super T> comparator) {
            ArrayList<T> newList = new ArrayList<>(listData.getValue());
            newList.add(newItem);
            if (comparator != null) {
                Collections.sort(newList, comparator);
            }
            listData.setValue(newList);
            return newItem;
        }

        static <T> void removeFromList(@NonNull MutableLiveData<List<T>> listData, int position) {
            List<T> oldList = listData.getValue();
            ArrayList<T> newList = new ArrayList<>(oldList);
            newList.remove(position);
            listData.setValue(newList);
        }

        static <T> void replaceItemInList(@NonNull MutableLiveData<List<T>> listData, int position, T newItem, Comparator<? super T> comparator) {
            List<T> oldList = listData.getValue();
            if (oldList == null) {
                oldList = new ArrayList<>();
            }
            int size = oldList.size();
            boolean hasReplaced = false;
            ArrayList<T> newList = new ArrayList<>(size);
            for (int index = 0; index < size; index++) {
                if (index == position) {
                    newList.add(newItem);
                    hasReplaced = true;
                } else {
                    newList.add(oldList.get(index));
                }
            }
            if (!hasReplaced) {
                newList.add(newItem);
            }
            if (comparator != null) {
                Collections.sort(newList, comparator);
            }
            listData.setValue(newList);
        }

        @SuppressWarnings("unused")
        static <T> void replaceItemInList(MutableLiveData<List<T>> listData, int position, T newItem) {
            replaceItemInList(listData, position, newItem, null);
        }

        static <T> void replaceItemInList(@NonNull MutableLiveData<List<T>> listData, T oldItem, T newItem, Comparator<? super T> comparator) {
            List<T> oldList = listData.getValue();
            if (oldList == null) {
                oldList = new ArrayList<>();
            }
            boolean hasReplaced = false;
            ArrayList<T> newList = new ArrayList<>(oldList.size());
            for (T item : oldList) {
                if (!hasReplaced && Objects.equals(item, oldItem)) {
                    newList.add(newItem);
                    hasReplaced = true;
                } else {
                    newList.add(item);
                }
            }
            if (!hasReplaced) {
                newList.add(newItem);
            }
            if (comparator != null) {
                Collections.sort(newList, comparator);
            }
            listData.setValue(newList);
        }

        static <T> void replaceItemInList(MutableLiveData<List<T>> listData, T oldItem, T newItem) {
            replaceItemInList(listData, oldItem, newItem, null);
        }

        static int unboxInteger(Integer value, int defaultIfNull) {
            if (value == null) {
                return defaultIfNull;
            }
            return value;
        }

        static <T> boolean moveItemInList(@NonNull ChangeTrackedLiveData<List<T>> listData, int from, int to) {
            List<T> oldList = listData.getValue();
            if (oldList == null) {
                oldList = new ArrayList<>();
            }
            ArrayList<T> newList = new ArrayList<>(oldList);
            T item = oldList.get(from);
            if (from > to) {
                from = from + 1;
            } else if (to > from) {
                to = to + 1;
            }
            newList.add(to, item);
            newList.remove(from);
            listData.setValue(newList);
            return true;
        }
    }
}
