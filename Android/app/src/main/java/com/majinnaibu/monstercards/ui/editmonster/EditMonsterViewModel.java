package com.majinnaibu.monstercards.ui.editmonster;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.majinnaibu.monstercards.models.Monster;

import java.util.UUID;

@SuppressWarnings({"ConstantConditions", "unused"})
public class EditMonsterViewModel extends ViewModel {
    private final MutableLiveData<String> mName;
    private final MutableLiveData<UUID> mMonsterId;
    private final MutableLiveData<String> mErrorMessage;
    private final MutableLiveData<Boolean> mHasError;
    private final MutableLiveData<Boolean> mHasLoaded;
    private final MutableLiveData<String> mSize;
    private final MutableLiveData<String> mType;
    private final MutableLiveData<String> mSubtype;
    private final MutableLiveData<String> mAlignment;
    private final MutableLiveData<String> mCustomHitPoints;
    private final MutableLiveData<Boolean> mHasChanges;

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
        mHasChanges.setValue(false);
    }

    public LiveData<String> getName() {
        return mName;
    }

    public void setName(@NonNull String name) {
        mName.setValue(name);
        mHasChanges.setValue(true);
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
        mHasChanges.setValue(true);
    }

    public LiveData<String> getType() {
        return mType;
    }

    public void setType(@NonNull String type) {
        mType.setValue(type);
        mHasChanges.setValue(true);
    }

    public LiveData<String> getSubtype() {
        return mSubtype;
    }

    public void setSubtype(@NonNull String subType) {
        mSubtype.setValue(subType);
        mHasChanges.setValue(true);
    }

    public LiveData<String> getAlignment() {
        return mAlignment;
    }

    public void setAlignment(@NonNull String alignment) {
        mAlignment.setValue(alignment);
        mHasChanges.setValue(true);
    }

    public LiveData<String> getCustomHitPoints() {
        return mCustomHitPoints;
    }

    public void setCustomHitPoints(String customHitPoints) {
        mCustomHitPoints.setValue(customHitPoints);
        mHasChanges.setValue(true);
    }

    public LiveData<Boolean> getHasChanges() {
        return mHasChanges;
    }

    public void setHasChanges(@NonNull Boolean hasChanges) {
        mHasChanges.setValue(hasChanges);
    }

    public boolean hasChanges() {
        return mHasChanges.getValue();
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

        return monster;
    }
}
