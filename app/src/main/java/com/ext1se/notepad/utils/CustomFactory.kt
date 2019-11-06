package com.ext1se.notepad.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ext1se.notepad.common.ProjectListener
import com.ext1se.notepad.common.SubTaskListener
import com.ext1se.notepad.common.TaskListener
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

class CustomFactory() : ViewModelProvider.NewInstanceFactory() {

    private lateinit var preferencesHelper: SharedPreferencesHelper
    private lateinit var projectRepository: ProjectRepository
    private lateinit var taskRepository: TaskRepository
    private lateinit var projectListener: ProjectListener
    private lateinit var taskListener: TaskListener
    private lateinit var subTaskListener: SubTaskListener

    constructor(
        preferencesHelper: SharedPreferencesHelper,
        projectRepository: ProjectRepository,
        projectListener: ProjectListener
    ) : this() {
        this.preferencesHelper = preferencesHelper
        this.projectRepository = projectRepository
        this.projectListener = projectListener
    }

    constructor(
        projectRepository: ProjectRepository,
        projectListener: ProjectListener
    ) : this() {
        this.projectRepository = projectRepository
        this.projectListener = projectListener
    }

    constructor(
        projectRepository: ProjectRepository,
        projectListener: ProjectListener,
        taskRepository: TaskRepository,
        taskListener: TaskListener
    ) : this() {
        this.projectRepository = projectRepository
        this.projectListener = projectListener
        this.taskRepository = taskRepository
        this.taskListener = taskListener
    }

    constructor(
        projectRepository: ProjectRepository,
        taskRepository: TaskRepository,
        taskListener: TaskListener
    ) : this() {
        this.projectRepository = projectRepository
        this.taskRepository = taskRepository
        this.taskListener = taskListener
    }

    constructor(
        subTaskListener: SubTaskListener
    ) : this() {
        this.subTaskListener = subTaskListener
    }

    @NotNull
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProjectsViewModel::class.java)) {
            return ProjectsViewModel(
                projectRepository,
                preferencesHelper,
                projectListener
            ) as T
        }

        if (modelClass.isAssignableFrom(ManagerProjectsViewModel::class.java)) {
            return ManagerProjectsViewModel(projectRepository, projectListener) as T
        }

        if (modelClass.isAssignableFrom(FavoriteProjectsViewModel::class.java)) {
            return FavoriteProjectsViewModel(
                projectRepository,
                taskRepository,
                projectListener,
                taskListener
            ) as T
        }

        if (modelClass.isAssignableFrom(RemovedTasksViewModel::class.java)) {
            return RemovedTasksViewModel(
                projectRepository,
                taskRepository,
                taskListener
            ) as T
        }

        if (modelClass.isAssignableFrom(ProjectViewModel::class.java)) {
            return ProjectViewModel() as T
        }

        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            return TaskViewModel(subTaskListener) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}