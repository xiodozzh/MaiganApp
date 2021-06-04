package com.mgtech.blelib.entity;

/**
 * Created by zhaixiang on 2018/3/8.
 */

public class StatusDataEvent {
    private int status;

    public StatusDataEvent(int status) {
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
        return "StatusDataEvent{" +
                "status=" + status +
                '}';
    }
}
