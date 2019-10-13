package com.ext1se.notepad.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ext1se.notepad.common.ProjectListener
import com.ext1se.notepad.data.ProjectRepository
import com.ext1se.notepad.data.model.Project
import com.ext1se.notepad.preferences.SharedPreferencesHelper
import com.ext1se.notepad.ui.projects.ProjectAdapter

class ProjectsViewModel(
    private val projectRepository: ProjectRepository,
    private val preferencesHelper: SharedPreferencesHelper,
    var projectListener: ProjectListener
) : ViewModel() {

    val projects: MutableLiveData<MutableList<Project>> = MutableLiveData()
    val selectedProject: MutableLiveData<Project?> = MutableLiveData()
    val isShowingList: MutableLiveData<Boolean> = MutableLiveData()

    var stateTheme: ThemeState = ThemeState.PROJECT

    init {
        projects.value = projectRepository.getProjectsSortedByDate()
        val project = projectRepository.getObject(preferencesHelper.getSelectedProjectId())
        if (project == null) {
            if (!projects.value.isNullOrEmpty()) {
                selectedProject.value = projects.value?.get(0)
                preferencesHelper.setSelectedProjectId(selectedProject.value!!.id)
                stateTheme = ThemeState.PROJECT
            } else {
                selectedProject.value = Project()
                stateTheme = ThemeState.PROJECTS_EMPTY
            }
        } else {
            selectedProject.value = project
            stateTheme = ThemeState.PROJECT
        }
        isShowingList.value = true
    }

    fun setSelectedProject(project: Project) {
        preferencesHelper.setSelectedProjectId(project.id)
        selectedProject.value = project
    }

    fun setSelectedProjectId(id: String) {
        selectedProject.value = projectRepository.getObject(preferencesHelper.getSelectedProjectId())
        preferencesHelper.setSelectedProjectId(id)
    }

    fun setThemeState(state: ThemeState) {
        stateTheme = state
    }

    fun updateNavigationShowing() {
        val isShowing = isShowingList.value
        isShowing?.let {
            isShowingList.value = !isShowing
        }
    }

    fun removeProject(project: Project) {
        if (project.id == selectedProject.value?.id) {
            val projectList = projects.value
            if (!projectList.isNullOrEmpty() && projectList.size > 1) {
                for (p in projectList) {
                    if (p != project) {
                        setSelectedProject(p)
                        break
                    }
                }
            } else {
                setSelectedProject(Project())
            }
        }
        projectRepository.removeObject(project)
        projects.value = projectRepository.getProjectsSortedByDate()
    }

    fun addProject(project: Project) {
        projectRepository.addObject(project)
        selectedProject.value = project
        projects.value = projectRepository.getProjectsSortedByDate()
    }

    fun updateProject(project: Project){
        projectRepository.updateObject(project)
        selectedProject.value = project
        projects.value = projectRepository.getProjectsSortedByDate()
    }
}