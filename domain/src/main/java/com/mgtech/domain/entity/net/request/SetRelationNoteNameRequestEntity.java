package com.mgtech.domain.entity.net.request;

import com.google.gson.Gson;
import com.mgtech.domain.entity.net.RequestEntity;
import com.mgtech.domain.utils.HttpApi;

/**
 * @author zhaixiang
 */
public class SetRelationNoteNameRequestEntity implements RequestEntity {
    public static final int MONITOR = 0;
    public static final int MONITORED = 1;

    /**
     * accountGuid :
     * targetAccountGuid :
     * type : 1
     * allowPushToOther : 1
     * allowOtherView : 1
     * whetherToViewData : 1
     * whetherToReceivePushData : 1
     * noteName :
     */

    private String accountGuid;
    private String targetAccountGuid;
    private int type;
    private int allowPushToOther;
    private int allowOtherView;
    private int whetherToViewData;
    private int whetherToReceivePushData;
    private String noteName;

    @Override
    public String getUrl() {
        return HttpApi.API_SET_RELATION_NOTE_NAME;
    }

    @Override
    public String getBody() {
        return new Gson().toJson(this);
    }

    public SetRelationNoteNameRequestEntity(String accountGuid, String targetAccountGuid, int type,
                                            int allowTargetPush, int allowTargetRead, int getHisPush,
                                            int readHisData, String noteName) {
        this.accountGuid = accountGuid;
        this.targetAccountGuid = targetAccountGuid;
        this.type = type;
        this.allowPushToOther = allowTargetPush;
        this.allowOtherView = allowTargetRead;
        this.whetherToViewData = readHisData;
        this.whetherToReceivePushData = getHisPush;
        this.noteName = noteName;
    }

    public String getAccountGuid() {
        return accountGuid;
    }

    public void setAccountGuid(String accountGuid) {
        this.accountGuid = accountGuid;
    }

    public String getTargetAccountGuid() {
        return targetAccountGuid;
    }

    public void setTargetAccountGuid(String targetAccountGuid) {
        this.targetAccountGuid = targetAccountGuid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getAllowPushToOther() {
        return allowPushToOther;
    }

    public void setAllowPushToOther(int allowPushToOther) {
        this.allowPushToOther = allowPushToOther;
    }

    public int getAllowOtherView() {
        return allowOtherView;
    }

    public void setAllowOtherView(int allowOtherView) {
        this.allowOtherView = allowOtherView;
    }

    public int getWhetherToViewData() {
        return whetherToViewData;
    }

    public void setWhetherToViewData(int whetherToViewData) {
        this.whetherToViewData = whetherToViewData;
    }

    public int getWhetherToReceivePushData() {
        return whetherToReceivePushData;
    }

    public void setWhetherToReceivePushData(int whetherToReceivePushData) {
        this.whetherToReceivePushData = whetherToReceivePushData;
    }

    public String getNoteName() {
        return noteName;
    }

    public void setNoteName(String noteName) {
        this.noteName = noteName;
    }
}
