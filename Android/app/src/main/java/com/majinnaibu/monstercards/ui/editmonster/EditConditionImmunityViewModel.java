package com.majinnaibu.monstercards.ui.editmonster;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.majinnaibu.monstercards.utils.ChangeTrackedLiveData;

public class EditConditionImmunityViewModel extends ViewModel {
    private final ChangeTrackedLiveData<String> mDescription;
    private final MutableLiveData<Boolean> mHasChanges;

    public EditConditionImmunityViewModel() {
        mHasChanges = new MutableLiveData<>(false);
        ChangeTrackedLiveData.OnValueDirtiedCallback onDirtied = () -> mHasChanges.setValue(true);
        mDescription = new ChangeTrackedLiveData<>("", onDirtied);
    }

    public void reset(String description) {
        mHasChanges.setValue(false);
        mDescription.resetValue(description);
    }

    public LiveData<String> getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription.setValue(description);
    }

    public boolean hasChanges() {
        Boolean value = mHasChanges.getValue();
        return value != null && value;
    }
}