package com.majinnaibu.monstercards.ui.editmonster;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.majinnaibu.monstercards.data.enums.AbilityScore;
import com.majinnaibu.monstercards.data.enums.AdvantageType;
import com.majinnaibu.monstercards.data.enums.ProficiencyType;
import com.majinnaibu.monstercards.models.Skill;
import com.majinnaibu.monstercards.utils.ChangeTrackedLiveData;

public class EditSkillViewModel extends ViewModel {
    private final ChangeTrackedLiveData<AbilityScore> mAbilityScore;
    private final ChangeTrackedLiveData<AdvantageType> mAdvantageType;
    private final MutableLiveData<Boolean> mHasChanges;
    private final ChangeTrackedLiveData<ProficiencyType> mProficiencyType;
    private final ChangeTrackedLiveData<String> mName;
    private final ChangeTrackedLiveData<Skill> mSkill;

    public EditSkillViewModel() {
        mHasChanges = new MutableLiveData<>(false);
        ChangeTrackedLiveData.OnValueDirtiedCallback onDirtied = () -> mHasChanges.setValue(true);

        mAbilityScore = new ChangeTrackedLiveData<>(AbilityScore.STRENGTH, onDirtied);
        mAdvantageType = new ChangeTrackedLiveData<>(AdvantageType.NONE, onDirtied);
        mProficiencyType = new ChangeTrackedLiveData<>(ProficiencyType.NONE, onDirtied);
        mName = new ChangeTrackedLiveData<>("Unknown Skill", onDirtied);
        mSkill = new ChangeTrackedLiveData<>(makeSkill(), onDirtied);
    }

    public EditSkillViewModel(Skill skill) {
        mHasChanges = new MutableLiveData<>(false);
        ChangeTrackedLiveData.OnValueDirtiedCallback onDirtied = () -> mHasChanges.setValue(true);

        mAbilityScore = new ChangeTrackedLiveData<>(skill.abilityScore, onDirtied);
        mAdvantageType = new ChangeTrackedLiveData<>(skill.advantageType, onDirtied);
        mProficiencyType = new ChangeTrackedLiveData<>(skill.proficiencyType, onDirtied);
        mName = new ChangeTrackedLiveData<>(skill.name, onDirtied);
        mSkill = new ChangeTrackedLiveData<>(makeSkill(), onDirtied);
    }

    public void copyFromSkill(Skill skill) {
        mAbilityScore.resetValue(skill.abilityScore);
        mAdvantageType.resetValue(skill.advantageType);
        mProficiencyType.resetValue(skill.proficiencyType);
        mName.resetValue(skill.name);
    }

    public void copyFromSkill(String name, AbilityScore abilityScore, ProficiencyType proficiency, AdvantageType advantage) {
        mAbilityScore.resetValue(abilityScore);
        mAdvantageType.resetValue(advantage);
        mProficiencyType.resetValue(proficiency);
        mName.resetValue(name);
    }

    public LiveData<Skill> getSkill() {
        return mSkill;
    }

    public LiveData<AbilityScore> getAbilityScore() {
        return mAbilityScore;
    }

    public void setAbilityScore(AbilityScore value) {
        mAbilityScore.setValue(value);
        mSkill.setValue(makeSkill());
    }

    public LiveData<AdvantageType> getAdvantage() {
        return mAdvantageType;
    }

    public void setAdvantage(AdvantageType value) {
        mAdvantageType.setValue(value);
        mSkill.setValue(makeSkill());
    }

    public LiveData<ProficiencyType> getProficiency() {
        return mProficiencyType;
    }

    public void setProficiency(ProficiencyType value) {
        mProficiencyType.setValue(value);
        mSkill.setValue(makeSkill());
    }

    public LiveData<String> getName() {
        return mName;
    }

    public void setName(String value) {
        mName.setValue(value);
        mSkill.setValue(makeSkill());
    }

    public LiveData<Boolean> getHasChanges() {
        return mHasChanges;
    }

    public boolean hasChanges() {
        return mHasChanges.getValue();
    }

    private Skill makeSkill() {
        return new Skill(mName.getValue(), mAbilityScore.getValue(), mAdvantageType.getValue(), mProficiencyType.getValue());
    }
}
