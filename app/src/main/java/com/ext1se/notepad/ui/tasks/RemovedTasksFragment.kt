package com.ext1se.notepad.ui.tasks

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import com.ext1se.notepad.App
import com.ext1se.notepad.common.BaseFragment
import com.ext1se.notepad.data.ProjectRepository
import com.ext1se.notepad.data.TaskRepository
import com.ext1se.notepad.data.model.Task
import com.ext1se.notepad.databinding.RemovedTasksBinding
import com.ext1se.notepad.ui.ThemeState
import com.ext1se.notepad.utils.CustomFactoryRemovedTasks
import toothpick.Toothpick
import javax.inject.Inject

class RemovedTasksFragment : BaseFragment(),
    RemovedTasksAdapter.OnTaskListener {

    @Inject
    lateinit var projectRepository: ProjectRepository
    @Inject
    lateinit var taskRepository: TaskRepository

    private lateinit var removedTasksViewModel: RemovedTasksViewModel
    private lateinit var binding: RemovedTasksBinding

    companion object {
        fun newInstance() = RemovedTasksFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val factory = CustomFactoryRemovedTasks(projectRepository, taskRepository, this)
        removedTasksViewModel = ViewModelProviders.of(this, factory).get(RemovedTasksViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = RemovedTasksBinding.inflate(inflater, container, false)
        removedTasksViewModel.onTaskListener = this
        binding.vm = removedTasksViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activityObserver.updateTheme(null, ThemeState.BUCKET)
        activityObserver.updateNavigation(dataObserver.getSelectedProject(), true)
    }

    override fun onSwipeTask(task: Task, position: Int, direction: Int) {
        if (direction == ItemTouchHelper.START) {
            taskRepository.removeObject(task)
            return
        }
        if (direction == ItemTouchHelper.END) {
            taskRepository.setStateRemoved(task, false)
            return
        }
    }

    override fun inject() {
        Toothpick.inject(this, App.appScope)
    }

    override fun close() {
        Toothpick.closeScope(App.appScope)
    }
}