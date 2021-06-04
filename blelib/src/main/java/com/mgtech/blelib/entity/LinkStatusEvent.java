package com.mgtech.blelib.entity;

/**
 * Created by zhaixiang on 2018/3/8.
 * 连接状态
 */

public class LinkStatusEvent {
    private boolean link;

    public LinkStatusEvent(boolean link) {
        this.link = link;
    }

    public boolean isLink() {
        return link;
    }

    public void setLink(boolean link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return "LinkStatusData{" +
                "link=" + link +
                '}';
    }
}
