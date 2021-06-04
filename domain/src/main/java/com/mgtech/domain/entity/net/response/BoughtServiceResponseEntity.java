package com.mgtech.domain.entity.net.response;

import java.util.List;

/**
 * @author Jesse
 */
public class BoughtServiceResponseEntity {


    /**
     * serviceGuid : 70343a11-b1c0-4a19-85ed-c705fd471fee
     * serviceCode : 80343a11-b1c0-4a19-85ed-123
     * serviceName : 急救服务
     * iconUrl : 服务的icon链接
     * servicePageUrl : 公司提供的该服务的链接
     * promotionTitle : 公司提供的该服务的宣传标题
     * promotionSubtitle : 公司提供的该服务的副宣传标题
     * promotionImageUrl : 公司提供的该服务的宣传图链接
     * serviceCompanies : [{"companyGuid":"70343a11-b1c0-4a19-85ed-c705fd471fee","companyName":"某某科技有限公司","promotionTitle":"公司的宣传标题","promotionSubtitle":"公司的副宣传标题","promotionImageUrl":"","pageUrl":"www.jinye.com/jijiu.html","companyService":[{"serviceGuid":"70343a11-b1c0-4a19-85ed-c705fd471fee","serviceCode":"80343a11-b1c0-4a19-85ed-123","serviceName":"急救服务","iconUrl":"服务的icon链接","servicePageUrl":"公司提供的该服务的链接","promotionTitle":"公司提供的该服务的宣传标题","promotionSubtitle":"公司提供的该服务的副宣传标题","promotionImageUrl":"公司提供的该服务的宣传图链接"}]}]
     */

    private String serviceGuid;
    private String serviceCode;
    private String serviceName;
    private String iconUrl;
    private String servicePageUrl;
    private String promotionTitle;
    private String promotionSubtitle;
    private String promotionImageUrl;
    private List<ServiceCompaniesBean> serviceCompanies;

    public String getServiceGuid() {
        return serviceGuid;
    }

    public void setServiceGuid(String serviceGuid) {
        this.serviceGuid = serviceGuid;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getServicePageUrl() {
        return servicePageUrl;
    }

    public void setServicePageUrl(String servicePageUrl) {
        this.servicePageUrl = servicePageUrl;
    }

    public String getPromotionTitle() {
        return promotionTitle;
    }

    public void setPromotionTitle(String promotionTitle) {
        this.promotionTitle = promotionTitle;
    }

    public String getPromotionSubtitle() {
        return promotionSubtitle;
    }

    public void setPromotionSubtitle(String promotionSubtitle) {
        this.promotionSubtitle = promotionSubtitle;
    }

    public String getPromotionImageUrl() {
        return promotionImageUrl;
    }

    public void setPromotionImageUrl(String promotionImageUrl) {
        this.promotionImageUrl = promotionImageUrl;
    }

    public List<ServiceCompaniesBean> getServiceCompanies() {
        return serviceCompanies;
    }

    public void setServiceCompanies(List<ServiceCompaniesBean> serviceCompanies) {
        this.serviceCompanies = serviceCompanies;
    }

    public static class ServiceCompaniesBean {
        /**
         * companyGuid : 70343a11-b1c0-4a19-85ed-c705fd471fee
         * companyName : 某某科技有限公司
         * promotionTitle : 公司的宣传标题
         * promotionSubtitle : 公司的副宣传标题
         * promotionImageUrl :
         * pageUrl : www.jinye.com/jijiu.html
         * companyService : [{"serviceGuid":"70343a11-b1c0-4a19-85ed-c705fd471fee","serviceCode":"80343a11-b1c0-4a19-85ed-123","serviceName":"急救服务","iconUrl":"服务的icon链接","servicePageUrl":"公司提供的该服务的链接","promotionTitle":"公司提供的该服务的宣传标题","promotionSubtitle":"公司提供的该服务的副宣传标题","promotionImageUrl":"公司提供的该服务的宣传图链接"}]
         */

        private String companyGuid;
        private String companyName;
        private String promotionTitle;
        private String promotionSubtitle;
        private String promotionImageUrl;
        private String pageUrl;
        private List<CompanyServiceBean> companyService;

        public String getCompanyGuid() {
            return companyGuid;
        }

        public void setCompanyGuid(String companyGuid) {
            this.companyGuid = companyGuid;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getPromotionTitle() {
            return promotionTitle;
        }

        public void setPromotionTitle(String promotionTitle) {
            this.promotionTitle = promotionTitle;
        }

        public String getPromotionSubtitle() {
            return promotionSubtitle;
        }

        public void setPromotionSubtitle(String promotionSubtitle) {
            this.promotionSubtitle = promotionSubtitle;
        }

        public String getPromotionImageUrl() {
            return promotionImageUrl;
        }

        public void setPromotionImageUrl(String promotionImageUrl) {
            this.promotionImageUrl = promotionImageUrl;
        }

        public String getPageUrl() {
            return pageUrl;
        }

        public void setPageUrl(String pageUrl) {
            this.pageUrl = pageUrl;
        }

        public List<CompanyServiceBean> getCompanyService() {
            return companyService;
        }

        public void setCompanyService(List<CompanyServiceBean> companyService) {
            this.companyService = companyService;
        }


    }

    public static class CompanyServiceBean {
        /**
         * serviceGuid : 70343a11-b1c0-4a19-85ed-c705fd471fee
         * serviceCode : 80343a11-b1c0-4a19-85ed-123
         * serviceName : 急救服务
         * iconUrl : 服务的icon链接
         * servicePageUrl : 公司提供的该服务的链接
         * promotionTitle : 公司提供的该服务的宣传标题
         * promotionSubtitle : 公司提供的该服务的副宣传标题
         * promotionImageUrl : 公司提供的该服务的宣传图链接
         */

        private String serviceGuid;
        private String serviceCode;
        private String serviceName;
        private String iconUrl;
        private String servicePageUrl;
        private String promotionTitle;
        private String promotionSubtitle;
        private String promotionImageUrl;

        public String getServiceGuid() {
            return serviceGuid;
        }

        public void setServiceGuid(String serviceGuid) {
            this.serviceGuid = serviceGuid;
        }

        public String getServiceCode() {
            return serviceCode;
        }

        public void setServiceCode(String serviceCode) {
            this.serviceCode = serviceCode;
        }

        public String getServiceName() {
            return serviceName;
        }

        public void setServiceName(String serviceName) {
            this.serviceName = serviceName;
        }

        public String getIconUrl() {
            return iconUrl;
        }

        public void setIconUrl(String iconUrl) {
            this.iconUrl = iconUrl;
        }

        public String getServicePageUrl() {
            return servicePageUrl;
        }

        public void setServicePageUrl(String servicePageUrl) {
            this.servicePageUrl = servicePageUrl;
        }

        public String getPromotionTitle() {
            return promotionTitle;
        }

        public void setPromotionTitle(String promotionTitle) {
            this.promotionTitle = promotionTitle;
        }

        public String getPromotionSubtitle() {
            return promotionSubtitle;
        }

        public void setPromotionSubtitle(String promotionSubtitle) {
            this.promotionSubtitle = promotionSubtitle;
        }

        public String getPromotionImageUrl() {
            return promotionImageUrl;
        }

        public void setPromotionImageUrl(String promotionImageUrl) {
            this.promotionImageUrl = promotionImageUrl;
        }
    }
}
