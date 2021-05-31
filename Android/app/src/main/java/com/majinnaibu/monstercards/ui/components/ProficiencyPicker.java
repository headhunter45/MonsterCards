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
import com.majinnaibu.monstercards.data.enums.ProficiencyType;

@SuppressWarnings("unused")
public class ProficiencyPicker extends ConstraintLayout {
    private final ViewHolder mHolder;
    private OnValueChangedListener mOnValueChangedListener;
    private ProficiencyType mSelectedValue;

    public ProficiencyPicker(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mSelectedValue = ProficiencyType.NONE;
        mOnValueChangedListener = null;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View root = inflater.inflate(R.layout.component_proficiency_picker, this, true);

        mHolder = new ViewHolder(root);

        setValue(ProficiencyType.NONE);
        mHolder.group.setOnCheckedChangeListener((group, checkedId) -> {
            if (R.id.proficient == checkedId) {
                setValue(ProficiencyType.PROFICIENT);
            } else if (R.id.expertise == checkedId) {
                setValue(ProficiencyType.EXPERTISE);
            } else {
                setValue(ProficiencyType.NONE);
            }
        });
    }

    public ProficiencyPicker(@NonNull Context context) {
        this(context, null);
    }

    public ProficiencyType getValue() {
        return mSelectedValue;
    }

    public void setValue(ProficiencyType value) {
        if (mSelectedValue != value) {
            mSelectedValue = value;
            if (mOnValueChangedListener != null) {
                mOnValueChangedListener.onValueChanged(mSelectedValue);
            }
        }
        final int checkedId = mHolder.group.getCheckedRadioButtonId();
        if (mSelectedValue == ProficiencyType.PROFICIENT) {
            if (checkedId != R.id.proficient) {
                mHolder.proficient.setChecked(true);
            }
        } else if (mSelectedValue == ProficiencyType.EXPERTISE) {
            if (checkedId != R.id.expertise) {
                mHolder.expertise.setChecked(true);
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
        void onValueChanged(ProficiencyType value);
    }

    private static class ViewHolder {
        final RadioGroup group;
        final MaterialRadioButton none;
        final MaterialRadioButton proficient;
        final MaterialRadioButton expertise;

        ViewHolder(View root) {
            group = root.findViewById(R.id.group);
            none = root.findViewById(R.id.none);
            proficient = root.findViewById(R.id.proficient);
            expertise = root.findViewById(R.id.expertise);
        }
    }
}
