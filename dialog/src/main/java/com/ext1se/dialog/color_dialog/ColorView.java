package com.ext1se.dialog.color_dialog;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

import com.ext1se.dialog.R;

public class ColorView extends AppCompatImageView {

    private int mPrimaryColor = Color.GRAY;
    private int mPrimaryDarkColor = Color.DKGRAY;
    private boolean mIsSelected = false;

    public ColorView(Context context) {
        super(context);
    }

    public ColorView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ColorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if (attrs != null && !isInEditMode()){
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ColorView);
            int colorPrimary = ta.getColor(R.styleable.ColorView_colorPrimary, Color.GRAY);
            int colorPrimaryDark = ta.getColor(R.styleable.ColorView_colorPrimaryDark, Color.DKGRAY);
            setColors(colorPrimary, colorPrimaryDark);
            ta.recycle();
        }
    }

    public void setColors(int mainColor, int strokeColor){
        mPrimaryColor = mainColor;
        mPrimaryDarkColor = strokeColor;
        invalidate();
    }

    public void setSelected(boolean isSelected){
        mIsSelected = isSelected;
        invalidate();
    }

    public void onDraw(Canvas canvas) {
        Paint paintCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintCircle.setColor(mPrimaryColor);
        int min = getWidth() < getHeight() ? getWidth() : getHeight();
        float widthStroke = min * 0.038f;
        float radius = (min - widthStroke) / 2;
        canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, radius, paintCircle);
        if (mIsSelected) {
            Paint paintStroke = new Paint(Paint.ANTI_ALIAS_FLAG);
            paintStroke.setStyle(Paint.Style.STROKE);
            paintStroke.setColor(mPrimaryDarkColor);
            paintStroke.setStrokeWidth(widthStroke);
            canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, radius, paintStroke);
        }
        super.onDraw(canvas);
    }
}
