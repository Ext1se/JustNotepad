package com.ext1se.notepad.di.models

import com.ext1se.notepad.di.providers.ProjectViewModelProvider
import com.ext1se.notepad.ui.projects.ProjectFragment
import com.ext1se.notepad.ui.projects.ProjectViewModel
import toothpick.config.Module

class ProjectModule(private val fragment: ProjectFragment) : Module() {

    init {
        bind(ProjectFragment::class.java).toInstance(provideProjectFragment())
        bind(ProjectViewModel::class.java).toProvider(ProjectViewModelProvider::class.java).providesSingletonInScope()
    }

    fun provideProjectFragment(): ProjectFragment = fragment

}