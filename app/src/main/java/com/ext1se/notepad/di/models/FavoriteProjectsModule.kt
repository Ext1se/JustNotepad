package com.ext1se.notepad.di.models

import com.ext1se.notepad.common.ProjectListener
import com.ext1se.notepad.common.TaskListener
import com.ext1se.notepad.di.providers.FavoriteProjectsViewModelProvider
import com.ext1se.notepad.ui.projects.favorite.FavoriteProjectsFragment
import com.ext1se.notepad.ui.projects.favorite.FavoriteProjectsViewModel

class FavoriteProjectsModule(fragment: FavoriteProjectsFragment) :
    BaseFragmentModule<FavoriteProjectsFragment>(fragment, FavoriteProjectsFragment::class.java) {

    init {
        bind(ProjectListener::class.java).toInstance(provideProjectsListener())
        bind(TaskListener::class.java).toInstance(provideTasksListener())
        bind(FavoriteProjectsViewModel::class.java).toProvider(FavoriteProjectsViewModelProvider::class.java).providesSingletonInScope()
    }

    fun provideProjectsListener(): ProjectListener = fragment

    fun provideTasksListener(): TaskListener = fragment

}