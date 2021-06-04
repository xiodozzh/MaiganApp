package com.mgtech.maiganapp.data.wrapper;

import android.util.SparseArray;

import com.mgtech.domain.entity.net.MedicationPlanEntity;
import com.mgtech.maiganapp.data.model.MedicationPlanModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author zhaixiang
 */
public class MedicationPlanModelWrapper {
    private static DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    public static MedicationPlanModel getModelFromMedicationPlanEntity(MedicationPlanEntity entity, boolean isStopped) {
        MedicationPlanModel model = new MedicationPlanModel();
        model.stopped = isStopped;
        model.completeNumber = entity.getCompletedNumber();
        model.forgetNumber = entity.getForgetNumber();
        model.totalNumber = entity.getTotalNumber();
        model.ignoreNumber = entity.getIgnoreNumber();
        model.customMedicine = entity.getCustom() == 1;
        try {
            model.startTime = format.parse(entity.getStartDate()).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            model.endTime = format.parse(entity.getEndDate()).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        model.name = entity.getDrugName();
        model.planId = entity.getPlanGuid();
        model.repeatPeriod = entity.getRepeatPeriod();
        model.repeatType = entity.getRepeatType();
        model.medicineId = entity.getDrugGuid();
        model.dosageMap = new SparseArray<>();
        Map<String,List<MedicationPlanEntity.DosageBean>> map = entity.getDosage();
        if (map != null && !map.isEmpty()) {
            for (Map.Entry<String, List<MedicationPlanEntity.DosageBean>> entry : entity.getDosage().entrySet()) {
                List<MedicationPlanModel.DosageItem>itemList = new ArrayList<>();
                for (MedicationPlanEntity.DosageBean bean:entry.getValue()) {
                    itemList.add(getDosageItemFormNet(bean));
                }
                try {
                    // key 为天数-1
                    model.dosageMap.put(Integer.parseInt(entry.getKey())-1,itemList);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return model;
    }

    private static MedicationPlanModel.DosageItem getDosageItemFormNet(MedicationPlanEntity.DosageBean bean){
        MedicationPlanModel.DosageItem item = new MedicationPlanModel.DosageItem();
        item.dosageUnitType = bean.getDosageUnitType();
        item.dosage = bean.getDosage();
        item.time = bean.getTime();
        return item;
    }
}
