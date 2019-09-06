package com.ext1se.notepad.data.model

import io.realm.RealmObject
import io.realm.annotations.Ignore
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmField
import java.io.Serializable
import java.util.*

open class Task : RealmObject(), Serializable {
    @PrimaryKey
    var id: String = UUID.randomUUID().toString();
    var name: String = ""
    var description: String = ""
    @RealmField(name = "id_project")
    var idProject: String = "1"
    var dateCreated: Long = 0
    var dateUpdated: Long = 0
    var isCompleted: Boolean = false
    var isRemoved: Boolean = false

    @Ignore
    var project: Project? = null
}