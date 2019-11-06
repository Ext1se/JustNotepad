package com.ext1se.notepad.data

import com.ext1se.notepad.data.model.SubTask
import com.ext1se.notepad.data.model.Task
import io.realm.Realm
import io.realm.Sort

class TaskRepository(realm: Realm) : BaseRepository<Task>(realm, Task::class.java) {

    fun getTasksOfProject(idProject: String): List<Task> {
        return realm.where(Task::class.java)
            .equalTo("idProject", idProject)
            .equalTo("isRemoved", false)
            .findAll().sort("dateCreated", Sort.DESCENDING)
    }

    fun getRemovedTasks(): List<Task>{
        return realm.where(Task::class.java)
            .equalTo("isRemoved", true)
            .findAll().sort("dateCreated", Sort.DESCENDING)
    }

    fun setStateRemoved(task: Task, isRemoved: Boolean){
        with(realm){
            beginTransaction()
            task.isRemoved = isRemoved
            commitTransaction()
        }
    }

    fun setStateCompleted(task: Task, isCompleted: Boolean){
        with(realm){
            beginTransaction()
            task.isCompleted = isCompleted
            commitTransaction()
        }
    }

    fun addSubTask(task: Task, subTask: SubTask) {
        with(realm) {
            beginTransaction()
            task.subTasks.add(subTask)
            commitTransaction()
        }
    }
}