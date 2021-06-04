package com.mgtech.domain.entity;

/**
 * Created by zhaixiang on 2018/4/2.
 * socket 监听器
 */

public interface SocketListener {
    /**
     * 开启
     */
    void onOpen();

    /**
     * 收到信息
     * @param msg 信息
     */
    void onMessage(String msg);

    /**
     * 关闭socket
     */
    void onClose();

    /**
     * 连接失败
     */
    void onFail();
}
