package com.majinnaibu.monstercards.models;

import androidx.annotation.Nullable;

import java.util.Comparator;
import java.util.Objects;

public class Language implements Comparator<Language>, Comparable<Language> {

    private String mName;
    private boolean mSpeaks;

    public Language(String name, boolean speaks) {
        mName = name;
        mSpeaks = speaks;
    }

    public String getName() {
        return mName;
    }

    public void setName(String value) {
        mName = value;
    }

    public boolean getSpeaks() {
        return mSpeaks;
    }

    public void setSpeaks(boolean value) {
        mSpeaks = value;
    }

    @Override
    public int compareTo(Language o) {
        if (this.mSpeaks && !o.mSpeaks) {
            return -1;
        }
        if (!this.mSpeaks && o.mSpeaks) {
            return 1;
        }
        return this.mName.compareToIgnoreCase(o.mName);
    }

    @Override
    public int compare(Language o1, Language o2) {
        if (o1.mSpeaks && !o2.mSpeaks) {
            return -1;
        }
        if (!o1.mSpeaks && o2.mSpeaks) {
            return 1;
        }
        return o1.mName.compareToIgnoreCase(o2.mName);
    }

    @Override
    public boolean equals(@Nullable @org.jetbrains.annotations.Nullable Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Language)) {
            return false;
        }
        Language otherLanguage = (Language) obj;
        if (!Objects.equals(this.mName, otherLanguage.mName)) {
            return false;
        }
        if (this.mSpeaks != otherLanguage.mSpeaks) {
            return false;
        }
        return true;
    }
}
