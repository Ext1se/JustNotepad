package com.ext1se.notepad.ui

import androidx.fragment.app.Fragment
import com.ext1se.notepad.data.model.Project

interface ActivityObserver {
    fun updateTheme(project: Project?, state: ThemeState = ThemeState.PROJECT)
    fun updateNavigation(project: Project, isShowDrawerMenu: Boolean)
    fun updateFragment(fragment: Fragment, isCheckExisting: Boolean, isBackToStack: Boolean)
}