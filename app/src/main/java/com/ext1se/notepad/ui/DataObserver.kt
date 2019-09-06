package com.ext1se.notepad.ui

import com.ext1se.notepad.data.model.Project

interface DataObserver {
    fun getProjects() : List<Project>
    fun setSelectedProject(project: Project)
    fun getSelectedProject(): Project

    fun addProject(project: Project)
    fun removeProject(project: Project)
    fun updateProject(project: Project)
}