package com.mgtech.maiganapp.data.event;

/**
 * @author zhaixiang
 */
public class NotificationPageEvent {
    public static final int MEASURE_PW_ACTIVITY = 432;
    public static final int MEASURE_ECG_ACTIVITY = 424;
    public static final int MAIN_ACTIVITY = 642;

    private int pageId;

    public NotificationPageEvent(int pageId) {
        this.pageId = pageId;
    }

    public int getPageId() {
        return pageId;
    }
}
