package com.mgtech.maiganapp.viewmodel;

import android.app.Application;
import androidx.databinding.ObservableField;
import androidx.annotation.NonNull;
import android.text.format.DateFormat;

import java.util.Calendar;

/**
 * @author zhaixiang
 */
public class MedicationPlanEditTimeViewModel extends BaseViewModel {
    public ObservableField<String> startTimeString = new ObservableField<>("");
    public ObservableField<String> endTimeString = new ObservableField<>("");
    private Calendar startCalendar;
    private Calendar endCalendar;
    private static final String DATE_FORMAT = "yyyy.MM.dd";

    public MedicationPlanEditTimeViewModel(@NonNull Application application) {
        super(application);
        startCalendar = Calendar.getInstance();
        endCalendar = Calendar.getInstance();
        endCalendar.setTimeInMillis(startCalendar.getTimeInMillis());
        endCalendar.add(Calendar.MONTH,1);
    }

    public void setStartCalendar(long time){
        if (time > endCalendar.getTimeInMillis()){
//            endCalendar.setTimeInMillis(time);
//            endCalendar.add(Calendar.YEAR,1);
//            endTimeString.set(DateFormat.format(DATE_FORMAT,endCalendar).toString());
            showToast("开始时间必须小于结束时间");
            return;
        }
        startCalendar.setTimeInMillis(time);
        startTimeString.set(DateFormat.format(DATE_FORMAT,startCalendar).toString());
    }

    public void setEndCalendar(long time){
        if (time < startCalendar.getTimeInMillis()){
            showToast("结束时间必须大于开始时间");
            return;
        }
        endCalendar.setTimeInMillis(time);
        endTimeString.set(DateFormat.format(DATE_FORMAT,endCalendar).toString());
    }

    public boolean isValid(){
        Calendar temp = Calendar.getInstance();
        temp.setTimeInMillis(startCalendar.getTimeInMillis());
        temp.add(Calendar.YEAR,1);
        return temp.getTimeInMillis() >= endCalendar.getTimeInMillis();
    }

    public Calendar getStartCalendar() {
        return startCalendar;
    }

    public Calendar getEndCalendar() {
        return endCalendar;
    }
}
