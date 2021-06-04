package com.mgtech.domain.entity.net.response;

/**
 * Created by zhaixiang on 2017/1/9.
 * 网络返回类
 */

public abstract class ResponseEntity {
    private String flag;
    private String message;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "flag='" + flag + '\'' +
                ", message='" + message + '\'';
    }
}
