package com.mgtech.maiganapp.data.event;

/**
 *
 * @author zhaixiang
 * @date 2018/4/20
 * socket事件
 */

public class SocketEvent {
    private boolean link;

    public SocketEvent(boolean link) {
        this.link = link;
    }

    public boolean isLink() {
        return link;
    }

    public void setLink(boolean link) {
        this.link = link;
    }
}
