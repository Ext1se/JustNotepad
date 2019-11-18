package com.ext1se.notepad.di.models

import com.ext1se.notepad.common.TaskListener
import com.ext1se.notepad.di.providers.RemovedTasksViewModelProvider
import com.ext1se.notepad.ui.tasks.removed.RemovedTasksFragment
import com.ext1se.notepad.ui.tasks.removed.RemovedTasksViewModel


class RemovedTasksModule(fragment: RemovedTasksFragment) :
    BaseFragmentModule<RemovedTasksFragment>(fragment, RemovedTasksFragment::class.java) {

    init {
        bind(TaskListener::class.java).toInstance(provideTasksListener())
        bind(RemovedTasksViewModel::class.java).toProvider(RemovedTasksViewModelProvider::class.java).providesSingletonInScope()
    }

    fun provideTasksListener(): TaskListener = fragment
}