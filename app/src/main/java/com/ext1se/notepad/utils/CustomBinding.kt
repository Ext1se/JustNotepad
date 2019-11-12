package com.ext1se.notepad.utils

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.*
import com.ext1se.dialog.color_dialog.ColorHelper
import com.ext1se.dialog.icon_dialog.IconView
import com.ext1se.notepad.R
import com.ext1se.notepad.common.ProjectListener
import com.ext1se.notepad.common.SubTaskListener
import com.ext1se.notepad.common.TaskListener
import com.ext1se.notepad.data.model.Project
import com.ext1se.notepad.data.model.SubTask
import com.ext1se.notepad.data.model.Task
import com.ext1se.notepad.ui.projects.ProjectAdapter
import com.ext1se.notepad.ui.projects.favorite.FavoriteProjectsAdapter
import com.ext1se.notepad.ui.projects.manage.ManagerProjectsAdapter
import com.ext1se.notepad.ui.tasks.RemovedTasksAdapter
import com.ext1se.notepad.ui.tasks.SubTasksAdapter
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
     * @param taskListener is callback for click item's
     */
    @JvmStatic
    @BindingAdapter("bind:tasks", "bind:selectedProject", "bind:taskListener", "bind:subTaskListener")
    fun configureTasksRecyclerView(
        recyclerView: RecyclerView,
        realmTasks: MutableList<Task>?,
        selectedProject: Project?,
        taskListener: TaskListener?,
        subTaskListener: SubTaskListener?
        ) {
        if (realmTasks == null || taskListener == null || subTaskListener == null) {
            return
        }
        val tasks = realmTasks.toMutableList()
        if (selectedProject != null){
            tasks.forEach{it.project = selectedProject}
        }
        val adapter: TasksAdapter
        if (recyclerView.adapter == null) {
            adapter = TasksAdapter(tasks, taskListener, subTaskListener)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(recyclerView.context, RecyclerView.VERTICAL, false)
            val callback = ItemSwipeColorHelper(adapter
                    //,dragFlag = ItemTouchHelper.UP or ItemTouchHelper.DOWN
            )
            val touchHelper = ItemTouchHelper(callback)
            touchHelper.attachToRecyclerView(recyclerView)
        } else {
            adapter = recyclerView.adapter as TasksAdapter
            adapter.update(tasks)
        }
    }

    /**
     * Configure RecyclerView for subtasks of selected task
     *
     * @param subTasks is list of tasks
     * @param listener is callback for click item's
     */
    @JvmStatic
    @BindingAdapter("bind:data", "bind:subTaskListener")
    fun configureSubTasksRecyclerView(
        recyclerView: RecyclerView,
        subTasks: MutableList<SubTask>?,
        listener: SubTaskListener?
    ) {
        if (subTasks == null || listener == null) {
            return
        }
        //recyclerView.addItemDecoration(DividerItemDecoration(recyclerView.context, RecyclerView.VERTICAL))
        val adapter: SubTasksAdapter
        if (recyclerView.adapter == null) {
            adapter = SubTasksAdapter(subTasks, listener)
            recyclerView.adapter = adapter
            recyclerView.layoutManager =
                LinearLayoutManager(recyclerView.context, RecyclerView.VERTICAL, false)

            val callback = ItemSwipeColorHelper(
                adapter,
                itemType = ItemType.DEFAULT_ITEM,
                //swipeFlag = ItemTouchHelper.START or ItemTouchHelper.END,
                dragFlag = ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                idColorRight = R.color.colorLight,
                idColorLeft = R.color.colorLight
            )
            val touchHelper = ItemTouchHelper(callback)
            touchHelper.attachToRecyclerView(recyclerView)
        } else {
            adapter = recyclerView.adapter as SubTasksAdapter
            adapter.update(subTasks)
        }
    }

    /**
     * Configure RecyclerView for removed tasks
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
        recyclerView.layoutManager =
            LinearLayoutManager(recyclerView.context, RecyclerView.VERTICAL, false)

        val callback =
            ItemSwipeColorHelper(adapter, swipeFlag = ItemTouchHelper.START or ItemTouchHelper.END)
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
        recyclerView.layoutManager =
            LinearLayoutManager(recyclerView.context, RecyclerView.VERTICAL, false)

        val callback = ItemSwipeColorHelper(adapter, swipeFlag = ItemTouchHelper.START)
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
        recyclerView.layoutManager =
            LinearLayoutManager(recyclerView.context, RecyclerView.VERTICAL, false)
        recyclerView.adapter = ProjectAdapter(projects, listener)
    }

    @JvmStatic
    @BindingAdapter("bind:color")
    fun configureButton(button: FloatingActionButton, idTheme: Int) {
        val color = ColorHelper.getColor(button.context, idTheme).primaryColor
        button.setBackgroundTintList(ColorStateList.valueOf(color));
    }

    @JvmStatic
    @BindingAdapter("bind:color")
    fun configureBackgroundRecyclerView(recyclerView: RecyclerView, idTheme: Int) {
        val color = ColorHelper.getColor(recyclerView.context, idTheme).primaryColor
        val c = Color.argb(255, Color.red(color), Color.green(color), Color.blue(color))
        recyclerView.setBackgroundColor(c)
        /*val c1 = Color.argb(255, Color.red(color), Color.green(color), Color.blue(color))
        val c2 = Color.argb(100, Color.red(color), Color.green(color), Color.blue(color))
        val gradientDrawable = GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, intArrayOf(c1, c2))
        recyclerView.setBackgroundDrawable(gradientDrawable)*/
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