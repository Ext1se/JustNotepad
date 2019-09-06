package com.ext1se.dialog.common;

import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public abstract class BaseAdapter<I extends Item, VH extends BaseViewHolder>
        extends RecyclerView.Adapter<VH> {

    protected List<I> mItems;
    protected OnItemClickListener mListener;

    public BaseAdapter(List<I> items, OnItemClickListener listener) {
        setHasStableIds(true);
        mItems = items;
        mListener = listener;
    }

    @Override
    public @NonNull
    abstract VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(@NonNull final VH holder, final int position) {
        holder.bindViewHolder(mItems.get(position), mListener);
    }

    @Override
    public int getItemCount() {
        return (mItems != null) ? mItems.size() : 0;
    }

    @Override
    public long getItemId(int position) {
        return mItems.get(position).getId();
    }

    public interface OnItemClickListener<I extends Item> {
        void onItemClickListener(I item, int position);
    }
}
