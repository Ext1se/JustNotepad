package com.ext1se.notepad.data

import com.ext1se.notepad.data.model.SubTask
import io.realm.DynamicRealm
import io.realm.FieldAttribute
import io.realm.RealmMigration
import io.realm.RealmSchema

open class RealmMigrations : RealmMigration {
    override fun migrate(realm: DynamicRealm, oldVersion: Long, newVersion: Long) {
        val schema = realm.schema
        when (oldVersion) {
            1L -> {
                schema.create("SubTask")
                    .addField("id", String::class.javaPrimitiveType, FieldAttribute.PRIMARY_KEY)
                    .addField("name", String::class.javaPrimitiveType)
                    .addField("position", Int::class.javaPrimitiveType)
                    .addField("dateCreated", Long::class.javaPrimitiveType)
                    .addField("dateUpdated", Long::class.javaPrimitiveType)
                    .addField("isCompleted", Boolean::class.javaPrimitiveType)

                val taskSchema = schema.get("Task")
                taskSchema?.let{
                    it.addRealmListField("subTasks", schema.get("SubTask"))
                    it.addField("position", Int::class.javaPrimitiveType)
                }

            }
        }
    }
}