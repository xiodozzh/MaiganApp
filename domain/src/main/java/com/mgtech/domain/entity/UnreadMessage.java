package com.mgtech.domain.entity;

import com.mgtech.domain.entity.net.response.GetAllUnreadMessageResponseEntity;
import com.tencent.mmkv.MMKV;

public class UnreadMessage {
    private static final String MMKV_ID = "unread";
    private static final String ABNORMAL_DATA = "pwAbnormalDataUnread";
    private static final String NORMAL_DATA = "pwDataUnread";
    private static final String HEALTH_KNOWLEDGE = "healthKnowledgeUnread";
    private static final String FRIEND_REQUEST = "friendRequestUnread";
    private static final String WEEK_REPORT = "weekReport";

    public static void save(GetAllUnreadMessageResponseEntity entity){
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        mmkv.encode(ABNORMAL_DATA,entity.getPwAbnormalDataUnread());
        mmkv.encode(NORMAL_DATA,entity.getPwDataUnread());
        mmkv.encode(HEALTH_KNOWLEDGE,entity.getHealthKnowledgeUnread());
        mmkv.encode(FRIEND_REQUEST,entity.getFriendRequestUnread());
        mmkv.encode(WEEK_REPORT,entity.getWeekReportUnread());
    }

    public static int getAbnormalPwUnreadNumber(){
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        return mmkv.decodeInt(ABNORMAL_DATA);
    }

    public static int getNormalPwUnreadNumber(){
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        return mmkv.decodeInt(NORMAL_DATA);
    }

    public static int getHealthKnowledgeUnreadNumber(){
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        return mmkv.decodeInt(HEALTH_KNOWLEDGE);
    }

    public static int getFriendRequestUnreadNumber(){
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        return mmkv.decodeInt(FRIEND_REQUEST);
    }

    public static int getWeekReportUnreadNumber(){
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        return mmkv.decodeInt(WEEK_REPORT);
    }
}
