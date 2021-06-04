package com.mgtech.domain.entity.net.request;

public class MarkWeeklyReportReadRequestEntity {
    private String userId;
    private String itemId;

    public MarkWeeklyReportReadRequestEntity(String userId, String itemId) {
        this.userId = userId;
        this.itemId = itemId;
    }

    public String getUserId() {
        return userId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
}
