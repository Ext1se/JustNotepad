package com.ext1se.notepad.utils

import android.graphics.*
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ItemTouchHelper
import com.ext1se.notepad.R


open class ItemSwipeHelper(
    protected val touchHelperAdapter: ItemSwipeHelperAdapter,
    protected val swipeFlag: Int = ItemTouchHelper.START,
    protected val dragFlag: Int = 0
) : ItemTouchHelper.Callback() {

    interface ItemSwipeHelperAdapter {
        fun onItemDismiss(position: Int, direction: Int)
    }

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        return makeMovementFlags(dragFlag, swipeFlag)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        touchHelperAdapter.onItemDismiss(viewHolder.adapterPosition, direction)
    }
}