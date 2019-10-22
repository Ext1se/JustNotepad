package com.ext1se.notepad.ui.projects.manage

import android.app.AlertDialog
import android.os.Bundle
import toothpick.Toothpick
import javax.inject.Inject
import android.view.*
import android.widget.Toast
import com.ext1se.notepad.R
import com.ext1se.notepad.common.BaseFragmentOptionsMenu
import com.ext1se.notepad.common.ProjectListener
import com.ext1se.notepad.data.ProjectRepository
import com.ext1se.notepad.data.model.Project
import com.ext1se.notepad.databinding.ManagerProjectsBinding
import com.ext1se.notepad.di.DI
import com.ext1se.notepad.di.models.ManagerProjectsModule
import com.ext1se.notepad.ui.ThemeState
import com.ext1se.notepad.ui.projects.ProjectFragment
import com.ext1se.notepad.ui.projects.favorite.FavoriteProjectsFragment

class ManagerProjectsFragment : BaseFragmentOptionsMenu(), ProjectListener {

    @Inject
    lateinit var projectRepository: ProjectRepository
    @Inject
    lateinit var projectsViewModel: ManagerProjectsViewModel

    private lateinit var binding: ManagerProjectsBinding

    companion object {
        fun newInstance() = ManagerProjectsFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = ManagerProjectsBinding.inflate(inflater, container, false)
        projectsViewModel.setProjects(dataObserver.getProjects())
        projectsViewModel.projectListener = this
        binding.vm = projectsViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fabAdd.setOnClickListener {
            activityObserver.updateFragment(ProjectFragment.newInstance(), false, true)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activityObserver.updateTheme(dataObserver.getSelectedProject(), ThemeState.PROJECTS)
        activityObserver.updateNavigation(dataObserver.getSelectedProject(), true)
    }

    override fun selectProject(project: Project, position: Int) {
        dataObserver.setSelectedProject(project)
        activityObserver.updateFragment(FavoriteProjectsFragment.newInstance(), true, false)
    }

    override fun setActiveProject(project: Project, position: Int) {
        val value = !project.isFavorite
        if (value) {
            if (projectsViewModel.countActiveProject < 5) {
                projectsViewModel.increaseActiveProject()
                projectRepository.setActive(project, true)
                binding.rvProjects.adapter?.notifyItemChanged(position)
            } else {
                Toast.makeText(context, getString(R.string.projects_max_count), Toast.LENGTH_LONG).show()
            }
        } else {
            projectsViewModel.decreaseActiveProject()
            projectRepository.setActive(project, false)
            binding.rvProjects.adapter?.notifyItemChanged(position)
        }
    }

    override fun editProject(project: Project) {
        val copyProject = projectRepository.getCopyObject(project)
        activityObserver.updateFragment(ProjectFragment.newInstance(copyProject), false, true)
    }

    override fun deleteProject(project: Project, position: Int) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(getString(R.string.project_deleting))
        builder.setMessage(getString(R.string.project_deleting_warning))
        builder.setPositiveButton(android.R.string.ok, { dialog, which ->
            dataObserver.removeProject(project)
            binding.rvProjects.adapter?.notifyItemRemoved(position)
        })
        builder.setNegativeButton(android.R.string.cancel, { dialog, which ->
            binding.rvProjects.adapter?.notifyItemChanged(position)
        })
        builder.create().show()
    }

    override fun getMenuResource(): Int = R.menu.menu_add

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_add) {
            activityObserver.updateFragment(ProjectFragment.newInstance(), false, true)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun inject() {
        val scope = Toothpick.openScopes(DI.APP_SCOPE, DI.MANAGER_PROJECTS_SCOPE)
        scope.installModules(ManagerProjectsModule(this))
        Toothpick.inject(this, scope)
    }

    override fun close() {
        Toothpick.closeScope(DI.MANAGER_PROJECTS_SCOPE)
    }
}