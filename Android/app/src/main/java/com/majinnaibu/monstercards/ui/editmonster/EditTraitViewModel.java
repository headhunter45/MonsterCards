package com.majinnaibu.monstercards.ui.editmonster;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.majinnaibu.monstercards.models.Trait;
import com.majinnaibu.monstercards.ui.shared.ChangeTrackedViewModel;
import com.majinnaibu.monstercards.utils.ChangeTrackedLiveData;

public class EditTraitViewModel extends ChangeTrackedViewModel {
    private final ChangeTrackedLiveData<String> mName;
    private final ChangeTrackedLiveData<String> mDescription;
    private final MutableLiveData<Trait> mAbility;

    public EditTraitViewModel() {
        super();
        mName = new ChangeTrackedLiveData<>("", this::makeDirty);
        mDescription = new ChangeTrackedLiveData<>("", this::makeDirty);
        mAbility = new MutableLiveData<>(makeAbility());
    }

    public LiveData<String> getName() {
        return mName;
    }

    public void setName(String name) {
        mName.setValue(name);
        mAbility.setValue(makeAbility());
    }

    public String getNameAsString() {
        return mName.getValue();
    }

    public LiveData<String> getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription.setValue(description);
        mAbility.setValue(makeAbility());
    }

    public String getDescriptionAsString() {
        return mDescription.getValue();
    }

    public Trait getAbilityValue() {
        return mAbility.getValue();
    }

    public void copyFromTrait(Trait trait) {
        makeClean();
        mName.resetValue(trait.name);
        mDescription.resetValue(trait.description);
    }

    private Trait makeAbility() {
        return new Trait(mName.getValue(), mDescription.getValue());
    }
}
