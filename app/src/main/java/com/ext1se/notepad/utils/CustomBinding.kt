package com.ext1se.notepad.utils

import android.content.res.ColorStateList
import android.view.View
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ext1se.dialog.color_dialog.ColorHelper
import com.ext1se.dialog.icon_dialog.IconView
import com.ext1se.notepad.common.ProjectListener
import com.ext1se.notepad.common.TaskListener
import com.ext1se.notepad.data.model.Project
import com.ext1se.notepad.data.model.Task
import com.ext1se.notepad.ui.projects.ProjectAdapter
import com.ext1se.notepad.ui.projects.favorite.FavoriteProjectsAdapter
import com.ext1se.notepad.ui.projects.manage.ManagerProjectsAdapter
import com.ext1se.notepad.ui.tasks.RemovedTasksAdapter
import com.ext1se.notepad.ui.tasks.TasksAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

object CustomBinding {
    /**
     * Configure RecyclerView for favorite projects
     *
     * @param projects is list of favorite projects
     * @param selectedProject is used for setting active project and updating necessary items
     * @param listener is callback for click item's
     */
    @JvmStatic
    @BindingAdapter("bind:data", "bind:selectedItem", "bind:projectListener")
    fun configureFavoriteProjectsRecyclerView(
        recyclerView: RecyclerView,
        projects: List<Project>?,
        selectedProject: Project?,
        listener: ProjectListener?
    ) {
        if (projects == null || listener == null || selectedProject == null) {
            return
        }
        if (projects.size < 2) {
            recyclerView.visibility = View.GONE
            return
        }
        if (recyclerView.adapter != null) {
            val adapter = recyclerView.adapter as FavoriteProjectsAdapter
            adapter.setSelectedPosition(selectedProject)
        } else {
            val adapter = FavoriteProjectsAdapter(projects, listener)
            adapter.setSelectedPosition(selectedProject)
            var size = projects.size
            size = if (size > 5) 5 else size
            val layoutManager = GridLayoutManager(recyclerView.context, size)
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = adapter
        }
    }

    /**
     * Configure RecyclerView for tasks of selected project
     *
     * @param tasks is list of tasks
     * @param listener is callback for click item's
     */
    @JvmStatic
    @BindingAdapter("bind:data", "bind:taskListener")
    fun configureTasksRecyclerView(
        recyclerView: RecyclerView,
        tasks: MutableList<Task>?, //RealmResult
        listener: TaskListener?
    ) {
        if (tasks == null || listener == null) {
            return
        }
        val adapter: TasksAdapter
        if (recyclerView.adapter == null) {
            adapter = TasksAdapter(tasks, listener)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(recyclerView.context, RecyclerView.VERTICAL, false)

            val callback = ItemSwipeHelper(adapter)
            val touchHelper = ItemTouchHelper(callback)
            touchHelper.attachToRecyclerView(recyclerView)
        } else {
            adapter = recyclerView.adapter as TasksAdapter
            adapter.update(tasks)
        }
    }

    /**
     * Configure RecyclerView for tasks of selected project
     *
     * @param tasks is list of tasks
     * @param listener is callback for click item's
     */
    @JvmStatic
    @BindingAdapter("bind:data", "bind:removedTaskListener")
    fun configureRemovedTasksRecyclerView(
        recyclerView: RecyclerView,
        tasks: MutableList<Task>?,
        listener: TaskListener?
    ) {
        if (tasks == null || listener == null) {
            return
        }
        val adapter: RemovedTasksAdapter = RemovedTasksAdapter(tasks, listener)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(recyclerView.context, RecyclerView.VERTICAL, false)

        val callback = ItemSwipeHelper(adapter, swipeFlag = ItemTouchHelper.START or ItemTouchHelper.END)
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(recyclerView)
    }

    /**
     * Configure RecyclerView for management projects
     *
     * @param projects is list of projects
     * @param listener is callback for click item's
     */
    @JvmStatic
    @BindingAdapter("bind:data", "bind:manageProjectListener")
    fun configureManagerProjectsRecyclerView(
        recyclerView: RecyclerView,
        projects: List<Project>?,
        listener: ProjectListener?
    ) {
        if (projects == null || listener == null) {
            return
        }
        val adapter: ManagerProjectsAdapter = ManagerProjectsAdapter(projects, listener)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(recyclerView.context, RecyclerView.VERTICAL, false)

        val callback = ItemSwipeHelper(adapter, swipeFlag = ItemTouchHelper.START)
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(recyclerView)
    }

    /**
     * Configure RecyclerView in navigation menu_edit for projects
     *
     * @param projects is list of all projects
     * @param listener is callback for click item's
     */
    @JvmStatic
    @BindingAdapter("bind:data", "bind:menuProjectListener")
    fun configureMenuProjectsRecyclerView(
        recyclerView: RecyclerView,
        projects: List<Project>?,
        listener: ProjectListener?
    ) {
        if (projects == null || listener == null) {
            return
        }
        recyclerView.layoutManager = LinearLayoutManager(recyclerView.context, RecyclerView.VERTICAL, false)
        recyclerView.adapter = ProjectAdapter(projects, listener)
    }

    @JvmStatic
    @BindingAdapter("bind:color")
    fun configureButton(button: FloatingActionButton, idTheme: Int) {
        val color = ColorHelper.getColor(button.context, idTheme).primaryColor
        button.setBackgroundTintList(ColorStateList.valueOf(color));
    }

    @JvmStatic
    @BindingAdapter("bind:idIcon")
    fun configureIconView(iconView: IconView, idIcon: Int) {
        iconView.setIcon(idIcon)
    }

    @JvmStatic
    @BindingAdapter("bind:isShowing")
    fun configureShowingToolbarList(
        recyclerView: DropDownRecyclerView,
        isShowing: Boolean
    ) {
        if (recyclerView.isAnimating) {
            recyclerView.clearAnimation()
        }
        if (isShowing) {
            recyclerView.expand()
        } else {
            recyclerView.collapse()
        }
    }
}