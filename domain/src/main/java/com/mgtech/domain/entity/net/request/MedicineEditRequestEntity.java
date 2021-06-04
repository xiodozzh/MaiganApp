package com.mgtech.domain.entity.net.request;

import com.google.gson.annotations.SerializedName;

public class MedicineEditRequestEntity {
    @SerializedName("accountGuid")
    private String userId;
    private String name;
    @SerializedName("drugGuid")
    private String guid;
    private String memo;

    public MedicineEditRequestEntity(String userId,String name, String guid) {
        this.userId = userId;
        this.name = name;
        this.guid = guid;
    }

    public MedicineEditRequestEntity(String userId,String name, String guid, String memo) {
        this.userId = userId;
        this.name = name;
        this.guid = guid;
        this.memo = memo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    @Override
    public String toString() {
        return "MedicineEditRequestEntity{" +
                "name='" + name + '\'' +
                ", guid='" + guid + '\'' +
                ", memo='" + memo + '\'' +
                '}';
    }
}
