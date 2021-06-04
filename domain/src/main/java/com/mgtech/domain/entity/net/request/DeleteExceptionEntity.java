package com.mgtech.domain.entity.net.request;

import com.google.gson.Gson;
import com.mgtech.domain.entity.net.RequestEntity;
import com.mgtech.domain.utils.HttpApi;

/**
 * Created by zhaixiang on 2017/8/31.
 * 删除异常信息
 */

public class DeleteExceptionEntity implements RequestEntity {
    private String id;
    private String exceptionId;

    public DeleteExceptionEntity(String id, String exceptionId) {
        this.id = id;
        this.exceptionId = exceptionId;
    }

    public String getId() {
        return id;
    }

    public String getExceptionId() {
        return exceptionId;
    }

    @Override
    public String getUrl() {
        return HttpApi.API_DELETE_EXCEPTION;
    }

    @Override
    public String getBody() {
        return new Gson().toJson(this);
    }
}
