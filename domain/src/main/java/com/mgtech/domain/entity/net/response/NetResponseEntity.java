package com.mgtech.domain.entity.net.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mgtech.domain.utils.NetConstant;

/**
 *
 * @author zhaixiang
 * 网络返回值模板
 */

public class NetResponseEntity<T> {
    @Expose
    @SerializedName(value = NetConstant.JSON_DATA, alternate = {"data"})
    private T data;
    @Expose
    @SerializedName(value = NetConstant.JSON_CODE,alternate = {"code"})
    private int code;
    @Expose
    @SerializedName(value = NetConstant.JSON_MESSAGE,alternate = {"msg","message"})
    private String message;

    private boolean isCache;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isCache() {
        return isCache;
    }

    public void setCache(boolean cache) {
        isCache = cache;
    }

    @Override
    public String toString() {
        return "NetResponseEntity{" +
                "data=" + data +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", isCache=" + isCache +
                '}';
    }
}
