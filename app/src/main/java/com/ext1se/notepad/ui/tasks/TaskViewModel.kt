package com.ext1se.notepad.ui.tasks

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ext1se.notepad.data.TaskRepository
import com.ext1se.notepad.data.model.Project
import com.ext1se.notepad.data.model.Task

class TaskViewModel : ViewModel() {

    val task: MutableLiveData<Task> = MutableLiveData()
    val idColorTheme: MutableLiveData<Int> = MutableLiveData()
    val selectedProject: MutableLiveData<Project> = MutableLiveData()

    init {
        task.value = Task()
    }

    fun setTask(task: Task?) {
        if (task != null) {
            this.task.value = task
        } else {
            this.task.value = Task()
        }
    }

    fun setProject(project: Project) {
        selectedProject.value = project
        idColorTheme.value = project.idColorTheme
    }
}