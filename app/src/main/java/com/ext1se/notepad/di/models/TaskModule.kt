package com.ext1se.notepad.di.models

import com.ext1se.notepad.common.SubTaskListener
import com.ext1se.notepad.di.providers.TaskViewModelProvider
import com.ext1se.notepad.ui.tasks.TaskFragment
import com.ext1se.notepad.ui.tasks.TaskViewModel
import toothpick.config.Module

class TaskModule(fragment: TaskFragment) :
    BaseFragmentModule<TaskFragment>(fragment, TaskFragment::class.java) {

    init {
        bind(SubTaskListener::class.java).toInstance(provideTaskFragment())
        bind(TaskViewModel::class.java).toProvider(TaskViewModelProvider::class.java).providesSingletonInScope()
    }

    fun provideTaskFragment(): SubTaskListener = fragment

}