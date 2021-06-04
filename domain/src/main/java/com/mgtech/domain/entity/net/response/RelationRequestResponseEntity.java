package com.mgtech.domain.entity.net.response;

/**
 *
 * @author zhaixiang
 * 关系请求返回
 */

public class RelationRequestResponseEntity extends ResponseEntity {
    public static final int DEFAULT = 2;
    public static final int ACCEPT = 1;
    public static final int DENY = 0;
    /**
     * requestGuid :
     * senderAccountGuid :
     * senderAvatarUrl :
     * senderName :
     * result : 2
     */

    private String requestGuid;
    private String senderAccountGuid;
    private String senderAvatarUrl;
    private String senderName;
    private int result;

    public String getRequestGuid() {
        return requestGuid;
    }

    public void setRequestGuid(String requestGuid) {
        this.requestGuid = requestGuid;
    }

    public String getSenderAccountGuid() {
        return senderAccountGuid;
    }

    public void setSenderAccountGuid(String senderAccountGuid) {
        this.senderAccountGuid = senderAccountGuid;
    }

    public String getSenderAvatarUrl() {
        return senderAvatarUrl;
    }

    public void setSenderAvatarUrl(String senderAvatarUrl) {
        this.senderAvatarUrl = senderAvatarUrl;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "RelationRequestResponseEntity{" +
                "requestGuid='" + requestGuid + '\'' +
                ", senderAccountGuid='" + senderAccountGuid + '\'' +
                ", senderAvatarUrl='" + senderAvatarUrl + '\'' +
                ", senderName='" + senderName + '\'' +
                ", result=" + result +
                '}';
    }
}
