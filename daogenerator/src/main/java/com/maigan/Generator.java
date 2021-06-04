package com.maigan;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class Generator {
    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(3, "com.mgtech.domain.entity.database");
        addReminderTable(schema);

        new DaoGenerator().generateAll(schema, "./domain/src/main/java");
    }

    private static void addTable(Schema schema) {
//        Entity user = schema.addEntity("UserEntity");
//        user.addStringProperty("name");
//        user.addStringProperty("phone");
//        user.addStringProperty("accountId").primaryKey();
//
//        Entity ecgData = schema.addEntity("EcgDataEntity");
//        ecgData.implementsInterface("android.os.Parcelable");
//        ecgData.addIdProperty();
//        ecgData.addDateProperty("time");
//        ecgData.addStringProperty("data");
//        ecgData.addStringProperty("userId").notNull();
    }

    private static void addReminderTable(Schema schema) {
        Entity reminder = schema.addEntity("ReminderEntity");
        reminder.addIdProperty();
        reminder.addDateProperty("startTime");
        reminder.addDateProperty("endTime");
        reminder.addStringProperty("name");
        reminder.addStringProperty("dosage");
        reminder.addLongProperty("remindTime").notNull();
        reminder.addIntProperty("hashCodeId").notNull().unique();


//        user.addStringProperty("name");
//        user.addStringProperty("phone");
//        user.addStringProperty("accountId").primaryKey();
//
//        Entity ecgData = schema.addEntity("EcgDataEntity");
//        ecgData.implementsInterface("android.os.Parcelable");
//        ecgData.addIdProperty();
//        ecgData.addDateProperty("time");
//        ecgData.addStringProperty("data");
//        ecgData.addStringProperty("userId").notNull();
    }
}
