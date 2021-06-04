package com.mgtech.domain.entity.net.request;

import com.google.gson.annotations.SerializedName;
import com.mgtech.domain.utils.NetConstant;

/**
 * Created by zhaixiang on 2017/1/10.
 * 获取最新数据请求
 */

public class GetRefreshDataRequestEntity {
    @SerializedName(NetConstant.JSON_USER_ID)
    private String id;

    public GetRefreshDataRequestEntity(String id) {
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
        return "GetRefreshDataRequestEntity{" +
                "id='" + id + '\'' +
                '}';
    }
}
