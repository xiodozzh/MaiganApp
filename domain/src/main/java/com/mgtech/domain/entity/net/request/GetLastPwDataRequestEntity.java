package com.mgtech.domain.entity.net.request;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.mgtech.domain.utils.HttpApi;
import com.mgtech.domain.utils.NetConstant;
import com.mgtech.domain.entity.net.RequestEntity;

/**
 *
 * @author zhaixiang
 * 获取最近数据
 */

public class GetLastPwDataRequestEntity implements RequestEntity {
    @SerializedName(NetConstant.JSON_USER_ID)
    private String id;
    @SerializedName(NetConstant.JSON_PW_DATA_TYPE)
    private int type;

    public GetLastPwDataRequestEntity(String id, int type) {
        this.id = id;
        this.type = type;
    }

    @Override
    public String getUrl() {
        return HttpApi.API_GET_REFRESH_DATA;
    }

    @Override
    public String getBody() {
        return new Gson().toJson(this);
    }
}
