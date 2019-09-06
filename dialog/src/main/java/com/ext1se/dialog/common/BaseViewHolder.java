package com.ext1se.dialog.common;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public abstract class BaseViewHolder<I extends Item> extends RecyclerView.ViewHolder {

    public BaseViewHolder(View view) {
        super(view);
    }

    protected abstract void bindViewHolder(I item, BaseAdapter.OnItemClickListener<I> listener);
}
