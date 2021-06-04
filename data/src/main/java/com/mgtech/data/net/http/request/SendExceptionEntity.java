package com.mgtech.data.net.http.request;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.mgtech.domain.utils.HttpApi;
import com.mgtech.domain.entity.net.request.SendExceptionRequestEntity;
import com.mgtech.domain.utils.NetConstant;
import com.mgtech.domain.entity.net.RequestEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaixiang on 2017/5/19.
 * 获取异常提醒
 */

public class SendExceptionEntity implements RequestEntity {
    @SerializedName(NetConstant.HEADER)
    private String header;
    @SerializedName(NetConstant.FLAG)
    private String flag;
    @SerializedName(NetConstant.BODY)
    private List<SendExceptionRequestEntity> bodyList;
    @SerializedName(NetConstant.ADDITIONAL)
    private List<Object> additionList;

    public SendExceptionEntity(SendExceptionRequestEntity entity, LogAdditionEntity logAdditionEntity) {
        this.header = NetConstant.TABLE_ABNORMAL;
        this.flag = NetConstant.ACTION_INSERT;
        this.additionList = new ArrayList<>();
        this.bodyList = new ArrayList<>();
        this.bodyList.add(entity);
        this.additionList.add(logAdditionEntity);
    }

    @Override
    public String getUrl() {
        Gson gson = new Gson();
        return HttpApi.API_PUSH_EXCEPTION + "?param=[" + gson.toJson(this) + "]";
    }

    @Override
    public String getBody() {
        return "";
    }
}
