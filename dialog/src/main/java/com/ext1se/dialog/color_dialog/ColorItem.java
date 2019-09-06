package com.ext1se.dialog.color_dialog;

import com.ext1se.dialog.common.Item;

public class ColorItem extends Item {

    private int mPrimaryColor;
    private int mPrimaryDarkColor;
    private int mLightColor;

    public ColorItem() {
    }

    public ColorItem(int id, int primaryColor, int primaryDarkColor, int lightColor) {
        mId = id;
        mPrimaryColor = primaryColor;
        mPrimaryDarkColor = primaryDarkColor;
        mLightColor = lightColor;
    }

    public ColorItem(int primaryColor, int primaryDarkColor, int lightColor) {
        mPrimaryColor = primaryColor;
        mPrimaryDarkColor = primaryDarkColor;
        mLightColor = lightColor;
    }

    public int getPrimaryColor() {
        return mPrimaryColor;
    }

    public void setPrimaryColor(int primaryColor) {
        mPrimaryColor = primaryColor;
    }

    public int getPrimaryDarkColor() {
        return mPrimaryDarkColor;
    }

    public void setPrimaryDarkColor(int primaryDarkColor) {
        mPrimaryDarkColor = primaryDarkColor;
    }

    public int getLightColor() {
        return mLightColor;
    }

    public void setLightColor(int lightColor) {
        mLightColor = lightColor;
    }
}

