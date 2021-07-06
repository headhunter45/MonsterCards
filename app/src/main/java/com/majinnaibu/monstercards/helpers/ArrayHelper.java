package com.majinnaibu.monstercards.helpers;

import androidx.annotation.NonNull;

import java.util.Objects;

public final class ArrayHelper {
    public static int indexOf(@NonNull Object[] array, Object target) {
        for (int index = 0; index < array.length; index++) {
            if (Objects.equals(array[index], target)) {
                return index;
            }
        }

        return -1;
    }
}
