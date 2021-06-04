package com.mgtech.domain.entity.net.response;

public class GetAuthorizedCompanyResponseEntity {

    /**
     * companyGuid : 70343a11-b1c0-4a19-85ed-c705fd471fee
     * companyName : 某某科技有限公司
     * promotionTitle : 公司的宣传标题
     * promotionSubtitle : 公司的副宣传标题
     * promotionImageUrl :
     * pageUrl : www.jinye.com/jijiu.html
     */

    private String companyGuid;
    private String companyName;
    private String promotionTitle;
    private String promotionSubtitle;
    private String promotionImageUrl;
    private String pageUrl;

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

    @Override
    public String toString() {
        return "GetAuthorizedCompanyResponseEntity{" +
                "companyGuid='" + companyGuid + '\'' +
                ", companyName='" + companyName + '\'' +
                ", promotionTitle='" + promotionTitle + '\'' +
                ", promotionSubtitle='" + promotionSubtitle + '\'' +
                ", promotionImageUrl='" + promotionImageUrl + '\'' +
                ", pageUrl='" + pageUrl + '\'' +
                '}';
    }
}
