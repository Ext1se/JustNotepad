package com.ext1se.notepad.common

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater

abstract class BaseFragmentWithOptionsMenu : BaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(getMenuResource(), menu)
    }

    abstract fun getMenuResource(): Int
}