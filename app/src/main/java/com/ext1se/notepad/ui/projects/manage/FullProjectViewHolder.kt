package com.ext1se.notepad.ui.projects.manage

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ext1se.dialog.color_dialog.ColorHelper
import com.ext1se.dialog.color_dialog.ColorItem
import com.ext1se.dialog.common.RectIconView
import com.ext1se.notepad.R
import com.ext1se.notepad.common.ProjectListener
import com.ext1se.notepad.data.model.Project

class FullProjectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val icon: RectIconView = itemView.findViewById(R.id.icon)
    private val title: TextView = itemView.findViewById(R.id.tv_title)
    private val description: TextView = itemView.findViewById(R.id.tv_description)
    private val star: ImageButton = itemView.findViewById(R.id.iv_star)
    private val edit: ImageView = itemView.findViewById(R.id.iv_edit)

    fun bind(project: Project, listener: ProjectListener) {
        icon.setIconTheme(project.idColorTheme, project.idIcon)

        if (project.name.isBlank()) {
            title.visibility = View.GONE
        } else {
            title.visibility = View.VISIBLE
            title.text = project.name
        }

        if (project.description.isBlank()) {
            description.visibility = View.GONE
        } else {
            description.visibility = View.VISIBLE
            description.text = project.description
        }

        if (project.isFavorite) {
            star.setImageResource(R.drawable.ic_star_active)
        } else {
            star.setImageResource(R.drawable.ic_star)
        }

        itemView.setOnClickListener {
            listener.selectProject(project, adapterPosition)
        }

        star.setOnClickListener{
            listener.setActiveProject(project, adapterPosition)
        }

        edit.setOnClickListener {
            listener.editProject(project)
        }
    }
}