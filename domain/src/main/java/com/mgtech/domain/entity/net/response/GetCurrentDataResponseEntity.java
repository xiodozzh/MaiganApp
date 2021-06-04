//package com.mgtech.domain.entity.net.response;
//
//import com.google.gson.annotations.Expose;
//import com.google.gson.annotations.SerializedName;
//import com.mgtech.domain.utils.BleConstant;
//import com.mgtech.domain.utils.NetConstant;
//
//import java.util.List;
//
///**
// * Created by zhaixiang on 2017/9/25.
// * 获取
// */
//
//public class GetCurrentDataResponseEntity {
//    @Expose
//    @SerializedName(NetConstant.JSON_DATA_GUID)
//    private String guid;
//    @Expose
//    @SerializedName(NetConstant.JSON_IS_SPORT)
//    private int isSport;
//    @Expose
//    @SerializedName(NetConstant.JSON_TIME)
//    private String time;
//    @Expose
//    @SerializedName(NetConstant.JSON_INDICATOR)
//    private List<IndicatorItemResponseEntity> indicator;
//
//    public String getGuid() {
//        return guid;
//    }
//
//    public void setGuid(String guid) {
//        this.guid = guid;
//    }
//
//    public int getIsSport() {
//        return isSport;
//    }
//
//    public void setIsSport(int isSport) {
//        this.isSport = isSport;
//    }
//
//    public String getTime() {
//        return time;
//    }
//
//    public void setTime(String time) {
//        this.time = time;
//    }
//
//    public List<IndicatorItemResponseEntity> getIndicator() {
//        return indicator;
//    }
//
//    public void setIndicator(List<IndicatorItemResponseEntity> indicator) {
//        this.indicator = indicator;
//    }
//
//    public IndicatorItemResponseEntity getItemValue(@BleConstant.IndicatorType int type){
//        if (indicator == null){
//            return null;
//        }
//        IndicatorItemResponseEntity item;
//        switch (type){
//            case BleConstant.RANK_HR:
//                item = scanEntity("RANK_HR");
//                break;
//            case BleConstant.RANK_AC:
//                item = scanEntity("RANK_AC");
//                break;
//            case BleConstant.RANK_PD:
//                item = scanEntity("RANK_PD");
//                break;
//            case BleConstant.RANK_PS:
//                item = scanEntity("RANK_PS");
//                break;
//            case BleConstant.RANK_PM:
//                item = scanEntity("RANK_PM");
//                break;
//            case BleConstant.RANK_TM:
//                item = scanEntity("RANK_TM");
//                break;
//            case BleConstant.RANK_TPR:
//                item = scanEntity("RANK_TPR");
//                break;
//            case BleConstant.RANK_CO:
//                item = scanEntity("RANK_CO");
//                break;
//            case BleConstant.RANK_V0:
//                item = scanEntity("RANK_V0");
//                break;
//            case BleConstant.RANK_SI:
//                item = scanEntity("RANK_SI");
//                break;
//            case BleConstant.RANK_SV:
//                item = scanEntity("RANK_SV");
//                break;
//            case BleConstant.RANK_ALK:
//                item = scanEntity("RANK_ALK");
//                break;
//            case BleConstant.RANK_ALT:
//                item = scanEntity("RANK_ALT");
//                break;
//            case BleConstant.RANK_K:
//                item = scanEntity("RANK_K");
//                break;
//            case BleConstant.RANK_BV:
//                item = scanEntity("RANK_BV");
//                break;
//            default:
//                return null;
//        }
//        return item;
//    }
//
//    private IndicatorItemResponseEntity scanEntity(String type){
//        if (indicator == null){
//            return null;
//        }
//        for (IndicatorItemResponseEntity entity : indicator) {
//            if (type.equals(entity.getName())){
//                return entity;
//            }
//        }
//        return null;
//    }
//
//    @Override
//    public String toString() {
//        return "GetCurrentDataResponseEntity{" +
//                "guid='" + guid + '\'' +
//                ", isSport=" + isSport +
//                ", time='" + time + '\'' +
//                ", indicator=" + indicator +
//                '}';
//    }
//}
