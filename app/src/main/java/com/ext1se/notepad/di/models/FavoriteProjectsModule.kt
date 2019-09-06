package com.ext1se.notepad.di.models

import com.ext1se.notepad.di.providers.FavoriteProjectsViewModelProvider
import com.ext1se.notepad.ui.projects.favorite.FavoriteProjectsAdapter
import com.ext1se.notepad.ui.projects.favorite.FavoriteProjectsFragment
import com.ext1se.notepad.ui.projects.favorite.FavoriteProjectsViewModel
import com.ext1se.notepad.ui.tasks.TasksAdapter
import toothpick.config.Module

class FavoriteProjectsModule(private val fragment: FavoriteProjectsFragment) : Module() {

    init {
        bind(FavoriteProjectsFragment::class.java).toInstance(provideFavoriteProjects())
        bind(FavoriteProjectsAdapter.OnProjectListener::class.java).toInstance(provideFavoriteProjects())
        bind(TasksAdapter.OnTaskListener::class.java).toInstance(provideTasksListener())
        bind(FavoriteProjectsViewModel::class.java).toProvider(FavoriteProjectsViewModelProvider::class.java).providesSingletonInScope()
    }

    fun provideFavoriteProjects(): FavoriteProjectsFragment = fragment

    fun provideProjectsListener(): FavoriteProjectsAdapter.OnProjectListener = fragment

    fun provideTasksListener(): TasksAdapter.OnTaskListener = fragment

}