package com.mgtech.maiganapp.data.event;

import android.util.Log;

/**
 *
 * @author zhaixiang
 * @date 2018/3/29
 * 登出事件（用户在其他设备上登录，收到此事件需情况登录信息，并返回登录页面）
 */

public class LogoutEvent {
    public LogoutEvent() {
        Log.i("LogoutEvent", "init logout ");
    }

    public LogoutEvent(String deviceName) {
        this.deviceName = deviceName;
    }

    private String deviceName;

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
}
