package com.ext1se.notepad.data

import com.ext1se.notepad.data.model.Project
import com.ext1se.notepad.data.model.SubTask
import com.ext1se.notepad.data.model.Task
import io.realm.Realm
import io.realm.Sort
import java.util.*

class TaskRepository(realm: Realm) : BaseRepository<Task>(realm, Task::class.java) {

    override fun addObject(obj: Task) {
        updatePositionBeforeInsertOrUpdate(obj)
        super.addObject(obj)
    }

    fun updateTask(newTask: Task, oldProjectId: String, oldPosition: Int) {
        updatePositionBeforeInsertOrUpdate(newTask)
        newTask.position = 0
        super.updateObject(newTask)

        val tasks = getTasksOfProject(oldProjectId)
        realm.beginTransaction()
        for (i in oldPosition..tasks.size - 1) {
            val task = tasks[i]
            task.position = i
        }
        realm.commitTransaction()
    }

    private fun updatePositionBeforeInsertOrUpdate(newTask: Task) {
        val tasks = getTasksOfProject(newTask.idProject)
        realm.beginTransaction()
        for (i in tasks.size - 1 downTo 0) {
            val task = tasks[i]
            task.position = i + 1
        }
        realm.commitTransaction()
    }

    fun getTasksOfProject(idProject: String): List<Task> {
        return realm.where(Task::class.java)
            .equalTo("idProject", idProject)
            .equalTo("isRemoved", false)
            //.findAll().sort("dateCreated", Sort.DESCENDING)
            .findAll().sort("position")
    }

    fun getRemovedTasks(): List<Task> {
        return realm.where(Task::class.java)
            .equalTo("isRemoved", true)
            .findAll().sort("dateCreated", Sort.DESCENDING)
    }

    private fun setStateRemoved(task: Task, isRemoved: Boolean) {
        with(realm) {
            beginTransaction()
            task.isRemoved = isRemoved
            commitTransaction()
        }
    }

    fun removeTask(removedTask: Task) {
        setStateRemoved(removedTask, true)
        val position = removedTask.position
        val tasks = getTasksOfProject(removedTask.idProject)
        realm.beginTransaction()
        for (i in position..tasks.size - 1) {
            tasks[i].position = i
        }
        realm.commitTransaction()
    }

    fun restoreTask(restoredTask: Task) {
        val tasks = getTasksOfProject(restoredTask.idProject)
        val lastPosition = tasks.size - 1
        val position =
            if (restoredTask.position > lastPosition) lastPosition else restoredTask.position
        realm.beginTransaction()
        if (restoredTask.position <= lastPosition) {
            for (i in lastPosition downTo position) {
                tasks[i].position = i + 1
            }
        }
        restoredTask.position =
            if (restoredTask.position > lastPosition) lastPosition + 1 else restoredTask.position
        realm.commitTransaction()
        setStateRemoved(restoredTask, false)
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
                val position = i + 1
                val task1 = tasks[i]
                val task2 = tasks[i + 1]
                task1.position = position
                task2.position = i
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                val position = i - 1
                val task1 = tasks[i]
                val task2 = tasks[i - 1]
                task1.position = position
                task2.position = i
            }
        }
        realm.commitTransaction()
    }
}