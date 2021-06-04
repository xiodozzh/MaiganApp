package com.mgtech.domain.entity.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

public class ReminderManager {
    private ReminderEntityDao dao;
    private volatile static ReminderManager manager;

    private ReminderManager(Context context) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        this.dao = daoSession.getReminderEntityDao();
    }

    public static ReminderManager getInstance(Context context) {
        if (manager == null){
            synchronized (ReminderManager.class){
                if (manager == null){
                    manager = new ReminderManager(context);
                }
            }
        }
        return manager;
    }

    public List<ReminderEntity> getReminders() {
        QueryBuilder<ReminderEntity> queryBuilder = dao.queryBuilder();
        return queryBuilder.list();
    }

    public void updateAllReminder(List<ReminderEntity> list) {
        dao.deleteAll();
        dao.insertOrReplaceInTx(list);
    }

    public void removeReminders(List<ReminderEntity> list){
        QueryBuilder<ReminderEntity> queryBuilder = dao.queryBuilder();
        List<Integer>hashCodes = new ArrayList<>();
        for (ReminderEntity e : list) {
            hashCodes.add(e.getHashCodeId());
        }
        queryBuilder.where(ReminderEntityDao.Properties.HashCodeId.in(hashCodes));
        List<ReminderEntity> deleteEntity = queryBuilder.list();
        for (ReminderEntity e : deleteEntity) {
            dao.delete(e);
        }
    }

    public void addReminders(List<ReminderEntity> list){
        dao.insertOrReplaceInTx(list);
    }

    public void removeAllReminder(){
        dao.deleteAll();
    }



}
