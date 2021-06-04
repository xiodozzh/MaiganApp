package com.mgtech.domain.entity.net.response;

import com.google.gson.annotations.SerializedName;
import com.mgtech.domain.utils.NetConstant;

/**
 * Created by zhaixiang on 2017/3/28.
 * 获取固件
 */

public class GetFirmwareResponseEntity extends ResponseEntity {
    @SerializedName(NetConstant.JSON_VERSION)
    private String version;
    @SerializedName(NetConstant.JSON_IS_NEW)
    private String isNew;
    @SerializedName(NetConstant.JSON_FILE_NAME)
    private String fileName;
    @SerializedName(NetConstant.JSON_FILE_PATH)
    private String filePath;
    @SerializedName(NetConstant.JSON_FILE_NAME_SECOND)
    private String fileNameSecond;
    @SerializedName(NetConstant.JSON_FILE_PATH_SECOND)
    private String filePathSecond;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getIsNew() {
        return isNew;
    }

    public void setIsNew(String isNew) {
        this.isNew = isNew;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileNameSecond() {
        return fileNameSecond;
    }

    public void setFileNameSecond(String fileNameSecond) {
        this.fileNameSecond = fileNameSecond;
    }

    public String getFilePathSecond() {
        return filePathSecond;
    }

    public void setFilePathSecond(String filePathSecond) {
        this.filePathSecond = filePathSecond;
    }

    @Override
    public String toString() {
        return "GetFirmwareResponseEntity{" +
                "version='" + version + '\'' +
                ", isNew='" + isNew + '\'' +
                ", fileName='" + fileName + '\'' +
                ", filePath='" + filePath + '\'' +
                ", fileNameSecond='" + fileNameSecond + '\'' +
                ", filePathSecond='" + filePathSecond + '\'' +
                '}';
    }
}
