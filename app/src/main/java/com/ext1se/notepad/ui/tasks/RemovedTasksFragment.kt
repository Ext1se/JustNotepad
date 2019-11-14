package com.ext1se.notepad.ui.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import com.ext1se.notepad.R
import com.ext1se.notepad.common.BaseFragment
import com.ext1se.notepad.common.BaseFragmentOptionsMenu
import com.ext1se.notepad.common.TaskListener
import com.ext1se.notepad.data.TaskRepository
import com.ext1se.notepad.data.model.Task
import com.ext1se.notepad.databinding.RemovedTasksBinding
import com.ext1se.notepad.di.DI
import com.ext1se.notepad.di.models.RemovedTasksModule
import com.ext1se.notepad.ui.ThemeState
import toothpick.Toothpick
import javax.inject.Inject

class RemovedTasksFragment : BaseFragmentOptionsMenu(), TaskListener {

    @Inject
    lateinit var taskRepository: TaskRepository
    @Inject
    lateinit var removedTasksViewModel: RemovedTasksViewModel

    private lateinit var binding: RemovedTasksBinding

    companion object {
        fun newInstance() = RemovedTasksFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = RemovedTasksBinding.inflate(inflater, container, false)
        removedTasksViewModel.taskListener = this
        binding.vm = removedTasksViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activityObserver.updateTheme(null, ThemeState.BUCKET)
        activityObserver.updateNavigation(dataObserver.getSelectedProject(), true)
    }

    override fun selectTask(task: Task, position: Int) {}

    override fun swipeTask(
        task: Task,
        position: Int,
        direction: Int,
        callback: TaskListener.RestoreTaskListener?
    ) {
        if (direction == ItemTouchHelper.START) {
            taskRepository.removeObject(task)
            return
        }
        if (direction == ItemTouchHelper.END) {
            //taskRepository.setStateRemoved(task, false)
            taskRepository.restoreTask(task)
            return
        }
    }

    override fun getMenuResource(): Int = R.menu.menu_bucket

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_delete_all) {
            if (taskRepository.cleanRemovedTasks()) {
                removedTasksViewModel.tasks.value?.clear()
                binding.rvRemovedTasks.adapter?.notifyDataSetChanged()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun inject() {
        val scope = Toothpick.openScopes(DI.APP_SCOPE, DI.REMOVED_TASKS_SCOPE)
        scope.installModules(RemovedTasksModule(this))
        Toothpick.inject(this, scope)
    }

    override fun close() {
        Toothpick.closeScope(DI.REMOVED_TASKS_SCOPE)
    }
}