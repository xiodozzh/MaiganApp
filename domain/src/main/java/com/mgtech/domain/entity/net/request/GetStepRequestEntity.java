package com.mgtech.domain.entity.net.request;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.mgtech.domain.utils.HttpApi;
import com.mgtech.domain.utils.NetConstant;
import com.mgtech.domain.entity.net.RequestEntity;

/**
 * Created by zhaixiang on 2017/12/15.
 * 获取计步具体数据请求
 */

public class GetStepRequestEntity implements RequestEntity{
    public static final int HOUR = 1;
    public static final int DAY = 2;
    public static final int WEEK = 3;
    public static final int MONTH = 4;

    @SerializedName("accountGuid")
    private String id;
    @SerializedName("type")
    private int type;
    @SerializedName("startTime")
    private long startTime;
    @SerializedName("endTime")
    private long endTime;

    public GetStepRequestEntity(String id, int type, long startTime, long endTime) {
        this.id = id;
        this.type = type;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    @Override
    public String getUrl() {
        return HttpApi.API_GET_STEP_DETAIL_BY_DATE;
    }

    @Override
    public String getBody() {
        return new Gson().toJson(this);
    }
}
