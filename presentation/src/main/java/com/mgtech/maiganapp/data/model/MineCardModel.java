package com.mgtech.maiganapp.data.model;

/**
 * @author zhaixiang
 */
public class MineCardModel {
    public static final int BP = 84;
    public static final int ABNORMAL = 908;
    public static final int MEDICATION_PLAN = 887;
    public static final int ECG = 293;
    public static final int SPORT = 840;
    public static final int WEEKLY_REPORT = 973;
    public static final int KNOWLEDGE = 601;
    public static final int WEIGHT = 217;
    public static final int DISEASE_HISTORY = 701;
    public static final int FONT_SIZE = 53;
    public static final int ALARM = 349;

    public String title;
    public int imgSrc;
    public int type;
    public boolean isRead;

    public MineCardModel(int type,String title, int imgSrc,boolean isRead) {
        this.type = type;
        this.title = title;
        this.imgSrc = imgSrc;
        this.isRead = isRead;
    }

    @Override
    public String toString() {
        return "MineCardModel{" +
                "title='" + title + '\'' +
                ", imgSrc=" + imgSrc +
                ", type=" + type +
                '}';
    }
}
