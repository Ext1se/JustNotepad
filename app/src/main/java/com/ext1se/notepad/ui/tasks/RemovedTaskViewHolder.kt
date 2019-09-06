package com.ext1se.notepad.ui.tasks

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ext1se.dialog.common.RectIconView
import com.ext1se.notepad.R
import com.ext1se.notepad.data.model.Task

class RemovedTaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val titleTask: TextView = itemView.findViewById(R.id.tv_task_title)
    private val titleProject: TextView = itemView.findViewById(R.id.tv_project_title)
    private val description: TextView = itemView.findViewById(R.id.tv_description)
    private val icon: RectIconView = itemView.findViewById(R.id.icon_project)

    fun bind(task: Task) {
        if (task.name.isBlank()) {
            titleTask.visibility = View.GONE
        } else {
            titleTask.visibility = View.VISIBLE
            titleTask.text = task.name
        }

        if (task.description.isBlank()) {
            description.visibility = View.GONE
        } else {
            description.visibility = View.VISIBLE
            description.text = task.description
        }

        task.project?.let {
            titleProject.text = it.name
            icon.setIconTheme(it.idColorTheme,it.idIcon)
        }
    }
}
