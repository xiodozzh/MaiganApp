package com.mgtech.domain.entity.net.request;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.mgtech.domain.utils.HttpApi;
import com.mgtech.domain.utils.NetConstant;
import com.mgtech.domain.entity.net.RequestEntity;

/**
 * Created by zhaixiang on 2017/1/10.
 * 获取最新数据请求
 */

public class GetCalculateDataRequestEntity implements RequestEntity{
    @SerializedName(NetConstant.JSON_USER_ID)
    private String id;
    @SerializedName(NetConstant.JSON_START_TIME)
    private String startTime;
    @SerializedName(NetConstant.JSON_END_TIME)
    private String endTime;
    @SerializedName(NetConstant.JSON_PW_DATA_TYPE)
    private int type;
//    @SerializedName(NetConstant.JSON_TIME_ZONE)
//    private String timeZone;

    public GetCalculateDataRequestEntity(String id, String startTime, String endTime,int type) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.type = type;
//        this.timeZone = timeZone;
    }

    @Override
    public String toString() {
        return "GetCalculateDataRequestEntity{" +
                "id='" + id + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }

    @Override
    public String getUrl() {
        return HttpApi.API_GET_INDICATOR_DATA;
    }

    @Override
    public String getBody() {
        return new Gson().toJson(this);
    }
}
