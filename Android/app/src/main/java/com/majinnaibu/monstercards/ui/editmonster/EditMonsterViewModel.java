package com.majinnaibu.monstercards.ui.editmonster;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.majinnaibu.monstercards.data.enums.AdvantageType;
import com.majinnaibu.monstercards.data.enums.ArmorType;
import com.majinnaibu.monstercards.data.enums.ProficiencyType;
import com.majinnaibu.monstercards.helpers.StringHelper;
import com.majinnaibu.monstercards.models.Monster;
import com.majinnaibu.monstercards.utils.Logger;

import java.util.Objects;
import java.util.UUID;

@SuppressWarnings({"ConstantConditions", "unused"})
public class EditMonsterViewModel extends ViewModel {
    private final MutableLiveData<UUID> mMonsterId;
    private final MutableLiveData<Boolean> mHasError;
    private final MutableLiveData<Boolean> mHasLoaded;
    private final MutableLiveData<Boolean> mHasChanges;
    private final MutableLiveData<Boolean> mHasCustomHitPoints;
    private final MutableLiveData<Boolean> mHasShield;
    private final MutableLiveData<Boolean> mCanHover;
    private final MutableLiveData<Boolean> mHasCustomSpeed;
    private final MutableLiveData<ArmorType> mArmorType;
    private final MutableLiveData<ProficiencyType> mStrengthProficiency;
    private final MutableLiveData<AdvantageType> mStrengthAdvantage;
    private final MutableLiveData<ProficiencyType> mDexterityProficiency;
    private final MutableLiveData<AdvantageType> mDexterityAdvantage;
    private final MutableLiveData<ProficiencyType> mConstitutionProficiency;
    private final MutableLiveData<AdvantageType> mConstitutionAdvantage;
    private final MutableLiveData<ProficiencyType> mIntelligenceProficiency;
    private final MutableLiveData<AdvantageType> mIntelligenceAdvantage;
    private final MutableLiveData<ProficiencyType> mWisdomProficiency;
    private final MutableLiveData<AdvantageType> mWisdomAdvantage;
    private final MutableLiveData<ProficiencyType> mCharismaProficiency;
    private final MutableLiveData<AdvantageType> mCharismaAdvantage;
    private final MutableLiveData<Integer> mHitDice;
    private final MutableLiveData<Integer> mNaturalArmorBonus;
    private final MutableLiveData<Integer> mShieldBonus;
    private final MutableLiveData<Integer> mWalkSpeed;
    private final MutableLiveData<Integer> mBurrowSpeed;
    private final MutableLiveData<Integer> mClimbSpeed;
    private final MutableLiveData<Integer> mFlySpeed;
    private final MutableLiveData<Integer> mSwimSpeed;
    private final MutableLiveData<Integer> mStrength;
    private final MutableLiveData<Integer> mDexterity;
    private final MutableLiveData<Integer> mConstitution;
    private final MutableLiveData<Integer> mIntelligence;
    private final MutableLiveData<Integer> mWisdom;
    private final MutableLiveData<Integer> mCharisma;
    private final MutableLiveData<String> mName;
    private final MutableLiveData<String> mErrorMessage;
    private final MutableLiveData<String> mSize;
    private final MutableLiveData<String> mType;
    private final MutableLiveData<String> mSubtype;
    private final MutableLiveData<String> mAlignment;
    private final MutableLiveData<String> mCustomHitPoints;
    private final MutableLiveData<String> mCustomArmor;
    private final MutableLiveData<String> mCustomSpeed;

