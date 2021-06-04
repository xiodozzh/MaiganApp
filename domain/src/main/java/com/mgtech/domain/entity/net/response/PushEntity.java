package com.mgtech.domain.entity.net.response;

/**
 * @author zhaixiang
 */
public class PushEntity {

    /**
     * type : 0
     * data : {"contentGuid":"","userGuid":""}
     */

    private int type;
    private String data;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * contentGuid :
         * contentLink :
         * shareTitle :
         * shareSubTitle :
         * shareImageUrl :
         * userGuid :
         */
        private String contentGuid;
        private String contentLink;
        private String shareTitle;
        private String shareSubTitle;
        private String shareImageUrl;
        private String userGuid;

        public String getContentGuid() {
            return contentGuid;
        }

        public void setContentGuid(String contentGuid) {
            this.contentGuid = contentGuid;
        }

        public String getContentLink() {
            return contentLink;
        }

        public void setContentLink(String contentLink) {
            this.contentLink = contentLink;
        }

        public String getShareTitle() {
            return shareTitle;
        }

        public void setShareTitle(String shareTitle) {
            this.shareTitle = shareTitle;
        }

        public String getShareSubTitle() {
            return shareSubTitle;
        }

        public void setShareSubTitle(String shareSubTitle) {
            this.shareSubTitle = shareSubTitle;
        }

        public String getShareImageUrl() {
            return shareImageUrl;
        }

        public void setShareImageUrl(String shareImageUrl) {
            this.shareImageUrl = shareImageUrl;
        }

        public String getUserGuid() {
            return userGuid;
        }

        public void setUserGuid(String userGuid) {
            this.userGuid = userGuid;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "contentGuid='" + contentGuid + '\'' +
                    ", contentLink='" + contentLink + '\'' +
                    ", shareTitle='" + shareTitle + '\'' +
                    ", shareSubTitle='" + shareSubTitle + '\'' +
                    ", shareImageUrl='" + shareImageUrl + '\'' +
                    ", userGuid='" + userGuid + '\'' +
                    '}';
        }
    }
}
