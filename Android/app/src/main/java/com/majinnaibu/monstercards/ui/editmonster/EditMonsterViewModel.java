package com.majinnaibu.monstercards.ui.editmonster;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.majinnaibu.monstercards.data.enums.AbilityScore;
import com.majinnaibu.monstercards.data.enums.AdvantageType;
import com.majinnaibu.monstercards.data.enums.ArmorType;
import com.majinnaibu.monstercards.data.enums.ChallengeRating;
import com.majinnaibu.monstercards.data.enums.ProficiencyType;
import com.majinnaibu.monstercards.helpers.StringHelper;
import com.majinnaibu.monstercards.models.Language;
import com.majinnaibu.monstercards.models.Monster;
import com.majinnaibu.monstercards.models.Skill;
import com.majinnaibu.monstercards.models.Trait;
import com.majinnaibu.monstercards.utils.ChangeTrackedLiveData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@SuppressWarnings({"ConstantConditions", "unused"})
public class EditMonsterViewModel extends ViewModel {
    private final ChangeTrackedLiveData<UUID> mMonsterId;
    private final MutableLiveData<Boolean> mHasError;
    private final MutableLiveData<Boolean> mHasLoaded;
    private final MutableLiveData<Boolean> mHasChanges;
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
        mErrorMessage = new MutableLiveData<>("");
        mHasError = new MutableLiveData<>(false);
        mHasLoaded = new MutableLiveData<>(false);
        mHasChanges = new MutableLiveData<>(false);
        ChangeTrackedLiveData.OnValueDirtiedCallback onDirtied = () -> mHasChanges.setValue(true);

