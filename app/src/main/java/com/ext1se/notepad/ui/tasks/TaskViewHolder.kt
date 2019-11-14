package com.ext1se.notepad.ui.tasks

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ext1se.notepad.R
import com.ext1se.notepad.common.TaskListener
import com.ext1se.notepad.data.model.Task
import android.graphics.Paint
import android.os.Build
import android.text.Html
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
    private val tvSubTasks: TextView = itemView.findViewById(R.id.tv_subtasks)

    private val colorPassive: Int = itemView.resources.getColor(R.color.colorDefaultLight)
    private var colorActive: Int = colorPassive

    fun setRecycledViewPool(viewPool: RecyclerView.RecycledViewPool){
        rvSubTasks.setRecycledViewPool(viewPool)
    }

    fun bind(
        task: Task,
        project: Project?,
        taskListener: TaskListener,
        subTaskListener: SubTaskListener
    ) {

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
        } else {
            checkCompleted.visibility = View.VISIBLE
        }

        /*     description.visibility = View.VISIBLE
             description.text = "position = " + task.position
             if (!task.description.isNullOrBlank()) {
                 description.text = description.text.toString() + "\n" + task.description
             }
     */
        val useRecyclerView = false

        if (useRecyclerView) {
            tvSubTasks.visibility = View.GONE
            if (task.subTasks.size == 0) {
                rvSubTasks.visibility = View.GONE
            } else {
                rvSubTasks.visibility = View.VISIBLE
                val adapter =
                    InlineSubTasksAdapter(task, task.subTasks, taskListener, subTaskListener)
                val linearLayoutManager =
                    LinearLayoutManager(itemView.context, RecyclerView.VERTICAL, false)
                linearLayoutManager.initialPrefetchItemCount = task.subTasks.size
                rvSubTasks.getRecycledViewPool().setMaxRecycledViews(0, task.subTasks.size)
                rvSubTasks.layoutManager = linearLayoutManager
                rvSubTasks.adapter = adapter
            }
        } else {
            checkCompleted.visibility = View.GONE
            rvSubTasks.visibility = View.GONE
            if (task.subTasks.size > 0) {
                tvSubTasks.visibility = View.VISIBLE
                var stringSubTasks = ""
                for ((index, subtask) in task.subTasks.withIndex()) {
                    if (index != 0) {
                        stringSubTasks += "<br>"
                    }
                    var stringSubTask = "${index + 1}. ${subtask.name}"

                    if (subtask.isCompleted) {
                        val color = itemView.resources.getColor(R.color.colorDisabled)
                        stringSubTask = "<s><font color='$color'>$stringSubTask</font></s>"
                    } else {
                        val color = itemView.resources.getColor(R.color.colorDefault)
                        stringSubTask = "<font color='$color'>$stringSubTask</font>"
                    }

                    stringSubTasks += stringSubTask
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    tvSubTasks.setText(Html.fromHtml(stringSubTasks, Html.FROM_HTML_MODE_LEGACY));
                } else {
                    tvSubTasks.setText(Html.fromHtml(stringSubTasks));
                }
            } else {
                tvSubTasks.visibility = View.GONE
            }
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