package com.mgtech.domain.entity.net.response;

public class GetAllUnreadMessageResponseEntity {

    /**
     * pwAbnormalDataUnread : 33
     * pwDataUnread : 12
     * healthKnowledgeUnread : 2
     * friendRequestUnread : 5
     * weekReportUnread : 1
     */

    private int pwAbnormalDataUnread;
    private int pwDataUnread;
    private int healthKnowledgeUnread;
    private int friendRequestUnread;
    private int weekReportUnread;

    public int getPwAbnormalDataUnread() {
        return pwAbnormalDataUnread;
    }

    public void setPwAbnormalDataUnread(int pwAbnormalDataUnread) {
        this.pwAbnormalDataUnread = pwAbnormalDataUnread;
    }

    public int getPwDataUnread() {
        return pwDataUnread;
    }

    public void setPwDataUnread(int pwDataUnread) {
        this.pwDataUnread = pwDataUnread;
    }

    public int getHealthKnowledgeUnread() {
        return healthKnowledgeUnread;
    }

    public void setHealthKnowledgeUnread(int healthKnowledgeUnread) {
        this.healthKnowledgeUnread = healthKnowledgeUnread;
    }

    public int getFriendRequestUnread() {
        return friendRequestUnread;
    }

    public void setFriendRequestUnread(int friendRequestUnread) {
        this.friendRequestUnread = friendRequestUnread;
    }

    public int getWeekReportUnread() {
        return weekReportUnread;
    }

    public void setWeekReportUnread(int weekReportUnread) {
        this.weekReportUnread = weekReportUnread;
    }

    @Override
    public String toString() {
        return "GetAllUnreadMessageResponseEntity{" +
                "pwAbnormalDataUnread=" + pwAbnormalDataUnread +
                ", pwDataUnread=" + pwDataUnread +
                ", healthKnowledgeUnread=" + healthKnowledgeUnread +
                ", friendRequestUnread=" + friendRequestUnread +
                ", weekReportUnread=" + weekReportUnread +
                '}';
    }
}
