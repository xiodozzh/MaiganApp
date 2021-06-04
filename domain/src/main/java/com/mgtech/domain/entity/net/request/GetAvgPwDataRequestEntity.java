package com.mgtech.domain.entity.net.request;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.mgtech.domain.utils.HttpApi;
import com.mgtech.domain.utils.NetConstant;
import com.mgtech.domain.entity.net.RequestEntity;

/**
 *
 * @author zhaixiang
 * 查询平均值
 */

public class GetAvgPwDataRequestEntity implements RequestEntity {
    @SerializedName(NetConstant.JSON_RESULT_NAME)
    private int[] field;
    @SerializedName(NetConstant.JSON_START_DATE)
    private long startDate;
    @SerializedName(NetConstant.JSON_END_DATE)
    private long endDate;
    @SerializedName(NetConstant.JSON_PW_DATA_TYPE)
    private int type;

    public GetAvgPwDataRequestEntity(int[] field, long startDate, long endDate, int type) {
        this.field = field;
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
    }

    public int[] getField() {
        return field;
    }

    public long getStartDate() {
        return startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public int getType() {
        return type;
    }

    @Override
    public String getUrl() {
        return HttpApi.API_GET_STATISTIC_PW_BY_DATE_RANGE;
    }

    @Override
    public String getBody() {
        return new Gson().toJson(this);
    }
}
