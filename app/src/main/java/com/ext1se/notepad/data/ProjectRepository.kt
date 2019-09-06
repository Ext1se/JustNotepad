package com.ext1se.notepad.data

import com.ext1se.notepad.data.model.Project
import com.ext1se.notepad.data.model.Task
import io.realm.Realm
import io.realm.Sort

class ProjectRepository(realm: Realm) : BaseRepository<Project>(realm, Project::class.java) {

    fun getFavoriteProjects(): List<Project> {
        return realm.where(Project::class.java).equalTo("isFavorite", true).findAll()
    }

    fun getProjectsSortedByFavorite(): MutableList<Project> {
        return realm.where(Project::class.java).findAll().sort(arrayOf("isFavorite", "dateCreated"), arrayOf(Sort.DESCENDING, Sort.DESCENDING))
    }

    fun getProjectsSortedByDate(): MutableList<Project> {
        return realm.where(Project::class.java).findAll().sort("dateCreated")
    }

    fun setActive(project: Project, value: Boolean) {
        with(realm){
            beginTransaction()
            project.isFavorite = value
            copyToRealmOrUpdate(project)
            commitTransaction()
        }
    }

    override fun removeObject(obj: Project) {
        val tasks = realm.where(Task::class.java).equalTo("idProject", obj.id).findAll()
        with(realm) {
            beginTransaction()
            tasks.deleteAllFromRealm()
            commitTransaction()
        }
        super.removeObject(obj)
    }

    fun getCountActiveProjects(): Int = realm.where(Project::class.java).equalTo("isFavorite", true).count().toInt()

    fun getCountTasks(id: Long): Int = realm.where(Task::class.java).equalTo("idProject", id).count().toInt()
}