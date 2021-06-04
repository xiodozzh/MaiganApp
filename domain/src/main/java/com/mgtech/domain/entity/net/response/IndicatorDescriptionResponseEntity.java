package com.mgtech.domain.entity.net.response;

/**
 *
 * @author zhaixiang
 * @date 2017/1/10
 * 计算数据
 */

public class IndicatorDescriptionResponseEntity {
    private static final int MAN = 1;
    private static final int WOMAN = 2;
    private static final int BOTH = 0;
    /**
     * upperLimit : 140
     * lowerLimit : 90
     * pwDataItemType : 0
     * applicableSex : 1
     * explain :
     * features :
     * itemName :
     * abbreviation :
     */

    private float upperLimit;
    private float lowerLimit;
    private int pwDataItemType;
    private int applicableSex;
    private String explain;
    private String features;
    private String itemName;
    private String abbreviation;


    public boolean isSexRight(boolean isMan){
        if (isMan) {
            return applicableSex == MAN || applicableSex == BOTH;
        } else{
            return applicableSex == WOMAN || applicableSex == BOTH;
        }
    }


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

    @Override
    public String toString() {
        return "IndicatorDescriptionResponseEntity{" +
                "upperLimit=" + upperLimit +
                ", lowerLimit=" + lowerLimit +
                ", pwDataItemType=" + pwDataItemType +
                ", applicableSex=" + applicableSex +
                ", explain='" + explain + '\'' +
                ", features='" + features + '\'' +
                ", itemName='" + itemName + '\'' +
                ", abbreviation='" + abbreviation + '\'' +
                '}';
    }
}
