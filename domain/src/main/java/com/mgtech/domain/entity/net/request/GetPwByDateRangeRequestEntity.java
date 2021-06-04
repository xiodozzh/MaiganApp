package com.mgtech.domain.entity.net.request;

import com.google.gson.Gson;
import com.mgtech.domain.utils.HttpApi;
import com.mgtech.domain.entity.net.RequestEntity;

import java.util.Arrays;

/**
 *
 * @author zhaixiang
 * 获取数据请求
 */

public class GetPwByDateRangeRequestEntity implements RequestEntity{
    private long startDate;
    private long endDate;
    private int[] items;
    private int type;
    private String userId;

    public GetPwByDateRangeRequestEntity(String userId,long startDate, long endDate, int[] items, int type) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.items = items;
        this.type = type;
        this.userId = userId;
    }

    public long getStartDate() {
        return startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public int[] getItems() {
        return items;
    }

    public int getType() {
        return type;
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public String getUrl() {
        return HttpApi.API_GET_PW_BY_DATE_RANGE;
    }

    @Override
    public String getBody() {
        return new Gson().toJson(this);
    }
}
