package com.mgtech.domain.entity.net.request;

import com.google.gson.Gson;
import com.mgtech.domain.entity.net.RequestEntity;
import com.mgtech.domain.utils.HttpApi;

public class SetExceptionReadRequestEntity implements RequestEntity {
    private String id;
    private String targetId;
    private String exceptionId;

    public SetExceptionReadRequestEntity(String id, String targetId, String exceptionId) {
        this.id = id;
        this.targetId = targetId;
        this.exceptionId = exceptionId;
    }

    public String getId() {
        return id;
    }

    public String getTargetId() {
        return targetId;
    }

    public String getExceptionId() {
        return exceptionId;
    }

    @Override
    public String getUrl() {
        return HttpApi.API_MARK_EXCEPTION_READ;
    }

    @Override
    public String getBody() {
        return new Gson().toJson(this);
    }
}
