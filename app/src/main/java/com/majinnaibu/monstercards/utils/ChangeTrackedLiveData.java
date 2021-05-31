package com.majinnaibu.monstercards.utils;

import androidx.lifecycle.MutableLiveData;

import java.util.Objects;

public class ChangeTrackedLiveData<T> extends MutableLiveData<T> {
    private final OnValueChangedCallback<T> mOnValueChangedCallback;
    private final OnValueDirtiedCallback mOnValueDirtiedCallback;
    private T mReferenceValue;

    public ChangeTrackedLiveData(T initialValue, OnValueChangedCallback<T> onValueChanged, OnValueDirtiedCallback onValueDirtied) {
        super(initialValue);
        mReferenceValue = initialValue;
        mOnValueChangedCallback = onValueChanged;
        if (mOnValueChangedCallback != null) {
            mOnValueChangedCallback.onValueChanged(initialValue);
        }
        mOnValueDirtiedCallback = onValueDirtied;
    }

    public ChangeTrackedLiveData(T initialValue, OnValueChangedCallback<T> callback) {
        this(initialValue, callback, null);
    }

    public ChangeTrackedLiveData(T initialValue, OnValueDirtiedCallback callback) {
        this(initialValue, null, callback);
    }

    public void setReferenceValue(T referenceValue) {
        mReferenceValue = referenceValue;
    }

    public void setCurrentValueAsReference() {
        mReferenceValue = getValue();
    }

    public void resetValue(T value) {
        mReferenceValue = value;
        setValue(value);
    }

    @Override
    public void setValue(T value) {
        if (!Objects.equals(getValue(), value)) {
            super.setValue(value);

            if (mOnValueChangedCallback != null) {
                mOnValueChangedCallback.onValueChanged(value);
            }
            if (!Objects.equals(mReferenceValue, value) && mOnValueDirtiedCallback != null) {
                mOnValueDirtiedCallback.onValueDirtied();
            }
        }
    }

    public interface OnValueDirtiedCallback {
        void onValueDirtied();
    }

    public interface OnValueChangedCallback<T> {
        void onValueChanged(T value);
    }
}
