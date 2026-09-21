package com.majinnaibu.monstercards.utils;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.google.android.material.snackbar.Snackbar;

/**
 * Centralized helper for managing Snackbar notifications and durations across the app.
 */
public class SnackbarHelper {
    // Configurable default durations in milliseconds
    public static int DEFAULT_SHORT_DURATION = 2500; // 2.5 seconds
    public static int DEFAULT_LONG_DURATION = 4000;  // 4.0 seconds (e.g. for Undo or action messages)

    public static Snackbar makeShort(@NonNull View view, @StringRes int resId) {
        return make(view, view.getContext().getString(resId), DEFAULT_SHORT_DURATION);
    }

    public static Snackbar makeShort(@NonNull View view, @NonNull CharSequence message) {
        return make(view, message, DEFAULT_SHORT_DURATION);
    }

    public static Snackbar makeLong(@NonNull View view, @StringRes int resId) {
        return make(view, view.getContext().getString(resId), DEFAULT_LONG_DURATION);
    }

    public static Snackbar makeLong(@NonNull View view, @NonNull CharSequence message) {
        return make(view, message, DEFAULT_LONG_DURATION);
    }

    public static Snackbar makeIndefinite(@NonNull View view, @StringRes int resId) {
        return Snackbar.make(view, resId, Snackbar.LENGTH_INDEFINITE);
    }

    public static Snackbar makeIndefinite(@NonNull View view, @NonNull CharSequence message) {
        return Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE);
    }

    public static Snackbar make(@NonNull View view, @NonNull CharSequence message, int durationMs) {
        return Snackbar.make(view, message, durationMs);
    }

    public static void showShort(@NonNull View view, @StringRes int resId) {
        makeShort(view, resId).show();
    }

    public static void showShort(@NonNull View view, @NonNull CharSequence message) {
        makeShort(view, message).show();
    }

    public static void showLong(@NonNull View view, @StringRes int resId) {
        makeLong(view, resId).show();
    }

    public static void showLong(@NonNull View view, @NonNull CharSequence message) {
        makeLong(view, message).show();
    }
}
