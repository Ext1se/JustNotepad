package com.ext1se.notepad.ui.tasks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ext1se.notepad.R

class StringsAdapter(
    private var strings: List<String> = listOf<String>()
) : RecyclerView.Adapter<StringViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StringViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_string, parent, false)
        return StringViewHolder(view)
    }

    override fun getItemCount(): Int = strings.size

    override fun onBindViewHolder(holder: StringViewHolder, position: Int) = holder.bind(strings[position])
}