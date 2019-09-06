package com.ext1se.notepad.preferences

import android.content.Context
import android.content.SharedPreferences
import com.ext1se.notepad.data.model.Project
import com.ext1se.notepad.utils.DatabaseUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SharedPreferencesHelper(context: Context) {

    val SHARED_PREFERENCES = "SHARED_PREFERENCES"
    val KEY_FIRST_RUN = "KEY_FIRST_RUN"
    val KEY_PROJECT = "KEY_PROJECT"
    val KEY_PROJECT_ID = "KEY_PROJECT_ID"

    val TYPE_PROJECT = object : TypeToken<Project>() {}.type

    private val sharedPreferences: SharedPreferences
    private val gson: Gson = Gson()

    init {
        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)
    }

    fun isFirstRun(): Boolean = sharedPreferences.getBoolean(KEY_FIRST_RUN, true)

    fun setFirstRun(value: Boolean) = sharedPreferences.edit().putBoolean(KEY_FIRST_RUN, value).apply()

    fun setSelectedProject(project: Project) {
        sharedPreferences.edit().putString(KEY_PROJECT, gson.toJson(project, TYPE_PROJECT)).apply()
    }

    fun getSelectedProject(): Project = gson.fromJson(sharedPreferences.getString(KEY_PROJECT, " "), TYPE_PROJECT)

    fun setSelectedProjectId(id: String){
        sharedPreferences.edit().putString(KEY_PROJECT_ID, id).apply()
    }

    fun getSelectedProjectId() = sharedPreferences.getString(KEY_PROJECT_ID, DatabaseUtils.ID_DEFAULT_SELECTED_PROJECT)
}