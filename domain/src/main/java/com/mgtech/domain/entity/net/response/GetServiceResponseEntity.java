package com.mgtech.domain.entity.net.response;

import java.util.List;

public class GetServiceResponseEntity {

    /**
     * guid : 70343a11-b1c0-4a19-85ed-c705fd471fee
     * serviceCode : 80343a11-b1c0-4a19-85ed-123
     * name : 急救服务
     * remark : XXXXXXXXXXXX
     * companies : [{"guid":"35d1a376-6e24-4dee-8e22-1ae765af1bc7","name":"金野","pageUrl":"www.jinye.com/jijiu.html","activationStatus":1}]
     */

    private String guid;
    private String serviceCode;
    private String name;
    private String imageUrl;
    private String remark;
    private List<CompaniesBean> companies;

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<CompaniesBean> getCompanies() {
        return companies;
    }

    public void setCompanies(List<CompaniesBean> companies) {
        this.companies = companies;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "GetServiceResponseEntity{" +
                "guid='" + guid + '\'' +
                ", serviceCode='" + serviceCode + '\'' +
                ", name='" + name + '\'' +
                ", remark='" + remark + '\'' +
                ", companies=" + companies +
                '}';
    }

    public static class CompaniesBean {
        /**
         * guid : 35d1a376-6e24-4dee-8e22-1ae765af1bc7
         * name : 金野
         * pageUrl : www.jinye.com/jijiu.html
         * activationStatus : 1
         */

        private String guid;
        private String name;
        private String pageUrl;
        private int activationStatus;

        public String getGuid() {
            return guid;
        }

        public void setGuid(String guid) {
            this.guid = guid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPageUrl() {
            return pageUrl;
        }

        public void setPageUrl(String pageUrl) {
            this.pageUrl = pageUrl;
        }

        public int getActivationStatus() {
            return activationStatus;
        }

        public void setActivationStatus(int activationStatus) {
            this.activationStatus = activationStatus;
        }

        @Override
        public String toString() {
            return "CompaniesBean{" +
                    "guid='" + guid + '\'' +
                    ", name='" + name + '\'' +
                    ", pageUrl='" + pageUrl + '\'' +
                    ", activationStatus=" + activationStatus +
                    '}';
        }
    }
}
