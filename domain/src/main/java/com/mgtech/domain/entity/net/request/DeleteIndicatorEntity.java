package com.mgtech.domain.entity.net.request;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.mgtech.domain.utils.HttpApi;
import com.mgtech.domain.utils.NetConstant;
import com.mgtech.domain.entity.net.RequestEntity;

/**
 *
 * @author zhaixiang
 * 删除某个生理数据
 */

public class DeleteIndicatorEntity implements RequestEntity {
    private String measureGuid;

    public DeleteIndicatorEntity( String measureGuid) {
        this.measureGuid = measureGuid;
    }

    public String getMeasureGuid() {
        return measureGuid;
    }

    @Override
    public String getUrl() {
        return HttpApi.API_DELETE_PW;
    }

    @Override
    public String getBody() {
        return new Gson().toJson(this);
    }
}
