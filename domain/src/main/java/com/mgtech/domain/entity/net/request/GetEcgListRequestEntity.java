package com.mgtech.domain.entity.net.request;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.mgtech.domain.utils.HttpApi;
import com.mgtech.domain.utils.NetConstant;
import com.mgtech.domain.entity.net.RequestEntity;

/**
 *
 * @author zhaixiang
 * 获取ECG list
 */

public class GetEcgListRequestEntity implements RequestEntity{
    private long startTime;
    private long endTime;
    private String userId;
    private int dataLength;

    public GetEcgListRequestEntity(long startTime, long endTime,String userId, int dataLength) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.dataLength = dataLength;
        this.userId = userId;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public String getUserId() {
        return userId;
    }

    public int getDataLength() {
        return dataLength;
    }

    @Override
    public String getUrl() {
        return HttpApi.API_GET_ECG_LIST_BY_DATE;
    }

    @Override
    public String getBody() {
        return new Gson().toJson(this);
    }
}
