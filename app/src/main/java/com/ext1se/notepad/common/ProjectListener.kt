package com.ext1se.notepad.common

import com.ext1se.notepad.data.model.Project

interface ProjectListener {
    fun selectProject(project: Project, position: Int)
    fun setActiveProject(project: Project, position: Int) {}
    fun editProject(project: Project) {}
    fun deleteProject(project: Project, position: Int) {}


}