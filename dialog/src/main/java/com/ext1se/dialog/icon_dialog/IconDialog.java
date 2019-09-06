package com.ext1se.dialog.icon_dialog;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.SparseArray;

import androidx.annotation.NonNull;

import com.ext1se.dialog.R;
import com.ext1se.dialog.common.BaseDialog;

import java.util.ArrayList;
import java.util.List;

public class IconDialog extends BaseDialog<IconItem>{

    private IconHelper mIconHelper;
    private int[] mIconColors;

    public IconDialog() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TypedArray taIconDialog = mContext.obtainStyledAttributes(R.styleable.IconDialog);
        mItemSize = taIconDialog.getDimensionPixelSize(R.styleable.IconDialog_iconSize, -1);
        mIconColors = new int[]{
                taIconDialog.getColor(R.styleable.IconDialog_iconColor, 0),
                taIconDialog.getColor(R.styleable.IconDialog_selectedIconColor, 0),
        };

        if (mSelectedColorId != -1) {
            mIconColors[1] = mContext.getResources().getIntArray(mSelectedColorId)[0];
        }
        taIconDialog.recycle();

        mIconHelper = new IconHelper(getContext(), false);
    }

    @Override
    protected int getItemSize() {
        return mItemSize;
    }

    @Override
    protected List<IconItem> getItems() {
        SparseArray<IconItem> allIcons = mIconHelper.getIcons();
        IconItem icon = null;
        if (mSelectedIconId != -1) {
            icon = allIcons.get(mSelectedIconId);
        } else {
            if (mSelectedItem != null) {
                icon = allIcons.get(mSelectedItem.getId(), mSelectedItem);
            }
        }

        if (icon != null) {
            icon.setSelected(true);
            mSelectedItem = icon;
        }

        List<IconItem> items = new ArrayList<>();
        for (int i = 0; i < allIcons.size(); i++) {
            items.add(allIcons.valueAt(i));
        }

        return items;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle state) {
        super.onSaveInstanceState(state);
        state.putInt("iconId", mSelectedItem.getId());
    }

    @Override
    protected void restoreState(Bundle state) {
        super.restoreState(state);
        mSelectedIconId = state.getInt("iconId", -1);
    }

    @Override
    protected IconAdapter getAdapter() {
        return new IconAdapter(mItems, this, mIconColors);
    }

    @Override
    protected int getDialogStyle() {
        return R.style.IconDialogStyle;
    }

    @Override
    protected int getTitle() {
        return R.string.dialog_icon;
    }

    @Override
    protected int getItemsPosition(IconItem icon) {
        for (int i = 0; i < mItems.size(); i++) {
            if (mItems.get(i) == icon) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void onItemClickListener(IconItem item, int position) {
        super.onItemClickListener(item, position);
        mPreviewView.setIcon(item);
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

    public interface Callback  {
        void onItemDialogSelected(IconDialog dialog, @NonNull IconItem icon);
    }

    public interface DismissCallback {
        void onDialogDismissed();
    }
}
