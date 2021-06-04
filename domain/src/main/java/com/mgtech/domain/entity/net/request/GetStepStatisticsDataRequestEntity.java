package com.mgtech.domain.entity.net.request;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.mgtech.domain.utils.HttpApi;
import com.mgtech.domain.utils.NetConstant;
import com.mgtech.domain.entity.net.RequestEntity;

/**
 * Created by zhaixiang on 2017/12/15.
 */

public class GetStepStatisticsDataRequestEntity implements RequestEntity{
    @SerializedName("accountGuid")
    private String id;
    @SerializedName("type")
    private int type;
    @SerializedName("lastTime")
    private long time;
    @SerializedName("count")
    private int count;

    public GetStepStatisticsDataRequestEntity(String id, int type, long time, int count) {
        this.id = id;
        this.type = type;
        this.time = time;
        this.count = count;
    }

    @Override
    public String getUrl() {
        return HttpApi.API_GET_STATISTICS_STEP_INFO;
    }

    @Override
    public String getBody() {
        return new Gson().toJson(this);
    }
}
