package com.mgtech.domain.entity.net.request;

import android.os.Looper;

import com.google.gson.Gson;
import com.mgtech.domain.entity.net.RequestEntity;
import com.mgtech.domain.utils.HttpApi;
import com.mgtech.domain.utils.MyConstant;

/**
 *
 * @author zhaixiang
 * 保存并绑定手环设备信息
 */

public class BindDeviceRequestEntity implements RequestEntity {


    /**
     * type : 1
     * pairingCode : 23456
     * encryptionKey : 123456
     * encryptionVector : 78
     * macAddress : de:vd:ev:00:00:yy
     * braceletName : maigan888
     * firmwareVersion : 1.2.2
     */

    private int type;
    private int pairingCode;
    private String encryptionKey;
    private String encryptionVector;
    private String macAddress;
    private String braceletName;
    private String firmwareVersion;
    private String accountGuid;

    public BindDeviceRequestEntity(String id,int pairingCode, String encryptionKey,
                                   String encryptionVector, String macAddress, String braceletName,
                                   String firmwareVersion) {
        this.type = MyConstant.BRACELET_TYPE;
        this.accountGuid = id;
        this.pairingCode = pairingCode;
        this.encryptionKey = encryptionKey;
        this.encryptionVector = encryptionVector;
        this.macAddress = macAddress;
        this.braceletName = braceletName;
        this.firmwareVersion = firmwareVersion;
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

    public String getAccountGuid() {
        return accountGuid;
    }

    public void setAccountGuid(String accountGuid) {
        this.accountGuid = accountGuid;
    }

    @Override
    public String getUrl() {
        return HttpApi.API_BIND_BRACELET;
    }

    @Override
    public String getBody() {
        return new Gson().toJson(this);
    }

}
