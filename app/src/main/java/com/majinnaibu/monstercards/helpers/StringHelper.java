package com.majinnaibu.monstercards.helpers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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

    @NonNull
    public static String join(String delimiter, @NonNull Collection<String> strings) {
        int length = strings.size();
        if (length < 1) {
            return "";
        } else {
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

    public static String oxfordJoin(String delimiter, String lastDelimiter, String onlyDelimiter, @NonNull Collection<String> strings) {
        int length = strings.size();
        if (length < 1) {
            return "";
        } else if (length == 2) {
            return join(onlyDelimiter, strings);
        } else {
            StringBuilder sb = new StringBuilder();
            int index = 0;
            int lastIndex = length - 1;
            for (String element : strings) {
                if (index > 0 && index < lastIndex) {
                    sb.append(delimiter);
                } else if (index > 0 && index >= lastIndex) {
                    sb.append(lastDelimiter);
                }
                sb.append(element);
                index++;
            }
            return sb.toString();
        }
    }

    @Nullable
    public static Integer parseInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException _ex) {
            return null;
        }
    }

    public static boolean containsCaseInsensitive(@NonNull String text, @NonNull String search) {
        // TODO: find a locale independent way to do this
        return text.toLowerCase().contains(search.toLowerCase());
    }
}
