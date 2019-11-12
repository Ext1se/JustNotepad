package com.ext1se.notepad.utils

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ItemTouchHelper


open class ItemSwipeHelper(
    protected val touchHelperAdapter: OnItemHelperAdapter,
    protected val swipeFlag: Int = ItemTouchHelper.START,
    protected val dragFlag: Int = 0
) : ItemTouchHelper.Callback() {

    interface OnItemHelperAdapter {
        fun onItemDismiss(position: Int, direction: Int)
        fun onItemMove(fromPosition: Int, toPosition: Int){}
    }

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        return makeMovementFlags(dragFlag, swipeFlag)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        touchHelperAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        touchHelperAdapter.onItemDismiss(viewHolder.adapterPosition, direction)
    }
}