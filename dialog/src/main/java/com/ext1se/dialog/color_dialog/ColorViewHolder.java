package com.ext1se.dialog.color_dialog;

import android.view.View;

import com.ext1se.dialog.common.BaseAdapter;
import com.ext1se.dialog.common.BaseViewHolder;

public class ColorViewHolder extends BaseViewHolder<ColorItem> {

    private ColorView mColorView;

    public ColorViewHolder(View view) {
        super(view);
        mColorView = (ColorView) view;
    }

    @Override
    protected void bindViewHolder(ColorItem item, BaseAdapter.OnItemClickListener<ColorItem> listener) {
        mColorView.setColors(item.getPrimaryColor(), item.getPrimaryDarkColor());
        mColorView.setOnClickListener(v -> {
            listener.onItemClickListener(item, getAdapterPosition());
        });

        if (item.isSelected()) {
            mColorView.setSelected(true);
        } else {
            mColorView.setSelected(false);
        }
    }
}
