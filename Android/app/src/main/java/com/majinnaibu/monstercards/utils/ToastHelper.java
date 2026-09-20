package com.majinnaibu.monstercards.utils;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

/**
 * Centralized helper for managing Toast notifications and durations across the app.
 */
public class ToastHelper {
    // Configurable default durations
    public static int DEFAULT_SHORT_DURATION = Toast.LENGTH_SHORT; // ~2.0 seconds
    public static int DEFAULT_LONG_DURATION = Toast.LENGTH_LONG;   // ~3.5 seconds

    private static Toast sCurrentToast;

    public static void showShort(@NonNull Context context, @StringRes int resId) {
        show(context, context.getString(resId), DEFAULT_SHORT_DURATION);
    }

    public static void showShort(@NonNull Context context, @NonNull String message) {
        show(context, message, DEFAULT_SHORT_DURATION);
    }

    public static void showLong(@NonNull Context context, @StringRes int resId) {
        show(context, context.getString(resId), DEFAULT_LONG_DURATION);
    }

    public static void showLong(@NonNull Context context, @NonNull String message) {
        show(context, message, DEFAULT_LONG_DURATION);
    }

    public static void show(@NonNull Context context, @NonNull String message, int duration) {
        if (sCurrentToast != null) {
            sCurrentToast.cancel();
        }
        sCurrentToast = Toast.makeText(context.getApplicationContext(), message, duration);
        sCurrentToast.show();
    }
}
