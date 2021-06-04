package com.mgtech.domain.entity.net.request;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.mgtech.domain.utils.HttpApi;
import com.mgtech.domain.utils.NetConstant;
import com.mgtech.domain.entity.net.RequestEntity;

/**
 * Created by zhaixiang on 2017/1/6.
 * 短信验证请求 Entity
 */

public class VerifyRequestEntity implements RequestEntity{
    @SerializedName(NetConstant.JSON_PHONE)
    private String phone;
    @SerializedName(NetConstant.JSON_VERIFICATION)
    private String verification;
    @SerializedName(NetConstant.JSON_ZONE)
    private String zone;
//    @SerializedName(NetConstant.JSON_APP_KEY)
//    private String appKey;

    public VerifyRequestEntity(String phone, String verification, String zone) {
        this.phone = phone;
        this.verification = verification;
        this.zone = zone;
//        this.appKey = BleConstant.SMSAppKey;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getVerification() {
        return verification;
    }

    public void setVerification(String verification) {
        this.verification = verification;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

//    public String getAppKey() {
//        return appKey;
//    }
//
//    public void setAppKey(String appKey) {
//        this.appKey = appKey;
//    }

    @Override
    public String toString() {
        return "RegisterRequestEntity{" +
                "phone='" + phone + '\'' +
                ", verification='" + verification + '\'' +
                ", zone='" + zone + '\'' +
                '}';
    }

    @Override
    public String getUrl() {
        return HttpApi.API_MESSAGE_VERIFICATION_URL;
    }

    @Override
    public String getBody() {
        return new Gson().toJson(this);
    }
}
