package com.mgtech.maiganapp.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import android.util.SparseArray;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.model.MedicationPlanModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhaixiang
 */
public class MedicationPlanEditDosageViewModel extends BaseViewModel {
    public SparseArray<List<MedicationPlanModel.DosageItem>> list = new SparseArray<>();
    public String dosageNumberString = "";
    private String format;

    public MedicationPlanEditDosageViewModel(@NonNull Application application) {
        super(application);
        format = application.getString(R.string.activity_medication_plan_cycle_days);
    }

    public void setDosageCycleDays(int day) {
        int size = list.size();
        if (size < day) {
            for (int i = size; i < day; i++) {
                list.put(i, new ArrayList<MedicationPlanModel.DosageItem>());
            }
        } else if (size > day) {
            list.removeAtRange(day, size);
        }
        dosageNumberString = String.format(format, list.size());
    }

    public void initWithDefaultData() {
        list.put(0, new ArrayList<MedicationPlanModel.DosageItem>());
        dosageNumberString = String.format(format, list.size());
    }

    public void initWithEventData(SparseArray<List<MedicationPlanModel.DosageItem>> sparseArray) {
        list.clear();
        for (int i = 0; i < sparseArray.size(); i++) {
            List<MedicationPlanModel.DosageItem> list = sparseArray.valueAt(i);
            this.list.put(i, list);
        }
        dosageNumberString = String.format(format, list.size());
    }


    public void removeData(int day, int itemIndex) {
        List<MedicationPlanModel.DosageItem> dataList = list.get(day);
        if (dataList != null && dataList.size() > itemIndex) {
            dataList.remove(itemIndex);
        }
    }
}
