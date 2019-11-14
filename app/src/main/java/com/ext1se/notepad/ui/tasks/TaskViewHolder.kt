package com.ext1se.notepad.ui.tasks

import android.content.res.ColorStateList
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ext1se.notepad.R
import com.ext1se.notepad.common.TaskListener
import com.ext1se.notepad.data.model.Task
import android.graphics.Paint
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.recyclerview.widget.LinearLayoutManager
import com.ext1se.dialog.color_dialog.ColorHelper
import com.ext1se.notepad.common.SubTaskListener
import com.ext1se.notepad.data.model.Project


class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val title: TextView = itemView.findViewById(R.id.tv_title)
    private val description: TextView = itemView.findViewById(R.id.tv_description)
    private val checkCompleted: AppCompatCheckBox = itemView.findViewById(R.id.checkbox_completed)
    private val rvSubTasks: RecyclerView = itemView.findViewById(R.id.rv_subtasks)

    private val colorPassive: Int = itemView.resources.getColor(R.color.colorDefaultLight)
    private var colorActive: Int = colorPassive

    fun bind(task: Task, project: Project?, taskListener: TaskListener, subTaskListener: SubTaskListener) {

        itemView.setOnClickListener {
            taskListener.selectTask(task, adapterPosition)
        }

        project?.let {
            colorActive = ColorHelper.getColor(itemView.context, it.idColorTheme).lightColor
        }

        checkCompleted.setOnClickListener {
            taskListener.setTaskState(task, adapterPosition)
            setCheckBox(task.isCompleted)
            setStrikeFlag(task.isCompleted)
        }

        if (task.name.isBlank()) {
            title.visibility = View.GONE
        } else {
            title.visibility = View.VISIBLE
            title.text = task.name
        }

        if (task.description.isBlank()) {
            description.visibility = View.GONE
        } else {
            description.visibility = View.VISIBLE
            description.text = task.description
        }

        setCheckBox(task.isCompleted)
        setStrikeFlag(task.isCompleted)

        if (task.name.isBlank() && task.description.isBlank()) {
            checkCompleted.visibility = View.GONE
            rvSubTasks.setPadding(0, 0, 0, 0)
        } else {
            val marginTop = itemView.resources.getDimensionPixelOffset(R.dimen.rv_subtasks_margin)
            rvSubTasks.setPadding(0, marginTop, 0, 0)
            if (task.subTasks.size == 0) {
                checkCompleted.visibility = View.VISIBLE
            } else {
                checkCompleted.visibility = View.VISIBLE
            }
        }

        if (task.subTasks.size == 0) {
            rvSubTasks.visibility = View.GONE
        } else {
            rvSubTasks.visibility = View.VISIBLE
        }

        val adapter = InlineSubTasksAdapter(task, task.subTasks, taskListener, subTaskListener)
        /*      rvSubTasks.addItemDecoration(
                  DividerItemDecoration(
                      rvSubTasks.context,
                      RecyclerView.VERTICAL
                  )
              )*/
        rvSubTasks.layoutManager =
            LinearLayoutManager(itemView.context, RecyclerView.VERTICAL, false)
        rvSubTasks.adapter = adapter


        description.visibility = View.VISIBLE
        description.text = "position = " + task.position
        if (!task.description.isNullOrBlank()){
            description.text = description.text.toString() + "\n" + task.description
        }
    }

    private fun setCheckBox(isCompleted: Boolean) {
        checkCompleted.isChecked = isCompleted
        if (isCompleted) {
            checkCompleted.supportButtonTintList = ColorStateList.valueOf(colorActive)
        } else {
            checkCompleted.supportButtonTintList = ColorStateList.valueOf(colorPassive)
        }
    }

    private fun setStrikeFlag(isStriked: Boolean) {
        if (!isStriked) {
            title.setPaintFlags(title.getPaintFlags() and (Paint.STRIKE_THRU_TEXT_FLAG.inv()))
            description.setPaintFlags(title.getPaintFlags() and (Paint.STRIKE_THRU_TEXT_FLAG.inv()))
            val color = itemView.resources.getColor(R.color.colorDefault)
            title.setTextColor(color)
            description.setTextColor(color)
        } else {
            title.setPaintFlags(title.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG)
            description.setPaintFlags(title.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG)
            val color = itemView.resources.getColor(R.color.colorDisabled)
            title.setTextColor(color)
            description.setTextColor(color)
        }
    }
}