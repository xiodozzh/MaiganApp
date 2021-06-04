package com.mgtech.domain.entity.net.response;

public class GetServiceAuthCodeResponseEntity {

    /**
     * authCode : 70343a11-b1c0-4a19-85ed-c705fd471fee
     */

    private String authCode;

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    @Override
    public String toString() {
        return "GetServiceAuthCodeResponseEntity{" +
                "authCode='" + authCode + '\'' +
                '}';
    }
}
