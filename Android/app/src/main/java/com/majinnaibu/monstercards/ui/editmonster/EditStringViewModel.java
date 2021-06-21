package com.majinnaibu.monstercards.ui.editmonster;

import androidx.lifecycle.LiveData;

import com.majinnaibu.monstercards.ui.shared.ChangeTrackedViewModel;
import com.majinnaibu.monstercards.utils.ChangeTrackedLiveData;

public class EditStringViewModel extends ChangeTrackedViewModel {
    private final ChangeTrackedLiveData<String> mValue;

    public EditStringViewModel() {
        super();
        mValue = new ChangeTrackedLiveData<>("", this::makeDirty);
    }

    public LiveData<String> getValue() {
        return mValue;
    }

    public void setValue(String value) {
        mValue.setValue(value);
    }

    public String getValueAsString() {
        return mValue.getValue();
    }

    public void resetValue(String value) {
        makeClean();
        mValue.resetValue(value);
    }
}
