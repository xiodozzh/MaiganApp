package com.mgtech.domain.entity.net.request;

import com.google.gson.Gson;
import com.mgtech.domain.entity.net.RequestEntity;
import com.mgtech.domain.utils.HttpApi;

import java.util.Arrays;

/**
 *
 * @author zhaixiang
 * 保存ECG
 */

public class SaveEcgRequestEntity implements RequestEntity{


    /**
     * deviceInfo : {"macAddress":"","protocolVersion":"","firmwareVersion":""}
     * accountGuid :
     * measureTime : 1541477687272
     * rawData : [90,1234,3214]
     */

    private DeviceInfoBean deviceInfo;
    private String accountGuid;
    private float sampleRate;
    private long measureTime;
    private double[] rawData;

    public SaveEcgRequestEntity(DeviceInfoBean deviceInfo, String accountGuid, long measureTime, double[] rawData,float sampleRate) {
        this.deviceInfo = deviceInfo;
        this.accountGuid = accountGuid;
        this.measureTime = measureTime;
        this.sampleRate = sampleRate;
        this.rawData = rawData;
    }

    @Override
    public String getUrl() {
        return HttpApi.API_SAVE_ECG;
    }

    @Override
    public String getBody() {
        return new Gson().toJson(this);
    }

    public DeviceInfoBean getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(DeviceInfoBean deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public String getAccountGuid() {
        return accountGuid;
    }

    public void setAccountGuid(String accountGuid) {
        this.accountGuid = accountGuid;
    }

    public long getMeasureTime() {
        return measureTime;
    }

    public void setMeasureTime(long measureTime) {
        this.measureTime = measureTime;
    }

    public double[] getRawData() {
        return rawData;
    }

    public void setRawData(double[] rawData) {
        this.rawData = rawData;
    }

    public float getSampleRate() {
        return sampleRate;
    }

    public void setSampleRate(float sampleRate) {
        this.sampleRate = sampleRate;
    }

    public static class DeviceInfoBean {
        /**
         * macAddress :
         * protocolVersion :
         * firmwareVersion :
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

        @Override
        public String toString() {
            return "DeviceInfoBean{" +
                    "macAddress='" + macAddress + '\'' +
                    ", protocolVersion='" + protocolVersion + '\'' +
                    ", firmwareVersion='" + firmwareVersion + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "SaveEcgRequestEntity{" +
                "deviceInfo=" + deviceInfo +
                ", accountGuid='" + accountGuid + '\'' +
                ", measureTime=" + measureTime +
                ", rawData=" + Arrays.toString(rawData) +
                '}';
    }
}
