package com.majinnaibu.monstercards.ui.editmonster;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.majinnaibu.monstercards.models.Language;
import com.majinnaibu.monstercards.ui.shared.ChangeTrackedViewModel;
import com.majinnaibu.monstercards.utils.ChangeTrackedLiveData;

public class EditLanguageViewModel extends ChangeTrackedViewModel {
    private final ChangeTrackedLiveData<String> mName;
    private final ChangeTrackedLiveData<Boolean> mCanSpeak;
    private final ChangeTrackedLiveData<Language> mLanguage;

    public EditLanguageViewModel() {
        super();
        mName = new ChangeTrackedLiveData<>("New Language", this::makeDirty);
        mCanSpeak = new ChangeTrackedLiveData<>(true, this::makeDirty);
        mLanguage = new ChangeTrackedLiveData<>(makeLanguage(), this::makeDirty);
    }

    public void copyFromLanguage(@NonNull Language language) {
        mName.resetValue(language.getName());
        mCanSpeak.resetValue(language.getSpeaks());
        makeClean();
    }

    public LiveData<Language> getLanguage() {
        return mLanguage;
    }

    public LiveData<String> getName() {
        return mName;
    }

    public void setName(String name) {
        mName.setValue(name);
        mLanguage.setValue(makeLanguage());
    }

    public LiveData<Boolean> getCanSpeak() {
        return mCanSpeak;
    }

    public void setCanSpeak(boolean canSpeak) {
        mCanSpeak.setValue(canSpeak);
        mLanguage.setValue(makeLanguage());
    }

    public boolean getCanSpeakValue(boolean defaultIfNull) {
        Boolean boxedValue = mCanSpeak.getValue();
        if (boxedValue == null) {
            return defaultIfNull;
        }
        return boxedValue;
    }

    public boolean getCanSpeakValue() {
        return getCanSpeakValue(false);
    }

    @NonNull
    private Language makeLanguage() {
        Boolean boxedValue = mCanSpeak.getValue();
        boolean canSpeak = boxedValue != null && boxedValue;
        return new Language(mName.getValue(), canSpeak);
    }
}
