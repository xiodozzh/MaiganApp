package com.mgtech.blelib.entity;

/**
 * Created by zhaixiang on 2018/4/12.
 * 手机系统状态
 */

public class PhoneEvent {
    private int status;

    public PhoneEvent(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "PhoneEvent{" +
                "status=" + status +
                '}';
    }
}
