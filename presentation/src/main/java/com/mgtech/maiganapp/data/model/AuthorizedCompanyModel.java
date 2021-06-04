package com.mgtech.maiganapp.data.model;

/**
 * @author Jesse
 */
public class AuthorizedCompanyModel {
    public String id;
    public String name;
    public String title;
    public String subtitle;
    public String imgUrl;
    public String pageUrl;

    @Override
    public String toString() {
        return "AuthorizedCompanyModel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", subtitle='" + subtitle + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", pageUrl='" + pageUrl + '\'' +
                '}';
    }
}
