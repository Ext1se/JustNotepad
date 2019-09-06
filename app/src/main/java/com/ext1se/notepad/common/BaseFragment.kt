package com.ext1se.notepad.common

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.ext1se.notepad.ui.ActivityObserver
import com.ext1se.notepad.ui.DataObserver

abstract class BaseFragment : Fragment(), DependenceListener {

    protected lateinit var dataObserver: DataObserver
    protected lateinit var activityObserver: ActivityObserver

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject()
        if (context is ActivityObserver) {
            activityObserver = context as ActivityObserver
        }
        if (context is DataObserver) {
            dataObserver = context as DataObserver
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
    }

    override fun onDestroy() {
        super.onDestroy()
        close()
    }
}