package com.majinnaibu.monstercards.ui.editmonster;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.majinnaibu.monstercards.models.Monster;

import java.util.UUID;

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

    public void setSubtype(@NonNull String subType) {
        mSubtype.setValue(subType);
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
}
