package com.majinnaibu.monstercards.ui.monster;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MonsterViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MonsterViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is monster fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}