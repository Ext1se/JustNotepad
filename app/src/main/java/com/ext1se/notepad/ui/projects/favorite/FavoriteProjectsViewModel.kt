package com.ext1se.notepad.ui.projects.favorite

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ext1se.notepad.common.ProjectListener
import com.ext1se.notepad.common.TaskListener
import com.ext1se.notepad.data.ProjectRepository
import com.ext1se.notepad.data.TaskRepository
import com.ext1se.notepad.data.model.Project
import com.ext1se.notepad.data.model.Task
import com.ext1se.notepad.ui.tasks.TasksAdapter

class FavoriteProjectsViewModel(
    private val projectRepository: ProjectRepository,
    private val taskRepository: TaskRepository,
    var projectListener: ProjectListener,
    var taskListener: TaskListener
) : ViewModel() {

    val projects: MutableLiveData<List<Project>> = MutableLiveData()
    val tasks: MutableLiveData<List<Task>> = MutableLiveData()
    val idColorTheme: MutableLiveData<Int> = MutableLiveData()
    val selectedProject: MutableLiveData<Project> = MutableLiveData()

    init {
        projects.value = projectRepository.getFavoriteProjects()
    }

    fun setSelectedProject(project: Project) {
        if (selectedProject.value?.isValid == false || selectedProject.value != project) {
            selectedProject.value = project
            idColorTheme.value = project.idColorTheme
            tasks.value = taskRepository.getTasksOfProject(project.id)
        }
    }
}