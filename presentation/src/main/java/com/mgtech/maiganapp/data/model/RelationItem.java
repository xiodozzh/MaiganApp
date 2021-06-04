package com.mgtech.maiganapp.data.model;

import androidx.annotation.IntDef;

import com.mgtech.domain.entity.net.response.RelationshipResponseEntity;

/**
 * Created by zhaixiang on 2017/9/1.
 * 好友列表
 */

public class RelationItem {
    public static final int TYPE_NEW = 0;
    public static final int TYPE_HEADER_FOLLOW = 1;
    public static final int TYPE_HEADER_FANS = 2;
    public static final int TYPE_ITEM_FOLLOW = 4;
    public static final int TYPE_ITEM_FANS = 5;

    private int type;
    private RelationshipResponseEntity entity;

    public int getType() {
        return type;
    }

    public void setType(@ItemType int type) {
        this.type = type;
    }

    public RelationshipResponseEntity getEntity() {
        return entity;
    }

    public void setEntity(RelationshipResponseEntity entity) {
        this.entity = entity;
    }

    /**
     * 不用枚举而使用注解
     */
    @IntDef({TYPE_NEW,TYPE_HEADER_FOLLOW,TYPE_HEADER_FANS,TYPE_ITEM_FANS,TYPE_ITEM_FOLLOW})
    private @interface ItemType{}
}
