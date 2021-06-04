package com.mgtech.domain.entity.net.request;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.mgtech.domain.entity.net.RequestEntity;
import com.mgtech.domain.utils.HttpApi;
import com.mgtech.domain.utils.MyConstant;
import com.mgtech.domain.utils.NetConstant;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhaixiang
 * 计算数据
 */

public class SaveRawDataRequestEntity implements RequestEntity {
    @SerializedName("userDeviceLog")
    private UserDeviceLogBean userDeviceLog;
    @SerializedName("isAsync")
    private int isAsync;
    @SerializedName("isAutomaticSampling")
    private int isAutomaticSampling;
    @SerializedName("measureTime")
    private long time;
    @SerializedName("rawData")
    private List<Object> data;
    @SerializedName("pwDataType")
    private int pwDataType;

    public SaveRawDataRequestEntity(boolean isAutoSample, long time, List<Object> data, int sex, int age, int height, int weight,
                                    String macAddress, String protocolVersion, String firmwareVersion) {
        this.isAsync = isAutoSample ? 1 : 0;
        this.isAutomaticSampling = isAutoSample ? 1 : 0;
        this.time = time;
        this.data = new ArrayList<>();
        this.data.add(1);
        this.data.add(0xFFFF);
        this.data.add(sex);
        this.data.add(age);
        this.data.add(height);
        this.data.add(weight);
        this.data.add(0xFFFF);
        this.data.addAll(data);
        this.userDeviceLog = new UserDeviceLogBean();
        this.userDeviceLog.macAddress = macAddress;
        this.userDeviceLog.protocolVersion = protocolVersion;
        this.userDeviceLog.firmwareVersion = firmwareVersion;
        this.pwDataType = MyConstant.GET_PW_DATA_TYPE;
    }

    @Override
    public String getUrl() {
        return HttpApi.API_SAVE_RAW_DATA;
    }

    @Override
    public String getBody() {
        return new Gson().toJson(this);
    }

    public UserDeviceLogBean getUserDeviceLog() {
        return userDeviceLog;
    }

    public void setUserDeviceLog(UserDeviceLogBean userDeviceLog) {
        this.userDeviceLog = userDeviceLog;
    }

    public int getIsAsync() {
        return isAsync;
    }

    public void setIsAsync(int isAsync) {
        this.isAsync = isAsync;
    }

    public int getIsAutomaticSampling() {
        return isAutomaticSampling;
    }

    public void setIsAutomaticSampling(int isAutomaticSampling) {
        this.isAutomaticSampling = isAutomaticSampling;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public List<Object> getData() {
        return data;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }

    public static class UserDeviceLogBean {
        /**
         * macAddress : 11:EB:1A:2B:CB
         * protocolVersion : 9.117.2
         * firmwareVersion : 6.127
         */

        private String macAddress;
        private String protocolVersion;
        private String firmwareVersion;

        public String getMacAddress() {
            return macAddress;
        }

        public void setMacAddress(String macAddress) {
            this.macAddress = macAddress;
        }

        public String getProtocolVersion() {
            return protocolVersion;
        }

        public void setProtocolVersion(String protocolVersion) {
            this.protocolVersion = protocolVersion;
        }

        public String getFirmwareVersion() {
            return firmwareVersion;
        }

        public void setFirmwareVersion(String firmwareVersion) {
            this.firmwareVersion = firmwareVersion;
        }
    }

}
