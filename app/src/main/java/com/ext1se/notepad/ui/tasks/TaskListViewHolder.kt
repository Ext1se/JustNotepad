package com.ext1se.notepad.ui.tasks

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ext1se.notepad.R
import com.ext1se.notepad.common.SubTaskListener
import com.ext1se.notepad.common.TaskListener
import com.ext1se.notepad.data.model.Project
import com.ext1se.notepad.data.model.Task

class TaskListViewHolder(itemView: View) : BaseTaskViewHolder(itemView) {

    private val rvSubTasks: RecyclerView = itemView.findViewById(R.id.rv_subtasks)

    fun setRecycledViewPool(viewPool: RecyclerView.RecycledViewPool) {
        rvSubTasks.setRecycledViewPool(viewPool)
    }

    override fun bind(
        task: Task,
        project: Project?,
        taskListener: TaskListener,
        subTaskListener: SubTaskListener
    ) {
        super.bind(task, project, taskListener, subTaskListener)
        val adapter =
            InlineSubTasksAdapter(task, task.subTasks, taskListener, subTaskListener)
        val linearLayoutManager =
            LinearLayoutManager(itemView.context, RecyclerView.VERTICAL, false)
        linearLayoutManager.setItemPrefetchEnabled(true)
        linearLayoutManager.initialPrefetchItemCount = task.subTasks.size
        rvSubTasks.getRecycledViewPool().setMaxRecycledViews(0, task.subTasks.size)
        rvSubTasks.layoutManager = linearLayoutManager
        rvSubTasks.adapter = adapter
    }
}