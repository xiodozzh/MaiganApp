package com.mgtech.domain.entity.net.request;

/**
 * @author xiang
 */
public class SearchPermissionRequestEntity {

    /**
     * accountGuid :
     * forbidFindByPhone : 0
     */

    private String accountGuid;
    private int forbidFindByPhone;

    public String getAccountGuid() {
        return accountGuid;
    }

    public void setAccountGuid(String accountGuid) {
        this.accountGuid = accountGuid;
    }

    public int getForbidFindByPhone() {
        return forbidFindByPhone;
    }

    public void setForbidFindByPhone(int forbidFindByPhone) {
        this.forbidFindByPhone = forbidFindByPhone;
    }

    public SearchPermissionRequestEntity(String accountGuid, int forbidFindByPhone) {
        this.accountGuid = accountGuid;
        this.forbidFindByPhone = forbidFindByPhone;
    }

    @Override
    public String toString() {
        return "SearchPermissionRequestEntity{" +
                "accountGuid='" + accountGuid + '\'' +
                ", forbidFindByPhone=" + forbidFindByPhone +
                '}';
    }
}
