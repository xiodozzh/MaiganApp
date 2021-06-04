package com.mgtech.domain.entity.net.request;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.mgtech.domain.utils.HttpApi;
import com.mgtech.domain.entity.net.RequestEntity;

/**
 *
 * @author zhaixiang
 * 注册请求 Entity
 */

public class RegisterRequestEntity implements RequestEntity{
    @SerializedName("phone")
    private String phone;
    @SerializedName("password")
    private String password;
    @SerializedName("verificationCode")
    private String verification;
    @SerializedName("zone")
    private String zone;

    public RegisterRequestEntity(String phone, String password, String verification, String zone) {
        this.phone = phone;
        this.password = password;
        this.verification = verification;
        this.zone = zone;
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


    @Override
    public String toString() {
        return "RegisterRequestEntity{" +
                "phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", verification='" + verification + '\'' +
                ", zone='" + zone + '\'' +
                '}';
    }

    @Override
    public String getUrl() {
        return HttpApi.API_REGISTER_URL;
    }

    @Override
    public String getBody() {
        return new Gson().toJson(this);
    }
}
