package com.ext1se.notepad.ui.projects.favorite

import android.graphics.PorterDuff
import android.os.Build
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ext1se.dialog.color_dialog.ColorHelper
import com.ext1se.dialog.icon_dialog.IconView
import com.ext1se.notepad.R
import com.ext1se.notepad.common.ProjectListener
import com.ext1se.notepad.data.model.Project


class FavoriteProjectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val icon: IconView = itemView.findViewById(R.id.icon)
    private val name: TextView = itemView.findViewById(R.id.tv_name)

    fun bind(
        project: Project,
        listener: ProjectListener,
        isSelected: Boolean = false
    ) {
        icon.setIcon(project.idIcon)
        name.text = project.name
        itemView.setOnClickListener {
            listener.selectProject(project, adapterPosition)
        }
        val colorItem = ColorHelper.getColor(itemView.context, project.idColorTheme)
        if (isSelected) {
            setIconColor(colorItem.primaryColor)
            name.setTextColor(colorItem.primaryColor)
        } else {
            val color = itemView.context.resources.getColor(R.color.colorDefault)
            setIconColor(color)
            name.setTextColor(color)
        }
    }

    private fun setIconColor(color: Int) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            icon.setColorFilterPreLollipop(color)
        } else {
            icon.setColorFilter(color, PorterDuff.Mode.SRC_IN)
        }
    }
}