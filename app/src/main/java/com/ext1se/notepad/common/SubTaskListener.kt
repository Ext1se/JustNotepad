package com.ext1se.notepad.common

import com.ext1se.notepad.data.model.SubTask
import com.ext1se.notepad.data.model.Task
import com.ext1se.notepad.ui.tasks.TasksAdapter

interface SubTaskListener {
    fun swipeSubTask(subTask: SubTask, position: Int, direction: Int)
    fun setSubTaskState(subTask: SubTask, position: Int){}
}