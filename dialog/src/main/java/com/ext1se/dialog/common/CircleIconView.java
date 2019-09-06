package com.ext1se.dialog.common;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import androidx.annotation.ArrayRes;
import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

import com.ext1se.dialog.icon_dialog.IconItem;
import com.ext1se.dialog.icon_dialog.IconView;
import org.jetbrains.annotations.NotNull;

public class CircleIconView extends IconView {

    protected int mColor = Color.DKGRAY;
    protected boolean mIsStroked = false;

    public CircleIconView(Context context) {
        this(context, null, 0);
    }

    public CircleIconView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleIconView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setColor(@ColorInt int color) {
        mColor = color;
        invalidate();
    }

    public void setColorResource(@ArrayRes int colorId) {
        mColor = getColorResource(colorId);
        invalidate();
    }

    public void setIcon(@ColorInt int color, @NotNull IconItem icon) {
        mColor = color;
        super.setIcon(icon);
    }

    public void setIcon(@ColorInt int color, int iconId) {
        mColor = color;
        super.setIcon(iconId);
    }

    public void setIconTheme(@ArrayRes int colorId, int iconId){
        mColor = getColorResource(colorId);
        super.setIcon(iconId);
    }

    public void setIconTheme(@ArrayRes int colorId, @NotNull IconItem icon){
        mColor = getColorResource(colorId);
        super.setIcon(icon);
    }

    public void setStroke(boolean isStroked) {
        mIsStroked = isStroked;
    }

    public void onDraw(Canvas canvas) {
        drawBackground(canvas);
        super.onDraw(canvas);
    }

    protected void drawBackground(Canvas canvas){
        Paint paintCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintCircle.setColor(mColor);
        if (mIsStroked) {
            float width = getWidth() * 0.03f;
            float radius = (getWidth() - width) / 2;
            canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, radius, paintCircle);
            Paint paintStroke = new Paint(Paint.ANTI_ALIAS_FLAG);
            paintStroke.setStyle(Paint.Style.STROKE);
            paintStroke.setColor(Color.WHITE);
            paintStroke.setStrokeWidth(width);
            canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, radius, paintStroke);
        } else {
            canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, getWidth() / 2f, paintCircle);
        }
    }

    private @ColorInt int getColorResource(@ArrayRes int colorId){
        return getContext().getResources().getIntArray(colorId)[0];
    }
}
