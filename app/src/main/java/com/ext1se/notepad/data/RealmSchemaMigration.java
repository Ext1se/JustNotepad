package com.ext1se.notepad.data;

import io.realm.DynamicRealm;
import io.realm.FieldAttribute;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;

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
        }
    }
}
