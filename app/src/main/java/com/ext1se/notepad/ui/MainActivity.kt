package com.ext1se.notepad.ui

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProviders
import com.ext1se.dialog.color_dialog.ColorHelper
import com.ext1se.notepad.App
import com.ext1se.notepad.R
import com.ext1se.notepad.common.BaseActivity
import com.ext1se.notepad.data.ProjectRepository
import com.ext1se.notepad.data.model.Project
import com.ext1se.notepad.databinding.DrawerHeaderBinding
import com.ext1se.notepad.databinding.ProjectsBinding
import com.ext1se.notepad.di.DI
import com.ext1se.notepad.di.models.AppModule
import com.ext1se.notepad.di.models.FavoriteProjectsModule
import com.ext1se.notepad.di.models.ProjectsModule
import com.ext1se.notepad.preferences.SharedPreferencesHelper
import com.ext1se.notepad.ui.projects.favorite.FavoriteProjectsFragment
import com.ext1se.notepad.ui.projects.ProjectAdapter
import com.ext1se.notepad.ui.projects.ProjectFragment
import com.ext1se.notepad.ui.projects.manage.ManagerProjectsFragment
import com.ext1se.notepad.ui.tasks.RemovedTasksFragment
import com.ext1se.notepad.utils.CustomFactoryProjects
import com.ext1se.notepad.utils.DatabaseUtils
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.nav_header.view.*
import toothpick.Toothpick
import javax.inject.Inject
import javax.inject.Named

