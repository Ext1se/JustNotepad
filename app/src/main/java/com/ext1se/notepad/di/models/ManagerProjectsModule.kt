package com.ext1se.notepad.di.models

import com.ext1se.notepad.common.ProjectListener
import com.ext1se.notepad.di.providers.ManagerProjectsViewModelProvider
import com.ext1se.notepad.ui.projects.manage.ManagerProjectsAdapter
import com.ext1se.notepad.ui.projects.manage.ManagerProjectsFragment
import com.ext1se.notepad.ui.projects.manage.ManagerProjectsViewModel
import toothpick.config.Module

class ManagerProjectsModule(fragment: ManagerProjectsFragment) :
    BaseFragmentModule<ManagerProjectsFragment>(fragment, ManagerProjectsFragment::class.java) {

    init {
        bind(ProjectListener::class.java).toInstance(provideProjectsListener())
        bind(ManagerProjectsViewModel::class.java).toProvider(ManagerProjectsViewModelProvider::class.java)
            .providesSingletonInScope()
    }

    fun provideProjectsListener(): ProjectListener = fragment
}