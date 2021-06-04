package com.mgtech.domain.entity.net.response;

/**
 * @author zhaixiang
 */
public class HealthKnowledgeResponseEntity {

    /**
     * healthKnowledgeGuid :
     * title :
     * subTitle :
     * imageUrl :
     * detailUrl :
     * date : 12124332435
     */

    private String healthKnowledgeGuid;
    private String title;
    private String subTitle;
    private String imageUrl;
    private String detailUrl;
    private long date;
    private int isRead= 1;

    public String getHealthKnowledgeGuid() {
        return healthKnowledgeGuid;
    }

    public void setHealthKnowledgeGuid(String healthKnowledgeGuid) {
        this.healthKnowledgeGuid = healthKnowledgeGuid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    public int getIsRead() {
        return isRead;
    }

    @Override
    public String toString() {
        return "HealthKnowledgeResponseEntity{" +
                "healthKnowledgeGuid='" + healthKnowledgeGuid + '\'' +
                ", title='" + title + '\'' +
                ", subTitle='" + subTitle + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", detailUrl='" + detailUrl + '\'' +
                ", date=" + date +
                ", isRead=" + isRead +
                '}';
    }
}
