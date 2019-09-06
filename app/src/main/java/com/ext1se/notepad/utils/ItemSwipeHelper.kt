package com.ext1se.notepad.utils

import android.graphics.*
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ItemTouchHelper
import com.ext1se.notepad.R


class ItemSwipeHelper(
    private val touchHelperAdapter: ItemSwipeHelperAdapter,
    private val swipeFlag: Int = ItemTouchHelper.START,
    private val dragFlag: Int = 0
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

    override fun onChildDraw(
        canvas: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        val backgroundCornerOffset = 100
        val resources = recyclerView.context.resources
        val itemView = viewHolder.itemView
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        val rect = RectF()
        val top = itemView.top.toFloat()
        val left = itemView.left.toFloat()
        val bottom = itemView.bottom.toFloat()
        val right = itemView.right.toFloat()
        var alpha = (Math.abs(dX / itemView.width) * 255).toInt() + 64
        alpha = if (alpha < 255) alpha else 255
        if (dX > 0) {
            paint.color = resources.getColor(R.color.greenLight)
            rect.set(left, top, left + dX + backgroundCornerOffset, bottom)
        } else if (dX < 0) {
            paint.color = resources.getColor(R.color.redLight)
            rect.set(right + dX - backgroundCornerOffset, top, right, bottom)
        } else {
            rect.set(0f, 0f, 0f, 0f)
        }
        paint.alpha = alpha
        val radius = resources.displayMetrics.density * 6
        canvas.drawRoundRect(rect, radius, radius, paint)
    }
}