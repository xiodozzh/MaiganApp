package com.mgtech.domain.entity.net.response;

import com.google.gson.annotations.SerializedName;

/**
 *
 * @author zhaixiang
 */

public class CheckPhoneLoginResponseEntity{

    /**
     * accountGuid :
     * displayName : 张三
     * phone : 13117898653
     * zone : 86
     * relation : 1
     */
    private String accountGuid;
    private String displayName;
    private String phone;
    private String zone;
    private int relation;
    private String contractName;

    public String getAccountGuid() {
        return accountGuid;
    }

    public void setAccountGuid(String accountGuid) {
        this.accountGuid = accountGuid;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public int getRelation() {
        return relation;
    }

    public void setRelation(int relation) {
        this.relation = relation;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    @Override
    public String toString() {
        return "CheckPhoneLoginResponseEntity{" +
                "accountGuid='" + accountGuid + '\'' +
                ", displayName='" + displayName + '\'' +
                ", phone='" + phone + '\'' +
                ", zone='" + zone + '\'' +
                ", relation=" + relation +
                ", contractName='" + contractName + '\'' +
                '}';
    }
}
