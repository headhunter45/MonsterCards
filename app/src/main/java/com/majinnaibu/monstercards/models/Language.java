package com.majinnaibu.monstercards.models;

import java.util.Comparator;

public class Language implements Comparator<Language>, Comparable<Language> {

    public Language(String name, boolean speaks) {
        mName = name;
        mSpeaks = speaks;
    }

    private String mName;

    public String getName() {
        return mName;
    }

    public void setName(String value) {
        mName = value;
    }

    private boolean mSpeaks;

    public boolean getSpeaks() {
        return mSpeaks;
    }

    public void setSpeaks(boolean value) {
        mSpeaks = value;
    }

    @Override
    public int compareTo(Language o) {
        return this.getName().compareToIgnoreCase(o.getName());
    }

    @Override
    public int compare(Language o1, Language o2) {
        return o1.getName().compareToIgnoreCase(o2.getName());
    }
}
