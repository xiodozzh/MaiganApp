package com.mgtech.domain.entity.net.request;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.mgtech.domain.utils.HttpApi;
import com.mgtech.domain.utils.NetConstant;
import com.mgtech.domain.entity.net.RequestEntity;

/**
 *
 * @author zhaixiang
 * @date 2017/12/14
 * 获取主页数据
 */

public class GetHomeDataRequestEntity implements RequestEntity {
    private String id;
    private int pwDataType;

    public GetHomeDataRequestEntity(String id, int pwDataType) {
        this.id = id;
        this.pwDataType = pwDataType;
    }

    public String getId() {
        return id;
    }

    public int getPwDataType() {
        return pwDataType;
    }

    @Override
    public String getUrl() {
        return HttpApi.API_GET_HOME_PAGE_DATA;
    }

    @Override
    public String getBody() {
        return new Gson().toJson(this);
    }
}
