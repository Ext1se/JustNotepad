package com.ext1se.dialog.common;

public class Item {

    protected int mId;
    protected boolean mIsSelected = false;

    public Item() {
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public boolean isSelected() {
        return mIsSelected;
    }

    public void setSelected(boolean selected) {
        mIsSelected = selected;
    }
}

