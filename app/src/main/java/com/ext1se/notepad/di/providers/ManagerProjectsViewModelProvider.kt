package com.ext1se.notepad.di.providers

import androidx.lifecycle.ViewModelProviders
import com.ext1se.notepad.data.ProjectRepository
import com.ext1se.notepad.ui.projects.manage.ManagerProjectsAdapter
import com.ext1se.notepad.ui.projects.manage.ManagerProjectsFragment
import com.ext1se.notepad.ui.projects.manage.ManagerProjectsViewModel
import com.ext1se.notepad.utils.CustomFactoryManagerProjects
import javax.inject.Inject
import javax.inject.Provider


class ManagerProjectsViewModelProvider @Inject constructor(
    val fragment: ManagerProjectsFragment,
    val projectRepository: ProjectRepository,
    val projectListener: ManagerProjectsAdapter.OnProjectListener
) : Provider<ManagerProjectsViewModel> {

    override fun get(): ManagerProjectsViewModel {
        val factory = CustomFactoryManagerProjects(projectRepository, projectListener)
        return ViewModelProviders.of(fragment, factory).get(ManagerProjectsViewModel::class.java)
    }
}