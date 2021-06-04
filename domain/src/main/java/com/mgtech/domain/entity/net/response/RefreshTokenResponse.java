package com.mgtech.domain.entity.net.response;

public class RefreshTokenResponse {

    /**
     * accessToken :
     * refreshToken :
     * expiresTime : 16874326
     */

    private String accessToken;
    private String refreshToken;
    private long expiresTime;

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

    @Override
    public String toString() {
        return "RefreshTokenResponse{" +
                "accessToken='" + accessToken + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", expiresTime=" + expiresTime +
                '}';
    }
}
