package com.mgtech.domain.entity.net.request;

import com.google.gson.annotations.SerializedName;

public class GetCustomMedicineRequestEntity {
    @SerializedName("accountGuid")
    private String userId;
    private int page;
    private int pageSize;

    public GetCustomMedicineRequestEntity(String userId, int page, int pageSize) {
        this.userId = userId;
        this.page = page;
        this.pageSize = pageSize;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return "GetCustomMedicineRequestEntity{" +
                "userId='" + userId + '\'' +
                ", page=" + page +
                ", pageSize=" + pageSize +
                '}';
    }
}
