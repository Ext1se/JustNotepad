package com.ext1se.notepad.ui.tasks

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ext1se.notepad.R
import com.ext1se.notepad.common.SubTaskListener
import com.ext1se.notepad.common.TaskListener
import com.ext1se.notepad.data.model.SubTask
import com.ext1se.notepad.data.model.Task

class InlineSubTasksAdapter(
    private val task: Task,
    private var subTasks: MutableList<SubTask> = mutableListOf(),
    private val taskListener: TaskListener,
    private val subTaskListener: SubTaskListener
) : RecyclerView.Adapter<InlineSubTaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InlineSubTaskViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_subtask_inline, parent, false)
        return InlineSubTaskViewHolder(view)
    }

    override fun getItemCount(): Int = subTasks.size

    override fun onBindViewHolder(holder: InlineSubTaskViewHolder, position: Int) {
        Log.d("MyLog", "Adapter: " + position)
        holder.bind(task, subTasks[position], taskListener, subTaskListener)
    }
}