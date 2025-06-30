package com.majinnaibu.monstercards.utils;

import android.util.Log;

@SuppressWarnings("unused")
public class Logger {
    public static final String LOG_TAG = "MonsterCards";

    public static void logUnimplementedMethod() {
        Exception ex = new Exception();
        StackTraceElement[] stackTrace = ex.getStackTrace();

        String location = stackTrace[1].getClassName() + "." + stackTrace[1].getMethodName() + ":" + stackTrace[1].getLineNumber();
        logDebug("Method not yet implemented " + location);
    }

    public static void logUnhandledError(Throwable e) {
        StackTraceElement stackTraceElement = e.getStackTrace()[0];

        String location = stackTraceElement.getClassName() + "." + stackTraceElement.getMethodName() + ":" + stackTraceElement.getLineNumber();
        logDebug("Exception was caught but not properly handled " + location);
    }

    public static void logUnimplementedFeature(String featureDescription) {
        Exception ex = new Exception();
        StackTraceElement[] stackTrace = ex.getStackTrace();

        String location = stackTrace[1].getClassName() + "." + stackTrace[1].getMethodName() + ":" + stackTrace[1].getLineNumber();
        logDebug("Feature not yet implemented " + featureDescription + " at " + location);
    }

    //region WTF
    public static void logWTF(String message) {
        Log.wtf(LOG_TAG, message);
    }

    public static void logWTF(Throwable throwable) {
        StackTraceElement stackTraceElement = throwable.getStackTrace()[0];

        String location = stackTraceElement.getClassName() + "." + stackTraceElement.getMethodName() + ":" + stackTraceElement.getLineNumber();
        String message = String.format("Unexpected error occurred at %s.", location);
        Log.wtf(LOG_TAG, message, throwable);
    }

    public static void logWTF(String message, Throwable throwable) {
        Log.wtf(LOG_TAG, message, throwable);
    }
    //endregion

    //region Error
    public static void logError(String message) {
        Log.e(LOG_TAG, message);
    }

    public static void logError(Throwable throwable) {
        StackTraceElement stackTraceElement = throwable.getStackTrace()[0];

        String location = stackTraceElement.getClassName() + "." + stackTraceElement.getMethodName() + ":" + stackTraceElement.getLineNumber();
        String message = String.format("Unexpected error occurred at %s.", location);
        Log.e(LOG_TAG, message, throwable);
    }

    public static void logError(String message, Throwable throwable) {
        Log.e(LOG_TAG, message, throwable);
    }
    //endregion

    //region Warning
    public static void logWarning(String message) {
        Log.w(LOG_TAG, message);
    }

    public static void logWarning(Throwable throwable) {
        StackTraceElement stackTraceElement = throwable.getStackTrace()[0];

        String location = stackTraceElement.getClassName() + "." + stackTraceElement.getMethodName() + ":" + stackTraceElement.getLineNumber();
        String message = String.format("Unexpected error occurred at %s.", location);
        Log.w(LOG_TAG, message, throwable);
    }

    public static void logWarning(String message, Throwable throwable) {
        Log.w(LOG_TAG, message, throwable);
    }
    //endregion

    //region Info
    public static void logInfo(String message) {
        Log.i(LOG_TAG, message);
    }

    public static void logInfo(Throwable throwable) {
        StackTraceElement stackTraceElement = throwable.getStackTrace()[0];

        String location = stackTraceElement.getClassName() + "." + stackTraceElement.getMethodName() + ":" + stackTraceElement.getLineNumber();
        String message = String.format("Unexpected error occurred at %s.", location);
        Log.i(LOG_TAG, message, throwable);
    }

    public static void logInfo(String message, Throwable throwable) {
        Log.i(LOG_TAG, message, throwable);
    }
    //endregion

    //region Debug
    public static void logDebug(String message) {
        Log.d(LOG_TAG, message);
    }

    public static void logDebug(Throwable throwable) {
        StackTraceElement stackTraceElement = throwable.getStackTrace()[0];

        String location = stackTraceElement.getClassName() + "." + stackTraceElement.getMethodName() + ":" + stackTraceElement.getLineNumber();
        String message = String.format("Unexpected error occurred at %s.", location);
        Log.d(LOG_TAG, message, throwable);
    }

    public static void logDebug(String message, Throwable throwable) {
        Log.d(LOG_TAG, message, throwable);
    }
    //endregion

    //region Verbose
    public static void logVerbose(String message) {
        Log.v(LOG_TAG, message);
    }

    public static void logVerbose(Throwable throwable) {
        StackTraceElement stackTraceElement = throwable.getStackTrace()[0];

        String location = stackTraceElement.getClassName() + "." + stackTraceElement.getMethodName() + ":" + stackTraceElement.getLineNumber();
        String message = String.format("Unexpected error occurred at %s.", location);
        Log.v(LOG_TAG, message, throwable);
    }

    public static void logVerbose(String message, Throwable throwable) {
        Log.v(LOG_TAG, message, throwable);
    }
    //endregion
}
