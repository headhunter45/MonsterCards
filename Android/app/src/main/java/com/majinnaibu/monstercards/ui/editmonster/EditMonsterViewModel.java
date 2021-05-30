package com.majinnaibu.monstercards.ui.editmonster;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.majinnaibu.monstercards.data.enums.ArmorType;
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
    private final MutableLiveData<ArmorType> mArmorType;
    private final MutableLiveData<Integer> mHitDice;
    private final MutableLiveData<Integer> mNaturalArmorBonus;
    private final MutableLiveData<Integer> mShieldBonus;
    private final MutableLiveData<String> mName;
    private final MutableLiveData<String> mErrorMessage;
    private final MutableLiveData<String> mSize;
    private final MutableLiveData<String> mType;
    private final MutableLiveData<String> mSubtype;
    private final MutableLiveData<String> mAlignment;
    private final MutableLiveData<String> mCustomHitPoints;
    private final MutableLiveData<String> mCustomArmor;

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

        return monster;
    }
}
