package com.majinnaibu.monstercards.models;

import com.majinnaibu.monstercards.helpers.StringHelper;

import java.util.ArrayList;
import java.util.List;

public class Monster {

    private String mName;
    public String getName() {
        return mName;
    }
    public void setName(String value) {
        mName = value;
    }

    private String mSize;
    public String getSize() {
        return mSize;
    }
    public void setSize(String value) {
        mSize = value;
    }

    private String mType;
    public String getType() {
        return mType;
    }
    public void setType(String value) {
        mType = value;
    }

    private String mTag;
    public String getTag() {
        return mTag;
    }
    public void setTag(String value) {
        mTag = value;
    }

    private String mAlignment;
    public String getAlignment() {
        return mAlignment;
    }
    public void setAlignment(String value) {
        mAlignment = value;
    }

    public String getMeta() {
        StringBuilder sb = new StringBuilder();
        boolean isFirstOutput = true;
        String size = getSize();
        if (!StringHelper.isNullOrEmpty(size)) {
            sb.append(size);
            isFirstOutput = false;
        }

        String type = getType();
        if (!StringHelper.isNullOrEmpty(type)) {
            if (!isFirstOutput) {
                sb.append(" ");
            }
            sb.append(type);
            isFirstOutput = false;
        }

        String tag = getTag();
        if (!StringHelper.isNullOrEmpty(tag)) {
            if (!isFirstOutput) {
                sb.append(" ");
            }
            sb.append("(");
            sb.append(tag);
            sb.append(")");
            isFirstOutput = false;
        }

        String alignment = getAlignment();
        if (!StringHelper.isNullOrEmpty(alignment)) {
            if (!isFirstOutput) {
                sb.append(", ");
            }
            sb.append(alignment);
        }

        return sb.toString();
    }

}
