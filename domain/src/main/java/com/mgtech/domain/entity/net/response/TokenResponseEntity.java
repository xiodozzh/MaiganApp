package com.mgtech.domain.entity.net.response;

import com.google.gson.annotations.SerializedName;
import com.mgtech.domain.utils.NetConstant;

/**
 * Created by zhaixiang on 2017/7/26.
 * token
 */

public class TokenResponseEntity {
    @SerializedName(NetConstant.ACCESS_TOKEN)
    private String accessToken;
    @SerializedName(NetConstant.TOKEN_TYPE)
    private String tokenType;
    @SerializedName(NetConstant.REFRESH_TOKEN)
    private String refreshToken;
    @SerializedName(NetConstant.JSON_USER_ID)
    private String id;
    @SerializedName(NetConstant.JSON_PHONE)
    private String phone;
    @SerializedName("IsGroup")
    private String isGroup;
    @SerializedName("GroupRowGuid")
    private String groupId;
    @SerializedName("TokenTime")
    private String tokenTime;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIsGroup() {
        return isGroup;
    }

    public void setIsGroup(String isGroup) {
        this.isGroup = isGroup;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getTokenTime() {
        return tokenTime;
    }

    public void setTokenTime(String tokenTime) {
        this.tokenTime = tokenTime;
    }

    @Override
    public String toString() {
        return "LoginResponseEntity{" +
                "accessToken='" + accessToken + '\'' +
                ", tokenType='" + tokenType + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", id='" + id + '\'' +
                ", phone='" + phone + '\'' +
                ", isGroup='" + isGroup + '\'' +
                ", groupId='" + groupId + '\'' +
                '}';
    }
}
