package com.mgtech.domain.entity.net.request;

import com.google.gson.annotations.SerializedName;
import com.mgtech.domain.entity.net.RequestEntity;
import com.mgtech.domain.utils.NetConstant;

/**
 * Created by zhaixiang on 2017/1/10.
 * 个人信息id
 */

public class UserIdRequestEntity {
    @SerializedName("accountGuid")
    private String id;

    public UserIdRequestEntity(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "UserIdRequestEntity{" +
                "id='" + id + '\'' +
                '}';
    }

}
