package com.ext1se.notepad.di.providers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.ext1se.notepad.common.BaseFragment
import com.ext1se.notepad.common.ProjectListener
import com.ext1se.notepad.common.TaskListener
import com.ext1se.notepad.data.ProjectRepository
import com.ext1se.notepad.data.TaskRepository
import com.ext1se.notepad.preferences.SharedPreferencesHelper
import com.ext1se.notepad.utils.SimpleCustomFactory
import java.lang.reflect.ParameterizedType
import javax.inject.Inject
import javax.inject.Provider

/*
Пока не работает, проблема с
ViewModelProvider<T : ViewModel, F : BaseFragment>::class.java
 */

open class ViewModelProvider<V : ViewModel> @Inject constructor(
    val fragment: BaseFragment,
    val preferencesHelper: SharedPreferencesHelper,
    val projectRepository: ProjectRepository,
    val taskRepository: TaskRepository,
    val projectListener: ProjectListener,
    val taskListener: TaskListener
) : Provider<V> {

    override fun get(): V {
        //Это не работает
        val persistentClass =
            (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<V>

        val factory = SimpleCustomFactory(
            preferencesHelper,
            projectRepository,
            taskRepository,
            projectListener,
            taskListener
        )
        return ViewModelProviders.of(fragment, factory).get(persistentClass)
    }
}