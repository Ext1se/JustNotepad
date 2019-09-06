package com.ext1se.notepad.di.models

import com.ext1se.notepad.di.providers.ManagerProjectsViewModelProvider
import com.ext1se.notepad.ui.projects.manage.ManagerProjectsAdapter
import com.ext1se.notepad.ui.projects.manage.ManagerProjectsFragment
import com.ext1se.notepad.ui.projects.manage.ManagerProjectsViewModel
import toothpick.config.Module

class ManagerProjectsModule(private val fragment: ManagerProjectsFragment) : Module() {

    init {
        bind(ManagerProjectsFragment::class.java).toInstance(provideManagerProjectsFragment())
        bind(ManagerProjectsAdapter.OnProjectListener::class.java).toInstance(provideProjectsListener())
        bind(ManagerProjectsViewModel::class.java).toProvider(ManagerProjectsViewModelProvider::class.java).providesSingletonInScope()
    }

    fun provideManagerProjectsFragment(): ManagerProjectsFragment = fragment

    fun provideProjectsListener(): ManagerProjectsAdapter.OnProjectListener = fragment
}