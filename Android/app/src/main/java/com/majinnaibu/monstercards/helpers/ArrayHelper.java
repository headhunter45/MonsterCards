package com.majinnaibu.monstercards.helpers;

import java.util.Objects;

public final class ArrayHelper {
    public static int indexOf(Object[] array, Object target) {
        for (int index = 0; index < array.length; index++) {
            if (Objects.equals(array[index], target)) {
                return index;
            }
        }

        return -1;
    }
}
