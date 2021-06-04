package com.mgtech.domain.entity;

import android.text.TextUtils;
import android.util.Log;

import com.mgtech.domain.entity.net.response.IndicatorDescriptionResponseEntity;
import com.mgtech.domain.utils.MyConstant;
import com.mgtech.domain.utils.NetConstant;
import com.tencent.mmkv.MMKV;

import java.util.List;

public class IndicatorDescription {
    private float upperLimit;
    private float lowerLimit;
    private int pwDataItemType;
    private int applicableSex;
    private String explain;
    private String features;
    private String itemName;
    private String abbreviation;

    private static final String MMKV_ID = "IndicatorDescription";
    private static final String UPPER = "upperLimit";
    private static final String LOWER = "lowerLimit";
    private static final String TYPE = "type";
    private static final String SEX = "sex";
    private static final String EXPLAIN = "explain";
    private static final String FEATURES = "features";
    private static final String NAME = "name";
    private static final String SHORT_NAME = "abbreviation";

    public float getUpperLimit() {
        return upperLimit;
    }

    public void setUpperLimit(float upperLimit) {
        this.upperLimit = upperLimit;
    }

    public float getLowerLimit() {
        return lowerLimit;
    }

    public void setLowerLimit(float lowerLimit) {
        this.lowerLimit = lowerLimit;
    }

    public int getPwDataItemType() {
        return pwDataItemType;
    }

    public void setPwDataItemType(int pwDataItemType) {
        this.pwDataItemType = pwDataItemType;
    }

    public int getApplicableSex() {
        return applicableSex;
    }

    public void setApplicableSex(int applicableSex) {
        this.applicableSex = applicableSex;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public static void saveToLocal(List<IndicatorDescriptionResponseEntity> list) {
        deleteAll();
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        for (IndicatorDescriptionResponseEntity entity : list) {
            int i = entity.getPwDataItemType();
            int sex = entity.getApplicableSex();
            mmkv.encode(""+i + sex + UPPER, entity.getUpperLimit());
            mmkv.encode(""+i + sex + LOWER, entity.getLowerLimit());
            mmkv.encode(""+i + sex + TYPE, entity.getPwDataItemType());
            mmkv.encode(""+i + sex + SEX, entity.getApplicableSex());
            mmkv.encode(""+i + sex + EXPLAIN, entity.getExplain());
            mmkv.encode(""+i + sex + FEATURES, entity.getFeatures());
            mmkv.encode(""+i + sex + NAME, entity.getItemName());
            mmkv.encode(""+i + sex + SHORT_NAME, entity.getAbbreviation());
        }
    }

    public static boolean isEmpty(){
        IndicatorDescription indicatorDescription = IndicatorDescription.get(NetConstant.HR,0);
        return indicatorDescription == null || TextUtils.isEmpty(indicatorDescription.getItemName());
    }

    public static IndicatorDescription get(int type, int sex) {
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        int selectType = mmkv.getInt(""+type + sex + TYPE, -100);
        if (selectType == -100){
            sex = 0;
            selectType = mmkv.getInt(""+type + sex + TYPE, -100);
        }
        if (selectType == -100){
            return null;
        }
        IndicatorDescription entity = new IndicatorDescription();
        entity.setUpperLimit(mmkv.getFloat(""+type + sex + UPPER, 0));
        entity.setLowerLimit(mmkv.getFloat(""+type + sex + LOWER, 0));
        entity.setPwDataItemType(mmkv.getInt(""+type + sex + TYPE, type));
        entity.setAbbreviation(mmkv.getString(""+type + sex + SHORT_NAME, ""));
        entity.setItemName(mmkv.getString(""+type + sex + NAME, ""));
        entity.setApplicableSex(mmkv.getInt(""+type + sex + SEX, 0));
        entity.setExplain(mmkv.getString(""+type + sex + EXPLAIN, ""));
        entity.setFeatures(mmkv.getString(""+type + sex + FEATURES, ""));
        return entity;
    }

    public static void deleteAll() {
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        for (int i = 0; i < 15; i++) {
            for (int sex = 0; sex < 3; sex++) {
                mmkv.removeValuesForKeys(new String[]{
                        ""+i + sex + UPPER,
                        ""+i + sex + LOWER,
                        ""+i + sex + TYPE,
                        ""+i + sex + SEX,
                        ""+i + sex + EXPLAIN,
                        ""+i + sex + FEATURES,
                        ""+i + sex + NAME,
                        ""+i + sex + SHORT_NAME,
                });
            }
        }
    }
}
