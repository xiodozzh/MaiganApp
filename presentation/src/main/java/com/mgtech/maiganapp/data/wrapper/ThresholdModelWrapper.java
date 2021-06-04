package com.mgtech.maiganapp.data.wrapper;

import android.text.TextUtils;

import com.mgtech.domain.entity.net.response.ThresholdRequestEntity;
import com.mgtech.maiganapp.data.model.ThresholdModel;

public class ThresholdModelWrapper {
    private static final int ON = 1;
    private static final int OFF = 0;

    public static ThresholdModel getModelFromNet(ThresholdRequestEntity entity){
        ThresholdModel model = new ThresholdModel();
        model.open = entity.getOpenPush() == ON;
        model.openHr = entity.getOpenPushHR() == ON;
        model.openPs = entity.getOpenPushPS() == ON;
        model.openV0 = entity.getOpenPushV0() == ON;
        model.openPd = entity.getOpenPushPD() == ON;

        int[] rangeHr = stringToIntArray(entity.getHrThreshold());
        int[] rangePs = stringToIntArray(entity.getPsThreshold());
        int[] rangePd = stringToIntArray(entity.getPdThreshold());
        int[] rangeV0 = stringToIntArray(entity.getV0Threshold());
        if (rangeHr != null) {
            model.hrLow = rangeHr[0];
            model.hrHigh = rangeHr[1];
        }
        if (rangePs != null) {
            model.psLow = rangePs[0];
            model.psHigh = rangePs[1];
        }
        if (rangePd != null) {
            model.pdLow = rangePd[0];
            model.pdHigh = rangePd[1];
        }
        if (rangeV0 != null) {
            model.v0Low = rangeV0[0];
            model.v0High = rangeV0[1];
        }
        return model;
    }

    public static ThresholdRequestEntity modelToNet(ThresholdModel model){
        ThresholdRequestEntity entity = new ThresholdRequestEntity();
//        entity.setOpenPush(model.open ? ON : OFF);
        entity.setOpenPush(ON);
        entity.setOpenPushHR(model.openHr ? ON : OFF);
        entity.setOpenPushPS(model.openPs ? ON : OFF);
        entity.setOpenPushPD(model.openPd ? ON : OFF);
        entity.setOpenPushV0(model.openV0 ? ON : OFF);
        entity.setHrThreshold(model.hrLow+","+model.hrHigh);
        entity.setPsThreshold(model.psLow+","+model.psHigh);
        entity.setPdThreshold(model.pdLow+","+model.pdHigh);
        entity.setV0Threshold(model.v0Low+","+model.v0High);
        return entity;
    }


    private static int[] stringToIntArray(String s){
        if (TextUtils.isEmpty(s)){
            return null;
        }
        String[] strings = s.split(",");
        if (strings.length != 2){
            return null;
        }
        int[] range = new int[2];
        try {
            range[0] = (int) Float.parseFloat(strings[0]);
            range[1] = (int) Float.parseFloat(strings[1]);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return range;
    }
}
