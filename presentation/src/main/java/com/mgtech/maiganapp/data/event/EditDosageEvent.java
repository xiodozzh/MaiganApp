package com.mgtech.maiganapp.data.event;

import android.util.SparseArray;

import com.mgtech.maiganapp.data.model.MedicationPlanModel;

import java.util.List;

/**
 * @author zhaixiang
 */
public class EditDosageEvent {
    private SparseArray<List<MedicationPlanModel.DosageItem>> list;

    public EditDosageEvent(SparseArray<List<MedicationPlanModel.DosageItem>> list) {
        this.list = list;
    }

    public SparseArray<List<MedicationPlanModel.DosageItem>> getList() {
        return list;
    }

    public void setList(SparseArray<List<MedicationPlanModel.DosageItem>> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "EditDosageEvent{" +
                "list=" + list +
                '}';
    }
}
