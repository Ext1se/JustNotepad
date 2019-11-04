package com.ext1se.notepad.di.models

import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.ext1se.notepad.App
import com.ext1se.notepad.data.ProjectRepository
import com.ext1se.notepad.data.RealmMigrations
import com.ext1se.notepad.data.RealmSchemaMigration
import com.ext1se.notepad.data.TaskRepository
import com.ext1se.notepad.di.PREFERENCES_DEFAULT
import com.ext1se.notepad.di.PREFERENCES_HELPER
import com.ext1se.notepad.preferences.SharedPreferencesHelper
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmMigration
import toothpick.config.Module

class AppModule(private val app: App) : Module() {

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
        val configuration = RealmConfiguration.Builder()
            .schemaVersion(2).migration(RealmSchemaMigration()).build()
        Realm.getDefaultConfiguration()
        return Realm.getInstance(configuration)
    }

    private fun provideProjectRepository() = ProjectRepository(realm)

    private fun provideTaskRepository() = TaskRepository(realm)
}