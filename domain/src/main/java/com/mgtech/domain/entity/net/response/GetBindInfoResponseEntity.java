package com.mgtech.domain.entity.net.response;

/**
 *
 * @author zhaixiang
 * 获取绑定信息
 */

public class GetBindInfoResponseEntity {
    private long bindTime;
    private int type;
    private int pairingCode;
    private String encryptionKey;
    private String encryptionVector;
    private String macAddress;
    private String braceletName;
    private String firmwareVersion;

    public long getBindTime() {
        return bindTime;
    }

    public void setBindTime(long bindTime) {
        this.bindTime = bindTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPairingCode() {
        return pairingCode;
    }

    public void setPairingCode(int pairingCode) {
        this.pairingCode = pairingCode;
    }

    public String getEncryptionKey() {
        return encryptionKey;
    }

    public void setEncryptionKey(String encryptionKey) {
        this.encryptionKey = encryptionKey;
    }

    public String getEncryptionVector() {
        return encryptionVector;
    }

    public void setEncryptionVector(String encryptionVector) {
        this.encryptionVector = encryptionVector;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getBraceletName() {
        return braceletName;
    }

    public void setBraceletName(String braceletName) {
        this.braceletName = braceletName;
    }

    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    public void setFirmwareVersion(String firmwareVersion) {
        this.firmwareVersion = firmwareVersion;
    }

    @Override
    public String toString() {
        return "GetBindInfoResponseEntity{" +
                "bindTime=" + bindTime +
                ", type=" + type +
                ", pairingCode=" + pairingCode +
                ", encryptionKey='" + encryptionKey + '\'' +
                ", encryptionVector='" + encryptionVector + '\'' +
                ", macAddress='" + macAddress + '\'' +
                ", braceletName='" + braceletName + '\'' +
                ", firmwareVersion='" + firmwareVersion + '\'' +
                '}';
    }
}
