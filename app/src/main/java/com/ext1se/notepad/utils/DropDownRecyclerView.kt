package com.ext1se.notepad.utils

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import android.view.animation.Animation
import android.view.animation.Transformation
import android.view.animation.DecelerateInterpolator
import android.animation.ValueAnimator
import android.view.View

class DropDownRecyclerView : RecyclerView {

    private var expandedHeight = 0
    private val duration = 500L

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
    }

    fun expand() {
        val matchParentMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY)
        val wrapContentMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        measure(matchParentMeasureSpec, wrapContentMeasureSpec)
        visibility = View.VISIBLE
        if (measuredHeight == 0) {
            return
        }
        val currentHeight = height
        val targetHeight = measuredHeight
        val deltaHeight = targetHeight - currentHeight
        layoutParams.height = currentHeight
        val animation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                if (interpolatedTime == 1f) {
                    layoutParams.height = LayoutParams.WRAP_CONTENT
                } else {
                    layoutParams.height = currentHeight + (deltaHeight * interpolatedTime).toInt()
                }
                requestLayout()
            }
        }
        animation.duration = duration
        startAnimation(animation)
    }

    fun collapse() {
        if (measuredHeight == 0) {
            visibility = View.GONE
            return
        }
        val initialHeight = measuredHeight
        val animation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                if (interpolatedTime == 1f) {
                    visibility = View.GONE
                } else {
                    layoutParams.height = initialHeight - (initialHeight * interpolatedTime).toInt()
                    requestLayout()
                }
            }
        }
        animation.duration = duration
        startAnimation(animation)
    }

    private fun startAnimation(fromY: Int, toY: Int) {
        val valueAnimator = ValueAnimator.ofInt(fromY, toY)
        valueAnimator.interpolator = DecelerateInterpolator()
        valueAnimator.addUpdateListener { animation ->
            layoutParams.height = animation.animatedValue as Int
            requestLayout()
        }
        valueAnimator.interpolator = DecelerateInterpolator()
        valueAnimator.duration = 1000
        valueAnimator.start()
    }
}