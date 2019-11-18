package com.ext1se.notepad.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ExtraLinearLayoutManager extends LinearLayoutManager {

    private int mPreload = 1000;

    public ExtraLinearLayoutManager(Context context) {
        super(context);
    }

    public ExtraLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
        mPreload = Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public ExtraLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected int getExtraLayoutSpace(RecyclerView.State state) {
        return mPreload;
    }
}
