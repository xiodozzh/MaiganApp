package com.mgtech.domain.entity.net.request;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.mgtech.domain.utils.HttpApi;
import com.mgtech.domain.utils.NetConstant;
import com.mgtech.domain.entity.net.RequestEntity;

/**
 *
 * @author zhaixiang
 * 发送关注请求
 */

public class SendRelationRequestRequestEntity implements RequestEntity{
    @SerializedName("message")
    private String message;
    @SerializedName("targetAccountGuid")
    private String targetId;

    public SendRelationRequestRequestEntity(String targetId, String message) {
        this.targetId = targetId;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }


    @Override
    public String toString() {
        return "SendRelationRequestRequestEntity{" +
                "message='" + message + '\'' +
                ", targetId='" + targetId + '\'' +
                '}';
    }

    @Override
    public String getUrl() {
        return HttpApi.API_SEND_RELATION_REQUEST;
    }

    @Override
    public String getBody() {
        return new Gson().toJson(this);
    }
}
