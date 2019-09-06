package com.ext1se.notepad.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ext1se.notepad.data.ProjectRepository
import com.ext1se.notepad.data.TaskRepository
import com.ext1se.notepad.ui.tasks.RemovedTasksAdapter
import com.ext1se.notepad.ui.tasks.RemovedTasksViewModel
import org.jetbrains.annotations.NotNull

class CustomFactoryRemovedTasks(
    private val projectRepository: ProjectRepository,
    private val taskRepository: TaskRepository,
    private val listener: RemovedTasksAdapter.OnTaskListener
) : ViewModelProvider.NewInstanceFactory() {

    @NotNull
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RemovedTasksViewModel(
            projectRepository,
            taskRepository,
            listener
        ) as T
    }
}