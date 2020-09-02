package com.majinnaibu.monstercards.helpers;

import java.util.Collection;

@SuppressWarnings({"RedundantIfStatement"})
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

    public static String join(String delimiter, Collection<String> strings) {
        int length = strings.size();
        if (length < 1) {
            return "";
        } else  {
            StringBuilder sb = new StringBuilder();
            boolean isFirst = true;
            for (String element : strings) {
                if (!isFirst) {
                    sb.append(delimiter);
                }

                sb.append(element);

                isFirst = false;
            }
            return sb.toString();
        }
    }
}
