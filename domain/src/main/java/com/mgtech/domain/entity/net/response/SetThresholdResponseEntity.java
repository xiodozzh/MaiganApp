package com.mgtech.domain.entity.net.response;

import com.google.gson.annotations.SerializedName;
import com.mgtech.domain.utils.NetConstant;

/**
 * Created by zhaixiang on 2017/8/29.
 * 设置阈值
 */

public class SetThresholdResponseEntity {
    @SerializedName(NetConstant.JSON_USER_ID)
    private String id;
    @SerializedName(NetConstant.JSON_PUSH)
    private int isPush;
    @SerializedName(NetConstant.JSON_PS)
    private String psLimit;
    @SerializedName(NetConstant.JSON_PD)
    private String pdLimit;
    @SerializedName(NetConstant.JSON_HR)
    private String hrLimit;
    @SerializedName(NetConstant.JSON_OPEN_PUSH_PS)
    private int isPushPs;
    @SerializedName(NetConstant.JSON_OPEN_PUSH_PD)
    private int isPushPd;
    @SerializedName(NetConstant.JSON_OPEN_PUSH_HR)
    private int isPushHr;
    @SerializedName(NetConstant.JSON_ADD_TIME)
    private String time;
    @SerializedName(NetConstant.JSON_ROW_ID)
    private String rowId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isPush() {
        return isPush == 1;
    }

    public void setPush(boolean push) {
        isPush = push ? 1 : 0;
    }

    public String getPsLimit() {
        return psLimit;
    }

    public void setPsLimit(String psLimit) {
        this.psLimit = psLimit;
    }

    public String getPdLimit() {
        return pdLimit;
    }

    public void setPdLimit(String pdLimit) {
        this.pdLimit = pdLimit;
    }

    public String getHrLimit() {
        return hrLimit;
    }

    public void setHrLimit(String hrLimit) {
        this.hrLimit = hrLimit;
    }

    public boolean isPushPs() {
        return isPushPs == 1;
    }

    public void setPushPs(boolean pushPs) {
        isPushPs = pushPs ? 1 : 0;
    }

    public boolean isPushPd() {
        return isPushPd == 1;
    }

    public void setPushPd(boolean pushPd) {
        isPushPd = pushPd ? 1 : 0;
    }

    public boolean isPushHr() {
        return isPushHr == 1;
    }

    public void setPushHr(boolean pushHr) {
        isPushHr = pushHr ? 1 : 0;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRowId() {
        return rowId;
    }

    public void setRowId(String rowId) {
        this.rowId = rowId;
    }

    public int[] getPsRange(){
        return stringToIntArray(psLimit);
    }

    public int[] getPdRange(){
        return stringToIntArray(pdLimit);
    }

    public int[] getHrRange(){
        return stringToIntArray(hrLimit);
    }

    private int[] stringToIntArray(String s){
        String[] strings = s.split(",");
        if (strings.length != 2){
            return null;
        }
        int[] array = new int[2];
        try {
            array[0] = Integer.parseInt(strings[0]);
            array[1] = Integer.parseInt(strings[1]);
        }catch (Exception e){
            e.printStackTrace();
        }
        return array;
    }
}