    public EditMonsterViewModel() {

        mName = new MutableLiveData<>("");
        mMonsterId = new MutableLiveData<>(UUID.randomUUID());
        mErrorMessage = new MutableLiveData<>("");
        mHasError = new MutableLiveData<>(false);
        mHasLoaded = new MutableLiveData<>(false);
        mSize = new MutableLiveData<>("");
        mType = new MutableLiveData<>("");
        mSubtype = new MutableLiveData<>("");
        mAlignment = new MutableLiveData<>("");
        mCustomHitPoints = new MutableLiveData<>("");
        mHitDice = new MutableLiveData<>(0);
        mNaturalArmorBonus = new MutableLiveData<>(0);
        mHasCustomHitPoints = new MutableLiveData<>(false);
        mArmorType = new MutableLiveData<>(ArmorType.NONE);
        mHasShield = new MutableLiveData<>(false);
        mShieldBonus = new MutableLiveData<>(0);
        mCustomArmor = new MutableLiveData<>("");
        mWalkSpeed = new MutableLiveData<>(0);
        mBurrowSpeed = new MutableLiveData<>(0);
        mClimbSpeed = new MutableLiveData<>(0);
        mFlySpeed = new MutableLiveData<>(0);
        mSwimSpeed = new MutableLiveData<>(0);
        mCanHover = new MutableLiveData<>(false);
        mHasCustomSpeed = new MutableLiveData<>(false);
        mCustomSpeed = new MutableLiveData<>("");
        mStrength = new MutableLiveData<>(10);
        mDexterity = new MutableLiveData<>(10);
        mConstitution = new MutableLiveData<>(10);
        mIntelligence = new MutableLiveData<>(10);
        mWisdom = new MutableLiveData<>(10);
        mCharisma = new MutableLiveData<>(10);
        mStrengthProficiency = new MutableLiveData<>(ProficiencyType.NONE);
        mStrengthAdvantage = new MutableLiveData<>(AdvantageType.NONE);
        mDexterityProficiency = new MutableLiveData<>(ProficiencyType.NONE);
        mDexterityAdvantage = new MutableLiveData<>(AdvantageType.NONE);
        mConstitutionProficiency = new MutableLiveData<>(ProficiencyType.NONE);
        mConstitutionAdvantage = new MutableLiveData<>(AdvantageType.NONE);
        mIntelligenceProficiency = new MutableLiveData<>(ProficiencyType.NONE);
        mIntelligenceAdvantage = new MutableLiveData<>(AdvantageType.NONE);
        mWisdomProficiency = new MutableLiveData<>(ProficiencyType.NONE);
        mWisdomAdvantage = new MutableLiveData<>(AdvantageType.NONE);
        mCharismaProficiency = new MutableLiveData<>(ProficiencyType.NONE);
        mCharismaAdvantage = new MutableLiveData<>(AdvantageType.NONE);

        // TODO: consider initializing this to true so all new monsters need saving
        mHasChanges = new MutableLiveData<>(false);
    }

    public void copyFromMonster(Monster monster) {
        // TODO: copy from monster to other fields
        mMonsterId.setValue(monster.id);
        mName.setValue(monster.name);
        mSize.setValue(monster.size);
        mType.setValue(monster.type);
        mSubtype.setValue(monster.subtype);
        mAlignment.setValue(monster.alignment);
        mCustomHitPoints.setValue(monster.customHPDescription);
        mHitDice.setValue(monster.hitDice);
        mNaturalArmorBonus.setValue(monster.naturalArmorBonus);
        mHasCustomHitPoints.setValue(monster.hasCustomHP);
        mArmorType.setValue(monster.armorType);
        mHasShield.setValue(monster.shieldBonus != 0);
        mShieldBonus.setValue(monster.shieldBonus);
        mCustomArmor.setValue(monster.otherArmorDescription);
        mWalkSpeed.setValue(monster.walkSpeed);
        mBurrowSpeed.setValue(monster.burrowSpeed);
        mClimbSpeed.setValue(monster.climbSpeed);
        mFlySpeed.setValue(monster.flySpeed);
        mSwimSpeed.setValue(monster.swimSpeed);
        mCanHover.setValue(monster.canHover);
        mHasCustomSpeed.setValue(monster.hasCustomSpeed);
        mCustomSpeed.setValue(monster.customSpeedDescription);
        mStrength.setValue(monster.strengthScore);
        mDexterity.setValue(monster.dexterityScore);
        mConstitution.setValue(monster.constitutionScore);
        mIntelligence.setValue(monster.intelligenceScore);
        mWisdom.setValue(monster.wisdomScore);
        mCharisma.setValue(monster.charismaScore);
        mStrengthProficiency.setValue(monster.strengthSavingThrowProficiency);
        mStrengthAdvantage.setValue(monster.strengthSavingThrowAdvantage);
        mDexterityProficiency.setValue(monster.dexteritySavingThrowProficiency);
        mDexterityAdvantage.setValue(monster.dexteritySavingThrowAdvantage);
        mConstitutionProficiency.setValue(monster.constitutionSavingThrowProficiency);
        mConstitutionAdvantage.setValue(monster.constitutionSavingThrowAdvantage);
        mIntelligenceProficiency.setValue(monster.intelligenceSavingThrowProficiency);
        mIntelligenceAdvantage.setValue(monster.intelligenceSavingThrowAdvantage);
        mWisdomProficiency.setValue(monster.wisdomSavingThrowProficiency);
        mWisdomAdvantage.setValue(monster.wisdomSavingThrowAdvantage);
        mCharismaProficiency.setValue(monster.charismaSavingThrowProficiency);
        mCharismaAdvantage.setValue(monster.charismaSavingThrowAdvantage);

        mHasChanges.setValue(false);
    }

