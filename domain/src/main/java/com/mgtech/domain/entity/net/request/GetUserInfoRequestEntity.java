package com.mgtech.domain.entity.net.request;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.mgtech.domain.utils.HttpApi;
import com.mgtech.domain.utils.NetConstant;
import com.mgtech.domain.entity.net.RequestEntity;

/**
 * Created by zhaixiang on 2017/1/17.
 * 获取个人信息
 */

public class GetUserInfoRequestEntity implements RequestEntity{
    @SerializedName(NetConstant.JSON_USER_ID)
    private String id;

    public GetUserInfoRequestEntity(String id) {
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
        return "GetUserInfoRequestEntity{" +
                "id='" + id + '\'' +
                '}';
    }

    @Override
    public String getUrl() {
        return HttpApi.API_GET_PERSONAL_INFO_URL;
    }

    @Override
    public String getBody() {
        return new Gson().toJson(this);
    }
}
