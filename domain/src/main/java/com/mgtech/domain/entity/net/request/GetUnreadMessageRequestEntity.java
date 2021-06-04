package com.mgtech.domain.entity.net.request;

public class GetUnreadMessageRequestEntity {
    private String id;

    public GetUnreadMessageRequestEntity(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "GetUnreadMessageRequestEntity{" +
                "id='" + id + '\'' +
                '}';
    }
}
