package com.mgtech.domain.entity.net.request;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.mgtech.domain.utils.HttpApi;
import com.mgtech.domain.utils.NetConstant;
import com.mgtech.domain.entity.net.RequestEntity;

/**
 *
 * @author zhaixiang
 * 获取ECG
 */

public class GetEcgRequestEntity implements RequestEntity {
    private String measureGuid;
    private String userId;

    public GetEcgRequestEntity(String measureGuid, String userId) {
        this.measureGuid = measureGuid;
        this.userId = userId;
    }

    public String getMeasureGuid() {
        return measureGuid;
    }

    public void setMeasureGuid(String measureGuid) {
        this.measureGuid = measureGuid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String getUrl() {
        return HttpApi.API_GET_ECG_BY_ID;
    }

    @Override
    public String getBody() {
        return new Gson().toJson(this);
    }
}
