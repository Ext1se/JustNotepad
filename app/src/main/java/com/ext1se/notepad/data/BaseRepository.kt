package com.ext1se.notepad.data

import io.realm.Realm
import io.realm.RealmObject

abstract class BaseRepository<T : RealmObject>(protected val realm: Realm, protected val type: Class<T>) {

    open fun getObject(id: String): T? = realm.where(type).equalTo("id", id).findFirst()

    open fun getCopyObject(id: String): T? {
        val obj = getObject(id)
        return if (obj != null) realm.copyFromRealm(obj) else null
    }

    open fun getCopyObject(obj: T): T = realm.copyFromRealm(obj)

    open fun getAllObjects(): MutableList<T> = realm.where(type).findAll()

    open fun addObject(obj: T) {
        with(realm) {
            beginTransaction()
            copyToRealm(obj)
            commitTransaction()
        }
    }

    open fun removeObject(id: String) = getObject(id)?.let { removeObject(it) }

    open fun removeObject(obj: T) {
        with(realm) {
            beginTransaction()
            obj.deleteFromRealm()
            commitTransaction()
        }
    }

    open fun updateObject(obj: T){
        with(realm){
            beginTransaction()
            copyToRealmOrUpdate(obj)
            commitTransaction()
        }
    }
}