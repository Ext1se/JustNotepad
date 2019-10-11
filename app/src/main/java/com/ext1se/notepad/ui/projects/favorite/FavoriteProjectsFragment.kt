package com.ext1se.notepad.ui.projects.favorite

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import toothpick.Toothpick
import javax.inject.Inject
import android.view.*
import androidx.lifecycle.ViewModelProviders
import com.ext1se.dialog.color_dialog.ColorHelper
import com.ext1se.notepad.R
import com.ext1se.notepad.data.ProjectRepository
import com.ext1se.notepad.data.TaskRepository
import com.ext1se.notepad.data.model.Project
import com.ext1se.notepad.data.model.Task
import com.ext1se.notepad.databinding.FavoriteProjectsBinding
import com.ext1se.notepad.ui.tasks.TaskFragment
import com.ext1se.notepad.ui.tasks.TasksAdapter
import com.ext1se.notepad.utils.CustomFactoryFavoriteProjects
import com.google.android.material.snackbar.Snackbar
import com.ext1se.notepad.common.BaseFragmentOptionsMenu
import com.ext1se.notepad.di.DI
import com.ext1se.notepad.di.models.FavoriteProjectsModule
import com.ext1se.notepad.ui.ThemeState
import com.ext1se.notepad.ui.projects.ProjectFragment

class FavoriteProjectsFragment : BaseFragmentOptionsMenu(),
    FavoriteProjectsAdapter.OnProjectListener,
    TasksAdapter.OnTaskListener {

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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val factory = CustomFactoryFavoriteProjects(projectRepository, taskRepository, this, this)
        favoriteProjectsViewModel = ViewModelProviders.of(this, factory).get(FavoriteProjectsViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FavoriteProjectsBinding.inflate(inflater, container, false)
        favoriteProjectsViewModel.onProjectListener = this
        favoriteProjectsViewModel.onTaskListener = this
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

    override fun onClickProject(project: Project, position: Int) {
        favoriteProjectsViewModel.setSelectedProject(project)
        if (dataObserver.getSelectedProject() != project) {
            //favoriteProjectsViewModel.setSelectedProject(project)
            dataObserver.setSelectedProject(project)
            activityObserver.updateTheme(project)
        }
    }

    override fun onClickTask(task: Task, position: Int) {
        val copyTask = taskRepository.getCopyObject(task)
        activityObserver.updateFragment(TaskFragment.newInstance(copyTask), false, true)
    }

    override fun onSwipeTask(task: Task, position: Int, onRestoreTaskListener: TasksAdapter.OnRestoreTaskListener) {
        val snackBar = Snackbar.make(binding.snackbarCoordinator, R.string.task_deleted, Snackbar.LENGTH_LONG)
        val colorItem = ColorHelper.getColor(context, dataObserver.getSelectedProject().idColorTheme)
        snackBar.setBackgroundTint(colorItem.primaryColor)
        snackBar.setTextColor(Color.WHITE)
        snackBar.setActionTextColor(Color.WHITE)
        snackBar.setAction(R.string.task_restore, {
            taskRepository.setStateRemoved(task, false)
            onRestoreTaskListener.onRestoreItem(task, position)
        })
        snackBar.show()
        taskRepository.setStateRemoved(task, true)
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