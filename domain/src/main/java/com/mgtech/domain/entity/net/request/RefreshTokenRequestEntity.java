package com.mgtech.domain.entity.net.request;

import com.google.gson.annotations.SerializedName;

public class RefreshTokenRequestEntity {

    /**
     * refreshToken :
     */

    private String refreshToken;
    @SerializedName("accountGuid")
    private String userId;

    public RefreshTokenRequestEntity(String id,String refreshToken) {
        this.refreshToken = refreshToken;
        this.userId = id;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "RefreshTokenRequestEntity{" +
                "refreshToken='" + refreshToken + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
