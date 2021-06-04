package com.mgtech.domain.entity.net.request;

import com.google.gson.Gson;
import com.mgtech.domain.entity.net.RequestEntity;
import com.mgtech.domain.utils.HttpApi;
import com.mgtech.domain.utils.MyConstant;
import com.mgtech.domain.utils.NetConstant;

import java.util.Arrays;

public class GetHealthManagementDataRequestEntity implements RequestEntity {
    private String id;
    private long startDate;
    private long endDate;
    private int[] items;
    private int dataType;

    public GetHealthManagementDataRequestEntity(String id, long startDate, long endDate, int[] items) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.items = items;
        this.dataType = MyConstant.GET_PW_DATA_TYPE;
    }

    public String getId() {
        return id;
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

    public int getDataType() {
        return dataType;
    }

    @Override
    public String toString() {
        return "GetHealthManagementDataRequestEntity{" +
                "id='" + id + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", items=" + Arrays.toString(items) +
                ", dataType=" + dataType +
                '}';
    }

    @Override
    public String getUrl() {
        return HttpApi.API_GET_HEALTH_MANAGEMENT_DATA;
    }

    @Override
    public String getBody() {
        return new Gson().toJson(this);
    }
}
