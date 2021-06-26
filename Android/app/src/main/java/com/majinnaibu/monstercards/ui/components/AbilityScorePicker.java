package com.majinnaibu.monstercards.ui.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.majinnaibu.monstercards.R;
import com.majinnaibu.monstercards.data.enums.AbilityScore;
import com.majinnaibu.monstercards.helpers.ArrayHelper;

import java.util.Objects;

public class AbilityScorePicker extends LinearLayout {
    private final ViewHolder mHolder;
    private OnValueChangedListener mOnValueChangedListener;
    private AbilityScore mSelectedValue;
    private String mLabel;

    public AbilityScorePicker(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mSelectedValue = AbilityScore.STRENGTH;
        mOnValueChangedListener = null;
        // TODO: use this as default but allow setting via attribute
        mLabel = "Ability Score";

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Stepper, 0, 0);
        String label = a.getString(R.styleable.Stepper_label);
        if (label != null) {
            mLabel = label;
        }

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View root = inflater.inflate(R.layout.component_ability_score_picker, this, true);

        mHolder = new ViewHolder(root);

        mHolder.label.setText(mLabel);

        mHolder.spinner.setAdapter(new ArrayAdapter<AbilityScore>(getContext(), R.layout.dropdown_list_item, AbilityScore.values()) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                AbilityScore item = getItem(position);
                TextView view = (TextView) super.getView(position, convertView, parent);
                view.setText(item.displayName);
                return view;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                AbilityScore item = getItem(position);
                TextView view = (TextView) super.getDropDownView(position, convertView, parent);
                view.setText(item.displayName);
                return view;
            }
        });
        mHolder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setValue((AbilityScore) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                setValue(mSelectedValue = AbilityScore.STRENGTH);
            }
        });
        mHolder.spinner.setSelection(ArrayHelper.indexOf(AbilityScore.values(), mSelectedValue));

        setValue(AbilityScore.STRENGTH);
        // TODO: listen for changes on the component to update mSelectedValue;
    }

    public AbilityScorePicker(@NonNull Context context) {
        this(context, null);
    }

    public AbilityScore getValue() {
        return mSelectedValue;
    }

    public void setValue(AbilityScore value) {
        if (value != mSelectedValue) {
            mSelectedValue = value;
            mHolder.spinner.setSelection(ArrayHelper.indexOf(AbilityScore.values(), value));
            if (mOnValueChangedListener != null) {
                mOnValueChangedListener.onValueChanged(value);
            }
        }
    }

    public void setOnValueChangedListener(OnValueChangedListener listener) {
        mOnValueChangedListener = listener;
    }

    public String getLabel() {
        return mLabel;
    }

    public void setLabel(String label) {
        if (!Objects.equals(mLabel, label)) {
            mLabel = label;
            mHolder.label.setText(label);
        }
    }

    public interface OnValueChangedListener {
        void onValueChanged(AbilityScore value);
    }

    private static class ViewHolder {

        private final Spinner spinner;
        private final TextView label;

        ViewHolder(View root) {
            spinner = root.findViewById(R.id.spinner);
            label = root.findViewById(R.id.label);
        }
    }
}
