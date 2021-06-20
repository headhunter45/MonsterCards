package com.majinnaibu.monstercards.models;

import java.util.Comparator;

public class Trait implements Comparator<Trait>, Comparable<Trait> {

    public String name;
    public String description;

    public Trait(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public int compareTo(Trait o) {
        return this.name.compareToIgnoreCase(o.name);
    }

    @Override
    public int compare(Trait o1, Trait o2) {
        return o1.name.compareToIgnoreCase(o2.name);
    }

}
