package com.ext1se.dialog.color_dialog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.ext1se.dialog.R;
import com.ext1se.dialog.common.BaseAdapter;

import java.util.List;

class ColorAdapter extends BaseAdapter<ColorItem, ColorViewHolder> {

    public ColorAdapter(List<ColorItem> items, OnItemClickListener listener) {
        super(items, listener);
    }

    @NonNull
    @Override
    public ColorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dialog_item_color, parent, false);
        return new ColorViewHolder(v);
    }
}
