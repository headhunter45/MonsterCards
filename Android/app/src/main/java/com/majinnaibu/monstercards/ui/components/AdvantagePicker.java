package com.majinnaibu.monstercards.ui.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.radiobutton.MaterialRadioButton;
import com.majinnaibu.monstercards.R;
import com.majinnaibu.monstercards.data.enums.AdvantageType;

@SuppressWarnings("unused")
public class AdvantagePicker extends ConstraintLayout {
    private final ViewHolder mHolder;
    private OnValueChangedListener mOnValueChangedListener;
    private AdvantageType mSelectedValue;

    public AdvantagePicker(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mSelectedValue = AdvantageType.NONE;
        mOnValueChangedListener = null;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View root = inflater.inflate(R.layout.component_advantage_picker, this, true);

        mHolder = new ViewHolder(root);

        setValue(AdvantageType.NONE);
        mHolder.group.setOnCheckedChangeListener((group, checkedId) -> {
            if (R.id.advantage == checkedId) {
                setValue(AdvantageType.ADVANTAGE);
            } else if (R.id.disadvantage == checkedId) {
                setValue(AdvantageType.DISADVANTAGE);
            } else {
                setValue(AdvantageType.NONE);
            }
        });
    }

    public AdvantagePicker(@NonNull Context context) {
        this(context, null);
    }

    public AdvantageType getValue() {
        return mSelectedValue;
    }

    public void setValue(AdvantageType value) {
        if (mSelectedValue != value) {
            mSelectedValue = value;
            if (mOnValueChangedListener != null) {
                mOnValueChangedListener.onValueChanged(mSelectedValue);
            }
        }
        final int checkedId = mHolder.group.getCheckedRadioButtonId();
        if (mSelectedValue == AdvantageType.ADVANTAGE) {
            if (checkedId != R.id.advantage) {
                mHolder.advantage.setChecked(true);
            }
        } else if (mSelectedValue == AdvantageType.DISADVANTAGE) {
            if (checkedId != R.id.disadvantage) {
                mHolder.disadvantage.setChecked(true);
            }
        } else {
            if (checkedId != R.id.none) {
                mHolder.none.setChecked(true);
            }
        }
    }

    public void setOnValueChangedListener(OnValueChangedListener listener) {
        mOnValueChangedListener = listener;
    }

    public interface OnValueChangedListener {
        void onValueChanged(AdvantageType value);
    }

    private static class ViewHolder {
        final RadioGroup group;
        final MaterialRadioButton none;
        final MaterialRadioButton advantage;
        final MaterialRadioButton disadvantage;

        ViewHolder(View root) {
            group = root.findViewById(R.id.group);
            none = root.findViewById(R.id.none);
            advantage = root.findViewById(R.id.advantage);
            disadvantage = root.findViewById(R.id.disadvantage);
        }
    }
}
