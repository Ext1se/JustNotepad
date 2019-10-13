package com.ext1se.notepad.ui.projects.manage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ext1se.notepad.common.ProjectListener
import com.ext1se.notepad.data.ProjectRepository
import com.ext1se.notepad.data.TaskRepository
import com.ext1se.notepad.data.model.Project
import com.ext1se.notepad.data.model.Task
import com.ext1se.notepad.ui.tasks.TasksAdapter

class ManagerProjectsViewModel(
    private val projectRepository: ProjectRepository,
    var projectListener: ProjectListener
) : ViewModel() {

    val projects: MutableLiveData<List<Project>> = MutableLiveData()
    var countActiveProject: Int = 0

    init {
        countActiveProject = projectRepository.getCountActiveProjects()
    }

    fun setProjects(projects: List<Project>) {
        this.projects.value = projects
    }

    fun increaseActiveProject() {
        if (countActiveProject < 5) {
            countActiveProject++
        }
    }

    fun decreaseActiveProject(){
        if (countActiveProject > 0){
            countActiveProject--
        }
    }
}