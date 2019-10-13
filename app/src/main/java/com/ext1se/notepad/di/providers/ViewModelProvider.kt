package com.ext1se.notepad.di.providers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.ext1se.notepad.common.ProjectListener
import com.ext1se.notepad.common.TaskListener
import com.ext1se.notepad.data.ProjectRepository
import com.ext1se.notepad.data.TaskRepository
import com.ext1se.notepad.preferences.SharedPreferencesHelper
import com.ext1se.notepad.ui.projects.favorite.FavoriteProjectsFragment
import com.ext1se.notepad.utils.CustomFactory1
import java.lang.reflect.ParameterizedType
import javax.inject.Inject
import javax.inject.Provider


class ViewModelProvider<T : ViewModel> @Inject constructor(
    val fragment: FavoriteProjectsFragment,
    val preferencesHelper: SharedPreferencesHelper,
    val projectRepository: ProjectRepository,
    val taskRepository: TaskRepository,
    val projectListener: ProjectListener,
    val taskListener: TaskListener
) : Provider<T> {

    override fun get(): T {
        val persistentClass =
            (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<T>

        val factory = CustomFactory1(
            preferencesHelper,
            projectRepository,
            taskRepository,
            projectListener,
            taskListener
        )
        return ViewModelProviders.of(fragment, factory).get(persistentClass)
    }
}