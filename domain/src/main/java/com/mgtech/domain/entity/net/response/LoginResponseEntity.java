package com.mgtech.domain.entity.net.response;

import com.google.gson.annotations.SerializedName;
import com.mgtech.domain.entity.net.PersonalInfoEntity;

/**
 *
 * @author zhaixiang
 * 登录返回
 */

public class LoginResponseEntity {
    private String accessToken;
    private String refreshToken;
    private long expiresTime;
    @SerializedName("isLoginOnOtherDevice")
    private int isLoginOtherDevice;
    private PersonalInfoEntity userInfo;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public long getExpiresTime() {
        return expiresTime;
    }

    public void setExpiresTime(long expiresTime) {
        this.expiresTime = expiresTime;
    }

    public boolean isLoginOtherDevice() {
        return isLoginOtherDevice == 1;
    }

    public void setLoginOtherDevice(boolean loginOtherDevice) {
        isLoginOtherDevice = loginOtherDevice ? 1:0;
    }

    public PersonalInfoEntity getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(PersonalInfoEntity userInfo) {
        this.userInfo = userInfo;
    }

    @Override
    public String toString() {
        return "LoginResponseEntity{" +
                "accessToken='" + accessToken + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", expiresTime=" + expiresTime +
                ", isLoginOtherDevice=" + isLoginOtherDevice +
                ", userInfo=" + userInfo +
                '}';
    }
}
