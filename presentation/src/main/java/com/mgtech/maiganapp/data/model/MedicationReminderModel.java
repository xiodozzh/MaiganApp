package com.mgtech.maiganapp.data.model;

import com.mgtech.domain.entity.net.request.SetMedicationRemindRequestEntity;
import com.mgtech.domain.entity.net.response.MedicationRemindResponseEntity;

/**
 * @author zhaixiang
 */
public class MedicationReminderModel {
    public static final int TAKEN = SetMedicationRemindRequestEntity.COMPLETE;
    public static final int IGNORE = SetMedicationRemindRequestEntity.IGNORE;
    public static final int DEFAULT = SetMedicationRemindRequestEntity.NONE;

    public String remindId;
    public String planId;
    public String name;
    public String time;
    public float dosage;
    public int dosageUnitType;
    public int state;
    public boolean future;

    @Override
    public String toString() {
        return "MedicationReminderModel{" +
                "remindId='" + remindId + '\'' +
                ", planId='" + planId + '\'' +
                ", name='" + name + '\'' +
                ", time='" + time + '\'' +
                ", dosage='" + dosage + '\'' +
                ", state=" + state +
                ", future=" + future +
                '}';
    }
}
