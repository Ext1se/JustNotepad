package com.ext1se.notepad.ui.tasks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ext1se.notepad.R
import com.ext1se.notepad.common.SubTaskListener
import com.ext1se.notepad.common.TaskListener
import com.ext1se.notepad.data.model.SubTask
import com.ext1se.notepad.data.model.Task
import com.ext1se.notepad.utils.ItemSwipeHelper

class SubTasksInItemAdapter(
    private val task: Task,
    private var subTasks: MutableList<SubTask> = mutableListOf(),
    private val taskListener: TaskListener,
    private val subTaskListener: SubTaskListener
) : RecyclerView.Adapter<SubTaskInItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubTaskInItemViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_subtask_1, parent, false)
        return SubTaskInItemViewHolder(view)
    }

    override fun getItemCount(): Int = subTasks.size

    override fun onBindViewHolder(holder: SubTaskInItemViewHolder, position: Int) =
        holder.bind(task, subTasks[position], taskListener, subTaskListener)
}