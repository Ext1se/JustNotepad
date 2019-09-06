package com.ext1se.notepad.ui.tasks

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProviders
import com.ext1se.notepad.App
import com.ext1se.notepad.R
import com.ext1se.notepad.common.BaseFragmentWithOptionsMenu
import com.ext1se.notepad.data.TaskRepository
import com.ext1se.notepad.data.model.Project
import com.ext1se.notepad.data.model.Task
import com.ext1se.notepad.databinding.TaskBinding
import com.ext1se.notepad.di.DI
import com.ext1se.notepad.di.models.FavoriteProjectsModule
import com.ext1se.notepad.di.models.TaskModule
import com.ext1se.notepad.ui.projects.favorite.FavoriteProjectsFragment
import com.ext1se.notepad.ui.projects.dialog.ProjectDialog
import com.ext1se.notepad.utils.CustomFactoryTask
import kotlinx.android.synthetic.main.fr_task.*
import toothpick.Toothpick
import javax.inject.Inject

class TaskFragment : BaseFragmentWithOptionsMenu(), ProjectDialog.Callback {

    @Inject
    lateinit var taskRepository: TaskRepository
    @Inject
    lateinit var taskViewModel: TaskViewModel

    private lateinit var binding: TaskBinding
    private var selectedTask: Task? = null

    companion object {
        const val KEY_TASK = "KEY_PROJECT"
        fun newInstance() = TaskFragment()
        fun newInstance(task: Task) = TaskFragment().apply {
            arguments = bundleOf(
                KEY_TASK to task
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (it.containsKey(KEY_TASK)) {
                selectedTask = it.getSerializable(KEY_TASK) as Task
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = TaskBinding.inflate(inflater, container, false)
        taskViewModel.setProject(dataObserver.getSelectedProject())
        taskViewModel.setTask(selectedTask)
        binding.vm = taskViewModel
        binding.executePendingBindings()
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.cardProject.setOnClickListener {
            ProjectDialog(dataObserver.getProjects())
                .show(childFragmentManager, "project_dialog")
        }
        binding.fabSave.setOnClickListener {
            updateTask()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activityObserver.updateNavigation(dataObserver.getSelectedProject(), false)
    }

    private fun updateTask() {
        if (binding.etTitle.text.isNullOrBlank() && binding.etDescription.text.isNullOrBlank()) {
            activity?.onBackPressed()
            return
        }
        val task = Task()
        task.idProject = dataObserver.getSelectedProject().id
        task.name = et_title.text.toString()
        task.description = et_description.text.toString()
        if (selectedTask == null) {
            task.dateCreated = System.currentTimeMillis()
            taskRepository.addObject(task)
        } else {
            task.id = selectedTask!!.id
            task.dateCreated = selectedTask!!.dateCreated
            task.dateUpdated = System.currentTimeMillis()
            taskRepository.updateObject(task)
            selectedTask = null
        }
        //activity?.onBackPressed()
        activityObserver.updateFragment(FavoriteProjectsFragment.newInstance(), true, false)
    }

    override fun onProjectSelected(projectDialog: ProjectDialog, project: Project) {
        if (dataObserver.getSelectedProject() != project) {
            dataObserver.setSelectedProject(project)
            taskViewModel.setProject(project)
            activityObserver.updateTheme(project)
        }
    }

    override fun getMenuResource(): Int = R.menu.menu_save

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_ok) {
            updateTask()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun inject() {
        val scope = Toothpick.openScopes(DI.APP_SCOPE, DI.TASK_SCOPE)
        scope.installModules(TaskModule(this))
        Toothpick.inject(this, scope)
    }

    override fun close() {
        Toothpick.closeScope(DI.TASK_SCOPE)
    }
}