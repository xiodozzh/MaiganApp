package com.mgtech.domain.entity.net.request;

public class RemindFriendMeasureRequestEntity {

    /**
     * accountGuid :
     * friendGuid :
     */

    private String accountGuid;
    private String friendGuid;

    public RemindFriendMeasureRequestEntity(String accountGuid, String friendGuid) {
        this.accountGuid = accountGuid;
        this.friendGuid = friendGuid;
    }

    public String getAccountGuid() {
        return accountGuid;
    }

    public void setAccountGuid(String accountGuid) {
        this.accountGuid = accountGuid;
    }

    public String getFriendGuid() {
        return friendGuid;
    }

    public void setFriendGuid(String friendGuid) {
        this.friendGuid = friendGuid;
    }

    @Override
    public String toString() {
        return "RemindFriendMeasureRequestEntity{" +
                "accountGuid='" + accountGuid + '\'' +
                ", friendGuid='" + friendGuid + '\'' +
                '}';
    }
}
