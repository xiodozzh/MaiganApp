package com.mgtech.maiganapp.data.model;

public class SearchPermissionsModel {
    public boolean forbidFindByPhone;

    public SearchPermissionsModel(boolean forbidFindByPhone) {
        this.forbidFindByPhone = forbidFindByPhone;
    }

    public SearchPermissionsModel() {
    }

    @Override
    public String toString() {
        return "SearchPermissionsModel{" +
                "forbidFindByPhone=" + forbidFindByPhone +
                '}';
    }
}
