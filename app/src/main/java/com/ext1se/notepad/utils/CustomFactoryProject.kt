package com.ext1se.notepad.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ext1se.notepad.ui.projects.ProjectViewModel
import com.ext1se.notepad.ui.tasks.TaskViewModel
import org.jetbrains.annotations.NotNull

class CustomFactoryProject() : ViewModelProvider.NewInstanceFactory() {

    @NotNull
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ProjectViewModel() as T
    }
}