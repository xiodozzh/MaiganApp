package com.mgtech.domain.entity.net.request;

import com.google.gson.Gson;
import com.mgtech.domain.entity.net.RequestEntity;
import com.mgtech.domain.utils.HttpApi;

/**
 * @author zhaixiang
 * 获取绑定信息
 */

public class GetDeviceBindInfoRequestEntity implements RequestEntity {
    private int type;
    private String id;


    public GetDeviceBindInfoRequestEntity(int type, String id) {
        this.type = type;
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    @Override
    public String getUrl() {
        return HttpApi.API_GET_BRACELET + "?type=" + type;
    }

    @Override
    public String getBody() {
        return new Gson().toJson(this);
    }
}
