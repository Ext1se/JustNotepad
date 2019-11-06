package com.ext1se.notepad.ui.projects

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.os.bundleOf
import com.ext1se.dialog.color_dialog.ColorDialog
import com.ext1se.dialog.color_dialog.ColorItem
import com.ext1se.dialog.icon_dialog.IconDialog
import com.ext1se.dialog.icon_dialog.IconItem
import com.ext1se.notepad.R
import com.ext1se.notepad.common.BaseFragmentOptionsMenu
import com.ext1se.notepad.data.model.Project
import com.ext1se.notepad.databinding.ProjectBinding
import com.ext1se.notepad.di.DI
import com.ext1se.notepad.di.models.ProjectModule
import com.ext1se.notepad.ui.ThemeState
import com.ext1se.notepad.ui.projects.favorite.FavoriteProjectsFragment
import kotlinx.android.synthetic.main.fr_task.*
import toothpick.Toothpick
import javax.inject.Inject

class ProjectFragment : BaseFragmentOptionsMenu(), IconDialog.Callback, ColorDialog.Callback {

    @Inject
    lateinit var projectViewModel: ProjectViewModel

    private lateinit var binding: ProjectBinding
    private var selectedProject: Project? = null

    companion object {
        const val KEY_PROJECT = "KEY_PROJECT"
        fun newInstance() = ProjectFragment()
        fun newInstance(project: Project) = ProjectFragment().apply {
            arguments = bundleOf(
                KEY_PROJECT to project
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (it.containsKey(KEY_PROJECT)) {
                selectedProject = it.getSerializable(KEY_PROJECT) as Project
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = ProjectBinding.inflate(inflater, container, false)
        if (projectViewModel.project.value == null) {
            if (selectedProject != null) {
                projectViewModel.setProject(selectedProject!!)
            } else {
                projectViewModel.setProject(Project())
            }
        }
        binding.vm = projectViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*(context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);*/

        binding.llIcon.setOnClickListener {
            IconDialog()
                .setSelectedIcon(projectViewModel.idIcon.value!!)
                .setColor(projectViewModel.idColorTheme.value!!)
                .show(childFragmentManager, "icon_dialog")
        }

        binding.llColor.setOnClickListener {
            ColorDialog()
                .setSelectedIcon(projectViewModel.idIcon.value!!)
                .setColor(projectViewModel.idColorTheme.value!!)
                .show(childFragmentManager, "color_dialog")
        }

        binding.fabSave.setOnClickListener {
            updateProject()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activityObserver.updateNavigation(dataObserver.getSelectedProject(), false)
        if (selectedProject != null) {
            activityObserver.updateTheme(projectViewModel.project.value, ThemeState.PROJECT_EDIT)
        } else {
            activityObserver.updateTheme(projectViewModel.project.value, ThemeState.PROJECT_CREATE)
            binding.etTitle.requestFocus()
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm!!.showSoftInput(binding.etTitle, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private fun updateProject() {
        if (binding.etTitle.text.isNullOrBlank()) {
            binding.etTitle.error = resources.getString(R.string.project_title_empty_error)
            return
        }

        val newProject = Project()
        newProject.name = et_title.text.toString()
        newProject.description = et_description.text.toString()
        newProject.idColorTheme = projectViewModel.idColorTheme.value!!
        newProject.idIcon = projectViewModel.idIcon.value!!
        if (selectedProject == null) {
            newProject.dateCreated = System.currentTimeMillis()
            dataObserver.addProject(newProject)
        } else {
            newProject.id = selectedProject!!.id
            newProject.dateCreated = selectedProject!!.dateCreated
            newProject.isFavorite = selectedProject!!.isFavorite
            dataObserver.updateProject(newProject)
            selectedProject = null
        }
        activityObserver.updateFragment(FavoriteProjectsFragment.newInstance(), true, false)
        //projectViewModel.project.value = null
    }

    override fun onItemDialogSelected(dialog: IconDialog?, icon: IconItem) {
        projectViewModel.setIcon(icon.id)
    }

    override fun onItemDialogSelected(dialog: ColorDialog?, colorItem: ColorItem) {
        projectViewModel.setColor(colorItem.id)
        if (selectedProject == null) {
            activityObserver.updateTheme(projectViewModel.project.value, ThemeState.PROJECT_CREATE)
        } else {
            activityObserver.updateTheme(projectViewModel.project.value, ThemeState.PROJECT_EDIT)
        }
    }

    override fun getMenuResource(): Int = R.menu.menu_project

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId

        if (itemId == R.id.action_ok) {
            updateProject()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun inject() {
        val scope = Toothpick.openScopes(DI.APP_SCOPE, DI.PROJECT_SCOPE)
        scope.installModules(ProjectModule(this))
        Toothpick.inject(this, scope)
    }

    override fun close() {
        Toothpick.closeScope(DI.PROJECT_SCOPE)
    }
}