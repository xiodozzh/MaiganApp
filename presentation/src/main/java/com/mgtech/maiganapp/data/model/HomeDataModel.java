package com.mgtech.maiganapp.data.model;

import java.util.List;

public class HomeDataModel {
    public float currentPs;
    public float currentPd;
    public float currentV0;
    public float currentHr;

    public int psLevel;
    public int pdLevel;
    public int hrLevel;
    public int v0Level;
    public long currentMeasureTime;

    public long pwTime;
    public List<Data> psList;
    public List<Data> pdList;

    public long ecgTime;
    public float[] ecgData;

    public String nextMedicationTime;
    public int nextDrugCount;
    public int futureDrugCount;
    public int forgetDrugCount;


    public static class Data{
        public long time;
        public float value;

        @Override
        public String toString() {
            return "Data{" +
                    "time=" + time +
                    ", value=" + value +
                    '}';
        }
    }

//    public static class MedicationRemindModel{
//        public String planId;
//        public String name;
//        public String remindId;
//        public MedicationPlanModel.DosageItem dosageItem;
//        public int state;
//    }
}
