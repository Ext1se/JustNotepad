package com.ext1se.notepad.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ext1se.notepad.ui.tasks.TaskViewModel
import org.jetbrains.annotations.NotNull

class CustomFactoryTask() : ViewModelProvider.NewInstanceFactory() {

    @NotNull
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TaskViewModel() as T
    }
}