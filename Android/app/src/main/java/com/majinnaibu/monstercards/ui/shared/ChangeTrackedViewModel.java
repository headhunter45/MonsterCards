package com.majinnaibu.monstercards.ui.shared;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ChangeTrackedViewModel extends ViewModel {
    private final MutableLiveData<Boolean> mHasChanges;

    public ChangeTrackedViewModel() {
        mHasChanges = new MutableLiveData<>(false);
    }

    public boolean hasChanges() {
        Boolean value = mHasChanges.getValue();
        return value != null && value;
    }

    protected void makeDirty() {
        mHasChanges.setValue(true);
    }

    protected void makeClean() {
        mHasChanges.setValue(false);
    }
}
