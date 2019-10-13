package com.ext1se.notepad.di.providers

import androidx.lifecycle.ViewModelProviders
import com.ext1se.notepad.common.TaskListener
import com.ext1se.notepad.data.ProjectRepository
import com.ext1se.notepad.data.TaskRepository
import com.ext1se.notepad.ui.tasks.RemovedTasksFragment
import com.ext1se.notepad.ui.tasks.RemovedTasksViewModel
import com.ext1se.notepad.utils.CustomFactory
import javax.inject.Inject
import javax.inject.Provider

class RemovedTasksViewModelProvider @Inject constructor(
    val fragment: RemovedTasksFragment,
    val projectRepository: ProjectRepository,
    val taskRepository: TaskRepository,
    val taskListener: TaskListener
) : Provider<RemovedTasksViewModel> {

    override fun get(): RemovedTasksViewModel {
        val factory = CustomFactory(projectRepository, taskRepository, taskListener)
        return ViewModelProviders.of(fragment, factory).get(RemovedTasksViewModel::class.java)
    }
}