package com.ext1se.dialog.color_dialog;

import android.content.res.TypedArray;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.ext1se.dialog.R;
import com.ext1se.dialog.common.BaseDialog;

import java.util.ArrayList;
import java.util.List;

public class ColorDialog extends BaseDialog<ColorItem> {

    private int[] mColorArrayIds = new int[]{
            R.array.redTheme,
            R.array.pinkTheme,
            R.array.purpleTheme,
            R.array.deepPurpleTheme,
            R.array.indigoTheme,
            R.array.blueTheme,
            R.array.lightBlueTheme,
            //R.array.cyanTheme,
            R.array.tealTheme,
            R.array.greenTheme,
            //R.array.limeTheme,
            //R.array.yellowTheme,
            R.array.amberTheme,
            R.array.orangeTheme,
            R.array.deepOrangeTheme,
            R.array.brownTheme,
            R.array.greyTheme,
            R.array.blueGreyTheme,
            R.array.blackTheme
    };

    public ColorDialog() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TypedArray taColorDialog = mContext.obtainStyledAttributes(R.styleable.ColorDialog);
        mItemSize = taColorDialog.getDimensionPixelSize(R.styleable.ColorDialog_colorSize, -1);
        taColorDialog.recycle();
    }

    @Override
    protected int getItemSize() {
        return mItemSize;
    }

    @Override
    protected List<ColorItem> getItems() {
        List<ColorItem> colorItems = new ArrayList<>();
        for (int id : mColorArrayIds) {
            int[] colors = mContext.getResources().getIntArray(id);
            ColorItem colorItem = new ColorItem(id, colors[0], colors[1], colors[2]);
            if (mSelectedColorId == id) {
                colorItem.setSelected(true);
                mSelectedItem = colorItem;
            }
            colorItems.add(colorItem);
        }
        return colorItems;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle state) {
        super.onSaveInstanceState(state);
        state.putInt("colorId", mSelectedItem.getId());
    }

    @Override
    protected void restoreState(Bundle state) {
        super.restoreState(state);
        mSelectedColorId = state.getInt("colorId", -1);
    }

    @Override
    protected ColorAdapter getAdapter() {
        return new ColorAdapter(mItems, this);
    }

    @Override
    protected int getDialogStyle() {
        return R.style.ColorDialogStyle;
    }

    @Override
    protected int getTitle() {
        return R.string.dialog_color;
    }

    @Override
    protected int getItemsPosition(ColorItem item) {
        for (int i = 0; i < mItems.size(); i++) {
            if (mItems.get(i) == item) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void onItemClickListener(ColorItem item, int position) {
        super.onItemClickListener(item, position);
        mPreviewView.setColor(item.getPrimaryColor());
    }

    @Override
    protected void callSelectCallback() {
        try {
            ((Callback) getCaller()).onItemDialogSelected(this, mSelectedItem);
        } catch (ClassCastException e) {
        }
    }

    @Override
    protected void callDismissCallback() {
        ((DismissCallback) getCaller()).onDialogDismissed();
    }

    public interface Callback {
       void onItemDialogSelected(ColorDialog dialog, @NonNull ColorItem colorItem);
    }

    public interface DismissCallback {
        void onDialogDismissed();
    }
}
