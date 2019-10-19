package com.ext1se.notepad.di.models

import com.ext1se.notepad.di.providers.ProjectViewModelProvider
import com.ext1se.notepad.ui.projects.ProjectFragment
import com.ext1se.notepad.ui.projects.ProjectViewModel
import com.ext1se.notepad.ui.projects.manage.ManagerProjectsFragment
import toothpick.config.Module

class ProjectModule(fragment: ProjectFragment) :
    BaseFragmentModule<ProjectFragment>(fragment, ProjectFragment::class.java) {

    init {
        bind(ProjectViewModel::class.java).toProvider(ProjectViewModelProvider::class.java)
            .providesSingletonInScope()
    }
}