package com.ext1se.notepad.ui.projects.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.*
import android.widget.FrameLayout
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ext1se.notepad.R
import com.ext1se.notepad.data.model.Project
import com.ext1se.notepad.ui.projects.ProjectAdapter

class ProjectDialog(private val projects: List<Project>) : DialogFragment(),
    ProjectAdapter.OnProjectListener {
    private lateinit var contextTheme: Context
    private lateinit var maxDialogDimensions: IntArray

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true

        val taBaseDialog = contextTheme!!.obtainStyledAttributes(R.styleable.BaseDialog)
        maxDialogDimensions = intArrayOf(
            taBaseDialog.getDimensionPixelSize(R.styleable.BaseDialog_maxWidth, -1),
            taBaseDialog.getDimensionPixelSize(R.styleable.BaseDialog_maxHeight, -1)
        )
        taBaseDialog.recycle()
    }

    override fun onCreateDialog(state: Bundle?): Dialog {
        val inflater = LayoutInflater.from(contextTheme)
        val view = inflater.inflate(R.layout.project_dialog, null)
        val background = view.findViewById<FrameLayout>(R.id.background)
        background.setOnClickListener {
            dismiss()
        }
        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_projects)
        val layoutManager = LinearLayoutManager(contextTheme, RecyclerView.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.setAdapter(ProjectAdapter(projects, this))

        val dialog = Dialog(contextTheme)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)  // Required if API <= 21
        dialog.setOnShowListener { dialogInterface ->
            val fgPadding = Rect()
            dialog.window!!.decorView.background.getPadding(fgPadding)
            dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
            dialog.window!!.decorView.setPadding(fgPadding.left, fgPadding.top, fgPadding.right, fgPadding.bottom)
            val metrics = contextTheme.getResources().getDisplayMetrics()
            var height = metrics.heightPixels - fgPadding.top - fgPadding.bottom
            var width = metrics.widthPixels - fgPadding.top - fgPadding.bottom

            if (width > maxDialogDimensions[0]) {
                width = maxDialogDimensions[0]
            }
            if (height > maxDialogDimensions[1]) {
                height = maxDialogDimensions[1]
            }
            dialog.window!!.setLayout(width, height)
            view.layoutParams = ViewGroup.LayoutParams(width, height)
            dialog.setContentView(view)
        }

        return dialog
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        val ta = context.obtainStyledAttributes(intArrayOf(R.attr.DialogStyle))
        val style = ta.getResourceId(0, R.style.IconDialogStyle)
        ta.recycle()
        contextTheme = ContextThemeWrapper(context, style)
    }


    protected fun getCaller(): Any? {
        return if (targetFragment != null) {
            targetFragment
        } else if (parentFragment != null) {
            parentFragment
        } else {
            activity
        }
    }

    override fun onClickProject(project: Project) {
        try {
            (getCaller() as Callback).onProjectSelected(this, project)
        } catch (e: ClassCastException) {
        }

        dismiss()
    }

    interface Callback {
        fun onProjectSelected(projectDialog: ProjectDialog, project: Project)
    }
}