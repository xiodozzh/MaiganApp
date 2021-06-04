package com.mgtech.domain.entity.net.request;

import com.google.gson.annotations.SerializedName;
import com.mgtech.domain.utils.NetConstant;

import java.util.Arrays;

/**
 * Created by zhaixiang on 2017/3/9.
 * 上传异常请求
 */

public class SendExceptionRequestEntity {
    @SerializedName(NetConstant.JSON_OPERATE_USER_NAME)
    private String operationUserName;
    @SerializedName(NetConstant.JSON_USER_ID)
    private String[] senderId;
    @SerializedName(NetConstant.JSON_ABNORMAL_TITLE)
    private String abnormalType;
    @SerializedName(NetConstant.JSON_CONTENT)
    private String abnormalValue;
//    @SerializedName(NetConstant.JSON_ABNORMAL_UNIT)
//    private String abnormalUnit;
    @SerializedName(NetConstant.JSON_ABNORMAL_DATE)
    private String abnormalDate;
    @SerializedName(NetConstant.JSON_ABNORMAL_NAME)
    private String getAbnormalName;
    @SerializedName(NetConstant.JSON_SENDER_USER_GUID)
    private String senderUserId;

    public SendExceptionRequestEntity(String operationUserName, String[] senderId, String abnormalType,
                                      String abnormalValue,  String abnormalDate,
                                      String getAbnormalName, String senderUserId) {
        this.operationUserName = operationUserName;
        this.senderId = senderId;
        this.abnormalType = abnormalType;
        this.abnormalValue = abnormalValue;
//        this.abnormalUnit = abnormalUnit;
        this.abnormalDate = abnormalDate;
        this.getAbnormalName = getAbnormalName;
        this.senderUserId = senderUserId;
    }

    public String getOperationUserName() {
        return operationUserName;
    }

    public void setOperationUserName(String operationUserName) {
        this.operationUserName = operationUserName;
    }

    public String[] getSenderId() {
        return senderId;
    }

    public void setSenderId(String[] senderId) {
        this.senderId = senderId;
    }

    public String getAbnormalType() {
        return abnormalType;
    }

    public void setAbnormalType(String abnormalType) {
        this.abnormalType = abnormalType;
    }

    public String getAbnormalValue() {
        return abnormalValue;
    }

    public void setAbnormalValue(String abnormalValue) {
        this.abnormalValue = abnormalValue;
    }



    public String getAbnormalDate() {
        return abnormalDate;
    }

    public void setAbnormalDate(String abnormalDate) {
        this.abnormalDate = abnormalDate;
    }

    public String getGetAbnormalName() {
        return getAbnormalName;
    }

    public void setGetAbnormalName(String getAbnormalName) {
        this.getAbnormalName = getAbnormalName;
    }

    public String getSenderUserId() {
        return senderUserId;
    }

    public void setSenderUserId(String senderUserId) {
        this.senderUserId = senderUserId;
    }

    @Override
    public String toString() {
        return "SendExceptionRequestEntity{" +
                "operationUserName='" + operationUserName + '\'' +
                ", senderId=" + Arrays.toString(senderId) +
                ", abnormalType='" + abnormalType + '\'' +
                ", abnormalValue='" + abnormalValue + '\'' +
                ", abnormalDate='" + abnormalDate + '\'' +
                ", getAbnormalName='" + getAbnormalName + '\'' +
                ", senderUserId='" + senderUserId + '\'' +
                '}';
    }
}
