package com.mgtech.domain.entity.socket;

/**
 *
 * @author zhaixiang
 * socket 推送信息
 */

public class SocketMsgEntity {


    /**
     * module : AccountManager
     * messageName : AccountManager/LoginOnOtherDevice
     * data : {"isLoginOnOtherDevice":1,"platform":"","phoneName":"","phoneModel":""}
     */

    private String module;
    private String messageName;
    private DataBean data;

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getMessageName() {
        return messageName;
    }

    public void setMessageName(String messageName) {
        this.messageName = messageName;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * isLoginOnOtherDevice : 1
         * platform :
         * phoneName :
         * phoneModel :
         */

        private int isLoginOnOtherDevice;
        private String platform;
        private String phoneName;
        private String phoneModel;

        public int getIsLoginOnOtherDevice() {
            return isLoginOnOtherDevice;
        }

        public void setIsLoginOnOtherDevice(int isLoginOnOtherDevice) {
            this.isLoginOnOtherDevice = isLoginOnOtherDevice;
        }

        public String getPlatform() {
            return platform;
        }

        public void setPlatform(String platform) {
            this.platform = platform;
        }

        public String getPhoneName() {
            return phoneName;
        }

        public void setPhoneName(String phoneName) {
            this.phoneName = phoneName;
        }

        public String getPhoneModel() {
            return phoneModel;
        }

        public void setPhoneModel(String phoneModel) {
            this.phoneModel = phoneModel;
        }
    }
}
