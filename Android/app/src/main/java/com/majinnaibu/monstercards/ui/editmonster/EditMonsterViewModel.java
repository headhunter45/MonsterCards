package com.majinnaibu.monstercards.ui.editmonster;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.majinnaibu.monstercards.models.Monster;

import java.util.UUID;

public class EditMonsterViewModel extends ViewModel {
    private MutableLiveData<String> mName;
    private MutableLiveData<UUID> mMonsterId;

    public EditMonsterViewModel() {

        mName = new MutableLiveData<>();
        mName.setValue("");

        mMonsterId = new MutableLiveData<>();
        mMonsterId.setValue(UUID.randomUUID());
    }

    public void copyFromMonster(Monster monster) {
        // TODO: copy from monster to other fields
        mMonsterId.setValue(monster.id);
        mName.setValue(monster.name);
    }

    public LiveData<String> getName() {
        return mName;
    }

    public void setName(@NonNull String name) {
        mName.setValue(name);
    }

}
