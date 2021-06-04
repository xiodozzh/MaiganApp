package com.mgtech.maiganapp.data.model;

import java.util.Objects;

public class HealthKnowledgeModel {
    public String knowledgeId;
    public String url;
    public String imgUrl;
    public String title;
    public String subTitle;
    public long time;
    public boolean isRead;

    @Override
    public String toString() {
        return "HealthKnowledgeModel{" +
                "knowledgeId='" + knowledgeId + '\'' +
                ", url='" + url + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", title='" + title + '\'' +
                ", subTitle='" + subTitle + '\'' +
                ", time=" + time +
                ", isRead=" + isRead +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HealthKnowledgeModel that = (HealthKnowledgeModel) o;
        return time == that.time &&
                Objects.equals(knowledgeId, that.knowledgeId) &&
                Objects.equals(url, that.url) &&
                Objects.equals(imgUrl, that.imgUrl) &&
                Objects.equals(title, that.title) &&
                isRead == that.isRead &&
                Objects.equals(subTitle, that.subTitle);
    }

    @Override
    public int hashCode() {

        return Objects.hash(knowledgeId, url, imgUrl, title, subTitle, time,isRead);
    }
}
