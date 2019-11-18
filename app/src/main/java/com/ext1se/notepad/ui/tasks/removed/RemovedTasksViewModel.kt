package com.ext1se.notepad.ui.tasks.removed

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ext1se.notepad.common.TaskListener
import com.ext1se.notepad.data.ProjectRepository
import com.ext1se.notepad.data.TaskRepository
import com.ext1se.notepad.data.model.Task

class RemovedTasksViewModel(
    private val projectRepository: ProjectRepository,
    private val taskRepository: TaskRepository,
    var taskListener: TaskListener
) : ViewModel() {

    val tasks: MutableLiveData<MutableList<Task>> = MutableLiveData()

    init {
        val removedTasks = taskRepository.getRemovedTasks().toMutableList()
        removedTasks.forEach {
            it.project = projectRepository.getCopyObject(it.idProject)
        }
        tasks.value = removedTasks
    }
}