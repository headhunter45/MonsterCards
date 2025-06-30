package com.majinnaibu.monstercards.utils;

import android.text.Editable;
import android.text.TextWatcher;

@SuppressWarnings("unused")
public class TextChangedListener implements TextWatcher {

    private final BeforeTextChangedCallback mBeforeTextChangedCallback;
    private final OnTextChangedCallback mOnTextChangedCallback;
    private final AfterTextChangedCallback mAfterTextChangedCallback;

    public TextChangedListener(BeforeTextChangedCallback beforeTextChangedCallback, OnTextChangedCallback onTextChangedCallback, AfterTextChangedCallback afterTextChangedCallback) {
        mBeforeTextChangedCallback = beforeTextChangedCallback;
        mOnTextChangedCallback = onTextChangedCallback;
        mAfterTextChangedCallback = afterTextChangedCallback;
    }

    public TextChangedListener(OnTextChangedCallback callback) {
        this(null, callback, null);
    }

    public TextChangedListener(BeforeTextChangedCallback callback) {
        this(callback, null, null);
    }

    public TextChangedListener(AfterTextChangedCallback callback) {
        this(null, null, callback);
    }

    public TextChangedListener(BeforeTextChangedCallback beforeTextChangedCallback, OnTextChangedCallback onTextChangedCallback) {
        this(beforeTextChangedCallback, onTextChangedCallback, null);
    }

    public TextChangedListener(BeforeTextChangedCallback beforeTextChangedCallback, AfterTextChangedCallback afterTextChangedCallback) {
        this(beforeTextChangedCallback, null, afterTextChangedCallback);
    }

    public TextChangedListener(OnTextChangedCallback onTextChangedCallback, AfterTextChangedCallback afterTextChangedCallback) {
        this(null, onTextChangedCallback, afterTextChangedCallback);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if (mBeforeTextChangedCallback != null) {
            mBeforeTextChangedCallback.beforeTextChanged(s, start, count, after);
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (mOnTextChangedCallback != null) {
            mOnTextChangedCallback.onTextChanged(s, start, before, count);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (mAfterTextChangedCallback != null) {
            mAfterTextChangedCallback.afterTextChanged(s);
        }
    }

    public interface BeforeTextChangedCallback {
        void beforeTextChanged(CharSequence s, int start, int count, int after);
    }

    public interface OnTextChangedCallback {
        void onTextChanged(CharSequence s, int start, int before, int count);
    }

    public interface AfterTextChangedCallback {
        void afterTextChanged(Editable s);
    }
}
