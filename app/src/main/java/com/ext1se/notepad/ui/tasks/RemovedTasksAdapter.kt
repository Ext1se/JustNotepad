package com.ext1se.notepad.ui.tasks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ext1se.notepad.R
import com.ext1se.notepad.common.TaskListener
import com.ext1se.notepad.data.model.Task
import com.ext1se.notepad.utils.ItemSwipeHelper

class RemovedTasksAdapter(
    private var tasks: MutableList<Task> = mutableListOf(),
    private val listener: TaskListener
) : RecyclerView.Adapter<RemovedTaskViewHolder>(),
    ItemSwipeHelper.ItemSwipeHelperAdapter {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RemovedTaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task_removed, parent, false)
        return RemovedTaskViewHolder(view)
    }

    override fun getItemCount(): Int = tasks.size

    override fun onBindViewHolder(holder: RemovedTaskViewHolder, position: Int) = holder.bind(tasks[position])

    override fun onItemDismiss(position: Int, direction: Int) {
        val task = tasks[position]
        tasks.removeAt(position)
        notifyItemRemoved(position)
        listener.swipeTask(task, position, direction, null)
    }
}