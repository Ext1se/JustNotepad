package com.ext1se.notepad.ui.projects.manage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ext1se.notepad.R
import com.ext1se.notepad.common.ProjectListener
import com.ext1se.notepad.data.model.Project
import com.ext1se.notepad.utils.ItemSwipeHelper

class ManagerProjectsAdapter(private val projects: List<Project>, private val listener: ProjectListener) :
    RecyclerView.Adapter<FullProjectViewHolder>(),
    ItemSwipeHelper.ItemSwipeHelperAdapter {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FullProjectViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_project_full, parent, false)
        return FullProjectViewHolder(view)
    }

    override fun getItemCount(): Int = projects.size

    override fun onBindViewHolder(holder: FullProjectViewHolder, position: Int) =
        holder.bind(projects[position], listener)

    override fun onItemDismiss(position: Int, direction: Int) {
        listener.deleteProject(projects[position], position)
    }
}