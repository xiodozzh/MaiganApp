package com.mgtech.domain.entity.net.request;

import com.google.gson.Gson;
import com.mgtech.domain.entity.net.RequestEntity;
import com.mgtech.domain.utils.HttpApi;

public class SetMedicationRemindRequestEntity implements RequestEntity {
    public static final int IGNORE = 2;
    public static final int COMPLETE = 1;
    public static final int NONE = 0;

    private String planGuid;
    private String remindGuid;
    private String userId;
    private int state;

    public SetMedicationRemindRequestEntity(String userId,String planGuid, String remindGuid, int state) {
        this.userId = userId;
        this.planGuid = planGuid;
        this.remindGuid = remindGuid;
        this.state = state;
    }

    public String getPlanGuid() {
        return planGuid;
    }

    public String getRemindGuid() {
        return remindGuid;
    }

    public int getState() {
        return state;
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public String getUrl() {
        return HttpApi.API_RECORD_MEDICATION_REMIND;
    }

    @Override
    public String getBody() {
        return new Gson().toJson(this);
    }
}
