package com.ext1se.notepad.ui.tasks

import android.content.res.ColorStateList
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ext1se.notepad.R
import com.ext1se.notepad.common.TaskListener
import com.ext1se.notepad.data.model.Task
import android.graphics.Paint
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.ext1se.dialog.color_dialog.ColorHelper
import com.ext1se.notepad.common.SubTaskListener


class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val title: TextView = itemView.findViewById(R.id.tv_title)
    private val description: TextView = itemView.findViewById(R.id.tv_description)
    private val checkCompleted: AppCompatCheckBox = itemView.findViewById(R.id.checkbox_completed)
    private val rvSubTasks: RecyclerView = itemView.findViewById(R.id.rv_subtasks)
    private val switchCompleted: SwitchCompat = itemView.findViewById(R.id.switch_completed)

    fun bind(task: Task, taskListener: TaskListener, subTaskListener: SubTaskListener) {

        itemView.setOnClickListener {
            taskListener.selectTask(task, adapterPosition)
        }

        rvSubTasks.setOnClickListener {
            taskListener.selectTask(task, adapterPosition)
        }

        var colorActive: Int = itemView.resources.getColor(R.color.colorDefaultLight)
        task.project?.let {
            colorActive = ColorHelper.getColor(itemView.context, it.idColorTheme).lightColor
        }

        checkCompleted.setOnClickListener {
            taskListener.setTaskState(task, adapterPosition)
            if (task.isCompleted) {
                checkCompleted.supportButtonTintList =
                    ColorStateList.valueOf(colorActive)
                setStrikeFlag(true)
            } else {
                setStrikeFlag(false)
                checkCompleted.supportButtonTintList =
                    ColorStateList.valueOf(itemView.resources.getColor(R.color.colorDefault))
            }
        }

        if (task.isCompleted) {
            checkCompleted.supportButtonTintList =
                ColorStateList.valueOf(colorActive)
        } else {
            checkCompleted.supportButtonTintList =
                ColorStateList.valueOf(itemView.resources.getColor(R.color.colorDefault))
        }

        switchCompleted.setOnClickListener {
            taskListener.setTaskState(task, adapterPosition)
            if (switchCompleted.isChecked) {
                setStrikeFlag(true)
            } else {
                setStrikeFlag(false)
            }
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

        if (task.isCompleted) {
            setStrikeFlag(true)
        } else {
            setStrikeFlag(false)
        }
        checkCompleted.isChecked = task.isCompleted
        switchCompleted.isChecked = task.isCompleted

        if (task.name.isBlank() && task.description.isBlank()) {
            checkCompleted.visibility = View.GONE
            switchCompleted.visibility = View.GONE
            rvSubTasks.setPadding(0, 0, 0, 0)
        } else {
            rvSubTasks.setPadding(
                0,
                itemView.resources.getDimensionPixelOffset(R.dimen.rv_subtasks_margin),
                0,
                0
            )
            //checkCompleted.visibility = View.GONE
            //switchCompleted.visibility = View.VISIBLE
            if (task.subTasks.size == 0) {
                checkCompleted.visibility = View.VISIBLE
                switchCompleted.visibility = View.GONE
            } else {
                switchCompleted.visibility = View.GONE
                checkCompleted.visibility = View.VISIBLE
            }
        }


        if (task.subTasks.size == 0) {
            rvSubTasks.visibility = View.GONE
        } else {
            rvSubTasks.visibility = View.VISIBLE
        }


        val adapter = SubTasksInItemAdapter(task, task.subTasks, taskListener, subTaskListener)
        /*      rvSubTasks.addItemDecoration(
                  DividerItemDecoration(
                      rvSubTasks.context,
                      RecyclerView.VERTICAL
                  )
              )*/
        rvSubTasks.layoutManager =
            LinearLayoutManager(itemView.context, RecyclerView.VERTICAL, false)
        rvSubTasks.adapter = adapter

        val subtasks = arrayListOf<String>()
        var position = 1
        for (subTask in task.subTasks) {
            subtasks.add("${position}. ${subTask.name}")
            position++
        }
        val stringAdapter = StringsAdapter(subtasks)
        //rvSubTasks.adapter = stringAdapter
    }

    private fun setPaintFlags(flag: Int) {
        title.setPaintFlags(title.getPaintFlags() or flag)
        description.setPaintFlags(title.getPaintFlags() or flag)
        if (flag == 0) {
            val color = itemView.resources.getColor(R.color.colorDefault)
            title.setTextColor(color)
            description.setTextColor(color)
        } else {
            val color = itemView.resources.getColor(R.color.colorDisabled)
            title.setTextColor(color)
            description.setTextColor(color)
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