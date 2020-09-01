package com.majinnaibu.monstercards.helpers;

@SuppressWarnings({"BooleanMethodIsAlwaysInverted", "RedundantIfStatement"})
public final class StringHelper {
    public static boolean isNullOrEmpty(CharSequence value) {
        if (value == null) {
            return true;
        }

        if ("".contentEquals(value)) {
            return true;
        }

        return false;
    }
}
