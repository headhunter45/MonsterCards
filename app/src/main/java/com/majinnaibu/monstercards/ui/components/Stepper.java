package com.majinnaibu.monstercards.ui.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.majinnaibu.monstercards.R;

import java.util.Objects;


public class Stepper extends ConstraintLayout {
    private final ViewHolder mHolder;
    private int mCurrentValue;
    private int mStep;
    private int mMinValue;
    private int mMaxValue;
    private String mLabel;
    private OnValueChangeListener mOnValueChangeListener;
    private OnFormatValueCallback mOnFormatValueCallback;

    public Stepper(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mCurrentValue = 0;

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Stepper, 0, 0);
        mStep = a.getInt(R.styleable.Stepper_stepAmount, 1);
        mMinValue = a.getInt(R.styleable.Stepper_minValue, Integer.MIN_VALUE);
        mMaxValue = a.getInt(R.styleable.Stepper_maxValue, Integer.MAX_VALUE);
        mLabel = a.getString(R.styleable.Stepper_label);
        a.recycle();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View root = inflater.inflate(R.layout.component_stepper, this, true);

        mHolder = new ViewHolder(root);

        setValue(mCurrentValue);
        updateDisplayedValue();
        mHolder.increment.setOnClickListener(v -> setValue(mCurrentValue + mStep));
        mHolder.decrement.setOnClickListener(v -> setValue(mCurrentValue - mStep));

        mHolder.label.setText(mLabel);
    }

    public Stepper(Context context) {
        this(context, null);
    }

    public String getLabel() {
        return mLabel;
    }

    public void setLabel(String newLabel) {
        if (!Objects.equals(mLabel, newLabel)) {
            mLabel = newLabel;
            mHolder.label.setText(mLabel);
        }
    }

    public int getValue() {
        return mCurrentValue;
    }

    public void setValue(int value) {
        int oldValue = this.mCurrentValue;
        int newValue = Math.min(mMaxValue, Math.max(mMinValue, value));
        if (newValue != oldValue) {
            this.mCurrentValue = newValue;
            if (mOnValueChangeListener != null) {
                mOnValueChangeListener.onChange(newValue, oldValue);
            }
            updateDisplayedValue();
        }
    }

    private void updateDisplayedValue() {
        if (mOnFormatValueCallback != null) {
            mHolder.text.setText(mOnFormatValueCallback.onFormatValue(this.mCurrentValue));
        } else {
            mHolder.text.setText(String.valueOf(this.mCurrentValue));
        }
    }

    public void setOnValueChangeListener(OnValueChangeListener listener) {
        mOnValueChangeListener = listener;
    }

    public void setOnFormatValueCallback(OnFormatValueCallback callback) {
        mOnFormatValueCallback = callback;
        updateDisplayedValue();
    }

    public int getStep() {
        return mStep;
    }

    public void setStep(int step) {
        this.mStep = step;
    }

    public int getMinValue() {
        return mMinValue;
    }

    public void setMinValue(int minValue) {
        this.mMinValue = minValue;
    }

    public int getMaxValue() {
        return mMaxValue;
    }

    public void setMaxValue(int maxValue) {
        this.mMaxValue = maxValue;
    }

    public interface OnValueChangeListener {
        void onChange(int value, int previousValue);
    }

    public interface OnFormatValueCallback {
        String onFormatValue(int value);
    }

    private static class ViewHolder {
        final TextView text;
        final TextView label;
        final Button increment;
        final Button decrement;

        ViewHolder(View root) {
            text = root.findViewById(R.id.text);
            label = root.findViewById(R.id.label);
            increment = root.findViewById(R.id.increment);
            decrement = root.findViewById(R.id.decrement);
        }
    }
}
