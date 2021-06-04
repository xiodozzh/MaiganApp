package com.mgtech.domain.entity.net.request;

public class LoginByOtherAppRequestEntity {

    /**
     * openId :
     * accessToken :
     * loginType : 1
     * isAuto : 0
     * deviceId :
     * nickName :
     * avatarUrl :
     */

    private String openId;
    private String accessToken;
    private int loginType;
    private int isAuto;
    private String deviceId;
    private String nickName;
    private String avatarUrl;
    private String unionId;

    public LoginByOtherAppRequestEntity() {
    }

    public LoginByOtherAppRequestEntity( int loginType,String openId,String unionId, String accessToken,
                                        int isAuto, String deviceId) {
        this.openId = openId;
        this.accessToken = accessToken;
        this.loginType = loginType;
        this.isAuto = isAuto;
        this.deviceId = deviceId;
        this.unionId = unionId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public int getLoginType() {
        return loginType;
    }

    public void setLoginType(int loginType) {
        this.loginType = loginType;
    }

    public int getIsAuto() {
        return isAuto;
    }

    public void setIsAuto(int isAuto) {
        this.isAuto = isAuto;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    @Override
    public String toString() {
        return "LoginByOtherAppRequestEntity{" +
                "openId='" + openId + '\'' +
                ", accessToken='" + accessToken + '\'' +
                ", loginType=" + loginType +
                ", isAuto=" + isAuto +
                ", deviceId='" + deviceId + '\'' +
                ", nickName='" + nickName + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                '}';
    }
}
