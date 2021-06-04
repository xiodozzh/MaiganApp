package com.mgtech.domain.entity.net.response;

/**
 * @author Jesse
 */
public class CheckServiceAuthorityResponseEntity {

    /**
     * authStatus : 1
     * authCode : 70343a11-b1c0-4a19-85ed-c705fd471fee
     */

    public static final int CHECKED= 2;
    public static final int CHECK_SUCCESS= 1;
    private int authStatus;
    private String authCode;

    public int getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(int authStatus) {
        this.authStatus = authStatus;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    @Override
    public String toString() {
        return "CheckServiceAuthorityResponseEntity{" +
                "authStatus=" + authStatus +
                ", authCode='" + authCode + '\'' +
                '}';
    }
}
