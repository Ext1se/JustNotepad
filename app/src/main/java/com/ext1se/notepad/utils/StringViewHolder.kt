package com.ext1se.notepad.utils

import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ext1se.notepad.R

class StringViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val textView: TextView = itemView.findViewById(R.id.textview)

    fun bind(string: String) {
        textView.setText(string)
    }
}