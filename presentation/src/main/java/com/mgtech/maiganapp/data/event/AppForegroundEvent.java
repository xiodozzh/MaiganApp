package com.mgtech.maiganapp.data.event;

/**
 * Created by zhaixiang on 2018/4/3.
 * app 进入前台后台事件
 */

public class AppForegroundEvent {
    private boolean foreground;

    public AppForegroundEvent(boolean foreground) {
        this.foreground = foreground;
    }

    public boolean isForeground() {
        return foreground;
    }

    @Override
    public String toString() {
        return "AppForegroundEvent{" +
                "foreground=" + foreground +
                '}';
    }
}
