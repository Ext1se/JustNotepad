package com.ext1se.notepad.ui.projects

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ext1se.notepad.R
import com.ext1se.notepad.data.ProjectRepository
import com.ext1se.notepad.data.TaskRepository
import com.ext1se.notepad.data.model.Project
import com.ext1se.notepad.data.model.Task
import com.ext1se.notepad.ui.tasks.TasksAdapter

class ProjectViewModel : ViewModel() {

    val project: MutableLiveData<Project> = MutableLiveData()
    val idColorTheme: MutableLiveData<Int> = MutableLiveData()
    val idIcon: MutableLiveData<Int> = MutableLiveData()

    fun setProject(project: Project){
        this.project.value = project
        idColorTheme.value = project.idColorTheme
        idIcon.value = project.idIcon
    }

    fun setColor(idColorTheme: Int) {
        project.value?.idColorTheme = idColorTheme
        this.idColorTheme.value = idColorTheme
    }

    fun setIcon(idIcon: Int) {
        project.value?.idIcon = idIcon
        this.idIcon.value = idIcon
    }
}