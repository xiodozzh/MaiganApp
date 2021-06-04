package com.mgtech.domain.entity.net.request;

import android.net.Uri;
import android.webkit.URLUtil;

import com.google.gson.Gson;
import com.mgtech.domain.utils.HttpApi;
import com.mgtech.domain.entity.net.RequestEntity;
import com.mgtech.domain.utils.NetConstant;

import okio.Utf8;

/**
 *
 * @author zhaixiang
 * 登录请求 Entity
 */
public class LoginByPhoneRequestEntity implements RequestEntity {
    public static final int MANUAL = 0;
    public static final int AUTO = 1;

    private String phone;
    private String password;
    private String zone;
    private int loginType;
    private String deviceId;
    private int isAuto;

    public LoginByPhoneRequestEntity(String phone, String password, String zone, String deviceId, boolean isAuto) {
        this.phone = phone;
        this.password = password;
        this.zone = zone;
        this.loginType = NetConstant.APP_LOGIN;
        this.deviceId = deviceId;
        this.isAuto = isAuto?AUTO:MANUAL;
    }


    @Override
    public String toString() {
        return "LoginByPhoneRequestEntity{" +
                "phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", zone='" + zone + '\'' +
                ", loginType=" + loginType +
                ", deviceId='" + deviceId + '\'' +
                ", isAuto=" + isAuto +
                '}';
    }

    @Override
    public String getUrl() {
        return HttpApi.API_LOGIN_URL;
    }

    @Override
    public String getBody() {
        return new Gson().toJson(this);
    }
}
