package com.ext1se.dialog.icon_dialog;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.ext1se.dialog.common.BaseAdapter;
import com.ext1se.dialog.common.BaseViewHolder;

public class IconViewHolder extends BaseViewHolder<IconItem> {

    private ImageView mIconImv;

    public IconViewHolder(View view) {
        super(view);
        mIconImv = (ImageView) view;
    }

    @Override
    protected void bindViewHolder(IconItem item, BaseAdapter.OnItemClickListener<IconItem> listener) {
        Drawable iconDrw = item.getDrawable(itemView.getContext());
        mIconImv.setImageDrawable(iconDrw);
        mIconImv.setOnClickListener(v -> {
            if (iconDrw == null) {
                return;
            }

            listener.onItemClickListener(item, getAdapterPosition());
        });
        mIconImv.setAlpha(iconDrw == null ? 0.3f : 1.0f);
    }

    public void setColor(boolean isSelected, int[] colors){
        if (isSelected) {
            mIconImv.setColorFilter(colors[1], PorterDuff.Mode.SRC_IN);
        } else {
            mIconImv.setColorFilter(colors[0], PorterDuff.Mode.SRC_IN);
        }
    }
}
