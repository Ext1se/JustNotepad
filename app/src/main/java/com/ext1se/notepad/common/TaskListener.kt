package com.ext1se.notepad.common

import com.ext1se.notepad.data.model.Task
import com.ext1se.notepad.ui.tasks.TasksAdapter

interface TaskListener {
    fun selectTask(task: Task, position: Int)
    fun swipeTask(task: Task, position: Int, direction: Int, callback: RestoreTaskListener?)
    fun setTaskState(task: Task, position: Int){}

    @FunctionalInterface
    interface RestoreTaskListener {
        fun restoreTask(task: Task, position: Int)
    }
}