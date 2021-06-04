package com.mgtech.maiganapp.data.model;

import android.util.Log;
import android.util.SparseArray;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author zhaixiang
 */
public class MedicationPlanModel {
    public static final int REPEAT_TIME_DAY = 0;
    public static final int REPEAT_TIME_WEEK = 1;
    public static final int REPEAT_TIME_MONTH = 2;

    public String planId;
    public String name;
    public long startTime;
    public long endTime;
    public int repeatType = REPEAT_TIME_DAY;
    public int repeatPeriod;
    public SparseArray<List<DosageItem>> dosageMap;
    public int completeNumber;
    public int forgetNumber;
    public int totalNumber;
    public int ignoreNumber;
    public String medicineId;
    public boolean stopped;
    public boolean customMedicine;

    public MedicationPlanModel() {
    }

    public MedicationPlanModel(MedicationPlanModel model){
        planId = model.planId;
        name = model.name;
        startTime = model.startTime;
        endTime = model.endTime;
        repeatType = model.repeatType;
        repeatPeriod = model.repeatPeriod;
        completeNumber = model.completeNumber;
        forgetNumber = model.forgetNumber;
        totalNumber = model.totalNumber;
        ignoreNumber = model.ignoreNumber;
        medicineId = model.medicineId;
        stopped = model.stopped;
        customMedicine = model.customMedicine;
        dosageMap = new SparseArray<>();
        for (int i = 0; i < model.dosageMap.size(); i++) {
            List<DosageItem> l = model.dosageMap.get(i);
            if (l == null){
                continue;
            }
            List<DosageItem> list = new ArrayList<>();
            for (DosageItem dosageItem:l) {
                DosageItem item = new DosageItem();
                item.dosage = dosageItem.dosage;
                item.dosageUnitType = dosageItem.dosageUnitType;
                item.time = dosageItem.time;
                list.add(item);
            }
            dosageMap.put(i,list);
        }
    }

    //    public boolean isUpdate(MedicationPlanModel model){
//        if (this == model){
//            return true;
//        }
//        if (model == null || getClass() != model.getClass()){
//            return false;
//        }
//        boolean flag =  startTime == model.startTime &&
//                endTime == model.endTime &&
//                repeatType == model.repeatType &&
//                repeatPeriod == model.repeatPeriod &&
//                Objects.equals(planId, model.planId) &&
//                Objects.equals(name, model.name) &&
//                Objects.equals(medicineId, model.medicineId);
//        if (!flag){
//            return false;
//        }
//        if (dosageMap == null && model.dosageMap == null){
//            return true;
//        }
//        if (dosageMap != null && model.dosageMap != null) {
//            int size = dosageMap.size();
//            int sizeModel = model.dosageMap.size();
//            if (size != sizeModel){
//                return false;
//            }
//            for (int i = 0; i < size; i++) {
//                List<DosageItem> items = dosageMap.get(i);
//                List<DosageItem> modelItems = model.dosageMap.get(i);
//            }
//        }else{
//            return false;
//        }
//
//    }

    private boolean dosageMapEquals(SparseArray<List<DosageItem>> map){
        if (dosageMap == map){
            return true;
        }
        if (map == null){
            return false;
        }
        int size = dosageMap.size();
        int sizeModel = map.size();
        if (size != sizeModel){
            return false;
        }
        for (int i = 0; i < size; i++) {
            List<DosageItem> items = dosageMap.get(i);
            List<DosageItem> modelItems = map.get(i);
            if (!dosageListEquals(items,modelItems)){
                return false;
            }
        }
        Log.i("test", "dosageMapEquals: t ");
        return true;
    }

    private boolean dosageListEquals(List<DosageItem> list1,List<DosageItem> list2){
        if (list1 == list2){
            Logger.i("list1 == list2");
            return true;
        }
        if (list1 == null || list2 == null){
            Logger.i("list1 == null || list2 == null");

            return false;
        }
        int size1 = list1.size();
        int size2 = list2.size();
        if (size1 != size2){
            Logger.i("size1 != size2");
            return false;
        }
        for (int i = 0; i < size1; i++) {
            DosageItem item1 = list1.get(i);
            DosageItem item2 = list2.get(i);
            Logger.i(item1.toString() +"\n" + item2.toString());
            if (!Objects.equals(item1,item2)){
                return false;
            }
        }
        return true;
    }

    public static class DosageItem{
        public String time;
        public float dosage ;
        public int dosageUnitType;

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            DosageItem that = (DosageItem) o;
            return Float.compare(that.dosage, dosage) == 0 &&
                    dosageUnitType == that.dosageUnitType &&
                    Objects.equals(time, that.time);
        }

        @Override
        public int hashCode() {

            return Objects.hash(time, dosage, dosageUnitType);
        }


        @Override
        public String toString() {
            return "DosageItem{" +
                    "time='" + time + '\'' +
                    ", dosage=" + dosage +
                    ", dosageUnitType=" + dosageUnitType +
                    '}';
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MedicationPlanModel model = (MedicationPlanModel) o;
        return startTime == model.startTime &&
                endTime == model.endTime &&
                repeatType == model.repeatType &&
                repeatPeriod == model.repeatPeriod &&
                completeNumber == model.completeNumber &&
                forgetNumber == model.forgetNumber &&
                ignoreNumber == model.ignoreNumber &&
                totalNumber == model.totalNumber &&
                stopped == model.stopped &&
                Objects.equals(planId, model.planId) &&
                Objects.equals(name, model.name) &&
//                Objects.equals(dosageMap, model.dosageMap) &&
                dosageMapEquals(model.dosageMap) &&
                Objects.equals(medicineId, model.medicineId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(planId, name, startTime, endTime, repeatType, repeatPeriod, dosageMap, completeNumber,
                forgetNumber, ignoreNumber,totalNumber, medicineId, stopped);
    }

    @Override
    public String toString() {
        return "MedicationPlanModel{" +
                "planId='" + planId + '\'' +
                ", name='" + name + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", repeatType=" + repeatType +
                ", repeatPeriod=" + repeatPeriod +
                ", dosageMap=" + dosageMap +
                ", completeNumber=" + completeNumber +
                ", forgetNumber=" + forgetNumber +
                ", totalNumber=" + totalNumber +
                ", ignoreNumber=" + ignoreNumber +
                ", medicineId='" + medicineId + '\'' +
                ", stopped=" + stopped +
                ", customMedicine=" + customMedicine +
                '}';
    }
}