    public LiveData<String> getName() {
        return mName;
    }

    public void setName(@NonNull String name) {
        if (!Objects.equals(mName.getValue(), name)) {
            mName.setValue(name);
            mHasChanges.setValue(true);
        }
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
        if (!Objects.equals(mSize.getValue(), size)) {
            mSize.setValue(size);
            mHasChanges.setValue(true);
        }
    }

    public LiveData<String> getType() {
        return mType;
    }

    public void setType(@NonNull String type) {
        if (!Objects.equals(mType.getValue(), type)) {
            mType.setValue(type);
            mHasChanges.setValue(true);
        }
    }

    public LiveData<String> getSubtype() {
        return mSubtype;
    }

    public void setSubtype(@NonNull String subtype) {
        if (!Objects.equals(mSubtype.getValue(), subtype)) {
            mSubtype.setValue(subtype);
            mHasChanges.setValue(true);
        }
    }

    public LiveData<String> getAlignment() {
        return mAlignment;
    }

    public void setAlignment(@NonNull String alignment) {
        if (!Objects.equals(mAlignment.getValue(), alignment)) {
            mAlignment.setValue(alignment);
            mHasChanges.setValue(true);
        }
    }

    public LiveData<String> getCustomHitPoints() {
        return mCustomHitPoints;
    }

    public void setCustomHitPoints(String customHitPoints) {
        if (!Objects.equals(mCustomHitPoints.getValue(), customHitPoints)) {
            mCustomHitPoints.setValue(customHitPoints);
            mHasChanges.setValue(true);
        }
    }

    public LiveData<Boolean> getHasChanges() {
        return mHasChanges;
    }

    public void setHasChanges(boolean hasChanges) {
        mHasChanges.setValue(hasChanges);
    }

    public boolean hasChanges() {
        return mHasChanges.getValue();
    }

    public LiveData<Integer> getHitDice() {
        return mHitDice;
    }

    public void setHitDice(int hitDice) {
        if (!Objects.equals(mHitDice.getValue(), hitDice)) {
            mHitDice.setValue(hitDice);
            mHasChanges.setValue(true);
        }
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
        if (!Objects.equals(mNaturalArmorBonus.getValue(), naturalArmorBonus)) {
            mNaturalArmorBonus.setValue(naturalArmorBonus);
            mHasChanges.setValue(true);
        }
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
        if (!Objects.equals(mHasCustomHitPoints.getValue(), hasCustomHitPoints)) {
            mHasCustomHitPoints.setValue(hasCustomHitPoints);
            mHasChanges.setValue(true);
        }
    }

    public boolean getHasCustomHitPointsValueAsBoolean() {
        return mHasCustomHitPoints.getValue();
    }

    public LiveData<ArmorType> getArmorType() {
        return mArmorType;
    }

    public void setArmorType(ArmorType armorType) {
        Logger.logDebug(String.format("Setting ArmorType to %s", armorType.displayName));
        if (!Objects.equals(mArmorType.getValue(), armorType)) {
            mArmorType.setValue(armorType);
            mHasChanges.setValue(true);
        }
    }

    public LiveData<Boolean> getHasShield() {
        return mHasShield;
    }

    public void setHasShield(boolean hasShield) {
        mHasShield.setValue(hasShield);
        mHasChanges.setValue(true);
    }

