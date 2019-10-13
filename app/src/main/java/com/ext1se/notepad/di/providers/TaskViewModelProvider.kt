package com.ext1se.notepad.di.providers

import androidx.lifecycle.ViewModelProviders
import com.ext1se.notepad.ui.tasks.TaskFragment
import com.ext1se.notepad.ui.tasks.TaskViewModel
import com.ext1se.notepad.utils.CustomFactory
import javax.inject.Inject
import javax.inject.Provider

class TaskViewModelProvider @Inject constructor(
    val fragment: TaskFragment
) : Provider<TaskViewModel> {

    override fun get(): TaskViewModel {
        val factory = CustomFactory()
        return ViewModelProviders.of(fragment, factory).get(TaskViewModel::class.java)
    }
}