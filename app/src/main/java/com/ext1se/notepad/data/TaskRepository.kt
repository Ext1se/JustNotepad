package com.ext1se.notepad.data

import com.ext1se.notepad.data.model.SubTask
import com.ext1se.notepad.data.model.Task
import io.realm.Realm
import io.realm.Sort
import java.util.*

class TaskRepository(realm: Realm) : BaseRepository<Task>(realm, Task::class.java) {

    fun getTasksOfProject(idProject: String): List<Task> {
        return realm.where(Task::class.java)
            .equalTo("idProject", idProject)
            .equalTo("isRemoved", false)
            .findAll().sort("dateCreated", Sort.DESCENDING)
            //.findAll().sort("position")
    }

    fun getRemovedTasks(): List<Task> {
        return realm.where(Task::class.java)
            .equalTo("isRemoved", true)
            .findAll().sort("dateCreated", Sort.DESCENDING)
    }

    fun setStateRemoved(task: Task, isRemoved: Boolean) {
        with(realm) {
            beginTransaction()
            task.isRemoved = isRemoved
            commitTransaction()
        }
    }

    fun setStateCompleted(task: Task, isCompleted: Boolean) {
        with(realm) {
            beginTransaction()
            task.isCompleted = isCompleted
            commitTransaction()
        }
    }

    fun setStateCompleted(subTask: SubTask, isCompleted: Boolean) {
        with(realm) {
            beginTransaction()
            subTask.isCompleted = isCompleted
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

    //Неправильный метод, потому что не происходит перемещение двух элементов, а потом ошибочко изменяется позиция у других элементов
    fun updatePositions(tasks: List<Task>, fromPosition: Int, toPosition: Int) {
        realm.beginTransaction()
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                tasks[i].position = i + 1
                tasks[i + 1].position = i
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                tasks[i].position = i - 1
                tasks[i - 1].position = i
            }
        }
        realm.commitTransaction()
    }
}