package com.mgtech.domain.entity.net.request;

public class SetCustomerContactPermissionRequestEntity {
    public static final int ALLOW = 1;
    public static final int DENY = 0;

    /**
     * accountGuid :
     * allowByWechat : 1
     * allowByPhone : 1
     */

    private String accountGuid;
    private int allowByWechat;
    private int allowByPhone;

    public String getAccountGuid() {
        return accountGuid;
    }

    public void setAccountGuid(String accountGuid) {
        this.accountGuid = accountGuid;
    }

    public int getAllowByWechat() {
        return allowByWechat;
    }

    public void setAllowByWechat(int allowByWechat) {
        this.allowByWechat = allowByWechat;
    }

    public int getAllowByPhone() {
        return allowByPhone;
    }

    public void setAllowByPhone(int allowByPhone) {
        this.allowByPhone = allowByPhone;
    }

    @Override
    public String toString() {
        return "SetCustomerContactPermissionRequestEntity{" +
                "accountGuid='" + accountGuid + '\'' +
                ", allowByWechat=" + allowByWechat +
                ", allowByPhone=" + allowByPhone +
                '}';
    }
}
