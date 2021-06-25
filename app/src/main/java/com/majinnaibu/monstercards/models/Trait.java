package com.majinnaibu.monstercards.models;

import androidx.annotation.Nullable;

import java.util.Comparator;
import java.util.Objects;

public class Trait implements Comparator<Trait>, Comparable<Trait> {

    public String name;
    public String description;

    public Trait(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public int compareTo(Trait o) {
        return compare(this, o);
    }

    @Override
    public int compare(Trait o1, Trait o2) {
        int result = o1.name.compareToIgnoreCase(o2.name);
        if (result != 0) {
            return result;
        }
        return o1.description.compareToIgnoreCase(o2.description);
    }

    @Override
    public boolean equals(@Nullable @org.jetbrains.annotations.Nullable Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Trait)) {
            return false;
        }
        Trait otherTrait = (Trait) obj;
        if (!Objects.equals(this.name, otherTrait.name)) {
            return false;
        }
        if (!Objects.equals(this.description, otherTrait.description)) {
            return false;
        }
        return true;
    }
}
