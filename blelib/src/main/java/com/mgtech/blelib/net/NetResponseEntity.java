//package com.mgtech.blelib.net;
//
//import com.google.gson.annotations.Expose;
//import com.google.gson.annotations.SerializedName;
//import com.mgtech.blelib.constant.NetConstant;
//
///**
// * Created by zhaixiang on 2017/8/23.
// * 网络返回值模板
// */
//
//public class NetResponseEntity<T> {
//    @Expose
//    @SerializedName(value = NetConstant.JSON_DATA, alternate = {"data"})
//    private T data;
//    @Expose
//    @SerializedName(NetConstant.JSON_CODE)
//    private int code;
//    @Expose
//    @SerializedName(NetConstant.JSON_MESSAGE)
//    private String message;
//
//    public T getData() {
//        return data;
//    }
//
//    public void setData(T data) {
//        this.data = data;
//    }
//
//    public int getCode() {
//        return code;
//    }
//
//    public void setCode(int code) {
//        this.code = code;
//    }
//
//    public String getMessage() {
//        return message;
//    }
//
//    public void setMessage(String message) {
//        this.message = message;
//    }
//
//    @Override
//    public String toString() {
//        return "NetResponseEntity{" +
//                "data=" + data +
//                ", code=" + code +
//                ", message='" + message + '\'' +
//                '}';
//    }
//}
