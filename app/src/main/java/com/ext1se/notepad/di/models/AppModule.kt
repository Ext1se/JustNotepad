package com.ext1se.notepad.di.models

import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.ext1se.notepad.App
import com.ext1se.notepad.data.ProjectRepository
import com.ext1se.notepad.data.TaskRepository
import com.ext1se.notepad.preferences.SharedPreferencesHelper
import io.realm.Realm
import toothpick.config.Module

class AppModule(private val app: App) : Module() {

    companion object {
        const val PREFERENCES_HELPER = "PREFERENCES_HELPER"
        const val PREFERENCES_DEFAULT = "PREFERENCES_DEFAULT"
    }

    private val realm = provideRealm()

    init {
        bind(App::class.java).toInstance(provideApp())
        bind(SharedPreferencesHelper::class.java).withName(PREFERENCES_HELPER).toInstance(provideSharedPreferencesHelper())
        bind(SharedPreferences::class.java).withName(PREFERENCES_DEFAULT).toInstance(provideSharedPreferencesDefault())
        bind(ProjectRepository::class.java).toInstance(provideProjectRepository())
        bind(TaskRepository::class.java).toInstance(provideTaskRepository())
    }

    private fun provideApp() = app

    private fun provideSharedPreferencesHelper() = SharedPreferencesHelper(app)

    private fun provideSharedPreferencesDefault() = PreferenceManager.getDefaultSharedPreferences(app)

    private fun provideRealm(): Realm {
        Realm.init(app)
        return Realm.getDefaultInstance()
    }

    private fun provideProjectRepository() = ProjectRepository(realm)

    private fun provideTaskRepository() = TaskRepository(realm)
}