package com.mgtech.domain.entity.net.response;

import com.google.gson.annotations.SerializedName;

public class CustomMedicineEntity {

    /**
     * id : 12
     * guid : 45298687-9380-488c-951f-b1a76a0b7ba9
     * name : 盐酸哌唑嗪片
     * memo :
     * deleted : 0
     * accountGuid : 4de29a7d-a907-43dc-bf88-b3c343b64c0d
     * updatedAt : 123123
     * createdAt : 123123
     */

    private int id;
    @SerializedName("drugGuid")
    private String guid;
    private String name;
    private String memo;
    private int deleted;
    private String accountGuid;
    private long updatedAt;
    private long createdAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
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

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    public String getAccountGuid() {
        return accountGuid;
    }

    public void setAccountGuid(String accountGuid) {
        this.accountGuid = accountGuid;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(int updatedAt) {
        this.updatedAt = updatedAt;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(int createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "CustomMedicineEntity{" +
                "id=" + id +
                ", guid='" + guid + '\'' +
                ", name='" + name + '\'' +
                ", memo='" + memo + '\'' +
                ", deleted=" + deleted +
                ", accountGuid='" + accountGuid + '\'' +
                ", updatedAt=" + updatedAt +
                ", createdAt=" + createdAt +
                '}';
    }
}
