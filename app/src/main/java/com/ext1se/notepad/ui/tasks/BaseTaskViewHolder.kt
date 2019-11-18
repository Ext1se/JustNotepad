package com.ext1se.notepad.ui.tasks

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ext1se.notepad.R
import com.ext1se.notepad.common.TaskListener
import com.ext1se.notepad.data.model.Task
import android.graphics.Paint
import android.os.Build
import android.text.Html
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.recyclerview.widget.LinearLayoutManager
import com.ext1se.dialog.color_dialog.ColorHelper
import com.ext1se.notepad.common.SubTaskListener
import com.ext1se.notepad.data.model.Project

open class BaseTaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    protected val title: TextView = itemView.findViewById(R.id.tv_title)
    protected val description: TextView = itemView.findViewById(R.id.tv_description)
    protected val checkCompleted: AppCompatCheckBox = itemView.findViewById(R.id.checkbox_completed)

    protected val colorPassive: Int = itemView.resources.getColor(R.color.colorDefaultLight)
    protected var colorActive: Int = colorPassive

    open fun bind(
        task: Task,
        project: Project?,
        taskListener: TaskListener,
        subTaskListener: SubTaskListener
    ) {

        itemView.setOnClickListener {
            taskListener.selectTask(task, adapterPosition)
        }

        project?.let {
            colorActive = ColorHelper.getColor(itemView.context, it.idColorTheme).lightColor
        }

        checkCompleted.setOnClickListener {
            taskListener.setTaskState(task, adapterPosition)
            setCheckBox(task.isCompleted)
            setStrikeFlag(task.isCompleted)
        }

        if (task.name.isBlank()) {
            title.visibility = View.GONE
        } else {
            title.visibility = View.VISIBLE
            title.text = task.name
        }

        if (task.description.isBlank()) {
            description.visibility = View.GONE
        } else {
            description.visibility = View.VISIBLE
            description.text = task.description
        }

        setCheckBox(task.isCompleted)
        setStrikeFlag(task.isCompleted)

        if (task.name.isBlank() && task.description.isBlank()) {
            checkCompleted.visibility = View.GONE
        } else {
            checkCompleted.visibility = View.VISIBLE
        }
    }

    protected fun setCheckBox(isCompleted: Boolean) {
        checkCompleted.isChecked = isCompleted
        if (isCompleted) {
            checkCompleted.supportButtonTintList = ColorStateList.valueOf(colorActive)
        } else {
            checkCompleted.supportButtonTintList = ColorStateList.valueOf(colorPassive)
        }
    }

    protected fun setStrikeFlag(isStriked: Boolean) {
        if (!isStriked) {
            title.setPaintFlags(title.getPaintFlags() and (Paint.STRIKE_THRU_TEXT_FLAG.inv()))
            description.setPaintFlags(title.getPaintFlags() and (Paint.STRIKE_THRU_TEXT_FLAG.inv()))
            val color = itemView.resources.getColor(R.color.colorDefault)
            title.setTextColor(color)
            description.setTextColor(color)
        } else {
            title.setPaintFlags(title.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG)
            description.setPaintFlags(title.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG)
            val color = itemView.resources.getColor(R.color.colorDisabled)
            title.setTextColor(color)
            description.setTextColor(color)
        }
    }
}