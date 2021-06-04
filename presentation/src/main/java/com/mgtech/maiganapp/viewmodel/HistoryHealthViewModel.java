package com.mgtech.maiganapp.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;

import java.util.Calendar;

/**
 * @author zhaixiang
 */
public class HistoryHealthViewModel extends BaseOtherBleViewModel {
    private Calendar calendar = Calendar.getInstance();
    private String id;
    private boolean man;

    public HistoryHealthViewModel(@NonNull Application application) {
        super(application);
    }

    public void setCalendar(Calendar calendar){
        this.calendar.setTimeInMillis(calendar.getTimeInMillis());
    }

    public Calendar getCalendar(){
        return calendar;
    }

    public void setUserId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public boolean isMan() {
        return man;
    }

    public void setMan(boolean man) {
        this.man = man;
    }
}
