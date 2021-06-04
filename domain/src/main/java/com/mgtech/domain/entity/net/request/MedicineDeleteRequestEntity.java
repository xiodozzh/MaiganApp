package com.mgtech.domain.entity.net.request;

import com.google.gson.annotations.SerializedName;

public class MedicineDeleteRequestEntity {
    @SerializedName("drugGuid")
    private String guid;
    @SerializedName("accountGuid")
    private String userId;

    public MedicineDeleteRequestEntity(String userId,String guid) {
        this.userId = userId;
        this.guid = guid;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "MedicineDeleteRequestEntity{" +
                "guid='" + guid + '\'' +
                '}';
    }
}
