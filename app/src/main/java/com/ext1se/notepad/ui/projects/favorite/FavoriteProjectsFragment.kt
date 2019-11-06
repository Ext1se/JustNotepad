package com.ext1se.notepad.ui.projects.favorite

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.ext1se.dialog.color_dialog.ColorHelper
import com.ext1se.notepad.R
import com.ext1se.notepad.common.BaseFragmentOptionsMenu
import com.ext1se.notepad.common.ProjectListener
import com.ext1se.notepad.common.TaskListener
import com.ext1se.notepad.data.ProjectRepository
import com.ext1se.notepad.data.TaskRepository
import com.ext1se.notepad.data.model.Project
import com.ext1se.notepad.data.model.Task
import com.ext1se.notepad.databinding.FavoriteProjectsBinding
import com.ext1se.notepad.di.DI
import com.ext1se.notepad.di.models.FavoriteProjectsModule
import com.ext1se.notepad.ui.ThemeState
import com.ext1se.notepad.ui.projects.ProjectFragment
import com.ext1se.notepad.ui.tasks.TaskFragment
import com.google.android.material.snackbar.Snackbar
import toothpick.Toothpick
import javax.inject.Inject

class FavoriteProjectsFragment : BaseFragmentOptionsMenu(),
    ProjectListener,
    TaskListener {

    @Inject
    lateinit var projectRepository: ProjectRepository
    @Inject
    lateinit var taskRepository: TaskRepository
    @Inject
    lateinit var favoriteProjectsViewModel: FavoriteProjectsViewModel

    private lateinit var binding: FavoriteProjectsBinding

    companion object {
        fun newInstance() = FavoriteProjectsFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FavoriteProjectsBinding.inflate(inflater, container, false)
        favoriteProjectsViewModel.projectListener = this
        favoriteProjectsViewModel.taskListener = this
        favoriteProjectsViewModel.setSelectedProject(dataObserver.getSelectedProject())
        binding.vm = favoriteProjectsViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fabAdd.setOnClickListener {
            activityObserver.updateFragment(TaskFragment.newInstance(), false, true)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val state: ThemeState
        if (!dataObserver.getProjects().isNullOrEmpty()) {
            state = ThemeState.PROJECT
        } else {
            state = ThemeState.PROJECTS_EMPTY
        }
        activityObserver.updateTheme(dataObserver.getSelectedProject(), state)
        activityObserver.updateNavigation(dataObserver.getSelectedProject(), true)
    }

    override fun selectProject(project: Project, position: Int) {
        favoriteProjectsViewModel.setSelectedProject(project)
        if (dataObserver.getSelectedProject() != project) {
            dataObserver.setSelectedProject(project)
            activityObserver.updateTheme(project)
        }
    }

    override fun selectTask(task: Task, position: Int) {
        //val copyTask = taskRepository.getCopyObject(task)
        //activityObserver.updateFragment(TaskFragment.newInstance(copyTask), false, true)
        activityObserver.updateFragment(TaskFragment.newInstance(task.id), false, true)
    }

    override fun swipeTask(
        task: Task,
        position: Int,
        direction: Int,
        callback: TaskListener.RestoreTaskListener?
    ) {
        val snackBar = Snackbar.make(binding.snackbarCoordinator, R.string.task_deleted, Snackbar.LENGTH_LONG)
        val colorItem = ColorHelper.getColor(context, dataObserver.getSelectedProject().idColorTheme)
        snackBar.setBackgroundTint(colorItem.primaryColor)
        snackBar.setTextColor(Color.WHITE)
        snackBar.setActionTextColor(Color.WHITE)
        snackBar.setAction(R.string.task_restore, {
            taskRepository.setStateRemoved(task, false)
            callback?.restoreTask(task, position)
        })
        snackBar.show()
        taskRepository.setStateRemoved(task, true)
    }

    override fun setTaskState(task: Task, position: Int) {
        taskRepository.setStateCompleted(task, !task.isCompleted)
        //binding.rvTasks.adapter?.notifyItemChanged(position)

    }

    override fun getMenuResource(): Int = R.menu.menu_edit

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_project_edit) {
            val copyProject = projectRepository.getCopyObject(dataObserver.getSelectedProject().id)
            copyProject?.let {
                activityObserver.updateFragment(ProjectFragment.newInstance(copyProject), false, true)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun inject() {
        val scope = Toothpick.openScopes(DI.APP_SCOPE, DI.FAVORITE_PROJECTS_SCOPE)
        scope.installModules(FavoriteProjectsModule(this))
        Toothpick.inject(this, scope)
    }

    override fun close() {
        Toothpick.closeScope(DI.FAVORITE_PROJECTS_SCOPE)
    }
}