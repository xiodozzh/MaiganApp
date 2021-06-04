package com.mgtech.maiganapp.data.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhaixiang
 */
public class IndicatorStatisticDataModel {
    public int hrScore;
    public int psScore;
    public int pdScore;
    public int pmScore;
    public int v0Score;
    public int coScore;
    public int tprScore;

    public List<Data> hrList = new ArrayList<>();
    public List<Data> psList = new ArrayList<>();
    public List<Data> pdList = new ArrayList<>();
    public List<Data> pmList = new ArrayList<>();
    public List<Data> v0List = new ArrayList<>();
    public List<Data> coList = new ArrayList<>();
    public List<Data> tprList = new ArrayList<>();


    public static class Data{
        public long time;
        public float value;
        private String unit;

        @Override
        public String toString() {
            return "Data{" +
                    "time=" + time +
                    ", value=" + value +
                    ", unit='" + unit + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "IndicatorStatisticDataModel{" +
                "hrScore=" + hrScore +
                ", psScore=" + psScore +
                ", pdScore=" + pdScore +
                ", pmScore=" + pmScore +
                ", v0Score=" + v0Score +
                ", coScore=" + coScore +
                ", tprScore=" + tprScore +
                ", hrList=" + hrList +
                ", psList=" + psList +
                ", pdList=" + pdList +
                ", pmList=" + pmList +
                ", v0List=" + v0List +
                ", coList=" + coList +
                ", tprList=" + tprList +
                '}';
    }
}
