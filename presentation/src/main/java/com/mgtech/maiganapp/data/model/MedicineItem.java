package com.mgtech.maiganapp.data.model;

import androidx.annotation.IntDef;

import com.mgtech.domain.entity.net.response.MedicineResponseEntity;

/**
 * Created by zhaixiang on 2017/8/31.
 * 显示药物列表的每项
 */

public class MedicineItem {
    public static final int TITLE_TAKING = 1;
    public static final int TITLE_TAKEN = 2;
    public static final int ITEM_TAKING = 3;
    public static final int ITEM_TAKEN = 4;

    private int type;
    private MedicineResponseEntity entity;

    public void setType(@ItemType int type) {
        this.type = type;
    }

    public void setEntity(MedicineResponseEntity entity) {
        this.entity = entity;
    }

    public int getType() {
        return type;
    }

    public MedicineResponseEntity getEntity() {
        return entity;
    }

    @Override
    public String toString() {
        return "MedicineItem{" +
                "type=" + type +
                ", entity=" + entity +
                '}';
    }

    @IntDef({TITLE_TAKEN,TITLE_TAKING,ITEM_TAKEN,ITEM_TAKING})
    private @interface ItemType{}
}
