package com.ext1se.notepad.di.models

import com.ext1se.notepad.common.ProjectListener
import com.ext1se.notepad.common.TaskListener
import com.ext1se.notepad.di.providers.FavoriteProjectsViewModelProvider
import com.ext1se.notepad.ui.projects.favorite.FavoriteProjectsFragment
import com.ext1se.notepad.ui.projects.favorite.FavoriteProjectsViewModel
import toothpick.config.Module

class FavoriteProjectsModule(private val fragment: FavoriteProjectsFragment) : Module() {

    init {
        bind(FavoriteProjectsFragment::class.java).toInstance(provideFavoriteProjects())
        bind(ProjectListener::class.java).toInstance(provideFavoriteProjects())
        bind(TaskListener::class.java).toInstance(provideTasksListener())
        bind(FavoriteProjectsViewModel::class.java).toProvider(FavoriteProjectsViewModelProvider::class.java).providesSingletonInScope()
    }

    fun provideFavoriteProjects(): FavoriteProjectsFragment = fragment

    fun provideProjectsListener(): ProjectListener = fragment

    fun provideTasksListener(): TaskListener = fragment

}