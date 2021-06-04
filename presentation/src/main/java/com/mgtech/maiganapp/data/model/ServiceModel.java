package com.mgtech.maiganapp.data.model;

import java.util.List;

/**
 * @author Jesse
 */
public class ServiceModel {
    public String id;
    public String serviceCode;
    public String name;
    public String remark;
    public String iconUrl;
    public String pageUrl;
    public String title;
    public String subTitle;
    public String imageUrl;
    public List<CompanyModel> companies;

    @Override
    public String toString() {
        return "ServiceModel{" +
                "id='" + id + '\'' +
                ", serviceCode='" + serviceCode + '\'' +
                ", name='" + name + '\'' +
                ", remark='" + remark + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                ", pageUrl='" + pageUrl + '\'' +
                ", title='" + title + '\'' +
                ", subTitle='" + subTitle + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", companies=" + companies +
                '}';
    }
}
