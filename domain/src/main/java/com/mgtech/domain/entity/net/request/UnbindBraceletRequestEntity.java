package com.mgtech.domain.entity.net.request;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.mgtech.domain.entity.net.RequestEntity;
import com.mgtech.domain.utils.HttpApi;

/**
 *
 * @author zhaixiang
 * 解绑请求
 */

public class UnbindBraceletRequestEntity implements RequestEntity{
    private String mac;
    private String id;

    public UnbindBraceletRequestEntity(String mac, String id) {
        this.mac = mac;
        this.id = id;
    }

    public String getMac() {
        return mac;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "UnbindBraceletRequestEntity{" +
                ", mac='" + mac + '\'' +
                '}';
    }

    @Override
    public String getUrl() {
        return HttpApi.API_UNBIND_BRACELET;
    }

    @Override
    public String getBody() {
        return new Gson().toJson(this);
    }
}
