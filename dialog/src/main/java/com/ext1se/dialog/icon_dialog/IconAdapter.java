package com.ext1se.dialog.icon_dialog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.ext1se.dialog.R;
import com.ext1se.dialog.common.BaseAdapter;

import java.util.List;

public class IconAdapter extends BaseAdapter<IconItem, IconViewHolder> {

    private int[] mColors;

    public IconAdapter(List<IconItem> items, OnItemClickListener listener) {
        super(items, listener);
    }

    public IconAdapter(List<IconItem> items, OnItemClickListener listener, int[] colors) {
        super(items, listener);
        mColors = colors;
    }

    @NonNull
    @Override
    public IconViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dialog_item_icon, parent, false);
        return new IconViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull IconViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.setColor(mItems.get(position).isSelected(), mColors);
    }
}
