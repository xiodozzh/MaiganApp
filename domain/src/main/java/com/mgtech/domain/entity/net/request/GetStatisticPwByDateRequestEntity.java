package com.mgtech.domain.entity.net.request;

import com.google.gson.Gson;
import com.mgtech.domain.entity.net.RequestEntity;
import com.mgtech.domain.utils.HttpApi;

import java.util.Arrays;

/**
 * @author zhaixiang
 */
public class GetStatisticPwByDateRequestEntity implements RequestEntity {
    private long date;
    private int[] items;
    private int dataType;
    private String userId;

    public GetStatisticPwByDateRequestEntity(String userId,long date, int[] items, int dataType) {
        this.date = date;
        this.items = items;
        this.dataType = dataType;
        this.userId = userId;
    }

    public long getDate() {
        return date;
    }

    public int[] getItems() {
        return items;
    }

    public int getDataType() {
        return dataType;
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "GetStatisticPwByDateRequestEntity{" +
                "date=" + date +
                ", items=" + Arrays.toString(items) +
                ", dataType=" + dataType +
                '}';
    }

    @Override
    public String getUrl() {
        return HttpApi.API_GET_STATISTIC_PW_BY_DATE;
    }

    @Override
    public String getBody() {
        return new Gson().toJson(this);
    }
}
