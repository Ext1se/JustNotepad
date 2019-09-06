package com.ext1se.notepad.di.models

import com.ext1se.notepad.di.providers.RemovedTasksViewModelProvider
import com.ext1se.notepad.ui.tasks.RemovedTasksAdapter
import com.ext1se.notepad.ui.tasks.RemovedTasksFragment
import com.ext1se.notepad.ui.tasks.RemovedTasksViewModel
import toothpick.config.Module

class RemovedTasksModule(private val fragment: RemovedTasksFragment) : Module() {

    init {
        bind(RemovedTasksFragment::class.java).toInstance(provideRemovedTasksFragment())
        bind(RemovedTasksAdapter.OnTaskListener::class.java).toInstance(provideTasksListener())
        bind(RemovedTasksViewModel::class.java).toProvider(RemovedTasksViewModelProvider::class.java).providesSingletonInScope()
    }

    fun provideRemovedTasksFragment(): RemovedTasksFragment = fragment

    fun provideTasksListener(): RemovedTasksAdapter.OnTaskListener = fragment
}