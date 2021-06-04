package com.mgtech.domain.entity.net.response;

public class SearchPermissionsResponseEntity {
    private int forbidFindByPhone;

    public int getForbidFindByPhone() {
        return forbidFindByPhone;
    }

    public void setForbidFindByPhone(int forbidFindByPhone) {
        this.forbidFindByPhone = forbidFindByPhone;
    }

    @Override
    public String toString() {
        return "SearchPermissionsResponseEntity{" +
                "forbidFindByPhone=" + forbidFindByPhone +
                '}';
    }
}
