package com.mgtech.maiganapp.data.event;

import android.util.Log;

/**
 * @author zhaixiang
 */
public class WeChatAuthorEvent {
    private String code;

    public WeChatAuthorEvent(String code) {
        this.code = code;
        Log.i("WeChatAuthorEvent", code);
    }

    public String getCode() {
        return code;
    }
}
