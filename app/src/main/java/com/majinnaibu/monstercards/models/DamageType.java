package com.majinnaibu.monstercards.models;

public class DamageType {

    public DamageType(String name, String note, String type) {
        mName = name;
        mNote = note;
        mType = type;
    }

    private String mName;

    public String getName() {
        return mName;
    }

    public void setName(String value) {
        mName = value;
    }

    private String mNote;

    public String getNote() {
        return mNote;
    }

    public void setNote(String value) {
        mNote = value;
    }

    private String mType;

    public String getType() {
        return mType;
    }

    public void setType(String value) {
        mType = value;
    }

}
