package com.mgtech.maiganapp.data.model;

public class CheckServiceAuthorModel {
    public boolean haveAuthor;
    public String authCode;

    @Override
    public String toString() {
        return "CheckServiceAuthorModel{" +
                "haveAuthor=" + haveAuthor +
                ", authCode='" + authCode + '\'' +
                '}';
    }
}
