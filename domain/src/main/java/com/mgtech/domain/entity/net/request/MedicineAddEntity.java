package com.mgtech.domain.entity.net.request;

import com.google.gson.annotations.SerializedName;

public class MedicineAddEntity {
    @SerializedName("accountGuid")
    private String userId;
    private String name;
    private String memo;

    public MedicineAddEntity(String userId, String name, String memo) {
        this.userId = userId;
        this.name = name;
        this.memo = memo;
    }

    public MedicineAddEntity(String userId, String name) {
        this.userId = userId;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    @Override
    public String toString() {
        return "MedicineAddEntity{" +
                "userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", memo='" + memo + '\'' +
                '}';
    }
}
