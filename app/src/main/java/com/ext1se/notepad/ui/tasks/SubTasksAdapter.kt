package com.ext1se.notepad.ui.tasks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ext1se.notepad.R
import com.ext1se.notepad.common.SubTaskListener
import com.ext1se.notepad.data.model.SubTask
import com.ext1se.notepad.utils.ItemSwipeHelper

class SubTasksAdapter(
    private var subTasks: MutableList<SubTask> = mutableListOf(),
    private val listener: SubTaskListener
) : RecyclerView.Adapter<SubTaskViewHolder>(),
    ItemSwipeHelper.ItemSwipeHelperAdapter {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubTaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_subtask_edit, parent, false)
        return SubTaskViewHolder(view)
    }

    override fun getItemCount(): Int = subTasks.size

    override fun onBindViewHolder(holder: SubTaskViewHolder, position: Int) = holder.bind(subTasks[position], listener)

    fun update(subTasks: MutableList<SubTask>) {
        this.subTasks = subTasks
        notifyDataSetChanged()
    }

    fun addNewItem(){

    }

    override fun onItemDismiss(position: Int, direction: Int) {
        listener.swipeSubTask(subTasks[position], position, direction)
    }
}