package com.ext1se.notepad.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ext1se.notepad.data.ProjectRepository
import com.ext1se.notepad.preferences.SharedPreferencesHelper
import com.ext1se.notepad.ui.ProjectsViewModel
import com.ext1se.notepad.ui.projects.ProjectAdapter
import com.ext1se.notepad.ui.projects.manage.ManagerProjectsAdapter
import com.ext1se.notepad.ui.projects.manage.ManagerProjectsViewModel
import org.jetbrains.annotations.NotNull

class CustomFactoryManagerProjects(
    private val projectRepository: ProjectRepository,
    private val listener: ManagerProjectsAdapter.OnProjectListener

) : ViewModelProvider.NewInstanceFactory() {

    @NotNull
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ManagerProjectsViewModel(projectRepository, listener) as T
    }
}