class MainActivity : BaseActivity(),
    DataObserver,
    ActivityObserver,
    NavigationView.OnNavigationItemSelectedListener,
    ProjectAdapter.OnProjectListener {

    @Inject
    lateinit var projectRepository: ProjectRepository
    @field:[Inject Named(AppModule.PREFERENCES_HELPER)]
    lateinit var preferencesHelper: SharedPreferencesHelper
    @Inject
    lateinit var projectsViewModel: ProjectsViewModel

    private lateinit var binding: ProjectsBinding
    private lateinit var headerBinding: DrawerHeaderBinding

    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (preferencesHelper.isFirstRun()) {
            DatabaseUtils.init(projectRepository, resources)
            preferencesHelper.setFirstRun(false)
        }
        setContentView()
        if (savedInstanceState == null) {
            if (!getProjects().isNullOrEmpty()) {
                updateFragment(FavoriteProjectsFragment.newInstance(), false, false)
            } else {
                updateFragment(ManagerProjectsFragment.newInstance(), false, false)
            }
        }
        updateTheme(projectsViewModel.selectedProject.value)
    }

    override fun setContentView() {
        val factory = CustomFactoryProjects(projectRepository, preferencesHelper, this)
        projectsViewModel = ViewModelProviders.of(this, factory).get(ProjectsViewModel::class.java)
        projectsViewModel.onProjectListener = this
        binding = ProjectsBinding.inflate(layoutInflater)
        binding.vm = projectsViewModel
        binding.lifecycleOwner = this
        setContentView(binding.root)

        val view = binding.navView.getHeaderView(0)
        headerBinding = DrawerHeaderBinding.bind(view)
        headerBinding.vm = projectsViewModel
        headerBinding.lifecycleOwner = this

        headerBinding.toolbar.setOnClickListener {
            projectsViewModel.updateNavigationShowing()
        }

        val toolbar = binding.content.appbar.toolbar
        setSupportActionBar(toolbar)

        toggle = ActionBarDrawerToggle(
            this, binding.drawerLayout, binding.content.appbar.toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        binding.navView.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        binding.drawerLayout.setDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerStateChanged(newState: Int) {
            }

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
            }

            override fun onDrawerOpened(drawerView: View) {
            }

            override fun onDrawerClosed(drawerView: View) {
                binding.drawerLayout.setDrawerListener(null)
                when (item.itemId) {
                    R.id.nav_tasks -> {
                        if (getProjects().isNullOrEmpty()) {
                            updateFragment(ManagerProjectsFragment.newInstance(), true, false)
                        } else {
                            updateFragment(FavoriteProjectsFragment.newInstance(), true, false)
                        }
                    }
                    R.id.nav_projects -> updateFragment(ManagerProjectsFragment.newInstance(), true, false)
                    R.id.nav_bucket -> updateFragment(RemovedTasksFragment.newInstance(), false, false)
                    R.id.nav_create_project -> updateFragment(ProjectFragment.newInstance(), false, true)
                }
            }
        })

        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }

        return super.onOptionsItemSelected(item)
    }

    override fun getProjects(): List<Project> = projectsViewModel.projects.value ?: mutableListOf()

    override fun setSelectedProject(project: Project) {
        projectsViewModel.setSelectedProject(project)
    }

    override fun getSelectedProject(): Project = projectsViewModel.selectedProject.value ?: Project()

    override fun addProject(project: Project) {
        projectsViewModel.addProject(project)
    }

    override fun removeProject(project: Project) {
        projectsViewModel.removeProject(project)
    }

    override fun updateProject(project: Project) {
        projectsViewModel.updateProject(project)
    }

    override fun updateTheme(project: Project?, state: ThemeState) {
        projectsViewModel.setThemeState(state)
        supportActionBar?.let {
            var title: String = resources.getString(R.string.app_name)
            var subtitle: String? = null
            var colorPrimary = resources.getColor(R.color.colorDefaultPrimary)
            var colorPrimaryDark = resources.getColor(R.color.colorDefaultPrimaryDark)
            when (state) {
                ThemeState.PROJECT, ThemeState.PROJECT_CREATE, ThemeState.PROJECT_EDIT -> {
                    if (project != null) {
                        title = project.name
                        subtitle = project.description
                        val colorItem = ColorHelper.getColor(this, project.idColorTheme)
                        colorPrimary = colorItem.primaryColor
                        colorPrimaryDark = colorItem.primaryDarkColor
                    }
                    if (state == ThemeState.PROJECT_CREATE) {
                        title = getString(R.string.project_creating)
                        subtitle = null
                    }

                }
                ThemeState.PROJECTS -> {
                    title = getString(R.string.projects)
                }
                ThemeState.PROJECTS_EMPTY -> {
                    title = getString(R.string.projects_empty)
                }
                ThemeState.BUCKET -> {
                    title = getString(R.string.bucket)
                }
                ThemeState.SETTINGS -> {
                    title = getString(R.string.settings)
                }
            }
            it.title = title
            it.subtitle = subtitle
            it.setBackgroundDrawable(ColorDrawable(colorPrimary))
            setStatusbarColor(colorPrimaryDark)
            headerBinding.toolbar.setBackgroundColor(colorPrimary)
        }
    }

    private fun setStatusbarColor(color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window?.let {
                it.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                it.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                it.statusBarColor = color
            }
        }
    }

    override fun updateNavigation(project: Project, isShowDrawerMenu: Boolean) {
        if (isShowDrawerMenu) {
            binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
            toggle.isDrawerIndicatorEnabled = true
            toggle.toolbarNavigationClickListener = null
        } else {
            binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            toggle.isDrawerIndicatorEnabled = false
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            toggle.setToolbarNavigationClickListener {
                onBackPressed()
            }
        }
    }

    override fun updateFragment(fragment: Fragment, isCheckExisting: Boolean, isBackToStack: Boolean) {
        val view = currentFocus
        view?.let { v ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.let { it.hideSoftInputFromWindow(v.windowToken, 0) }
        }
        val currentFragment = supportFragmentManager.findFragmentById(R.id.container)
        if (currentFragment != null) {
            if (currentFragment == fragment) {
                return
            }
        }
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        var replaceFragment: Fragment?
        if (isCheckExisting) {
            replaceFragment = supportFragmentManager.findFragmentByTag(fragment.javaClass.simpleName)
            if (replaceFragment == null) {
                replaceFragment = fragment
            }
        } else {
            replaceFragment = fragment
        }
        transaction.replace(R.id.container, replaceFragment, fragment.javaClass.simpleName)
        if (isBackToStack) {
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }

    override fun onClickProject(project: Project) {
        binding.drawerLayout.setDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerStateChanged(newState: Int) {
            }

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
            }

            override fun onDrawerOpened(drawerView: View) {
            }

            override fun onDrawerClosed(drawerView: View) {
                binding.drawerLayout.setDrawerListener(null)
                loadProject(project)
            }
        })
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }

    }

    private fun loadProject(project: Project) {
        updateTheme(project)
        setSelectedProject(project)
        var fragment: Fragment?
        fragment = supportFragmentManager.findFragmentById(R.id.container)
        if (fragment != null) {
            if (fragment is FavoriteProjectsFragment) {
                fragment as FavoriteProjectsFragment
                fragment.onClickProject(project, -1)
                return
            }
        }
        fragment = supportFragmentManager.findFragmentByTag(FavoriteProjectsFragment::class.java.simpleName)
        if (fragment == null) {
            fragment = FavoriteProjectsFragment.newInstance()
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, fragment, fragment.javaClass.simpleName)
                .commit()
        } else {
            supportFragmentManager.popBackStack()
        }
    }

    override fun inject() {
        val scope = Toothpick.openScopes(DI.APP_SCOPE, DI.PROJECT_SCOPE)
        scope.installModules(ProjectsModule(this))
        Toothpick.inject(this, scope)
    }

    override fun close() {
        Toothpick.closeScope(DI.PROJECTS_SCOPE)
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

/*
    override fun getResources(): Resources {
        return super.getResources().apply {
            configuration.fontScale = 1.0f
            updateConfiguration(configuration, displayMetrics)
        }
    }
*/
}