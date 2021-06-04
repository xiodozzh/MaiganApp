package com.mgtech.maiganapp.data.model;

import java.util.Objects;

/**
 * @author zhaixiang
 */
public class FriendRequestModel {
    public static final int RESULT_DENY  = 0;
    public static final int RESULT_ACCEPT = 1;
    public static final int RESULT_NONE  = 2;

    public String id;
    public String targetUserId;
    public String message;
    public String avatarUri;
    public String name;
    public int result;

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        FriendRequestModel that = (FriendRequestModel) o;
        return result == that.result &&
                Objects.equals(id, that.id) &&
                Objects.equals(targetUserId, that.targetUserId) &&
                Objects.equals(message, that.message) &&
                Objects.equals(avatarUri, that.avatarUri) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, targetUserId, message, avatarUri, name, result);
    }

    @Override
    public String toString() {
        return "FriendRequestModel{" +
                "id='" + id + '\'' +
                ", targetUserId='" + targetUserId + '\'' +
                ", message='" + message + '\'' +
                ", avatarUri='" + avatarUri + '\'' +
                ", name='" + name + '\'' +
                ", result=" + result +
                '}';
    }
}
