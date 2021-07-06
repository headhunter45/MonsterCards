package com.majinnaibu.monstercards.ui.editmonster;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.majinnaibu.monstercards.data.enums.AbilityScore;
import com.majinnaibu.monstercards.data.enums.AdvantageType;
import com.majinnaibu.monstercards.data.enums.ProficiencyType;
import com.majinnaibu.monstercards.models.Skill;
import com.majinnaibu.monstercards.ui.shared.ChangeTrackedViewModel;
import com.majinnaibu.monstercards.utils.ChangeTrackedLiveData;

public class EditSkillViewModel extends ChangeTrackedViewModel {
    private final ChangeTrackedLiveData<AbilityScore> mAbilityScore;
    private final ChangeTrackedLiveData<AdvantageType> mAdvantageType;
    private final ChangeTrackedLiveData<ProficiencyType> mProficiencyType;
    private final ChangeTrackedLiveData<String> mName;
    private final ChangeTrackedLiveData<Skill> mSkill;

    public EditSkillViewModel() {
        super();
        mAbilityScore = new ChangeTrackedLiveData<>(AbilityScore.STRENGTH, this::makeDirty);
        mAdvantageType = new ChangeTrackedLiveData<>(AdvantageType.NONE, this::makeDirty);
        mProficiencyType = new ChangeTrackedLiveData<>(ProficiencyType.NONE, this::makeDirty);
        mName = new ChangeTrackedLiveData<>("New Skill", this::makeDirty);
        mSkill = new ChangeTrackedLiveData<>(makeSkill(), this::makeDirty);
    }

    public void copyFromSkill(@NonNull Skill skill) {
        mAbilityScore.resetValue(skill.abilityScore);
        mAdvantageType.resetValue(skill.advantageType);
        mProficiencyType.resetValue(skill.proficiencyType);
        mName.resetValue(skill.name);
        makeClean();
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

    @NonNull
    private Skill makeSkill() {
        return new Skill(mName.getValue(), mAbilityScore.getValue(), mAdvantageType.getValue(), mProficiencyType.getValue());
    }
}
