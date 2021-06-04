package com.mgtech.domain.entity.net.request;

public class GetCompanyAuthorCodeRequestEntity {
    private String userId;
    private String companyId;

    public GetCompanyAuthorCodeRequestEntity(String userId, String companyId) {
        this.userId = userId;
        this.companyId = companyId;
    }

    public String getUserId() {
        return userId;
    }

    public String getCompanyId() {
        return companyId;
    }
}
