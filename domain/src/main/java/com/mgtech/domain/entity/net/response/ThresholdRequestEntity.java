package com.mgtech.domain.entity.net.response;

import com.google.gson.Gson;
import com.mgtech.domain.entity.net.RequestEntity;
import com.mgtech.domain.utils.HttpApi;

/**
 * Created by zhaixiang on 2017/8/28.
 * 获取阈值返回
 */

public class ThresholdRequestEntity implements RequestEntity {


    /**
     * accountGuid :
     * openPush : 1
     * psThreshold : 0,140
     * pdThreshold : 0,90
     * hrThreshold : 0,110
     * v0Threshold : 0,110
     * openPushPS : 0
     * openPushPD : 1
     * openPushHR : 1
     * openPushV0 : 1
     */

    private String accountGuid;
    private int openPush;
    private String psThreshold;
    private String pdThreshold;
    private String hrThreshold;
    private String v0Threshold;
    private int openPushPS;
    private int openPushPD;
    private int openPushHR;
    private int openPushV0;

    @Override
    public String getUrl() {
        return HttpApi.API_GET_EXCEPTION_THRESHOLD;
    }

    @Override
    public String getBody() {
        return new Gson().toJson(this);
    }

    public String getAccountGuid() {
        return accountGuid;
    }

    public void setAccountGuid(String accountGuid) {
        this.accountGuid = accountGuid;
    }

    public int getOpenPush() {
        return openPush;
    }

    public void setOpenPush(int openPush) {
        this.openPush = openPush;
    }

    public String getPsThreshold() {
        return psThreshold;
    }

    public void setPsThreshold(String psThreshold) {
        this.psThreshold = psThreshold;
    }

    public String getPdThreshold() {
        return pdThreshold;
    }

    public void setPdThreshold(String pdThreshold) {
        this.pdThreshold = pdThreshold;
    }

    public String getHrThreshold() {
        return hrThreshold;
    }

    public void setHrThreshold(String hrThreshold) {
        this.hrThreshold = hrThreshold;
    }

    public String getV0Threshold() {
        return v0Threshold;
    }

    public void setV0Threshold(String v0Threshold) {
        this.v0Threshold = v0Threshold;
    }

    public int getOpenPushPS() {
        return openPushPS;
    }

    public void setOpenPushPS(int openPushPS) {
        this.openPushPS = openPushPS;
    }

    public int getOpenPushPD() {
        return openPushPD;
    }

    public void setOpenPushPD(int openPushPD) {
        this.openPushPD = openPushPD;
    }

    public int getOpenPushHR() {
        return openPushHR;
    }

    public void setOpenPushHR(int openPushHR) {
        this.openPushHR = openPushHR;
    }

    public int getOpenPushV0() {
        return openPushV0;
    }

    public void setOpenPushV0(int openPushV0) {
        this.openPushV0 = openPushV0;
    }

    @Override
    public String toString() {
        return "ThresholdRequestEntity{" +
                "accountGuid='" + accountGuid + '\'' +
                ", openPush=" + openPush +
                ", psThreshold='" + psThreshold + '\'' +
                ", pdThreshold='" + pdThreshold + '\'' +
                ", hrThreshold='" + hrThreshold + '\'' +
                ", v0Threshold='" + v0Threshold + '\'' +
                ", openPushPS=" + openPushPS +
                ", openPushPD=" + openPushPD +
                ", openPushHR=" + openPushHR +
                ", openPushV0=" + openPushV0 +
                '}';
    }
}
