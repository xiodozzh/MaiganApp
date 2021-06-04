package com.mgtech.data.net.http.request;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.mgtech.domain.utils.HttpApi;
import com.mgtech.domain.entity.net.request.MarkDataRequestEntity;
import com.mgtech.domain.utils.NetConstant;
import com.mgtech.domain.entity.net.RequestEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaixiang on 2017/5/15.
 * 发送请求
 */

public class MarkIsSportEntity implements RequestEntity {
    @SerializedName(NetConstant.HEADER)
    private String header;
    @SerializedName(NetConstant.FLAG)
    private String flag;
    @SerializedName(NetConstant.BODY)
    private List<MarkDataRequestEntity> bodyList;
    @SerializedName(NetConstant.ADDITIONAL)
    private List<Object> additionList;

    public MarkIsSportEntity(MarkDataRequestEntity entity, LogAdditionEntity logAdditionEntity) {
        this.header = NetConstant.TABLE_ANALYZE_DATA;
        this.flag = NetConstant.ACTION_UPDATE;
        this.additionList = new ArrayList<>();
        this.bodyList = new ArrayList<>();
        this.bodyList.add(entity);
        this.additionList.add(logAdditionEntity);
    }

    @Override
    public String getUrl() {
        return HttpApi.API_MARK_DATA;
    }

    @Override
    public String getBody() {
        return "[" + new Gson().toJson(this) + "]";
    }
}