        mName = new ChangeTrackedLiveData<>("", onDirtied);
        mMonsterId = new ChangeTrackedLiveData<>(UUID.randomUUID(), onDirtied);
        mSize = new ChangeTrackedLiveData<>("", onDirtied);
        mType = new ChangeTrackedLiveData<>("", onDirtied);
        mSubtype = new ChangeTrackedLiveData<>("", onDirtied);
        mAlignment = new ChangeTrackedLiveData<>("", onDirtied);
        mCustomHitPoints = new ChangeTrackedLiveData<>("", onDirtied);
        mHitDice = new ChangeTrackedLiveData<>(0, onDirtied);
        mNaturalArmorBonus = new ChangeTrackedLiveData<>(0, onDirtied);
        mHasCustomHitPoints = new ChangeTrackedLiveData<>(false, onDirtied);
        mArmorType = new ChangeTrackedLiveData<>(ArmorType.NONE, onDirtied);
        mHasShield = new ChangeTrackedLiveData<>(false, onDirtied);
        mShieldBonus = new ChangeTrackedLiveData<>(0, onDirtied);
        mCustomArmor = new ChangeTrackedLiveData<>("", onDirtied);
        mWalkSpeed = new ChangeTrackedLiveData<>(0, onDirtied);
        mBurrowSpeed = new ChangeTrackedLiveData<>(0, onDirtied);
        mClimbSpeed = new ChangeTrackedLiveData<>(0, onDirtied);
        mFlySpeed = new ChangeTrackedLiveData<>(0, onDirtied);
        mSwimSpeed = new ChangeTrackedLiveData<>(0, onDirtied);
        mCanHover = new ChangeTrackedLiveData<>(false, onDirtied);
        mHasCustomSpeed = new ChangeTrackedLiveData<>(false, onDirtied);
        mCustomSpeed = new ChangeTrackedLiveData<>("", onDirtied);
        mStrength = new ChangeTrackedLiveData<>(10, onDirtied);
        mDexterity = new ChangeTrackedLiveData<>(10, onDirtied);
        mConstitution = new ChangeTrackedLiveData<>(10, onDirtied);
        mIntelligence = new ChangeTrackedLiveData<>(10, onDirtied);
        mWisdom = new ChangeTrackedLiveData<>(10, onDirtied);
        mCharisma = new ChangeTrackedLiveData<>(10, onDirtied);
        mStrengthProficiency = new ChangeTrackedLiveData<>(ProficiencyType.NONE, onDirtied);
        mStrengthAdvantage = new ChangeTrackedLiveData<>(AdvantageType.NONE, onDirtied);
        mDexterityProficiency = new ChangeTrackedLiveData<>(ProficiencyType.NONE, onDirtied);
        mDexterityAdvantage = new ChangeTrackedLiveData<>(AdvantageType.NONE, onDirtied);
        mConstitutionProficiency = new ChangeTrackedLiveData<>(ProficiencyType.NONE, onDirtied);
        mConstitutionAdvantage = new ChangeTrackedLiveData<>(AdvantageType.NONE, onDirtied);
        mIntelligenceProficiency = new ChangeTrackedLiveData<>(ProficiencyType.NONE, onDirtied);
        mIntelligenceAdvantage = new ChangeTrackedLiveData<>(AdvantageType.NONE, onDirtied);
        mWisdomProficiency = new ChangeTrackedLiveData<>(ProficiencyType.NONE, onDirtied);
        mWisdomAdvantage = new ChangeTrackedLiveData<>(AdvantageType.NONE, onDirtied);
        mCharismaProficiency = new ChangeTrackedLiveData<>(ProficiencyType.NONE, onDirtied);
        mCharismaAdvantage = new ChangeTrackedLiveData<>(AdvantageType.NONE, onDirtied);
        mChallengeRating = new ChangeTrackedLiveData<>(ChallengeRating.ONE_EIGHTH, onDirtied);
        mCustomChallengeRatingDescription = new ChangeTrackedLiveData<>("", onDirtied);
        mCustomProficiencyBonus = new ChangeTrackedLiveData<>(0, onDirtied);
        mTelepathyRange = new ChangeTrackedLiveData<>(0, onDirtied);
        mUnderstandsButDescription = new ChangeTrackedLiveData<>("", onDirtied);
        mSkills = new ChangeTrackedLiveData<>(new ArrayList<>(), onDirtied);
        mSenses = new ChangeTrackedLiveData<>(new ArrayList<>(), onDirtied);
        mDamageImmunities = new ChangeTrackedLiveData<>(new ArrayList<>(), onDirtied);
        mDamageResistances = new ChangeTrackedLiveData<>(new ArrayList<>(), onDirtied);
        mDamageVulnerabilities = new ChangeTrackedLiveData<>(new ArrayList<>(), onDirtied);
        mConditionImmunities = new ChangeTrackedLiveData<>(new ArrayList<>(), onDirtied);
        mLanguages = new ChangeTrackedLiveData<>(new ArrayList<>(), onDirtied);
        mAbilities = new ChangeTrackedLiveData<>(new ArrayList<>(), onDirtied);
        mActions = new ChangeTrackedLiveData<>(new ArrayList<>(), onDirtied);
        mReactions = new ChangeTrackedLiveData<>(new ArrayList<>(), onDirtied);
        mLairActions = new ChangeTrackedLiveData<>(new ArrayList<>(), onDirtied);
        mLegendaryActions = new ChangeTrackedLiveData<>(new ArrayList<>(), onDirtied);
        mRegionalActions = new ChangeTrackedLiveData<>(new ArrayList<>(), onDirtied);
    }

    public void copyFromMonster(Monster monster) {
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
        Collections.sort(skills, (skill1, skill2) -> skill1.name.compareToIgnoreCase(skill2.name));
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
        Collections.sort(languages, (lang1, lang2) -> lang1.getName().compareToIgnoreCase(lang2.getName()));
        mLanguages.resetValue(languages);
        mAbilities.resetValue(new ArrayList<>(monster.abilities));
        mActions.resetValue(new ArrayList<>(monster.actions));
        mReactions.resetValue(new ArrayList<>(monster.reactions));
        mLairActions.resetValue(new ArrayList<>(monster.lairActions));
        mLegendaryActions.resetValue(new ArrayList<>(monster.legendaryActions));
        mRegionalActions.resetValue(new ArrayList<>(monster.regionalActions));
        mHasChanges.setValue(false);
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

    public LiveData<String> getErrorMessage() {
        return mErrorMessage;
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

    public LiveData<Boolean> getHasChanges() {
        return mHasChanges;
    }

    public boolean hasChanges() {
        return mHasChanges.getValue();
    }

    public LiveData<Integer> getHitDice() {
        return mHitDice;
    }

    public void setHitDice(int hitDice) {
        mHitDice.setValue(hitDice);
    }

    public void setHitDice(String hitDice) {
        Integer parsedHitDice = StringHelper.parseInt(hitDice);
        this.setHitDice(parsedHitDice != null ? parsedHitDice : 0);
    }

    public String getHitDiceValueAsString() {
        return mHitDice.getValue().toString();
    }

    public LiveData<Integer> getNaturalArmorBonus() {
        return mNaturalArmorBonus;
    }

    public void setNaturalArmorBonus(int naturalArmorBonus) {
        mNaturalArmorBonus.setValue(naturalArmorBonus);
    }

    public void setNaturalArmorBonus(String naturalArmorBonus) {
        Integer parsedValue = StringHelper.parseInt(naturalArmorBonus);
        this.setNaturalArmorBonus(parsedValue != null ? parsedValue : 0);
    }

    public String getNaturalArmorBonusValueAsString() {
        return mNaturalArmorBonus.getValue().toString();
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

    public LiveData<Boolean> getHasShield() {
        return mHasShield;
    }

    public void setHasShield(boolean hasShield) {
        mHasShield.setValue(hasShield);
    }

    public boolean getHasShieldValueAsBoolean() {
        return mHasShield.getValue();
    }

    public LiveData<Integer> getShieldBonus() {
        return mShieldBonus;
    }

    public void setShieldBonus(int shieldBonus) {
        mShieldBonus.setValue(shieldBonus);
    }

    public void setShieldBonus(String shieldBonus) {
        Integer parsedValue = StringHelper.parseInt(shieldBonus);
        this.setShieldBonus(parsedValue != null ? parsedValue : 0);
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

    public void incrementWalkSpeed() {
        setWalkSpeed(mWalkSpeed.getValue() + 5);
    }

    public void decrementWalkSpeed() {
        setWalkSpeed(mWalkSpeed.getValue() - 5);
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

    public void incrementBurrowSpeed() {
        setBurrowSpeed(mBurrowSpeed.getValue() + 5);
    }

    public void decrementBurrowSpeed() {
        setBurrowSpeed(mBurrowSpeed.getValue() - 5);
    }

    public void incrementClimbSpeed() {
        setClimbSpeed(mClimbSpeed.getValue() + 5);
    }

    public void decrementClimbSpeed() {
        setClimbSpeed(mClimbSpeed.getValue() - 5);
    }

    public void incrementFlySpeed() {
        setFlySpeed(mFlySpeed.getValue() + 5);
    }

    public void decrementFlySpeed() {
        setFlySpeed(mFlySpeed.getValue() - 5);
    }

    public void incrementSwimSpeed() {
        setSwimSpeed(mSwimSpeed.getValue() + 5);
    }

    public void decrementSwimSpeed() {
        setSwimSpeed(mSwimSpeed.getValue() - 5);
    }

    public LiveData<Integer> getStrength() {
        return mStrength;
    }

    public void setStrength(int strength) {
        mStrength.setValue(strength);
    }

    public void incrementStrength() {
        setStrength(mStrength.getValue() + 1);
    }

    public void decrementStrength() {
        setStrength(mStrength.getValue() - 1);
    }

    public LiveData<Integer> getDexterity() {
        return mDexterity;
    }

    public void setDexterity(int dexterity) {
        mDexterity.setValue(dexterity);
    }

    public void incrementDexterity() {
        setDexterity(mDexterity.getValue() + 1);
    }

    public void decrementDexterity() {
        setDexterity(mDexterity.getValue() - 1);
    }

    public LiveData<Integer> getConstitution() {
        return mConstitution;
    }

    public void setConstitution(int constitution) {
        mConstitution.setValue(constitution);
    }

    public void incrementConstitution() {
        setConstitution(mConstitution.getValue() + 1);
    }

    public void decrementConstitution() {
        setConstitution(mConstitution.getValue() - 1);
    }

    public LiveData<Integer> getIntelligence() {
        return mIntelligence;
    }

    public void setIntelligence(int intelligence) {
        mIntelligence.setValue(intelligence);
    }

    public void incrementIntelligence() {
        setIntelligence(mIntelligence.getValue() + 1);
    }

    public void decrementIntelligence() {
        setIntelligence(mIntelligence.getValue() - 1);
    }

    public LiveData<Integer> getWisdom() {
        return mWisdom;
    }

    public void setWisdom(int wisdom) {
        mWisdom.setValue(wisdom);
    }

    public void incrementWisdom() {
        setWisdom(mWisdom.getValue() + 1);
    }

    public void decrementWisdom() {
        setWisdom(mWisdom.getValue() - 1);
    }

    public LiveData<Integer> getCharisma() {
        return mCharisma;
    }

    public void setCharisma(int charisma) {
        mCharisma.setValue(charisma);
    }

    public void incrementCharisma() {
        setCharisma(mCharisma.getValue() + 1);
    }

    public void decrementCharisma() {
        setCharisma(mCharisma.getValue() - 1);
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

    public List<String> getSensesArray() {
        return mSenses.getValue();
    }

    public String addNewSense() {
        return Helpers.addItemToList(mSenses, "", String::compareToIgnoreCase);
    }

    public void removeSense(int position) {
        Helpers.removeFromList(mSenses, position);
    }

    public void replaceSense(String oldSense, String newSense) {
        Helpers.replaceItemInList(mSenses, oldSense, newSense, String::compareToIgnoreCase);
    }

    public LiveData<List<String>> getDamageImmunities() {
        return mDamageImmunities;
    }

    public List<String> getDamageImmunitiesArray() {
        return mDamageImmunities.getValue();
    }

    public String addNewDamageImmunity() {
        return Helpers.addStringToList("", mDamageImmunities);
    }

    public void removeDamageImmunity(int position) {
        Helpers.removeFromList(mDamageImmunities, position);
    }

    public void replaceDamageImmunity(String oldDamageType, String newDamageType) {
        Helpers.replaceItemInList(mDamageImmunities, oldDamageType, newDamageType, String::compareToIgnoreCase);
    }

    public LiveData<List<String>> getDamageResistances() {
        return mDamageResistances;
    }

    public List<String> getDamageResistancesArray() {
        return mDamageResistances.getValue();
    }

    public String addNewDamageResistance() {
        return Helpers.addStringToList("", mDamageResistances);
    }

    public void removeDamageResistance(int position) {
        Helpers.removeFromList(mDamageResistances, position);
    }

    public void replaceDamageResistance(String oldDamageType, String newDamageType) {
        Helpers.replaceItemInList(mDamageResistances, oldDamageType, newDamageType, String::compareToIgnoreCase);
    }

    public LiveData<List<String>> getDamageVulnerabilities() {
        return mDamageVulnerabilities;
    }

    public List<String> getDamageVulnerabilitiesArray() {
        return mDamageVulnerabilities.getValue();
    }

    public String addNewDamageVulnerability() {
        return Helpers.addStringToList("", mDamageVulnerabilities);
    }

    public void removeDamageVulnerability(int position) {
        Helpers.removeFromList(mDamageVulnerabilities, position);
    }

    public void replaceDamageVulnerability(String oldDamageType, String newDamageType) {
        Helpers.replaceItemInList(mDamageVulnerabilities, oldDamageType, newDamageType, String::compareToIgnoreCase);
    }

    public LiveData<List<String>> getConditionImmunities() {
        return mConditionImmunities;
    }

    public List<String> getConditionImmunitiesArray() {
        return mConditionImmunities.getValue();
    }

    public String addNewConditionImmunity() {
        return Helpers.addStringToList("", mConditionImmunities);
    }

    public void removeConditionImmunity(int position) {
        Helpers.removeFromList(mConditionImmunities, position);
    }

    public void replaceConditionImmunity(String oldDamageType, String newDamageType) {
        Helpers.replaceItemInList(mConditionImmunities, oldDamageType, newDamageType, String::compareToIgnoreCase);
    }

    // TODO: add getters and setters for Languages
    // TODO: add getters and setters for traits (Abilities, Actions, Reactions, Lair Actions, Legendary Actions, and Regional Actions)

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
        monster.abilities = new HashSet<>(mAbilities.getValue());
        monster.actions = new HashSet<>(mActions.getValue());
        monster.reactions = new HashSet<>(mReactions.getValue());
        monster.lairActions = new HashSet<>(mLairActions.getValue());
        monster.legendaryActions = new HashSet<>(mLegendaryActions.getValue());
        monster.regionalActions = new HashSet<>(mRegionalActions.getValue());

        return monster;
    }

    private static class Helpers {
        static String addStringToList(String newString, MutableLiveData<List<String>> strings) {
            return addItemToList(strings, newString, String::compareToIgnoreCase);
        }

        static <T> T addItemToList(MutableLiveData<List<T>> listData, T newItem, Comparator<? super T> comparator) {
            ArrayList<T> newList = new ArrayList<>(listData.getValue());
            newList.add(newItem);
            if (comparator != null) {
                Collections.sort(newList, comparator);
            }
            listData.setValue(newList);
            return newItem;
        }

        static <T> void removeFromList(MutableLiveData<List<T>> listData, int position) {
            List<T> oldList = listData.getValue();
            ArrayList<T> newList = new ArrayList<>(oldList);
            newList.remove(position);
            listData.setValue(newList);
        }

        static <T> void replaceItemInList(MutableLiveData<List<T>> listData, int position, T newItem, Comparator<? super T> comparator) {
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

        static <T> void replaceItemInList(MutableLiveData<List<T>> listData, T oldItem, T newItem, Comparator<? super T> comparator) {
            List<T> oldList = listData.getValue();
            if (oldList == null) {
                oldList = new ArrayList<>();
            }
            boolean hasReplaced = false;
            ArrayList<T> newList = new ArrayList<>(oldList.size());
            for (T item : oldList) {
                if (Objects.equals(item, oldItem)) {
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
    }
}
