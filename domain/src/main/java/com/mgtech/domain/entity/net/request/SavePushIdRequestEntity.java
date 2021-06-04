package com.mgtech.domain.entity.net.request;

import com.google.gson.annotations.SerializedName;

public class SavePushIdRequestEntity {
    @SerializedName("accountGuid")
    private String userId;
    @SerializedName("registrationId")
    private String pushRegisterId;

    public SavePushIdRequestEntity(String userId, String pushRegisterId) {
        this.userId = userId;
        this.pushRegisterId = pushRegisterId;
    }

    public String getUserId() {
        return userId;
    }

    public String getPushRegisterId() {
        return pushRegisterId;
    }

    @Override
    public String toString() {
        return "SavePushIdRequestEntity{" +
                "userId='" + userId + '\'' +
                ", pushRegisterId='" + pushRegisterId + '\'' +
                '}';
    }
}
