package com.mgtech.maiganapp.data.model;

import com.mgtech.domain.entity.IndicatorDescription;

import java.util.List;

public interface IIndicatorModel {
    int TYPE_TIME = 1;
    int TYPE_BP_HR = 2;
    int TYPE_TITLE_START = 3;
    int TYPE_TITLE_MIDDLE = 4;
    int TYPE_TITLE_END = 5;
    int TYPE_FOOT = 6;
    int TYPE_MARK = 7;
    int TYPE_PW_IMAGE = 8;
    int TYPE_TAG = 9;

    int getType();

    class TAG implements IIndicatorModel{
        public String string;

        public TAG(String string) {
            this.string = string;
        }

        @Override
        public int getType() {
            return TYPE_TAG;
        }
    }

    class Image implements IIndicatorModel{
        private List<Float> data;
        public boolean isOpen;
        public Image(List<Float>  data) {
            this.data = data;
        }

        public List<Float> getData() {
            return data;
        }

        @Override
        public int getType() {
            return TYPE_PW_IMAGE;
        }
    }


    class Time implements IIndicatorModel{
        public String string;
        public boolean isAuto;

        public Time(String string,boolean isAuto) {
            this.string = string;
            this.isAuto = isAuto;
        }

        @Override
        public int getType() {
            return TYPE_TIME;
        }
    }

    class HrBp implements IIndicatorModel{
        public float valuePs;
        public  float valuePd;
        public String unitBp;
        public float valueHr;
        public String unitHr;
        public int levelPs;
        public int levelPd;
        public int levelHr;
        public boolean isBpOpen;
        public boolean isHrOpen;
        public IndicatorDescription descriptionItemHr;

        public HrBp(float valuePs, float valuePd, String unitBp, float valueHr, String unitHr,int levelPs, int levelPd, int levelHr,IndicatorDescription descriptionItemHr) {
            this.valuePs = valuePs;
            this.valuePd = valuePd;
            this.unitBp = unitBp;
            this.valueHr = valueHr;
            this.unitHr = unitHr;
            this.levelPs = levelPs;
            this.levelPd = levelPd;
            this.levelHr = levelHr;
            this.descriptionItemHr = descriptionItemHr;
        }

        @Override
        public int getType() {
            return TYPE_BP_HR;
        }

        public int getLevelBp(){
            if (levelPs == 0){
                return levelPd;
            }else{
                return levelPs;
            }
        }

        public float getHrHigh(){
            if (descriptionItemHr != null){
                return descriptionItemHr.getUpperLimit();
            }else{
                return 100;
            }
        }

        public float getHrLow(){
            if (descriptionItemHr != null){
                return descriptionItemHr.getLowerLimit();
            }else{
                return 40;
            }
        }
    }

    class Title implements IIndicatorModel{
        public boolean isOpen;
        public int index;
        public String name;
        public String extraTag;
        public float value;
        public int level;
        public String unit;
        public IndicatorDescription descriptionItem;
        private int type;

        public Title(int type,int index, String name, float value, int level, String unit,String extraTag, IndicatorDescription descriptionItem) {
            this.type = type;
            this.index = index;
            this.name = name;
            this.value = value;
            this.level = level;
            this.unit = unit;
            this.extraTag = extraTag;
            this.descriptionItem = descriptionItem;
        }

        public String getDesc(){
            if (descriptionItem == null){
                return "";
            }else{
                return descriptionItem.getFeatures()+"\n\n" + descriptionItem.getExplain();
            }
        }

        public float getHigh(){
            if (descriptionItem != null){
                return descriptionItem.getUpperLimit();
            }else{
                return 10;
            }
        }

        public float getLow(){
            if (descriptionItem != null){
                return descriptionItem.getLowerLimit();
            }else{
                return 0;
            }
        }

        @Override
        public int getType() {
            return type;
        }
    }

    class Footer implements IIndicatorModel{
        public String text;

        public Footer(String text) {
            this.text = text;
        }

        @Override
        public int getType() {
            return TYPE_FOOT;
        }
    }

    class Mark implements IIndicatorModel{
        public String title;
        public String content;
        public boolean isUser;

        public Mark(String title, String content,boolean isUser) {
            this.title = title;
            this.content = content;
            this.isUser = isUser;
        }

        @Override
        public int getType() {
            return TYPE_MARK;
        }
    }

}
