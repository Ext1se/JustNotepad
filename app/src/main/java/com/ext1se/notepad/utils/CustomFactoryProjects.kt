package com.ext1se.notepad.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ext1se.notepad.data.ProjectRepository
import com.ext1se.notepad.preferences.SharedPreferencesHelper
import com.ext1se.notepad.ui.ProjectsViewModel
import com.ext1se.notepad.ui.projects.ProjectAdapter
import org.jetbrains.annotations.NotNull

class CustomFactoryProjects(
    private val projectRepository: ProjectRepository,
    private val preferencesHelper: SharedPreferencesHelper,
    private val listener: ProjectAdapter.OnProjectListener

) : ViewModelProvider.NewInstanceFactory() {

    @NotNull
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ProjectsViewModel(projectRepository, preferencesHelper, listener) as T
    }
}