package com.ext1se.notepad.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ext1se.notepad.data.ProjectRepository
import com.ext1se.notepad.data.TaskRepository
import com.ext1se.notepad.ui.projects.favorite.FavoriteProjectsViewModel
import com.ext1se.notepad.ui.projects.favorite.FavoriteProjectsAdapter
import com.ext1se.notepad.ui.tasks.TasksAdapter
import org.jetbrains.annotations.NotNull

class CustomFactoryFavoriteProjects(
    private val projectRepository: ProjectRepository,
    private val taskRepository: TaskRepository,
    private val projectListener: FavoriteProjectsAdapter.OnProjectListener,
    private val taskListener: TasksAdapter.OnTaskListener
) : ViewModelProvider.NewInstanceFactory() {

    @NotNull
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FavoriteProjectsViewModel(
            projectRepository,
            taskRepository,
            projectListener,
            taskListener
        ) as T
    }
}