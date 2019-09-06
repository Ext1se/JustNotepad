package com.ext1se.notepad.common

import com.ext1se.notepad.di.DI
import toothpick.Toothpick

interface DependenceListener {
    fun inject()
    fun close()

    fun inject(any: Any) {
        val scope = Toothpick.openScope(DI.APP_SCOPE)
        Toothpick.inject(any, scope)
    }
}