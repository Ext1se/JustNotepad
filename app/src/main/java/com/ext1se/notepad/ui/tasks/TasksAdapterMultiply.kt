package com.ext1se.notepad.ui.tasks

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ext1se.notepad.R
import com.ext1se.notepad.common.SubTaskListener
import com.ext1se.notepad.common.TaskListener
import com.ext1se.notepad.data.model.Project
import com.ext1se.notepad.data.model.Task
import com.ext1se.notepad.utils.ItemSwipeHelper

const val TYPE_DEFAULT = 0
const val TYPE_RECYCLER_LIST = 1
const val TYPE_SIMPLE_LIST = 2

class TasksAdapterMultiply(
    private var tasks: MutableList<Task> = mutableListOf(),
    private var project: Project?,
    private val taskListener: TaskListener,
    private val subTaskListener: SubTaskListener
) : RecyclerView.Adapter<BaseTaskViewHolder>(),
    ItemSwipeHelper.OnItemHelperAdapter {

    private val viewPool: RecyclerView.RecycledViewPool

    init {
        viewPool = RecyclerView.RecycledViewPool()
    }

    override fun getItemViewType(position: Int): Int {
        if (tasks[position].subTasks.size > 0) {
            return TYPE_RECYCLER_LIST
            //return TYPE_SIMPLE_LIST
        } else {
            return TYPE_DEFAULT
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseTaskViewHolder {
        when (viewType) {
            TYPE_DEFAULT -> {
                val view = getView(parent, R.layout.item_task_base)
                return BaseTaskViewHolder(view)
            }
            TYPE_SIMPLE_LIST -> {
                val view = getView(parent, R.layout.item_task_simple_list)
                return TaskSimpleListViewHolder(view)
            }
            TYPE_RECYCLER_LIST -> {
                val view = getView(parent, R.layout.item_task_list)
                val holder = TaskListViewHolder(view)
                holder.setRecycledViewPool(viewPool)
                return holder
            }
            else -> {
                val view = getView(parent, R.layout.item_task_base)
                return BaseTaskViewHolder(view)
            }
        }
    }

    private fun getView(parent: ViewGroup, id: Int): View =
        LayoutInflater.from(parent.context).inflate(id, parent, false)

    override fun getItemCount(): Int = tasks.size

    override fun onBindViewHolder(holder: BaseTaskViewHolder, position: Int) {
        Log.d("MyLog", "--Adapter-- position = " + position)
        holder.bind(tasks[position], project, taskListener, subTaskListener)
    }

    fun updateTasks(tasks: MutableList<Task>) {
        this.tasks = tasks
        notifyDataSetChanged()
    }

    fun updateProject(project: Project?) {
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