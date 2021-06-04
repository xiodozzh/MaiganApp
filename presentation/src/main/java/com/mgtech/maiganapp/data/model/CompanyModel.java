package com.mgtech.maiganapp.data.model;

import java.util.List;

/**
 * @author Jesse
 */
public class CompanyModel {
    public String id;
    public String name;
    public String title = "";
    public String subTitle = "";
    public String imageUrl;
    public String pageUrl;
    public List<ServiceModel> serviceModels;
    public ServiceModel associatedServiceModel;

    @Override
    public String toString() {
        return "CompanyModel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", subTitle='" + subTitle + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", pageUrl='" + pageUrl + '\'' +
                ", serviceModels=" + serviceModels +
                '}';
    }
}
