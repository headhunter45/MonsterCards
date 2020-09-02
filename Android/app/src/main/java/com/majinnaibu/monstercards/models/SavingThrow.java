package com.majinnaibu.monstercards.models;

import java.util.Comparator;

public class SavingThrow implements Comparator<SavingThrow>, Comparable<SavingThrow> {

    public SavingThrow(String name, int order) {
        mName = name;
        mOrder = order;
    }

    private String mName;

    public String getName() {
        return mName;
    }

    public void setName(String value) {
        mName = value;
    }

    private int mOrder;

    public int getOrder() {
        return mOrder;
    }

    public void setOrder(int value) {
        mOrder = value;
    }

    @Override
    public int compareTo(SavingThrow o) {
        return Integer.compare(this.getOrder(), o.getOrder());
    }

    @Override
    public int compare(SavingThrow o1, SavingThrow o2) {
        return o1.getOrder() - o2.getOrder();
    }
}