    public boolean getHasShieldValueAsBoolean() {
        return mHasShield.getValue();
    }

    public LiveData<Integer> getShieldBonus() {
        return mShieldBonus;
    }

    public void setShieldBonus(int shieldBonus) {
        mShieldBonus.setValue(shieldBonus);
        mHasChanges.setValue(true);
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
        mHasChanges.setValue(true);
    }

    public String getShieldBonusValueAsString() {
        return mShieldBonus.getValue().toString();
    }

    public LiveData<Integer> getWalkSpeed() {
        return mWalkSpeed;
    }

    public void setWalkSpeed(int walkSpeed) {
        if (!Objects.equals(mWalkSpeed.getValue(), walkSpeed)) {
            mWalkSpeed.setValue(walkSpeed);
            mHasChanges.setValue(true);
        }
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
        if (!Objects.equals(mBurrowSpeed.getValue(), burrowSpeed)) {
            mBurrowSpeed.setValue(burrowSpeed);
            mHasChanges.setValue(true);
        }
    }

    public LiveData<Integer> getClimbSpeed() {
        return mClimbSpeed;
    }

    public void setClimbSpeed(int climbSpeed) {
        if (!Objects.equals(mClimbSpeed.getValue(), climbSpeed)) {
            mClimbSpeed.setValue(climbSpeed);
            mHasChanges.setValue(true);
        }
    }

    public LiveData<Integer> getFlySpeed() {
        return mFlySpeed;
    }

    public void setFlySpeed(int flySpeed) {
        if (!Objects.equals(mFlySpeed.getValue(), flySpeed)) {
            mFlySpeed.setValue(flySpeed);
            mHasChanges.setValue(true);
        }
    }

    public LiveData<Integer> getSwimSpeed() {
        return mSwimSpeed;
    }

    public void setSwimSpeed(int swimSpeed) {
        if (!Objects.equals(mSwimSpeed.getValue(), swimSpeed)) {
            mSwimSpeed.setValue(swimSpeed);
            mHasChanges.setValue(true);
        }
    }

    public LiveData<Boolean> getCanHover() {
        return mCanHover;
    }

    public void setCanHover(boolean canHover) {
        if (!Objects.equals(mCanHover.getValue(), canHover)) {
            mCanHover.setValue(canHover);
            mHasChanges.setValue(true);
        }
    }

    public LiveData<Boolean> getHasCustomSpeed() {
        return mHasCustomSpeed;
    }

    public void setHasCustomSpeed(boolean hasCustomSpeed) {
        if (!Objects.equals(mHasCustomSpeed.getValue(), hasCustomSpeed)) {
            mHasCustomSpeed.setValue(hasCustomSpeed);
            mHasChanges.setValue(true);
        }
    }

    public LiveData<String> getCustomSpeed() {
        return mCustomSpeed;
    }

    public void setCustomSpeed(String customSpeed) {
        if (!Objects.equals(mCustomSpeed.getValue(), customSpeed)) {
            mCustomSpeed.setValue(customSpeed);
            mHasChanges.setValue(true);
        }
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
        if (!Objects.equals(mStrength.getValue(), strength)) {
            mStrength.setValue(strength);
            mHasChanges.setValue(true);
        }
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
        if (!Objects.equals(mDexterity.getValue(), dexterity)) {
            mDexterity.setValue(dexterity);
            mHasChanges.setValue(true);
        }
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
        if (!Objects.equals(mConstitution.getValue(), constitution)) {
            mConstitution.setValue(constitution);
            mHasChanges.setValue(true);
        }
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
        if (!Objects.equals(mIntelligence.getValue(), intelligence)) {
            mIntelligence.setValue(intelligence);
            mHasChanges.setValue(true);
        }
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
        if (!Objects.equals(mWisdom.getValue(), wisdom)) {
            mWisdom.setValue(wisdom);
            mHasChanges.setValue(true);
        }
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
        if (!Objects.equals(mCharisma.getValue(), charisma)) {
            mCharisma.setValue(charisma);
            mHasChanges.setValue(true);
        }
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
        if (!Objects.equals(mStrengthProficiency.getValue(), proficiency)) {
            mStrengthProficiency.setValue(proficiency);
            mHasChanges.setValue(true);
        }
    }

