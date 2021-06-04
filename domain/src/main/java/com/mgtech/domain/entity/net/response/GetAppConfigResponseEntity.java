package com.mgtech.domain.entity.net.response;

import android.util.ArrayMap;

import com.google.gson.annotations.SerializedName;
import com.mgtech.domain.utils.MyConstant;
import com.mgtech.domain.utils.NetConstant;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhaixiang on 2017/9/6.
 * 获取后台APP配置信息中的数据
 */

public class GetAppConfigResponseEntity {
    /**
     * 配置内容
     * {
     "ShowEcg": 1,
     "IosInfo": {
     "DownloadUrl": "",
     "OpenUpdate": 1,
     "Version": "2.0.0",
     "VersionLog": ""
     },
     "AndroidInfo": {
     "DownloadUrl": "http://a.app.qq.com/o/simple.jsp?pkgname=com.mgtech.maiganapp",
     "OpenUpdate": 1,
     "Version": "21",
     "VersionLog": ""
     }
     }
     */

    @SerializedName(NetConstant.JSON_FIRMWARE_VERSION_INFO)
    private Map<String,DeviceInfo> firmwareVersionInfo;
    @SerializedName("appConfig")
    private AppConfig appConfig;

//    public AndroidInfo getAndroidInfo() {
//        return androidInfo;
//    }
//
//    public void setAndroidInfo(AndroidInfo androidInfo) {
//        this.androidInfo = androidInfo;
//    }

    public Map<String, DeviceInfo> getFirmwareVersionInfo() {
        return firmwareVersionInfo;
    }

    public void setFirmwareVersionInfo(Map<String, DeviceInfo> firmwareVersionInfo) {
        this.firmwareVersionInfo = firmwareVersionInfo;
    }

//    public int getShowEcg() {
//        return showEcg;
//    }
//
//    public void setShowEcg(int showEcg) {
//        this.showEcg = showEcg;
//    }


    public AppConfig getAppConfig() {
        return appConfig;
    }

    public void setAppConfig(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    @Override
    public String toString() {
        return "GetAppConfigResponseEntity{" +
                "firmwareVersionInfo=" + firmwareVersionInfo +
                ", appConfig=" + appConfig +
                '}';
    }

    public class AndroidInfo{
        @SerializedName(NetConstant.JSON_VERSION)
        int version;
        @SerializedName(NetConstant.JSON_OPEN_UPDATE)
        int openUpdate;
        @SerializedName(NetConstant.JSON_DOWNLOAD_URL)
        String url;
        @SerializedName(NetConstant.JSON_VERSION_LOG)
        String log;

        public int getVersion() {
            return version;
        }

        public void setVersion(int version) {
            this.version = version;
        }

        public int getOpenUpdate() {
            return openUpdate;
        }

        public void setOpenUpdate(int openUpdate) {
            this.openUpdate = openUpdate;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getLog() {
            return log;
        }

        public void setLog(String log) {
            this.log = log;
        }

        @Override
        public String toString() {
            return "AndroidInfo{" +
                    "version=" + version +
                    ", openUpdate=" + openUpdate +
                    ", url='" + url + '\'' +
                    ", log='" + log + '\'' +
                    '}';
        }
    }

    public class AppConfig{
        @SerializedName(NetConstant.JSON_ANDROID_INFO)
        private AndroidInfo androidInfo;
        @SerializedName("ShowEcg")
        private int showEcg;
        private String weekReportShareGuideUrl;

        public AndroidInfo getAndroidInfo() {
            return androidInfo;
        }

        public void setAndroidInfo(AndroidInfo androidInfo) {
            this.androidInfo = androidInfo;
        }

        public int getShowEcg() {
            return showEcg;
        }

        public void setShowEcg(int showEcg) {
            this.showEcg = showEcg;
        }

        public String getWeekReportShareGuideUrl() {
            return weekReportShareGuideUrl;
        }

        public void setWeekReportShareGuideUrl(String weekReportShareGuideUrl) {
            this.weekReportShareGuideUrl = weekReportShareGuideUrl;
        }

        @Override
        public String toString() {
            return "AppConfig{" +
                    "androidInfo=" + androidInfo +
                    ", showEcg=" + showEcg +
                    ", weekReportShareGuideUrl='" + weekReportShareGuideUrl + '\'' +
                    '}';
        }
    }

    public class DeviceInfo{
        @SerializedName(MyConstant.DEVICE_SUB_FIRMWARE_RELEASE)
        private SubVersion release;
        @SerializedName(MyConstant.DEVICE_SUB_FIRMWARE_DEBUG)
        private SubVersion debug;

        public SubVersion getRelease() {
            return release;
        }

        public void setRelease(SubVersion release) {
            this.release = release;
        }

        public SubVersion getDebug() {
            return debug;
        }

        public void setDebug(SubVersion debug) {
            this.debug = debug;
        }

        @Override
        public String toString() {
            return "DeviceInfo{" +
                    "app=" + release +
                    ", subVersion2=" + debug +
                    '}';
        }
    }

    public class SubVersion{
        @SerializedName(NetConstant.JSON_VERSION)
        private String version;
        @SerializedName(NetConstant.JSON_VERSION_LOG)
        private String log;
        @SerializedName(NetConstant.JSON_DOWNLOAD_URL)
        private String url;
        @SerializedName(NetConstant.JSON_OPEN_UPDATE)
        private int openUpdate;

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getLog() {
            return log;
        }

        public void setLog(String log) {
            this.log = log;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getOpenUpdate() {
            return openUpdate;
        }

        public void setOpenUpdate(int openUpdate) {
            this.openUpdate = openUpdate;
        }

        @Override
        public String toString() {
            return "SubVersion{" +
                    "version='" + version + '\'' +
                    ", log='" + log + '\'' +
                    ", url='" + url + '\'' +
                    ", openUpdate='" + openUpdate + '\'' +
                    '}';
        }
    }

}
