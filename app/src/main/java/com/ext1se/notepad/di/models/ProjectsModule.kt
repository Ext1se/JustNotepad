package com.ext1se.notepad.di.models

import com.ext1se.notepad.common.ProjectListener
import com.ext1se.notepad.di.providers.ProjectsViewModelProvider
import com.ext1se.notepad.ui.MainActivity
import com.ext1se.notepad.ui.ProjectsViewModel
import com.ext1se.notepad.ui.projects.ProjectAdapter
import toothpick.config.Module

class ProjectsModule(private val activity: MainActivity) : Module() {

    init {
        bind(MainActivity::class.java).toInstance(provideActivity())
        bind(ProjectListener::class.java).toInstance(provideProjectsListener())
        bind(ProjectsViewModel::class.java).toProvider(ProjectsViewModelProvider::class.java).providesSingletonInScope()
    }

    fun provideActivity(): MainActivity = activity

    fun provideProjectsListener(): ProjectListener = activity

}