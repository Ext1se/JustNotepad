package com.ext1se.notepad.data.model

import com.ext1se.notepad.R
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.io.Serializable
import java.util.*

open class Project : RealmObject(), Serializable {
    @PrimaryKey
    var id: String = UUID.randomUUID().toString();
    var name: String = ""
    var description: String = ""
    var dateCreated: Long = 0
    var idColorTheme: Int = R.array.blackTheme
    var idIcon: Int = 2
    var isFavorite : Boolean = false
}