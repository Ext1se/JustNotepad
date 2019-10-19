package com.ext1se.notepad.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ext1se.notepad.common.ProjectListener
import com.ext1se.notepad.common.TaskListener
import com.ext1se.notepad.data.ProjectRepository
import com.ext1se.notepad.data.TaskRepository
import com.ext1se.notepad.preferences.SharedPreferencesHelper
import com.ext1se.notepad.ui.ProjectsViewModel
import com.ext1se.notepad.ui.projects.ProjectViewModel
import com.ext1se.notepad.ui.projects.favorite.FavoriteProjectsViewModel
import com.ext1se.notepad.ui.projects.manage.ManagerProjectsViewModel
import com.ext1se.notepad.ui.tasks.RemovedTasksViewModel
import com.ext1se.notepad.ui.tasks.TaskViewModel
import org.jetbrains.annotations.NotNull

class SimpleCustomFactory(
    private val preferencesHelper: SharedPreferencesHelper? = null,
    private val projectRepository: ProjectRepository? = null,
    private val taskRepository: TaskRepository? = null,
    private val projectListener: ProjectListener? = null,
    private val tasksListener: TaskListener? = null
) : ViewModelProvider.NewInstanceFactory() {

    @NotNull
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProjectsViewModel::class.java)) {
            return ProjectsViewModel(
                projectRepository!!,
                preferencesHelper!!,
                projectListener!!
            ) as T
        }

        if (modelClass.isAssignableFrom(ManagerProjectsViewModel::class.java)) {
            return ManagerProjectsViewModel(projectRepository!!, projectListener!!) as T
        }

        if (modelClass.isAssignableFrom(FavoriteProjectsViewModel::class.java)) {
            return FavoriteProjectsViewModel(
                projectRepository!!,
                taskRepository!!,
                projectListener!!,
                tasksListener!!
            ) as T
        }

        if (modelClass.isAssignableFrom(RemovedTasksViewModel::class.java)) {
            return RemovedTasksViewModel(
                projectRepository!!,
                taskRepository!!,
                tasksListener!!
            ) as T
        }

        if (modelClass.isAssignableFrom(ProjectViewModel::class.java)) {
            return ProjectViewModel() as T
        }

        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            return TaskViewModel() as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}