    public LiveData<AdvantageType> getStrengthAdvantage() {
        return mStrengthAdvantage;
    }

    public void setStrengthAdvantage(AdvantageType advantage) {
        if (!Objects.equals(mStrengthAdvantage.getValue(), advantage)) {
            mStrengthAdvantage.setValue(advantage);
            mHasChanges.setValue(true);
        }
    }

    public LiveData<ProficiencyType> getDexterityProficiency() {
        return mDexterityProficiency;
    }

    public void setDexterityProficiency(ProficiencyType proficiency) {
        if (!Objects.equals(mDexterityProficiency.getValue(), proficiency)) {
            mDexterityProficiency.setValue(proficiency);
            mHasChanges.setValue(true);
        }
    }

    public LiveData<AdvantageType> getDexterityAdvantage() {
        return mDexterityAdvantage;
    }

    public void setDexterityAdvantage(AdvantageType advantage) {
        if (!Objects.equals(mDexterityAdvantage.getValue(), advantage)) {
            mDexterityAdvantage.setValue(advantage);
            mHasChanges.setValue(true);
        }
    }

    public LiveData<ProficiencyType> getConstitutionProficiency() {
        return mConstitutionProficiency;
    }

    public void setConstitutionProficiency(ProficiencyType proficiency) {
        if (!Objects.equals(mConstitutionProficiency.getValue(), proficiency)) {
            mConstitutionProficiency.setValue(proficiency);
            mHasChanges.setValue(true);
        }
    }

    public LiveData<AdvantageType> getConstitutionAdvantage() {
        return mConstitutionAdvantage;
    }

    public void setConstitutionAdvantage(AdvantageType advantage) {
        if (!Objects.equals(mConstitutionAdvantage.getValue(), advantage)) {
            mConstitutionAdvantage.setValue(advantage);
            mHasChanges.setValue(true);
        }
    }

    public LiveData<ProficiencyType> getIntelligenceProficiency() {
        return mIntelligenceProficiency;
    }

    public void setIntelligenceProficiency(ProficiencyType proficiency) {
        if (!Objects.equals(mIntelligenceProficiency.getValue(), proficiency)) {
            mIntelligenceProficiency.setValue(proficiency);
            mHasChanges.setValue(true);
        }
    }

    public LiveData<AdvantageType> getIntelligenceAdvantage() {
        return mIntelligenceAdvantage;
    }

    public void setIntelligenceAdvantage(AdvantageType advantage) {
        if (!Objects.equals(mIntelligenceAdvantage.getValue(), advantage)) {
            mIntelligenceAdvantage.setValue(advantage);
            mHasChanges.setValue(true);
        }
    }

    public LiveData<ProficiencyType> getWisdomProficiency() {
        return mWisdomProficiency;
    }

    public void setWisdomProficiency(ProficiencyType proficiency) {
        if (!Objects.equals(mWisdomProficiency.getValue(), proficiency)) {
            mWisdomProficiency.setValue(proficiency);
            mHasChanges.setValue(true);
        }
    }

    public LiveData<AdvantageType> getWisdomAdvantage() {
        return mWisdomAdvantage;
    }

    public void setWisdomAdvantage(AdvantageType advantage) {
        if (!Objects.equals(mWisdomAdvantage.getValue(), advantage)) {
            mWisdomAdvantage.setValue(advantage);
            mHasChanges.setValue(true);
        }
    }

    public LiveData<ProficiencyType> getCharismaProficiency() {
        return mCharismaProficiency;
    }

    public void setCharismaProficiency(ProficiencyType proficiency) {
        if (!Objects.equals(mCharismaProficiency.getValue(), proficiency)) {
            mCharismaProficiency.setValue(proficiency);
            mHasChanges.setValue(true);
        }
    }

    public LiveData<AdvantageType> getCharismaAdvantage() {
        return mCharismaAdvantage;
    }

    public void setCharismaAdvantage(AdvantageType advantage) {
        if (!Objects.equals(mCharismaAdvantage.getValue(), advantage)) {
            mCharismaAdvantage.setValue(advantage);
            mHasChanges.setValue(true);
        }
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

        return monster;
    }
}
