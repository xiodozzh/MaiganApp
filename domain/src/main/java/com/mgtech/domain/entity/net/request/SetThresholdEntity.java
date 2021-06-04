//package com.mgtech.domain.entity.net.request;
//
//import com.google.gson.GsonBuilder;
//import com.google.gson.annotations.Expose;
//import com.google.gson.annotations.SerializedName;
//import com.mgtech.domain.utils.HttpApi;
//import com.mgtech.domain.utils.NetConstant;
//import com.mgtech.domain.entity.net.RequestEntity;
//
///**
// * Created by zhaixiang on 2017/8/29.
// */
//
//public class SetThresholdEntity implements RequestEntity {
//    @SerializedName(NetConstant.JSON_USER_ID)
//    @Expose(serialize = false, deserialize = false)
//    private String id;
//    @SerializedName(NetConstant.JSON_PUSH)
//    @Expose
//    private int isPush;
//    @SerializedName(NetConstant.JSON_PS)
//    @Expose
//    private String psLimit;
//    @SerializedName(NetConstant.JSON_PD)
//    @Expose
//    private String pdLimit;
//    @SerializedName(NetConstant.JSON_HR)
//    @Expose
//    private String hrLimit;
//    @SerializedName(NetConstant.JSON_OPEN_PUSH_PS)
//    @Expose
//    private int isPushPs;
//    @SerializedName(NetConstant.JSON_OPEN_PUSH_PD)
//    @Expose
//    private int isPushPd;
//    @SerializedName(NetConstant.JSON_OPEN_PUSH_HR)
//    @Expose
//    private int isPushHr;
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public void setIsPush(boolean isPush) {
//        this.isPush = isPush ? 1 : 0;
//    }
//
//    public void setPsLimit(String psLimit) {
//        this.psLimit = psLimit;
//    }
//
//    public void setPdLimit(String pdLimit) {
//        this.pdLimit = pdLimit;
//    }
//
//    public void setHrLimit(String hrLimit) {
//        this.hrLimit = hrLimit;
//    }
//
//    public void setIsPushPs(boolean isPushPs) {
//        this.isPushPs = isPushPs ? 1 : 0;
//    }
//
//    public void setIsPushPd(boolean isPushPd) {
//        this.isPushPd = isPushPd ? 1 : 0;
//    }
//
//    public void setIsPushHr(boolean isPushHr) {
//        this.isPushHr = isPushHr ? 1 : 0;
//    }
//
//    @Override
//    public String getUrl() {
//        return HttpApi.API_SET_THRESHOLD + "/" + id;
//    }
//
//    @Override
//    public String getBody() {
//        return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(this);
//    }
//}
