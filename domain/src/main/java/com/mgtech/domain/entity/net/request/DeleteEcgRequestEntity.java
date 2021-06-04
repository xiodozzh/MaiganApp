package com.mgtech.domain.entity.net.request;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.mgtech.domain.utils.HttpApi;
import com.mgtech.domain.utils.NetConstant;
import com.mgtech.domain.entity.net.RequestEntity;

/**
 *
 * @author zhaixiang
 * 删除ECG
 */

public class DeleteEcgRequestEntity implements RequestEntity {
    private String measureGuid;

    public DeleteEcgRequestEntity(String measureGuid) {
        this.measureGuid = measureGuid;
    }

    public String getMeasureGuid() {
        return measureGuid;
    }

    @Override
    public String getUrl() {
        return HttpApi.API_DELETE_ECG;
    }

    @Override
    public String getBody() {
        return new Gson().toJson(this);
    }
}
