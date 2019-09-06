package com.ext1se.notepad.di.providers

import androidx.lifecycle.ViewModelProviders
import com.ext1se.notepad.ui.projects.ProjectFragment
import com.ext1se.notepad.ui.projects.ProjectViewModel
import com.ext1se.notepad.utils.CustomFactoryProject
import javax.inject.Inject
import javax.inject.Provider

class ProjectViewModelProvider @Inject constructor(
    val fragment: ProjectFragment
) : Provider<ProjectViewModel> {

    override fun get(): ProjectViewModel {
        val factory = CustomFactoryProject()
        return ViewModelProviders.of(fragment, factory).get(ProjectViewModel::class.java)
    }
}