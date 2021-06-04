package com.mgtech.maiganapp.data.model;

public class ServiceCompanyModel {
    public String id;
    public String name;
    public String url;
    public ServiceModel service;
    public boolean active;

    @Override
    public String toString() {
        return "ServiceCompanyModel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", active=" + active +
                '}';
    }
}
