package com.ext1se.notepad.ui.projects.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ext1se.notepad.R
import com.ext1se.notepad.data.model.Project

class FavoriteProjectsAdapter(private val projects: List<Project>, private val listener: OnProjectListener) :
    RecyclerView.Adapter<FavoriteProjectViewHolder>() {

    private var selectedPosition: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteProjectViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_project_favorite, parent, false)
        return FavoriteProjectViewHolder(view)
    }

    override fun getItemCount(): Int = projects.size

    override fun onBindViewHolder(holder: FavoriteProjectViewHolder, position: Int) = holder.bind(projects[position], listener, position == selectedPosition)

    fun setSelectedPosition(project: Project?) {
        if (projects.isNullOrEmpty() || project == null) {
            selectedPosition = -1
        }
        projects.forEachIndexed { index, item ->
            if (item.id == project?.id) {
                setSelectedPosition(index)
                return
            }
        }
        if (selectedPosition != -1){
            notifyItemChanged(selectedPosition)
            selectedPosition = -1
        }
    }

    fun setSelectedPosition(position: Int) {
        if (selectedPosition == position) {
            return
        }
        if (selectedPosition != -1) {
            notifyItemChanged(selectedPosition)
        }
        selectedPosition = position
        notifyItemChanged(selectedPosition)

    }

    interface OnProjectListener {
        fun onClickProject(project: Project, position: Int)
    }
}