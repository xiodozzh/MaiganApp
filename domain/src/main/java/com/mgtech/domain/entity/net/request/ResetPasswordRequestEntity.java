package com.mgtech.domain.entity.net.request;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.mgtech.domain.utils.HttpApi;
import com.mgtech.domain.entity.net.RequestEntity;

/**
 *
 * @author zhaixiang
 * 密码重置 Entity
 */

public class ResetPasswordRequestEntity implements RequestEntity{
    @SerializedName("phone")
    private String phone;
    @SerializedName("password")
    private String password;
    @SerializedName("zone")
    private String zone;
    @SerializedName("verificationCode")
    private String verificationCode;

    public ResetPasswordRequestEntity(String phone, String password,String zone,String verificationCode) {
        this.phone = phone;
        this.password = password;
        this.zone = zone;
        this.verificationCode = verificationCode;
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

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    @Override
    public String getUrl() {
        return HttpApi.API_RESET_PASSWORD_URL;
    }

    @Override
    public String getBody() {
        return new Gson().toJson(this);
    }
}
