package com.ext1se.notepad.ui.tasks

import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import com.ext1se.notepad.R
import com.ext1se.notepad.common.BaseFragmentOptionsMenu
import com.ext1se.notepad.data.TaskRepository
import com.ext1se.notepad.data.model.Project
import com.ext1se.notepad.data.model.Task
import com.ext1se.notepad.databinding.TaskBinding
import com.ext1se.notepad.di.DI
import com.ext1se.notepad.di.models.TaskModule
import com.ext1se.notepad.ui.projects.favorite.FavoriteProjectsFragment
import com.ext1se.notepad.ui.projects.dialog.ProjectDialog
import kotlinx.android.synthetic.main.fr_task.*
import toothpick.Toothpick
import javax.inject.Inject
import android.content.Context
import android.text.InputType
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.widget.SwitchCompat
import com.ext1se.notepad.common.SubTaskListener
import com.ext1se.notepad.data.model.SubTask


class TaskFragment : BaseFragmentOptionsMenu(), SubTaskListener, ProjectDialog.Callback {

    @Inject
    lateinit var taskRepository: TaskRepository
    @Inject
    lateinit var taskViewModel: TaskViewModel

    private lateinit var binding: TaskBinding
    private var selectedTask: Task? = null
    private val subTasks: MutableList<SubTask> = mutableListOf()
    private lateinit var switchViewCompletion: SwitchCompat

    companion object {
        const val KEY_TASK = "KEY_TASK"
        const val KEY_TASK_ID = "KEY_TASK_ID"
        fun newInstance() = TaskFragment()
        fun newInstance(task: Task) = TaskFragment().apply {
            arguments = bundleOf(
                KEY_TASK to task
            )
        }

        fun newInstance(id: String) = TaskFragment().apply {
            arguments = bundleOf(
                KEY_TASK_ID to id
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (it.containsKey(KEY_TASK)) {
                //Проблема с сериализацией RealmList
                selectedTask = it.getSerializable(KEY_TASK) as Task
            }
            if (it.containsKey(KEY_TASK_ID)) {
                selectedTask = taskRepository.getCopyObject(it.getString(KEY_TASK_ID))
            }
            selectedTask?.let {
                subTasks.addAll(it.subTasks)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        binding.etTitle.setImeOptions(EditorInfo.IME_ACTION_NEXT)
        binding.etTitle.setRawInputType(InputType.TYPE_CLASS_TEXT)
        binding.bSubtask.setOnClickListener {
            addNewSubTask()
        }
        binding.etSubtask.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                addNewSubTask()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    private fun addNewSubTask() {
        val title = binding.etSubtask.text.toString()
        if (!title.isNullOrBlank()) {
            val subTask = SubTask()
            subTask.name = title
            val currentTime = System.currentTimeMillis()
            subTask.dateCreated = currentTime
            subTask.dateUpdated = currentTime
            taskViewModel.addSubTask(subTask)
            val position: Int = taskViewModel.subTasks.value?.size ?: 0
            binding.etSubtask.setText("")
            binding.rvSubtasks.adapter?.notifyItemInserted(position)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activityObserver.updateNavigation(dataObserver.getSelectedProject(), false)
        if (taskViewModel.subTasks.value?.size ?: 0 == 0) {
            if (binding.etTitle.text.isNullOrEmpty()) {
                openKeyboard(binding.etTitle)
            } else {
                if (binding.etDescription.text.isNullOrEmpty()) {
                    openKeyboard(binding.etDescription)
                }
            }
        }
    }

    private fun openKeyboard(editText: EditText) {
        editText.requestFocus()
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm!!.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun updateTask() {
        if (binding.etTitle.text.isNullOrBlank()
            && binding.etDescription.text.isNullOrBlank()
            && taskViewModel.subTasks.value?.size ?: 0 == 0
        ) {
            activity?.onBackPressed()
            return
        }

        val subTasks = taskViewModel.subTasks.value ?: mutableListOf<SubTask>()
        var positionItem: Int = 0
        var positionView: Int = 0
        val sizeView = subTasks.size
        for (positionView in 0..sizeView - 1) {
            val viewHolder =
                binding.rvSubtasks.findViewHolderForAdapterPosition(positionView) as SubTaskViewHolder
            viewHolder.updateSubTask(subTasks[positionItem])
            if (subTasks[positionItem].name.isNullOrBlank()) {
                subTasks.removeAt(positionItem)
                positionItem--
            }
            positionItem++
        }

        val task = Task()
        task.idProject = dataObserver.getSelectedProject().id
        task.name = et_title.text.toString()
        task.description = et_description.text.toString()
        task.isCompleted = switchViewCompletion.isChecked
        if (selectedTask == null) {
            task.dateCreated = System.currentTimeMillis()
            task.subTasks.addAll(subTasks)
            taskRepository.addObject(task)
        } else {
            task.id = selectedTask!!.id
             task.dateCreated = selectedTask!!.dateCreated
            task.dateUpdated = System.currentTimeMillis()
            //task.subTasks = selectedTask!!.subTasks
            task.subTasks.addAll(subTasks)
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

    override fun swipeSubTask(subTask: SubTask, position: Int, direction: Int) {
        binding.rvSubtasks.adapter?.notifyItemRemoved(position)
        taskViewModel.removeSubTask(subTask)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val item = menu.findItem(R.id.action_completion)
        switchViewCompletion = item.actionView.findViewById<SwitchCompat>(R.id.switch_task)
        selectedTask?.let {
            switchViewCompletion.isChecked = it.isCompleted
        }
        super.onPrepareOptionsMenu(menu)
    }

    override fun getMenuResource(): Int = R.menu.menu_task

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