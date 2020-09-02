package com.majinnaibu.monstercards.models;

import java.util.Comparator;
import java.util.Locale;

public class Skill implements Comparator<Skill>, Comparable<Skill> {

    private String mName;
    private String mAbilityScoreName;
    private String mNote;

    public Skill(String name, String abilityScoreName) {
        mName = name;
        mAbilityScoreName = abilityScoreName;
        mNote = "";
    }

    public Skill(String name, String abilityScoreName, String note) {
        mName = name;
        mAbilityScoreName = abilityScoreName;
        mNote = note;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getAbilityScoreName() {
        return mAbilityScoreName;
    }

    public void setAbilityScoreName(String abilityScoreName) {
        mAbilityScoreName = abilityScoreName;
    }

    public String getNote() {
        return mNote;
    }

    public int getSkillBonus(Monster monster) {
        int bonus = monster.getAbilityModifier(mAbilityScoreName);
        if (" (ex)".equals(getNote())) {
            bonus += 2 * monster.getProficiencyBonus();
        } else {
            bonus += monster.getProficiencyBonus();
        }
        return bonus;
    }

    public String getText(Monster monster) {
        int bonus = getSkillBonus(monster);

        return String.format(Locale.US, "%s%s %d", mName.substring(0, 1), mName.substring(1), bonus);
    }

    @Override
    public int compareTo(Skill o) {
        return this.getName().compareToIgnoreCase(o.getName());
    }

    @Override
    public int compare(Skill o1, Skill o2) {
        return o1.getName().compareToIgnoreCase(o2.getName());
    }

}
