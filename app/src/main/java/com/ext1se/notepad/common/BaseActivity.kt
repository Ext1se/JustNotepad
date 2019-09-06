package com.ext1se.notepad.common

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ext1se.notepad.R

abstract class BaseActivity : AppCompatActivity(), DependenceListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject()
    }

    open protected fun setContentView() {
        setContentView(R.layout.ac_content)
    }

    override fun inject() {
        super.inject(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        close()
    }

    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount
        if (count == 0) {
            finish()
        } else if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}