package com.ext1se.notepad.utils

import android.content.res.Resources
import com.ext1se.notepad.R
import com.ext1se.notepad.data.ProjectRepository
import com.ext1se.notepad.data.model.Project

object DatabaseUtils {

    val ID_DEFAULT_SELECTED_PROJECT = "1"

    @JvmStatic
    fun init(projectRepository: ProjectRepository, resources: Resources) {
        projectRepository.addObject(Project().apply {
            id = ID_DEFAULT_SELECTED_PROJECT
            name = resources.getString(R.string.project_work)
            description = ""
            dateCreated = System.currentTimeMillis()
            idColorTheme = R.array.blueTheme
            idIcon = 2
            isFavorite = true
        })
        projectRepository.addObject(Project().apply {
            id = "2"
            name = resources.getString(R.string.project_education)
            description = ""
            dateCreated = System.currentTimeMillis()
            idColorTheme = R.array.greenTheme
            idIcon = 100
            isFavorite = true
        })
        projectRepository.addObject(Project().apply {
            id = "3"
            name = resources.getString(R.string.project_home)
            description = ""
            dateCreated = System.currentTimeMillis()
            idColorTheme = R.array.orangeTheme
            idIcon = 200
            isFavorite = true
        })
/*        projectRepository.addObject(Project().apply {
            id = "4"
            name = resources.getString(R.string.project_routine)
            //description = "Повседневные задачи"
            dateCreated = System.currentTimeMillis()
            idColorTheme = R.array.blackTheme
            idIcon = 102
            isFavorite = true
        })*/
        projectRepository.addObject(Project().apply {
            id = "5"
            name = resources.getString(R.string.project_ideas)
            description = ""
            dateCreated = System.currentTimeMillis()
            idColorTheme = R.array.redTheme
            idIcon = 101
            isFavorite = true
        })
    }
}