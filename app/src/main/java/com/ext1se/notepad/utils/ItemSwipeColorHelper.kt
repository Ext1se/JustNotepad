package com.ext1se.notepad.utils

import android.graphics.*
import android.graphics.drawable.shapes.RoundRectShape
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ItemTouchHelper
import com.ext1se.notepad.R

enum class ItemType {
    CARD_ITEM,
    DEFAULT_ITEM
}

class ItemSwipeColorHelper(
    touchHelperAdapter: ItemSwipeHelperAdapter,
    swipeFlag: Int = ItemTouchHelper.START,
    dragFlag: Int = 0,
    private val itemType: ItemType = ItemType.CARD_ITEM,
    private val idColorLeft: Int = R.color.redLight,
    private val idColorRight: Int = R.color.greenLight
) : ItemSwipeHelper(touchHelperAdapter, swipeFlag, dragFlag) {

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
        val backgroundCornerOffset = if (itemType == ItemType.CARD_ITEM) 100 else 0
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
            paint.color = resources.getColor(idColorRight)
            rect.set(left, top, left + dX + backgroundCornerOffset, bottom)
        } else if (dX < 0) {
            paint.color = resources.getColor(idColorLeft)
            rect.set(right + dX - backgroundCornerOffset, top, right, bottom)
        } else {
            rect.set(0f, 0f, 0f, 0f)
        }
        paint.alpha = alpha
        if (itemType == ItemType.CARD_ITEM) {
            val radius = resources.getDimension(R.dimen.item_card_corner_radius)
            canvas.drawRoundRect(rect, radius, radius, paint)
        } else {
            canvas.drawRect(rect, paint)
        }
    }
}