package com.ext1se.notepad.ui.tasks

import android.content.res.ColorStateList
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ext1se.notepad.R
import com.ext1se.notepad.common.TaskListener
import com.ext1se.notepad.data.model.Task
import android.graphics.Paint
import com.ext1se.dialog.color_dialog.ColorHelper


class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val title: TextView = itemView.findViewById(R.id.tv_title)
    private val description: TextView = itemView.findViewById(R.id.tv_description)
    private val checkCompleted: CheckBox = itemView.findViewById(R.id.checkbox_completed)

    fun bind(task: Task, listener: TaskListener) {

        itemView.setOnClickListener {
            listener.selectTask(task, adapterPosition)
        }

        checkCompleted.setOnClickListener {
            listener.setTaskState(task, adapterPosition)
            if (checkCompleted.isChecked) {
                setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG)
            } else {
                setPaintFlags(0)
            }
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

        if (task.isCompleted) {
            checkCompleted.isChecked = true
            setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG)
        } else {
            checkCompleted.isChecked = false
            setPaintFlags(0)
        }
    }

    private fun setPaintFlags(flag: Int) {
        title.setPaintFlags(flag)
        description.setPaintFlags(flag)
        if (flag == 0) {
            val color = itemView.resources.getColor(R.color.colorDefault)
            title.setTextColor(color)
            description.setTextColor(color)
        } else {
            val color = itemView.resources.getColor(R.color.colorDisabled)
            title.setTextColor(color)
            description.setTextColor(color)
        }
    }
}