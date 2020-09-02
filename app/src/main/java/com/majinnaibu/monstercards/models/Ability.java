package com.majinnaibu.monstercards.models;

public class Ability {

    public Ability(String name, String description) {
        mName = name;
        mDescription = description;
    }

    private String mName;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    private String mDescription;

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }
}
