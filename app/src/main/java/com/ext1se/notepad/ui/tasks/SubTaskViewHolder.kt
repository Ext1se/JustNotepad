package com.ext1se.notepad.ui.tasks

import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ext1se.notepad.R
import com.ext1se.notepad.common.TaskListener
import com.ext1se.notepad.data.model.Task
import android.graphics.Paint

class SubTaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val title: TextView = itemView.findViewById(R.id.tv_subtask)
    private val checkCompleted: CheckBox = itemView.findViewById(R.id.checkbox_completed)

    fun bind(task: Task, listener: TaskListener) {

        itemView.setOnClickListener {
            //listener.selectTask(task, adapterPosition)
        }

        checkCompleted.setOnClickListener {

        }

        title.text = task.name
    }
}