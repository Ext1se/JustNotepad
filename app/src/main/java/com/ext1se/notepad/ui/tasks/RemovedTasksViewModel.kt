package com.ext1se.notepad.ui.tasks

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ext1se.notepad.data.ProjectRepository
import com.ext1se.notepad.data.TaskRepository
import com.ext1se.notepad.data.model.Task

class RemovedTasksViewModel(
    private val projectRepository: ProjectRepository,
    private val taskRepository: TaskRepository,
    var onTaskListener: RemovedTasksAdapter.OnTaskListener
) : ViewModel() {

    val tasks: MutableLiveData<List<Task>> = MutableLiveData()

    init {
        val removedTasks = taskRepository.getRemovedTasks().toMutableList()
        removedTasks.forEach {
            it.project = projectRepository.getCopyObject(it.idProject)
        }
        tasks.value = removedTasks
    }
}