package com.mgtech.data;

import java.util.List;

public class TestData {

    /**
     * age : 80
     * height : 199
     * id : 6
     * name : 大数据
     * sex : 1
     * transcribeTime : 1564536274000
     * userGuid : b607e2d7-b968-43c9-a23c-bab36002f3ae
     * weight : 200
     * userMeasureInfo : [{"drugTime":1964536274000,"fileId":"group1/M00/04/10/XXX.PW","measureTime":1964536274000,"other":"二手烟","pd":22,"ps":222},{"drugTime":1564536274000,"fileId":"group1/M00/04/10/XXX.PW","measureTime":1564536274000,"other":"二手烟","pd":33,"ps":333}]
     */

    private int age;
    private int height;
    private int id;
    private String name;
    private int sex;
    private long transcribeTime;
    private String userGuid;
    private int weight;
    private List<UserMeasureInfoBean> userMeasureInfo;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public long getTranscribeTime() {
        return transcribeTime;
    }

    public void setTranscribeTime(long transcribeTime) {
        this.transcribeTime = transcribeTime;
    }

    public String getUserGuid() {
        return userGuid;
    }

    public void setUserGuid(String userGuid) {
        this.userGuid = userGuid;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public List<UserMeasureInfoBean> getUserMeasureInfo() {
        return userMeasureInfo;
    }

    public void setUserMeasureInfo(List<UserMeasureInfoBean> userMeasureInfo) {
        this.userMeasureInfo = userMeasureInfo;
    }

    public static class UserMeasureInfoBean {
        /**
         * drugTime : 1964536274000
         * fileId : group1/M00/04/10/XXX.PW
         * measureTime : 1964536274000
         * other : 二手烟
         * pd : 22
         * ps : 222
         */

        private long drugTime;
        private String fileId;
        private long measureTime;
        private String other;
        private int pd;
        private int ps;

        public long getDrugTime() {
            return drugTime;
        }

        public void setDrugTime(long drugTime) {
            this.drugTime = drugTime;
        }

        public String getFileId() {
            return fileId;
        }

        public void setFileId(String fileId) {
            this.fileId = fileId;
        }

        public long getMeasureTime() {
            return measureTime;
        }

        public void setMeasureTime(long measureTime) {
            this.measureTime = measureTime;
        }

        public String getOther() {
            return other;
        }

        public void setOther(String other) {
            this.other = other;
        }

        public int getPd() {
            return pd;
        }

        public void setPd(int pd) {
            this.pd = pd;
        }

        public int getPs() {
            return ps;
        }

        public void setPs(int ps) {
            this.ps = ps;
        }
    }
}
