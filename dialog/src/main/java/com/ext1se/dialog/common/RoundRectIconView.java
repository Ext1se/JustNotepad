package com.ext1se.dialog.common;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import androidx.annotation.Nullable;

public class RoundRectIconView extends CircleIconView {

    public RoundRectIconView(Context context) {
        this(context, null, 0);
    }

    public RoundRectIconView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundRectIconView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void drawBackground(Canvas canvas){
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(mColor);
        int radius = getWidth() < getHeight() ? getWidth() : getHeight();
        radius /= 2;
        canvas.drawRect(0, 0, getWidth() - radius, getHeight(), paint);
        canvas.drawCircle(getWidth() - radius, getHeight() / 2f, radius, paint);
    }
}
