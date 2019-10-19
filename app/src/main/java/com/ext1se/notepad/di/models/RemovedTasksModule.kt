package com.ext1se.notepad.di.models

import com.ext1se.notepad.common.TaskListener
import com.ext1se.notepad.di.providers.RemovedTasksViewModelProvider
import com.ext1se.notepad.di.providers.ViewModelProvider
import com.ext1se.notepad.ui.tasks.RemovedTasksAdapter
import com.ext1se.notepad.ui.tasks.RemovedTasksFragment
import com.ext1se.notepad.ui.tasks.RemovedTasksViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import toothpick.config.Module
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type


class RemovedTasksModule(fragment: RemovedTasksFragment) :
    BaseFragmentModule<RemovedTasksFragment>(fragment, RemovedTasksFragment::class.java) {

    init {
        bind(TaskListener::class.java).toInstance(provideTasksListener())
        bind(RemovedTasksViewModel::class.java).toProvider(RemovedTasksViewModelProvider::class.java).providesSingletonInScope()
    }

    fun provideTasksListener(): TaskListener = fragment
}