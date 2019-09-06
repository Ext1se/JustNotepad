package com.ext1se.dialog.common;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.annotation.StyleRes;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.ext1se.dialog.R;
import com.ext1se.dialog.color_dialog.ColorItem;
import com.ext1se.dialog.icon_dialog.IconItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

abstract public class BaseDialog<I extends Item>
        extends DialogFragment implements BaseAdapter.OnItemClickListener<I> {

    protected Context mContext;

    protected CircleIconView mPreviewView;
    protected DialogLayoutManager mLayoutManager;
    protected FloatingActionButton mSelectButton;
    protected RecyclerView mRecyclerView;

    protected int[] mMaxDialogDimensions;
    protected int mItemSize;

    protected BaseAdapter mAdapter;
    protected ColorItem mSelectedColorItem;
    protected int mSelectedColorId;
    protected IconItem mSelectedIcon;
    protected int mSelectedIconId;

    protected List<I> mItems;
    protected I mSelectedItem;

    public BaseDialog() {
        mSelectedItem = null;
        mSelectedIcon = null;
        mSelectedColorItem = null;
        mSelectedColorId = -1;
        mSelectedIconId = -1;
        mItemSize = 0;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        TypedArray taBaseDialog = mContext.obtainStyledAttributes(R.styleable.BaseDialog);
        mMaxDialogDimensions = new int[]{
                taBaseDialog.getDimensionPixelSize(R.styleable.BaseDialog_maxWidth, -1),
                taBaseDialog.getDimensionPixelSize(R.styleable.BaseDialog_maxHeight, -1),
        };
        taBaseDialog.recycle();
    }

    @Override
    public @NonNull
    Dialog onCreateDialog(final Bundle state) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.dialog, null);
        mPreviewView = view.findViewById(R.id.iv_icon);
        mPreviewView.setStroke(true);
        TextView titleTextView = view.findViewById(R.id.tv_title);
        titleTextView.setText(getTitle());
        FloatingActionButton cancelButton = view.findViewById(R.id.btn_cancel);
        mSelectButton = view.findViewById(R.id.btn_select);

        cancelButton.setOnClickListener(v -> dismiss());

        mSelectButton.setOnClickListener(v -> {
            callSelectCallback();
            dismiss();
        });

        mRecyclerView = view.findViewById(R.id.rv_icon_list);
        mLayoutManager = new DialogLayoutManager(mContext, getItemSize());
        mRecyclerView.setLayoutManager(mLayoutManager);

        if (state == null) {
            mItems = getItems();
            if (mSelectedItem != null) {
                int position = getItemsPosition(mSelectedItem);
                mLayoutManager.scrollToPositionWithOffset(position, getItemSize());
            } else {
                mSelectButton.setEnabled(false);
            }
        } else {
            restoreState(state);
        }

        if (mSelectedIconId != -1){
            mPreviewView.setIcon(mSelectedIconId);
        }

        if (mSelectedColorId != -1){
            int color = mContext.getResources().getIntArray(mSelectedColorId)[0];
            mPreviewView.setColor(color);
        }

        mAdapter = getAdapter();
        mRecyclerView.setAdapter(mAdapter);

        Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);  // Required if API <= 21
        setCancelable(false);
        dialog.setOnShowListener(dialogInterface -> {
            Rect fgPadding = new Rect();
            dialog.getWindow().getDecorView().getBackground().getPadding(fgPadding);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.getWindow().getDecorView().setPadding(fgPadding.left, fgPadding.top, fgPadding.right, fgPadding.bottom);
            DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
            int height = metrics.heightPixels - fgPadding.top - fgPadding.bottom;
            int width = metrics.widthPixels - fgPadding.top - fgPadding.bottom;

            if (width > mMaxDialogDimensions[0]) {
                width = mMaxDialogDimensions[0];
            }
            if (height > mMaxDialogDimensions[1]) {
                height = mMaxDialogDimensions[1];
            }
            dialog.getWindow().setLayout(width, height);

            view.setLayoutParams(new ViewGroup.LayoutParams(width, height));
            dialog.setContentView(view);
        });

        return dialog;
    }

    protected void restoreState(Bundle state){
        mLayoutManager.onRestoreInstanceState(state.getParcelable("listLayoutState"));
    }

    protected abstract int getItemSize();

    protected abstract List<I> getItems();

    protected abstract BaseAdapter getAdapter();

    protected abstract @StringRes
    int getTitle();

    protected abstract @StyleRes
    int getDialogStyle();

    protected abstract int getItemsPosition(I item);

    abstract protected void callSelectCallback();

    abstract protected void callDismissCallback();

    @Override
    public void onSaveInstanceState(@NonNull Bundle state) {
        super.onSaveInstanceState(state);
        state.putParcelable("listLayoutState", mLayoutManager.onSaveInstanceState());
    }

    @Override
    public void onDestroyView() {
        Dialog dialog = getDialog();
        if (dialog != null && getRetainInstance()) {
            dialog.setDismissMessage(null);
        }
        super.onDestroyView();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        TypedArray ta = context.obtainStyledAttributes(new int[]{R.attr.DialogStyle});
        int style = ta.getResourceId(0, getDialogStyle());
        ta.recycle();
        this.mContext = new ContextThemeWrapper(context, style);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mSelectedColorItem != null) {
            mSelectedColorItem.setSelected(false);
            mSelectedColorItem = null;
        }
        try {
            callDismissCallback();
        } catch (ClassCastException e) {
        }
    }

    protected Object getCaller() {
        if (getTargetFragment() != null) {
            // Caller was a fragment that used setTargetFragment.
            return getTargetFragment();
        } else if (getParentFragment() != null) {
            // Caller was a fragment using its child fragment manager.
            return getParentFragment();
        } else {
            // Caller was an activity.
            return getActivity();
        }
    }

    public BaseDialog setSelectedIcon(int iconId) {
        mSelectedIconId = iconId;
        return this;
    }

    public BaseDialog setSelectedIcon(@Nullable IconItem icon) {
        if (icon != null) {
            mSelectedIcon = icon;
            return setSelectedIcon(icon.getId());
        } else {
            mSelectedIconId = -1;
            return this;
        }
    }

    public BaseDialog setColor(int selectedColorId) {
        mSelectedColorId = selectedColorId;
        return this;
    }

    public BaseDialog setColorItem(ColorItem colorItem){
        if (colorItem != null){
            mSelectedColorItem = colorItem;
            return setColor(colorItem.getId());
        }else{
            mSelectedColorId = -1;
            return this;
        }
    }

    @Override
    public void onItemClickListener(I item, int position) {
        if (item.isSelected()) {
            item.setSelected(false);
            mSelectedItem = null;
        } else {
            if (mSelectedItem != null) {
                int pos = getItemsPosition(mSelectedItem);
                mSelectedItem.setSelected(false);
                mAdapter.notifyItemChanged(pos);

                item.setSelected(true);
                mSelectedItem = item;

            } else {
                item.setSelected(true);
                mSelectedItem = item;
            }
        }
        mAdapter.notifyItemChanged(position);
        mSelectButton.setEnabled(mSelectedItem != null);
        mSelectButton.setAlpha(mSelectedItem == null ? 0.7f : 1.0f);
    }

 /*   public interface BaseCallback<D extends BaseDialog, I extends Item> {
        void onItemDialogSelected(D dialog, @NonNull I colorItem);
    }

    public interface DismissCallback {
        void onDialogDismissed();
    }*/
}
