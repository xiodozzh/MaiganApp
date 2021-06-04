package com.mgtech.domain.entity.net.request;

import com.google.gson.Gson;
import com.mgtech.domain.entity.net.RequestEntity;
import com.mgtech.domain.utils.HttpApi;

public class SearchContactRequestEntity implements RequestEntity {
    private String userId;
    private String text;

    public SearchContactRequestEntity(String userId, String text) {
        this.userId = userId;
        this.text = text;
    }

    public String getUserId() {
        return userId;
    }

    public String getText() {
        return text;
    }

    @Override
    public String getUrl() {
        return HttpApi.API_SEARCH_CONTACT;
    }

    @Override
    public String getBody() {
        return new Gson().toJson(this);
    }
}
