package com.ext1se.notepad.ui.tasks

import android.graphics.Paint
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.ext1se.notepad.R
import com.ext1se.notepad.common.SubTaskListener
import com.ext1se.notepad.common.TaskListener
import com.ext1se.notepad.data.model.SubTask
import com.ext1se.notepad.data.model.Task

class SubTaskInItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val title: TextView = itemView.findViewById(R.id.tv_subtask)
    private val checkCompleted: CheckBox = itemView.findViewById(R.id.checkbox_completed)

    fun bind(
        task: Task,
        subTask: SubTask,
        taskListener: TaskListener,
        subTaskListener: SubTaskListener
    ) {
        title.text = subTask.name
        checkCompleted.isChecked = subTask.isCompleted
        itemView.setOnClickListener {
            taskListener.selectTask(task, 0)
        }
        checkCompleted.setOnClickListener {
            subTaskListener.setSubTaskState(subTask, adapterPosition)
            if (checkCompleted.isChecked) {
                setStrikeFlag(true)
            } else {
                setStrikeFlag(false)
            }
        }
        if (subTask.isCompleted) {
            setStrikeFlag(true)
        } else {
            setStrikeFlag(false)
        }
    }

    private fun setStrikeFlag(isStriked: Boolean) {
        if (!isStriked) {
            title.setPaintFlags(title.getPaintFlags() and (Paint.STRIKE_THRU_TEXT_FLAG.inv()))
            val color = itemView.resources.getColor(R.color.colorDefault)
            title.setTextColor(color)
        } else {
            title.setPaintFlags(title.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG)
            val color = itemView.resources.getColor(R.color.colorDisabled)
            title.setTextColor(color)
        }
    }
}