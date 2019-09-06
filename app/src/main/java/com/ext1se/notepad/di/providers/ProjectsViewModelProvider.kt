package com.ext1se.notepad.di.providers

import androidx.lifecycle.ViewModelProviders
import com.ext1se.notepad.data.ProjectRepository
import com.ext1se.notepad.di.models.AppModule
import com.ext1se.notepad.preferences.SharedPreferencesHelper
import com.ext1se.notepad.ui.MainActivity
import com.ext1se.notepad.ui.ProjectsViewModel
import com.ext1se.notepad.ui.projects.ProjectAdapter
import com.ext1se.notepad.utils.CustomFactoryProjects
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Provider

class ProjectsViewModelProvider @Inject constructor(
    val activity: MainActivity,
    @Named(AppModule.PREFERENCES_HELPER)
    val preferences: SharedPreferencesHelper,
    val projectRepository: ProjectRepository,
    val projectListener: ProjectAdapter.OnProjectListener
) : Provider<ProjectsViewModel> {

    override fun get(): ProjectsViewModel {
        val factory = CustomFactoryProjects(projectRepository, preferences, projectListener)
        return ViewModelProviders.of(activity, factory).get(ProjectsViewModel::class.java)
    }
}