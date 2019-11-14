package com.ext1se.notepad.ui.tasks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ext1se.notepad.R
import com.ext1se.notepad.common.SubTaskListener
import com.ext1se.notepad.common.TaskListener
import com.ext1se.notepad.data.model.Project
import com.ext1se.notepad.data.model.Task
import com.ext1se.notepad.utils.ItemSwipeHelper
import java.util.Collections

class TasksAdapter(
    private var tasks: MutableList<Task> = mutableListOf(),
    private var project: Project?,
    private val taskListener: TaskListener,
    private val subTaskListener: SubTaskListener
) : RecyclerView.Adapter<TaskViewHolder>(),
    ItemSwipeHelper.OnItemHelperAdapter {

    private val viewPool : RecyclerView.RecycledViewPool

    init {
        viewPool = RecyclerView.RecycledViewPool()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        val holder = TaskViewHolder(view)
        holder.setRecycledViewPool(viewPool)
        return holder
    }

    override fun getItemCount(): Int = tasks.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) =
        holder.bind(tasks[position], project, taskListener, subTaskListener)

    fun updateTasks(tasks: MutableList<Task>) {
        this.tasks = tasks
        notifyDataSetChanged()
    }

    fun updateProject(project: Project?){
        this.project = project
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
        taskListener.swipeTask(tasks[position], position, direction, callback)
        notifyItemRemoved(position)
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
/*        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(tasks, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(tasks, i, i - 1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)*/
        notifyItemMoved(fromPosition, toPosition)
        taskListener.moveTask(fromPosition, toPosition)
    }
}