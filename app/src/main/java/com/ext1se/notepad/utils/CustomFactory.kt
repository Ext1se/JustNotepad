package com.ext1se.notepad.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ext1se.notepad.data.ProjectRepository
import com.ext1se.notepad.data.TaskRepository
import com.ext1se.notepad.preferences.SharedPreferencesHelper
import com.ext1se.notepad.ui.ProjectsViewModel
import com.ext1se.notepad.ui.projects.ProjectAdapter
import com.ext1se.notepad.ui.projects.ProjectViewModel
import com.ext1se.notepad.ui.projects.favorite.FavoriteProjectsAdapter
import com.ext1se.notepad.ui.projects.favorite.FavoriteProjectsViewModel
import com.ext1se.notepad.ui.projects.manage.ManagerProjectsAdapter
import com.ext1se.notepad.ui.projects.manage.ManagerProjectsViewModel
import com.ext1se.notepad.ui.tasks.RemovedTasksAdapter
import com.ext1se.notepad.ui.tasks.RemovedTasksViewModel
import com.ext1se.notepad.ui.tasks.TaskViewModel
import com.ext1se.notepad.ui.tasks.TasksAdapter
import org.jetbrains.annotations.NotNull

class CustomFactory(
    private val preferencesHelper: SharedPreferencesHelper? = null,

    private val projectRepository: ProjectRepository? = null,
    private val taskRepository: TaskRepository? = null,

    private val managerProjectListener: ManagerProjectsAdapter.OnProjectListener? = null,

    private val favoriteProjectListener: FavoriteProjectsAdapter.OnProjectListener? = null,
    private val tasksListener: TasksAdapter.OnTaskListener? = null,

    private val removedTasksListener: RemovedTasksAdapter.OnTaskListener? = null,

    private val projectListener: ProjectAdapter.OnProjectListener? = null


) : ViewModelProvider.NewInstanceFactory() {

    @NotNull
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProjectsViewModel::class.java)) {
            return ProjectsViewModel(projectRepository!!, preferencesHelper!!, projectListener!!) as T
        }

        if (modelClass.isAssignableFrom(ManagerProjectsViewModel::class.java)) {
            return ManagerProjectsViewModel(projectRepository!!, managerProjectListener!!) as T
        }

        if (modelClass.isAssignableFrom(FavoriteProjectsViewModel::class.java)) {
            return FavoriteProjectsViewModel(projectRepository!!, taskRepository!!, favoriteProjectListener!!, tasksListener!!) as T
        }

        if (modelClass.isAssignableFrom(RemovedTasksViewModel::class.java)) {
            return RemovedTasksViewModel(projectRepository!!, taskRepository!!, removedTasksListener!!) as T
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