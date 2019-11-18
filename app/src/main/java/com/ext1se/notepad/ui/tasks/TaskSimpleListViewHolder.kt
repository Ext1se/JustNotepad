package com.ext1se.notepad.ui.tasks

import android.os.Build
import android.text.Html
import android.view.View
import android.widget.TextView
import com.ext1se.notepad.R
import com.ext1se.notepad.common.SubTaskListener
import com.ext1se.notepad.common.TaskListener
import com.ext1se.notepad.data.model.Project
import com.ext1se.notepad.data.model.Task

class TaskSimpleListViewHolder(itemView: View) : BaseTaskViewHolder(itemView) {

    private val tvSubTasks: TextView = itemView.findViewById(R.id.tv_subtasks)

    override fun bind(
        task: Task,
        project: Project?,
        taskListener: TaskListener,
        subTaskListener: SubTaskListener
    ) {
        super.bind(task, project, taskListener, subTaskListener)
        //checkCompleted.visibility = View.GONE
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
    }
}