package com.ext1se.notepad.ui.tasks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ext1se.notepad.R
import com.ext1se.notepad.common.TaskListener
import com.ext1se.notepad.data.model.Task
import com.ext1se.notepad.utils.ItemSwipeHelper

class TasksAdapter(
    private var tasks: MutableList<Task> = mutableListOf(),
    private val listener: TaskListener
) : RecyclerView.Adapter<TaskViewHolder>(),
    ItemSwipeHelper.ItemSwipeHelperAdapter {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun getItemCount(): Int = tasks.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) = holder.bind(tasks[position], listener)

    fun update(tasks: MutableList<Task>) {
        this.tasks = tasks
        notifyDataSetChanged()
    }

    override fun onItemDismiss(position: Int, direction: Int) {
        val callback = object : TaskListener.RestoreTaskListener {
            override fun restoreTask(task: Task, position: Int) {
                tasks.getOrNull(0)?.let {
                    if (task.idProject == it.idProject) {
                        notifyItemInserted(position)
                    }
                }
            }
        }
        listener.swipeTask(tasks[position], position, direction, callback)
        notifyItemRemoved(position)
    }
}