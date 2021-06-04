package com.mgtech.maiganapp.data.wrapper;

import com.mgtech.domain.entity.net.response.MedicationRemindResponseEntity;
import com.mgtech.maiganapp.data.model.MedicationReminderModel;

import java.util.ArrayList;
import java.util.List;

public class MedicationReminderModelWrapper {
    public static List<MedicationReminderModel> getModelFromNet(MedicationRemindResponseEntity entity) {
        List<MedicationReminderModel> list = new ArrayList<>();
        List<MedicationRemindResponseEntity.RemindBean> futureBeans = entity.getFutureRemind();
        List<MedicationRemindResponseEntity.RemindBean> currentBeans = entity.getTodayRemind();
        if (currentBeans != null && !currentBeans.isEmpty()) {
            addBeans(list,currentBeans,false);
        }
        if (futureBeans != null && !futureBeans.isEmpty()) {
            addBeans(list,futureBeans,true);
        }
        return list;
    }

    private static void addBeans(List<MedicationReminderModel> list, List<MedicationRemindResponseEntity.RemindBean> beans,boolean isFuture){
        for (MedicationRemindResponseEntity.RemindBean bean : beans) {
            MedicationRemindResponseEntity.DosageBean dosageBean = bean.getDosage();
            MedicationReminderModel model = new MedicationReminderModel();
            model.future = isFuture;
            model.state = bean.getState();
            if (dosageBean != null) {
                model.time = dosageBean.getTime();
                model.dosage = dosageBean.getDosage();
                model.dosageUnitType = dosageBean.getDosageUnitType();
            }
            model.name = bean.getDrugName();
            model.planId = bean.getPlanGuid();
            model.remindId = bean.getRemindGuid();
            list.add(model);
        }
    }
}
