package com.mgtech.domain.entity.net.request;

import com.google.gson.Gson;
import com.mgtech.domain.entity.net.RequestEntity;
import com.mgtech.domain.utils.HttpApi;
import com.mgtech.domain.utils.MyConstant;

public class GetWeeklyReportRequestEntity implements RequestEntity {
    private String id;
    private int pwDataType;

    public GetWeeklyReportRequestEntity(String id) {
        this.id = id;
        this.pwDataType = MyConstant.GET_PW_DATA_TYPE;
    }

    public String getId() {
        return id;
    }

    public int getPwDataType() {
        return pwDataType;
    }

    @Override
    public String getUrl() {
        return HttpApi.API_GET_WEEKLY_REPORT;
    }

    @Override
    public String getBody() {
        return new Gson().toJson(this);
    }
}
