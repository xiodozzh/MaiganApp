package com.mgtech.maiganapp.data.event;

/**
 * @author zhaixiang
 */
public class LoginOtherEvent {
    public static final int SUCCESS = 588;
    public static final int CANCEL = 843;
    public static final int FAIL = 900;

    public int status;
    public String openId;
    public String unionId;
    public String token;
    public String name;
    public String avatarUrl;
    public int type;

    public LoginOtherEvent(int status, String openId, String unionId, String token, String name, String avatarUrl,int type) {
        this.status = status;
        this.openId = openId;
        this.unionId = unionId;
        this.token = token;
        this.name = name;
        this.avatarUrl = avatarUrl;
        this.type = type;
    }

    public LoginOtherEvent(int status,int type) {
        this.status = status;
        this.type = type;
    }

    @Override
    public String toString() {
        return "LoginOtherEvent{" +
                "status=" + status +
                ", openId='" + openId + '\'' +
                ", unionId='" + unionId + '\'' +
                ", token='" + token + '\'' +
                ", name='" + name + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", type=" + type +
                '}';
    }
}
