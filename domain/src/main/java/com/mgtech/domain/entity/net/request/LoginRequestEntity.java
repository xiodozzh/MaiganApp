package com.mgtech.domain.entity.net.request;

import com.google.gson.annotations.SerializedName;
import com.mgtech.domain.utils.HttpApi;
import com.mgtech.domain.utils.NetConstant;
import com.mgtech.domain.entity.net.RequestEntity;

/**
 * Created by zhaixiang on 2017/1/5.
 * 登录请求 Entity
 */
public class LoginRequestEntity implements RequestEntity {
    @SerializedName(NetConstant.JSON_USER_NAME)
    private String phone;
    @SerializedName(NetConstant.Password)
    private String password;
    @SerializedName(NetConstant.GRANT_TYPE)
    private String type;
    @SerializedName(NetConstant.JSON_ZONE)
    private String zone;

    public LoginRequestEntity(String phone, String password,String zone) {
        this.phone = phone;
        this.password = password;
        this.zone = zone;
        this.type = NetConstant.PASSWORD;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    @Override
    public String toString() {
        return "LoginRequestEntity{" +
                "phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public String getUrl() {
        return HttpApi.API_LOGIN_URL;
    }

    @Override
    public String getBody() {
        return "UserName=" + phone + "&Password=" + password + "&Zone=" + zone +
                "&grant_type=password";
    }
}
