package com.mgtech.domain.entity.net.request;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.mgtech.domain.utils.HttpApi;
import com.mgtech.domain.utils.NetConstant;
import com.mgtech.domain.entity.net.RequestEntity;

/**
 * Created by zhaixiang on 2017/12/15.
 */

public class GetStepStatisticsDataByTimeRequestEntity implements RequestEntity{

    @SerializedName("accountGuid")
    private String id;
    @SerializedName("type")
    private int type;
    @SerializedName("startTime")
    private long startTime;
    @SerializedName("endTime")
    private long endTime;

    public GetStepStatisticsDataByTimeRequestEntity(String id, int type, long startTime, long endTime) {
        this.id = id;
        this.type = type;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public String getUrl() {
        return HttpApi.API_GET_STATISTICS_STEP_BY_TIME;
    }

    @Override
    public String getBody() {
        return new Gson().toJson(this);
    }
}
