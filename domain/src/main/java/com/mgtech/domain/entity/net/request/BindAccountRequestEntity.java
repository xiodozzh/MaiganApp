package com.mgtech.domain.entity.net.request;

import com.google.gson.Gson;
import com.mgtech.domain.entity.net.RequestEntity;
import com.mgtech.domain.utils.HttpApi;

public class BindAccountRequestEntity implements RequestEntity {


    /**
     * openId :
     * loginType : 1
     * bindAccountType : 0
     * phone :
     * zone :
     * verificationCode :
     * password :
     * accountGuid :
     * avatarUrl :
     * accessToken :
     * nickName :
     * unionId :
     */

    private String openId;
    private int loginType;
    private int bindAccountType;
    private String phone;
    private String zone;
    private String verificationCode;
    private String password;
    private String accountGuid;
    private String avatarUrl;
    private String accessToken;
    private String nickName;
    private String unionId;

    @Override
    public String getUrl() {
        return HttpApi.API_BIND_ACCOUNT;
    }

    @Override
    public String getBody() {
        return new Gson().toJson(this);
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public int getLoginType() {
        return loginType;
    }

    public void setLoginType(int loginType) {
        this.loginType = loginType;
    }

    public int getBindAccountType() {
        return bindAccountType;
    }

    public void setBindAccountType(int bindAccountType) {
        this.bindAccountType = bindAccountType;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccountGuid() {
        return accountGuid;
    }

    public void setAccountGuid(String accountGuid) {
        this.accountGuid = accountGuid;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    @Override
    public String toString() {
        return "BindAccountRequestEntity{" +
                "openId='" + openId + '\'' +
                ", loginType=" + loginType +
                ", bindAccountType=" + bindAccountType +
                ", phone='" + phone + '\'' +
                ", zone='" + zone + '\'' +
                ", verificationCode='" + verificationCode + '\'' +
                ", password='" + password + '\'' +
                ", accountGuid='" + accountGuid + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", accessToken='" + accessToken + '\'' +
                ", nickName='" + nickName + '\'' +
                ", unionId='" + unionId + '\'' +
                '}';
    }
}
