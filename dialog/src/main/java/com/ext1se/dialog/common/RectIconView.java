package com.ext1se.dialog.common;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import androidx.annotation.Nullable;

public class RectIconView extends CircleIconView {

    public RectIconView(Context context) {
        this(context, null, 0);
    }

    public RectIconView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RectIconView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void drawBackground(Canvas canvas) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(mColor);
        canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
    }
}
