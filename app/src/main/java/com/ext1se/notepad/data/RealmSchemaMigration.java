package com.ext1se.notepad.data;

import com.ext1se.notepad.data.model.Project;
import com.ext1se.notepad.data.model.Task;

import java.util.List;

import io.realm.DynamicRealm;
import io.realm.DynamicRealmObject;
import io.realm.FieldAttribute;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmResults;
import io.realm.RealmSchema;
import io.realm.Sort;

public class RealmSchemaMigration implements RealmMigration {
    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        RealmSchema schema = realm.getSchema();
        switch ((int) oldVersion) {
            case 1: {
                schema.create("SubTask")
                        .addField("id", String.class, FieldAttribute.PRIMARY_KEY, FieldAttribute.REQUIRED)
                        .addField("name", String.class, FieldAttribute.REQUIRED)
                        .addField("position", int.class)
                        .addField("dateCreated", long.class)
                        .addField("dateUpdated", long.class)
                        .addField("isCompleted", boolean.class);

                RealmObjectSchema taskSchema = schema.get("Task");
                if (taskSchema != null) {
                    taskSchema
                            .addRealmListField("subTasks", schema.get("SubTask"))
                            .addField("position", int.class);
                }
            }
            case 2: {
                RealmObjectSchema projectSchema = schema.get("Project");
                if (projectSchema != null) {
                    projectSchema.addField("position", int.class);
                }
                RealmResults<DynamicRealmObject> projects = realm.where("Project")
                        .sort("dateCreated", Sort.DESCENDING).findAll();
                for (int i = 0; i < projects.size(); i++) {
                    DynamicRealmObject project = projects.get(i);
                    project.setInt("position", i);
                    RealmResults<DynamicRealmObject> tasks = realm.where("Task")
                            .equalTo("id_project", project.getString("id"))
                            .equalTo("isRemoved", false)
                            .findAll().sort("dateCreated", Sort.DESCENDING);
                    for (int j = 0; j < tasks.size(); j++) {
                        DynamicRealmObject task = tasks.get(j);
                        task.setInt("position", j);
                    }
                }
            }
        }
    }
}
