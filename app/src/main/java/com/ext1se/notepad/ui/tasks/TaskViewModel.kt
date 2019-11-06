package com.ext1se.notepad.ui.tasks

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ext1se.notepad.common.SubTaskListener
import com.ext1se.notepad.common.TaskListener
import com.ext1se.notepad.data.TaskRepository
import com.ext1se.notepad.data.model.Project
import com.ext1se.notepad.data.model.SubTask
import com.ext1se.notepad.data.model.Task

class TaskViewModel(
    var subTaskListener: SubTaskListener
) : ViewModel() {

    val task: MutableLiveData<Task> = MutableLiveData()
    val subTasks: MutableLiveData<MutableList<SubTask>> = MutableLiveData()
    val idColorTheme: MutableLiveData<Int> = MutableLiveData()
    val selectedProject: MutableLiveData<Project> = MutableLiveData()

    init {
        task.value = Task()
        subTasks.value = mutableListOf()
    }

    fun setTask(task: Task?) {
        if (task != null) {
            this.task.value = task
            this.subTasks.value = task.subTasks.toMutableList()
        } else {
            this.task.value = Task()
        }
    }

    fun setSubTask(subTasks: MutableList<SubTask>){
        this.subTasks.value = subTasks
    }

    fun addSubTask(subTask: SubTask){
        subTasks.value?.add(subTask)
    }

    fun removeSubTask(subTask: SubTask){
        subTasks.value?.remove(subTask)
    }

    fun setProject(project: Project) {
        selectedProject.value = project
        idColorTheme.value = project.idColorTheme
    }
}