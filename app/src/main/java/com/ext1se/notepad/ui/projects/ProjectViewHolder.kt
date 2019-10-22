package com.ext1se.notepad.ui.projects

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ext1se.dialog.common.RoundRectIconView
import com.ext1se.notepad.R
import com.ext1se.notepad.common.ProjectListener
import com.ext1se.notepad.data.model.Project

class ProjectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val icon: RoundRectIconView = itemView.findViewById(R.id.icon)
    private val name: TextView = itemView.findViewById(R.id.tv_name)
    private val description: TextView = itemView.findViewById(R.id.tv_description)

    fun bind(project: Project, listener: ProjectListener) {
        icon.setIconTheme(project.idColorTheme, project.idIcon)
        name.text = project.name
        if (!project.description.isNullOrBlank()) {
            description.text = project.description
            description.visibility = View.VISIBLE
        } else {
            description.visibility = View.GONE
        }
        itemView.setOnClickListener {
            listener.selectProject(project, adapterPosition)
        }
    }
}