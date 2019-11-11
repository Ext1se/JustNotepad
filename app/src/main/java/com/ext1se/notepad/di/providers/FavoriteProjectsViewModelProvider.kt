package com.ext1se.notepad.di.providers

import androidx.lifecycle.ViewModelProviders
import com.ext1se.notepad.common.ProjectListener
import com.ext1se.notepad.common.SubTaskListener
import com.ext1se.notepad.common.TaskListener
import com.ext1se.notepad.data.ProjectRepository
import com.ext1se.notepad.data.TaskRepository
import com.ext1se.notepad.ui.projects.favorite.FavoriteProjectsFragment
import com.ext1se.notepad.ui.projects.favorite.FavoriteProjectsViewModel
import com.ext1se.notepad.utils.CustomFactory
import javax.inject.Inject
import javax.inject.Provider

class FavoriteProjectsViewModelProvider @Inject constructor(
    val fragment: FavoriteProjectsFragment,
    val projectRepository: ProjectRepository,
    val taskRepository: TaskRepository,
    val projectListener: ProjectListener,
    val taskListener: TaskListener,
    val subTaskListener: SubTaskListener
) : Provider<FavoriteProjectsViewModel> {

    override fun get(): FavoriteProjectsViewModel {
        val factory = CustomFactory(projectRepository, projectListener, taskRepository, taskListener, subTaskListener)
        return ViewModelProviders.of(fragment, factory).get(FavoriteProjectsViewModel::class.java)
    }
}