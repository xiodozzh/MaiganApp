package com.mgtech.domain.entity.net.request;

public class SetPwMarkRequestEntity {

    /**
     * accountGuid :
     * measureGuid :
     * remark : 刚运动结束
     */

    private String accountGuid;
    private String measureGuid;
    private String remark;

    public SetPwMarkRequestEntity(String accountGuid, String measureGuid, String remark) {
        this.accountGuid = accountGuid;
        this.measureGuid = measureGuid;
        this.remark = remark;
    }

    public String getAccountGuid() {
        return accountGuid;
    }

    public void setAccountGuid(String accountGuid) {
        this.accountGuid = accountGuid;
    }

    public String getMeasureGuid() {
        return measureGuid;
    }

    public void setMeasureGuid(String measureGuid) {
        this.measureGuid = measureGuid;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "SetPwMarkRequestEntity{" +
                "accountGuid='" + accountGuid + '\'' +
                ", measureGuid='" + measureGuid + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
