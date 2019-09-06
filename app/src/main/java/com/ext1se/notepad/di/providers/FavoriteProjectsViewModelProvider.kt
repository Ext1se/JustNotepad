package com.ext1se.notepad.di.providers

import androidx.lifecycle.ViewModelProviders
import com.ext1se.notepad.data.ProjectRepository
import com.ext1se.notepad.data.TaskRepository
import com.ext1se.notepad.ui.projects.favorite.FavoriteProjectsAdapter
import com.ext1se.notepad.ui.projects.favorite.FavoriteProjectsFragment
import com.ext1se.notepad.ui.projects.favorite.FavoriteProjectsViewModel
import com.ext1se.notepad.ui.tasks.TasksAdapter
import com.ext1se.notepad.utils.CustomFactoryFavoriteProjects
import javax.inject.Inject
import javax.inject.Provider


class FavoriteProjectsViewModelProvider @Inject constructor(
    val fragment: FavoriteProjectsFragment,
    val projectRepository: ProjectRepository,
    val taskRepository: TaskRepository,
    val projectListener: FavoriteProjectsAdapter.OnProjectListener,
    val taskListener: TasksAdapter.OnTaskListener
) : Provider<FavoriteProjectsViewModel> {

    override fun get(): FavoriteProjectsViewModel {
        val factory = CustomFactoryFavoriteProjects(projectRepository, taskRepository, projectListener, taskListener)
        return ViewModelProviders.of(fragment, factory).get(FavoriteProjectsViewModel::class.java)
    }
}