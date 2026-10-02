package com.majinnaibu.monstercards.ui.components;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.majinnaibu.monstercards.data.enums.GameSystem;
import com.majinnaibu.monstercards.helpers.StringHelper;
import com.majinnaibu.monstercards.models.Monster;

public class SourceTagView extends AppCompatTextView {

    public SourceTagView(@NonNull Context context) {
        super(context);
        init();
    }

    public SourceTagView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SourceTagView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
        setTextColor(Color.WHITE);
        setGravity(Gravity.CENTER);
        int padH = (int) (8 * getResources().getDisplayMetrics().density);
        int padV = (int) (2 * getResources().getDisplayMetrics().density);
        setPadding(padH, padV, padH, padV);
        setMaxLines(1);
    }

    public void setSource(@Nullable GameSystem system, @Nullable String sourceLabel) {
        GameSystem resolvedSystem = system != null ? system : GameSystem.DND_5E;
        String systemName = resolvedSystem.getShortName();

        StringBuilder sb = new StringBuilder(systemName);
        if (!StringHelper.isNullOrEmpty(sourceLabel)) {
            sb.append(" | ").append(sourceLabel);
        }
        setText(sb.toString());

        int bgColor;
        switch (resolvedSystem) {
            case PF_2E:
                bgColor = Color.parseColor("#1E3A8A"); // Deep Blue
                break;
            case SF_2E:
                bgColor = Color.parseColor("#4C1D95"); // Deep Purple
                break;
            case CUSTOM:
                bgColor = Color.parseColor("#334155"); // Slate Gray
                break;
            case DND_5E:
            default:
                bgColor = Color.parseColor("#7F1D1D"); // Deep Crimson
                break;
        }

        GradientDrawable bg = new GradientDrawable();
        bg.setShape(GradientDrawable.RECTANGLE);
        bg.setCornerRadius(12 * getResources().getDisplayMetrics().density);
        bg.setColor(bgColor);
        setBackground(bg);
    }

    public void setMonster(@NonNull Monster monster) {
        setSource(monster.gameSystem, monster.sourceLabel);
    }
}
