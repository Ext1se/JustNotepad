package com.ext1se.notepad.di.models

import com.ext1se.notepad.common.BaseFragment
import toothpick.config.Module

abstract class BaseFragmentModule<T : BaseFragment>(
    protected val fragment: T,
    protected val type: Class<T>
) : Module() {

    init {
        bind(type).toInstance(provideFragment())
    }

    fun provideFragment(): T = fragment
}