//package com.mgtech.domain.entity.net.response;
//
//import com.google.gson.annotations.SerializedName;
//import com.mgtech.domain.utils.NetConstant;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
///**
// *
// * @author zhaixiang
// * 查询数据
// */
//
//public class QueryDataResponseEntity {
//    @SerializedName(NetConstant.JSON_HR)
//    private List<IndicatorItemResponseEntity> hrItems;
//    @SerializedName(NetConstant.JSON_PS)
//    private List<IndicatorItemResponseEntity> psItems;
//    @SerializedName(NetConstant.JSON_PD)
//    private List<IndicatorItemResponseEntity> pdItems;
//    @SerializedName(NetConstant.JSON_CO)
//    private List<IndicatorItemResponseEntity> coItems;
//    @SerializedName(NetConstant.JSON_TPR)
//    private List<IndicatorItemResponseEntity> tprItems;
//    @SerializedName(NetConstant.JSON_V0)
//    private List<IndicatorItemResponseEntity> v0Items;
//    @SerializedName(NetConstant.JSON_PM)
//    private List<IndicatorItemResponseEntity> pmItems;
//
//    public QueryDataResponseEntity() {
//        hrItems = new ArrayList<>();
//        psItems = new ArrayList<>();
//        pdItems = new ArrayList<>();
//        coItems = new ArrayList<>();
//        tprItems = new ArrayList<>();
//        v0Items = new ArrayList<>();
//        pmItems = new ArrayList<>();
//    }
//
//    public List<IndicatorItemResponseEntity> getHrItems() {
//        return hrItems;
//    }
//
//    public void setHrItems(List<IndicatorItemResponseEntity> hrItems) {
//        this.hrItems = hrItems;
//    }
//
//    public List<IndicatorItemResponseEntity> getPsItems() {
//        return psItems;
//    }
//
//    public void setPsItems(List<IndicatorItemResponseEntity> psItems) {
//        this.psItems = psItems;
//    }
//
//    public List<IndicatorItemResponseEntity> getPdItems() {
//        return pdItems;
//    }
//
//    public void setPdItems(List<IndicatorItemResponseEntity> pdItems) {
//        this.pdItems = pdItems;
//    }
//
//    public List<IndicatorItemResponseEntity> getCoItems() {
//        return coItems;
//    }
//
//    public void setCoItems(List<IndicatorItemResponseEntity> coItems) {
//        this.coItems = coItems;
//    }
//
//
//    public List<IndicatorItemResponseEntity> getTprItems() {
//        return tprItems;
//    }
//
//    public void setTprItems(List<IndicatorItemResponseEntity> tprItems) {
//        this.tprItems = tprItems;
//    }
//
//    public List<IndicatorItemResponseEntity> getV0Items() {
//        return v0Items;
//    }
//
//    public void setV0Items(List<IndicatorItemResponseEntity> v0Items) {
//        this.v0Items = v0Items;
//    }
//
//    public List<IndicatorItemResponseEntity> getPmItems() {
//        return pmItems;
//    }
//
//    public void setPmItems(List<IndicatorItemResponseEntity> pmItems) {
//        this.pmItems = pmItems;
//    }
//
//    public static float getMaxItem(List<IndicatorItemResponseEntity> list) {
//        if (list.isEmpty()){
//            return 0;
//        }
//        float value = Float.MIN_VALUE;
//        for (IndicatorItemResponseEntity entity : list) {
//            float v = entity.getValue();
//            if (v != 0) {
//                value = Math.max(value, v);
//            }
//        }
//        return value;
//    }
//
//    public static float getMinItem(List<IndicatorItemResponseEntity> list) {
//        if (list.isEmpty()){
//            return 0;
//        }
//        float value = Float.MAX_VALUE;
//        for (IndicatorItemResponseEntity entity : list) {
//            float v = entity.getValue();
//            if (v != 0) {
//                value = Math.min(value, v);
//            }
//        }
//        return value;
//    }
//
//    public static float getAvgItem(List<IndicatorItemResponseEntity> list) {
//        if (list.isEmpty()){
//            return 0;
//        }
//        float total = 0;
//        int num = 0;
//        for (IndicatorItemResponseEntity entity : list) {
//            float v = entity.getValue();
//            if (v != 0) {
//                num++;
//                total += v;
//            }
//        }
//        if (num == 0){
//            return 0;
//        }
//        return total / num;
//    }
//
//
//    @Override
//    public String toString() {
//        return "QueryDataResponseEntity{" +
//                "hrItems=" + hrItems +
//                ", psItems=" + psItems +
//                ", pdItems=" + pdItems +
//                ", coItems=" + coItems +
//                ", tprItems=" + tprItems +
//                ", v0Items=" + v0Items +
//                ", pmItems=" + pmItems +
//                '}';
//    }
//}
