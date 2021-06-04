package com.mgtech.domain.entity.net.request;

import com.google.gson.Gson;
import com.mgtech.domain.entity.net.RequestEntity;
import com.mgtech.domain.utils.HttpApi;

/**
 * @author zhaixiang
 */
public class DeletePwRequestEntity implements RequestEntity {
    private String measureGuid;
    private String userId;

    public DeletePwRequestEntity(String measureGuid,String userId) {
        this.measureGuid = measureGuid;
        this.userId = userId;
    }

    public String getMeasureGuid() {
        return measureGuid;
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "DeletePwRequestEntity{" +
                "measureGuid='" + measureGuid + '\'' +
                ", userId='" + userId + '\'' +
                '}';
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
