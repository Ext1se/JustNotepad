package com.ext1se.notepad.ui.tasks

import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ext1se.notepad.R
import com.ext1se.notepad.common.SubTaskListener
import com.ext1se.notepad.data.model.SubTask

class SubTaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val title: EditText = itemView.findViewById(R.id.et_subtask)
    private val checkCompleted: CheckBox = itemView.findViewById(R.id.checkbox_completed)

    fun bind(subTask: SubTask, listener: SubTaskListener) {
        title.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus){
                subTask.name = title.text.toString()
            }
        }

        checkCompleted.setOnClickListener {
            subTask.isCompleted = checkCompleted.isChecked
        }

        title.setText(subTask.name)
        checkCompleted.isChecked = subTask.isCompleted
    }

    fun updateSubTask(subTask: SubTask){
        subTask.isCompleted = checkCompleted.isChecked
        subTask.name = title.text.toString()
        subTask.position = adapterPosition
    }
}